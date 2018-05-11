package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.EditText;

import com.frame.component.helper.NetReportHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.liaoinstan.springview.widget.SpringView;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleActivityComponent;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailContentBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEvaController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailZanController;

import butterknife.BindView;
import butterknife.ButterKnife;

@RouteNode(path = "/detail", desc = "趣晒详情")
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
            DialogSure.showDialog(this, "确定要举报该趣晒？", () -> {
                NetReportHelper.newInstance().netReportFunshow(this, talkId, () -> finish());
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
}
