package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.frame.base.BasicFragment;
import com.frame.component.helper.GuidePageHelper;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.home.R;
import com.wang.social.home.R2;
//import com.wang.social.home.di.component.DaggerSingleFragmentComponent;
import com.wang.social.home.mvp.contract.HomeContract;
import com.wang.social.home.mvp.ui.controller.HomeContentController;
import com.wang.social.home.mvp.ui.controller.DetailBannerBoardController;
import com.wang.social.home.mvp.ui.controller.HomeFunshowController;
import com.wang.social.home.mvp.ui.controller.HomeNaviboardController;

import java.util.ArrayList;

import butterknife.BindView;

import static com.app.hubert.guide.model.HighLight.Shape.CIRCLE;
import static com.app.hubert.guide.model.HighLight.Shape.ROUND_RECTANGLE;

/**
 */

public class HomeFragment extends BasicFragment implements HomeContract.View {

    @BindView(R2.id.spring)
    SpringView springView;

    private HomeNaviboardController naviboardController;
    private HomeFunshowController funshowController;
    private HomeContentController contentController;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.home_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        naviboardController = new HomeNaviboardController(this, getView().findViewById(R.id.include_naviboard));
        funshowController = new HomeFunshowController(this, getView().findViewById(R.id.include_funshow));
        contentController = new HomeContentController(this, getView().findViewById(R.id.include_content));
        FocusUtil.focusToTop(naviboardController.getRoot());

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                contentController.refreshData();
            }

            @Override
            public void onLoadmore() {
                contentController.loadmoreData();
            }
        });

        NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(true)
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(getView().findViewById(R.id.btn_samekind), ROUND_RECTANGLE, 30, 0)
                        .addHighLight(getView().findViewById(R.id.btn_circle), ROUND_RECTANGLE, 30, 0)
                        .addHighLight(getView().findViewById(R.id.img_find_tag), CIRCLE)
                        .setLayoutRes(R.layout.lay_guide_home, R.id.btn_go)
                        .setEverywhereCancelable(false)
                        .setEnterAnimation(GuidePageHelper.getEnterAnimation())
                        .setExitAnimation(GuidePageHelper.getExitAnimation()))
                .show();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        naviboardController.onDestory();
        funshowController.onDestory();
        contentController.onDestory();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
//        DaggerSingleFragmentComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void finishSpringView() {
        springView.onFinishFreshAndLoadDelay();
    }
}