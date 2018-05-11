package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
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

import butterknife.BindView;

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
