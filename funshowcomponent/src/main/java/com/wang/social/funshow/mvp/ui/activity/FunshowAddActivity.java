package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.BitmapUtil;
import com.frame.component.utils.FileUtil;
import com.frame.component.utils.ListUtil;
import com.frame.component.utils.VideoCoverUtil;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleActivityComponent;
import com.wang.social.funshow.helper.BundleUploadhelper;
import com.wang.social.funshow.mvp.entities.post.FunshowAddPost;
import com.wang.social.funshow.mvp.entities.post.ResourcePost;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddBottomBarController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddBundleController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddMusicBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddTagController;
import com.wang.social.funshow.mvp.ui.view.DispatchTouchNestedScrollView;
import com.wang.social.location.mvp.model.entities.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

@RouteNode(path = "/add", desc = "发布趣晒")
public class FunshowAddActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.scroll)
    DispatchTouchNestedScrollView scroll;

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    BundleUploadhelper uploadhelper;

    private FunshowAddEditController editController;
    private FunshowAddMusicBoardController musicBoardController;
    private FunshowAddBundleController bundleController;
    private FunshowAddBottomBarController bottomBarController;
    private FunshowAddTagController tagController;

    public static void start(Context context) {
        Intent intent = new Intent(context, FunshowAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_funshowadd;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        editController = new FunshowAddEditController(findViewById(R.id.include_edit));
        musicBoardController = new FunshowAddMusicBoardController(findViewById(R.id.include_musicboard));
        bundleController = new FunshowAddBundleController(findViewById(R.id.include_bundle));
        bottomBarController = new FunshowAddBottomBarController(findViewById(R.id.include_bottombar));
        tagController = new FunshowAddTagController(findViewById(R.id.include_tagbar));

        scroll.setOnDispatchTouchEventCallback(() -> bottomBarController.setVoiceRecordVisible(false));
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_right) {
            List<String> photoPaths = bundleController.getImgPaths();
            String videoPath = bundleController.getVideoPath();
            String musicPath = musicBoardController.getMusicPath();

            //如果存在视频，需要解析第一帧图片在图片资源中添加一张图片作为封面
            if (!TextUtils.isEmpty(videoPath)) {
                String path = FileUtil.getPhotoFullPath();
                Bitmap bitmap = VideoCoverUtil.createVideoThumbnail(videoPath);
                BitmapUtil.saveBitmap(bitmap, path);
                photoPaths.add(0, path);
            }

            uploadhelper.startUpload(this, musicPath, videoPath, photoPaths, (voiceUrl, videoUrl, photoUrls) -> postCommit(voiceUrl, videoUrl, photoUrls));
        }
    }

    //发布趣晒
    //1.音频2.视频3.图片
    private void postCommit(String voiceUrl, String videoUrl, List<String> photoUrls) {
        FunshowAddPost postBean = new FunshowAddPost();
        postBean.setContent(editController.getContent());
        postBean.setCreatorId(AppDataHelper.getUser().getUserId());
        postBean.setIsAnonymous(tagController.isHideName() ? "1" : "0");   //是否匿名
        postBean.setAuthority(bottomBarController.getLock());       //公开权限
        postBean.setRelateState(tagController.isPay() ? 1 : 0);     //是否收费
        postBean.setGemstone(tagController.getDiamond());           //付费钻石数

        //@用户列表
        postBean.setUsers(editController.getAiteUsers());

        //设置位置信息
        LocationInfo location = bundleController.getLocation();
        if (location != null) {
            postBean.setProvince(location.getProvince());
            postBean.setCity(location.getCity());
            postBean.setLatitude(String.valueOf(location.getLatitude()));
            postBean.setLongitude(String.valueOf(location.getLongitude()));
            postBean.setAddress(location.getAddress());
            postBean.setAdCode(location.getAdCode());
        }
        //图片资源
        ArrayList<ResourcePost> resources = new ArrayList<>();
        for (int i = 0; i < photoUrls.size(); i++) {
            String url = photoUrls.get(i);
            ResourcePost resourcePost = new ResourcePost();
            resourcePost.setMediaType(3);   //3：图片
            resourcePost.setUrl(url);
            resourcePost.setPicOrder(i);
            resources.add(resourcePost);
            postBean.setMediaType(3);
        }
        //音频资源
        if (!TextUtils.isEmpty(voiceUrl)) {
            ResourcePost resourcePostVoice = new ResourcePost();
            resourcePostVoice.setMediaType(1);
            resourcePostVoice.setUrl(voiceUrl);
            resources.add(resourcePostVoice);
            postBean.setMediaType(1);
        }
        //视频资源
        if (!TextUtils.isEmpty(videoUrl)) {
            ResourcePost resourcePostVideo = new ResourcePost();
            resourcePostVideo.setMediaType(2);
            resourcePostVideo.setUrl(videoUrl);
            resources.add(resourcePostVideo);
            postBean.setMediaType(2);
        }

        postBean.setResources(resources);

        netCommit(postBean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bundleController.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editController.onDestory();
        musicBoardController.onDestory();
        bundleController.onDestory();
        bottomBarController.onDestory();
        tagController.onDestory();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    //////////////////////////////////////////

    public FunshowAddEditController getEditController() {
        return editController;
    }

    public FunshowAddMusicBoardController getMusicBoardController() {
        return musicBoardController;
    }

    public FunshowAddBundleController getBundleController() {
        return bundleController;
    }

    public FunshowAddBottomBarController getBottomBarController() {
        return bottomBarController;
    }

    public FunshowAddTagController getTagController() {
        return tagController;
    }

    //////////////////////////////////////////

    public void netCommit(FunshowAddPost post) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(FunshowService.class).addFunshow(post),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastShort("发布成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
