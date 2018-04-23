package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.helper.VideoPhotoHelperEx;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddBottomBarController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddBundleController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddMusicBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowAddTagController;
import com.wang.social.funshow.mvp.ui.view.DispatchTouchNestedScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunshowAddActivity extends BasicAppActivity {

    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.scroll)
    DispatchTouchNestedScrollView scroll;

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
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
