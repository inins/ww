package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.User;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.acticity.PhotoActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterEva;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterZan;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailContentBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEvaController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailZanController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunshowDetailActivity extends BasicAppActivity {

    private FunshowDetailContentBoardController contentBoardController;
    private FunshowDetailZanController zanController;
    private FunshowDetailEvaController evaController;
    private FunshowDetailEditController editController;

    public static void start(Context context) {
        Intent intent = new Intent(context, FunshowDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_funshow_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        //初始化各部分控制器
        contentBoardController = new FunshowDetailContentBoardController(findViewById(R.id.include_contentboard));
        zanController = new FunshowDetailZanController(findViewById(R.id.include_zan));
        evaController = new FunshowDetailEvaController(findViewById(R.id.include_eva));
        editController = new FunshowDetailEditController(findViewById(R.id.include_edit));
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

    }
}
