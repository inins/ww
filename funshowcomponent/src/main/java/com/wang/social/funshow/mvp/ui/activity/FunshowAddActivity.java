package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.ListUtil;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleActivityComponent;
import com.wang.social.funshow.helper.VideoPhotoHelperEx;
import com.wang.social.funshow.mvp.entities.post.FunshowAddPost;
import com.wang.social.funshow.mvp.entities.post.ResourcePost;
import com.wang.social.funshow.mvp.entities.user.Friend;
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
import butterknife.ButterKnife;

public class FunshowAddActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.scroll)
    DispatchTouchNestedScrollView scroll;

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    QiNiuManager qiNiuManager;


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
            String content = editController.getContent();
            List<String> bundleRsc = bundleController.getBundleRsc();

            qiNiuManager.uploadFiles(this, ListUtil.transArrayList(bundleRsc), new QiNiuManager.OnBatchUploadListener() {
                @Override
                public void onSuccess(ArrayList<String> urls) {
                    FunshowAddPost postBean = new FunshowAddPost();
                    postBean.setContent(content);
                    postBean.setAdCode("610100");
                    postBean.setProvince("四川省");
                    postBean.setProvince("绵阳市");
                    postBean.setCreatorId(AppDataHelper.getUser().getUserId());
                    postBean.setLatitude("30.566729");
                    postBean.setLongitude("104.063642");
                    postBean.setIsAnonymous("0");   //是否匿名
                    postBean.setAuthority(0);       //公开权限
                    postBean.setRelateState(0);     //是否收费

                    ArrayList<ResourcePost> resources = new ArrayList<>();
                    for (int i = 0; i < urls.size(); i++) {
                        String url = urls.get(i);
                        ResourcePost resourcePost = new ResourcePost();
                        resourcePost.setMediaType(3);   //3：图片
                        resourcePost.setUrl(url);
                        resourcePost.setPicOrder(i);
                        resources.add(resourcePost);
                    }
                    postBean.setResources(resources);

                    netCommit(postBean);
                }

                @Override
                public void onFail() {
                    ToastUtil.showToastLong("上传失败");
                }
            });
        }
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
