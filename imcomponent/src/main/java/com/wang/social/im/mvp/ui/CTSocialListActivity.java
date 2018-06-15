package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerCTSocialListComponent;
import com.wang.social.im.di.modules.CTSocialListModule;
import com.wang.social.im.mvp.contract.CTSocialListContract;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.SimpleSocial;
import com.wang.social.im.mvp.presenter.CTSocialListPresenter;
import com.wang.social.im.mvp.ui.adapters.CTSocialAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frame.entities.EventBean.EVENT_NOTIFY_SHOW_CONVERSATION_LIST;

/**
 * 创建觅聊，趣聊选择列表
 */
public class CTSocialListActivity extends BaseAppActivity<CTSocialListPresenter> implements CTSocialListContract.View, BaseAdapter.OnItemClickListener<SimpleSocial> {

    @BindView(R2.id.ctl_toolbar)
    SocialToolbar ctlToolbar;
    @BindView(R2.id.ctl_list)
    RecyclerView ctlList;
    @BindView(R2.id.ctl_loading)
    LoadingLayoutEx ctlLoading;

    private CTSocialAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, CTSocialListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCTSocialListComponent
                .builder()
                .appComponent(appComponent)
                .cTSocialListModule(new CTSocialListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_ctsocial_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        ctlList.setLayoutManager(new LinearLayoutManager(this));
        HVItemDecoration itemDecoration = new HVItemDecoration(this, HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        int borderMargin = getResources().getDimensionPixelSize(R.dimen.common_border_margin);
        itemDecoration.setLeftMargin(borderMargin);
        itemDecoration.setRightMargin(borderMargin);
        ctlList.addItemDecoration(itemDecoration);
        mAdapter = new CTSocialAdapter();
        mAdapter.setOnItemClickListener(this);
        ctlList.setAdapter(mAdapter);

        mPresenter.getSocialList();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void initListener() {
        ctlToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                finish();
            }
        });
    }

    @Override
    public void showSocials(List<SimpleSocial> socials) {
        if (socials == null || socials.isEmpty()) {
            ctlLoading.showFailView(R.drawable.im_empty_social, "没有可创建觅聊的趣聊");
        } else {
            ctlLoading.showOut();
            mAdapter.refreshData(socials);
        }
    }

    @Override
    public void onItemClick(SimpleSocial simpleGroupInfo, int position) {
        CreateTeamActivity.start(this, simpleGroupInfo.getSocialId());
    }
}
