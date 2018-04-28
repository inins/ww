package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.ListUtil;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

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

            uploadhelper.startUpload(this, musicPath, videoPath, photoPaths, (voiceUrl, videoUrl, photoUrls) -> postCommit(voiceUrl, videoUrl, photoUrls));
        }
    }

    private void postCommit(String voiceUrl, String videoUrl, List<String> photoUrls) {
        FunshowAddPost postBean = new FunshowAddPost();
        postBean.setAdCode("610100");
        postBean.setProvince("四川省");
        postBean.setCity("绵阳市");
        postBean.setLatitude("30.566729");
        postBean.setLongitude("104.063642");
        postBean.setContent(editController.getContent());
        postBean.setCreatorId(AppDataHelper.getUser().getUserId());
        postBean.setIsAnonymous(tagController.isHideName() ? "1" : "0");   //是否匿名
        postBean.setAuthority(bottomBarController.getLock());       //公开权限
        postBean.setRelateState(tagController.isPay() ? 1 : 0);     //是否收费
        postBean.setGemstone(tagController.getDiamond());           //付费钻石数

        //@用户列表
        postBean.setUsers(editController.getAiteUsers());

        //图片资源
        ArrayList<ResourcePost> resources = new ArrayList<>();
        for (int i = 0; i < photoUrls.size(); i++) {
            String url = photoUrls.get(i);
            ResourcePost resourcePost = new ResourcePost();
            resourcePost.setMediaType(3);   //3：图片
            resourcePost.setUrl(url);
            resourcePost.setPicOrder(i);
            resources.add(resourcePost);
        }
        //音频资源
        if (!TextUtils.isEmpty(voiceUrl)) {
            ResourcePost resourcePostVoice = new ResourcePost();
            resourcePostVoice.setMediaType(1);
            resourcePostVoice.setUrl(voiceUrl);
            resources.add(resourcePostVoice);
        }
        //视频资源
        if (!TextUtils.isEmpty(videoUrl)) {
            ResourcePost resourcePostVideo = new ResourcePost();
            resourcePostVideo.setMediaType(2);
            resourcePostVideo.setUrl(videoUrl);
            resources.add(resourcePostVideo);
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
