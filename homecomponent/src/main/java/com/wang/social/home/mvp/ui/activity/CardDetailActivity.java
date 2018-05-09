package com.wang.social.home.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.frame.component.helper.ToolbarTansColorHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.ObservableNestedScrollView;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.ui.controller.DetailBannerBoardController;
import com.wang.social.home.mvp.ui.controller.DetailFunshowController;
import com.wang.social.home.mvp.ui.controller.DetailTopicController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardDetailActivity extends BasicAppActivity {

    @BindView(R2.id.scroll_view)
    ObservableNestedScrollView scrollView;
    @BindView(R2.id.btn_right)
    TextView btnRight;

    private DetailBannerBoardController bannerBoardController;
    private DetailFunshowController funshowController;
    private DetailTopicController topicController;

    public static void start(Context context) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.home_activity_carddetail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.bringToFront();
        StatusBarUtil.setTranslucent(this);

        bannerBoardController = new DetailBannerBoardController(findViewById(R.id.include_bannerboard));
        funshowController = new DetailFunshowController(findViewById(R.id.include_funshow));
        topicController = new DetailTopicController(findViewById(R.id.include_topic));

        scrollView.setOnScrollChangedListener((x, y, oldx, oldy) -> {
            //banner动态位置偏移
            bannerBoardController.getBannerView().setTranslationY(y / 2);
            //toolbar动态透明渐变
            ToolbarTansColorHelper.with(toolbar)
                    .initMaxHeight(SizeUtils.dp2px(200))
                    .initColorStart(Color.parseColor("#00ffffff"))
                    .initColorEnd(ContextCompat.getColor(CardDetailActivity.this, R.color.common_white))
                    .onPointCallback(upOrDown -> {
                        if (upOrDown) {
                            toolbar.setNavigationIcon(R.drawable.common_ic_back);
                            btnRight.setTextColor(ContextCompat.getColor(CardDetailActivity.this, R.color.common_text_blank_dark));
                        } else {
                            toolbar.setNavigationIcon(R.drawable.common_ic_back_white);
                            btnRight.setTextColor(ContextCompat.getColor(CardDetailActivity.this, R.color.common_white));
                        }
                    })
                    .start(y);
        });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerBoardController.onDestory();
        funshowController.onDestory();
        topicController.onDestory();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
