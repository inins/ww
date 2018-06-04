package com.wang.social.home.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.frame.component.helper.NetFriendHelper;
import com.frame.component.helper.NetReportHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.helper.ToolbarTansColorHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.ui.dialog.DialogActionSheet;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.view.ObservableNestedScrollView;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.ui.controller.DetailBannerBoardController;
import com.wang.social.home.mvp.ui.controller.DetailFunshowController;
import com.wang.social.home.mvp.ui.controller.DetailTopicController;
import com.wang.social.home.mvp.ui.fragment.CardUserFragment;
import com.wang.social.pictureselector.helper.PhotoHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@RouteNode(path = "/user_detail", desc = "个人详情页面（卡片页面进入）")
public class CardDetailActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.scroll_view)
    ObservableNestedScrollView scrollView;
    @BindView(R2.id.btn_right)
    TextView btnRight;
    @BindView(R2.id.btn_go)
    TextView btnGo;

    private PhotoHelper photoHelper;

    private DetailBannerBoardController bannerBoardController;
    private DetailFunshowController funshowController;
    private DetailTopicController topicController;

    private int userId;
    private CardUser cardUser;

    public static void start(Context context, int userId) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    public static void start(Context context, CardUser cardUser) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra("userId", cardUser.getUserId());
        intent.putExtra("cardUser", cardUser);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.home_activity_carddetail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        photoHelper = new PhotoHelper(this);
        userId = getIntent().getIntExtra("userId", 0);
        cardUser = (CardUser) getIntent().getSerializableExtra("cardUser");
        toolbar.bringToFront();
        StatusBarUtil.setTranslucent(this);

        bannerBoardController = new DetailBannerBoardController(findViewById(R.id.include_bannerboard), userId, cardUser);
        funshowController = new DetailFunshowController(findViewById(R.id.include_funshow), userId);
        topicController = new DetailTopicController(findViewById(R.id.include_topic), userId);

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

    public void setAddBtnVisible(boolean visible) {
        btnGo.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            String[] strings = {"语言谩骂/骚扰信息", "存在欺诈骗钱行为", "发布不适当内容"};
            DialogActionSheet.show(getSupportFragmentManager(), strings)
                    .setActionSheetListener((position, text) -> {
                        photoHelper.setOnPhotoCallback(path -> {
                            QiNiuManager.newInstance().uploadFile(CardDetailActivity.this, path, new QiNiuManager.OnSingleUploadListener() {
                                @Override
                                public void onSuccess(String url) {
                                    NetReportHelper.newInstance().netReportPerson(CardDetailActivity.this, userId, text, url, () -> {
                                        //finish();
                                        ToastUtil.showToastShort("举报成功");
                                    });
                                }

                                @Override
                                public void onFail() {
                                    ToastUtil.showToastShort("上传失败");
                                }
                            });
                        });
                        photoHelper.startPhoto();
                    });
        } else if (i == R.id.btn_go) {
            DialogValiRequest.showDialog(this, content -> {
                NetFriendHelper.newInstance().netSendFriendlyApply(CardDetailActivity.this, userId, content, () -> {
                    ToastUtil.showToastShort("请求已发送");
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_HOME_CARD_DETAIL_ADDFIREND));
                });
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerBoardController.onDestory();
        funshowController.onDestory();
        topicController.onDestory();
    }
}
