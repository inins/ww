package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.EditText;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleActivityComponent;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailContentBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEvaController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailZanController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunshowDetailActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R2.id.edit_eva)
    EditText editEva;


    private FunshowDetailContentBoardController contentBoardController;
    private FunshowDetailZanController zanController;
    private FunshowDetailEvaController evaController;
    private FunshowDetailEditController editController;

    private int talkId;

    public static void start(Context context, int talkId) {
        Intent intent = new Intent(context, FunshowDetailActivity.class);
        intent.putExtra("talkId", talkId);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_funshow_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        talkId = getIntent().getIntExtra("talkId", 0);

        //初始化各部分控制器
        contentBoardController = new FunshowDetailContentBoardController(findViewById(R.id.include_contentboard), talkId);
        zanController = new FunshowDetailZanController(findViewById(R.id.include_zan), appbarlayout, editEva, talkId);
        evaController = new FunshowDetailEvaController(findViewById(R.id.include_eva), springView, editEva, talkId);
        editController = new FunshowDetailEditController(findViewById(R.id.include_edit), talkId);


    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            switch (contentBoardController.getBundleViewColCount()) {
                case 4:
                    contentBoardController.changeBundleView(3, 0.87f);
                    break;
                case 3:
                    contentBoardController.changeBundleView(2, 0.87f);
                    break;
                case 2:
                    contentBoardController.changeBundleView(1, 1.76f);
                    break;
                case 1:
                    contentBoardController.changeBundleView(4, 1f);
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contentBoardController != null) contentBoardController.onDestory();
        if (zanController != null) zanController.onDestory();
        if (evaController != null) evaController.onDestory();
        if (editController != null) editController.onDestory();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
