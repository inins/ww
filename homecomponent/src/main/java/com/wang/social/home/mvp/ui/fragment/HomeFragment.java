package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.home.R;
import com.wang.social.home.mvp.ui.controller.HomeContentController;
import com.wang.social.home.mvp.ui.controller.HomeFunshowController;
import com.wang.social.home.mvp.ui.controller.HomeNaviboardController;

/**
 * 建设中 fragment 占位
 */

public class HomeFragment extends BasicFragment {

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
        naviboardController = new HomeNaviboardController(getView().findViewById(R.id.include_naviboard));
        funshowController = new HomeFunshowController(getView().findViewById(R.id.include_funshow));
        contentController = new HomeContentController(getView().findViewById(R.id.include_content));
        FocusUtil.focusToTop(naviboardController.getRoot());
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }
}
