package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.frame.component.app.Constant;
import com.frame.component.common.AppConstant;
import com.frame.component.path.ImPath;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerHappyWoodComponent;
import com.wang.social.im.di.modules.HappyWoodModule;
import com.wang.social.im.mvp.contract.HappyWoodContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.ShareInfo;
import com.wang.social.im.mvp.model.entities.ShareModel;
import com.wang.social.im.mvp.presenter.HappyWoodPresenter;
import com.wang.social.im.view.ShareView;
import com.wang.social.im.widget.RulesDialog;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分享树
 */
@RouteNode(path = ImPath.SHARE_WOOD_PATH, desc = "分享树")
public class HappyWoodActivity extends BaseAppActivity<HappyWoodPresenter> implements HappyWoodContract.View {

    @BindView(R2.id.hw_toolbar)
    SocialToolbar hwToolbar;
    @BindView(R2.id.hw_tv_income)
    TextView hwTvIncome;
    @BindView(R2.id.hw_tv_today)
    TextView hwTvToday;
    @BindView(R2.id.hw_tv_times)
    TextView hwTvTimes;
    @BindView(R2.id.hw_share_list)
    ShareView hwShareList;

    @Autowired
    String objectId;
    @Autowired
    int type;

    private String mType;

    public static void start(Context context, String objectId, int type) {
        Intent intent = new Intent(context, HappyWoodActivity.class);
        intent.putExtra("objectId", objectId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHappyWoodComponent
                .builder()
                .appComponent(appComponent)
                .happyWoodModule(new HappyWoodModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_happy_wood;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        switch (type) {
            case Constant.SHARE_WOOD_TYPE_GROUP:
                mType = GroupService.SHARE_WOOD_GROUP;
                break;
            case Constant.SHARE_WOOD_TYPE_TALK:
                mType = GroupService.SHARE_WOOD_TALK;
                break;
            case Constant.SHARE_WOOD_TYPE_TOPIC:
                mType = GroupService.SHARE_WOOD_TOPIC;
                break;
        }

        mPresenter.getShareInfo(mType, objectId);
        mPresenter.getTreeData(mType, objectId);
    }

    private void init() {
        hwToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        new RulesDialog(HappyWoodActivity.this, getString(R.string.im_happy_wood), AppConstant.Url.sharedTree).show();
                        break;
                }
            }
        });
    }

    @OnClick(R2.id.hw_ivb_list)
    public void onViewClicked() {
        ShareListActivity.start(this, type, objectId);
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
    public void showShareInfo(ShareInfo shareInfo) {
        hwTvIncome.setText(String.valueOf(shareInfo.getTotalIncome()));
        hwTvToday.setText(String.valueOf(shareInfo.getTodayIncome()));
        hwTvTimes.setText(String.valueOf(shareInfo.getShareCount()));
    }

    @Override
    public void showTree(List<ShareModel> data, Map<Integer, Integer> levmap, int maxWidth, int maxle, PointF startCenter) {
        hwShareList.setData(data, levmap, maxWidth, maxle, startCenter);
    }
}