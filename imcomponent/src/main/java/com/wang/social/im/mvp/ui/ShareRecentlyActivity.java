package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.enums.ShareSource;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.Autowired;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.mvp.contract.ShareContract;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.presenter.SharePresenter;
import com.wang.social.im.mvp.ui.adapters.RecentlyAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.frame.component.path.ImPath.SHARE_RECENTLY;

/**
 * 分享到最近联系人
 */
@RouteNode(path = SHARE_RECENTLY, desc = "分享到往往")
public class ShareRecentlyActivity extends BaseAppActivity<SharePresenter> implements ShareContract.View, BaseAdapter.OnItemClickListener<UIConversation> {

    @BindView(R2.id.sr_toolbar)
    SocialToolbar srToolbar;
    @BindView(R2.id.sr_recently)
    RecyclerView srRecently;

    @Autowired
    String title;
    @Autowired
    String content;
    @Autowired
    String imageUrl;
    @Autowired
    int source;
    @Autowired
    String objectId;

    private ShareSource mShareSource;
    private RecentlyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (source != -1) {
            mShareSource = ShareSource.values()[source];
        }

        srToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                finish();
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_share_recently;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        srRecently.setLayoutManager(new LinearLayoutManager(this));
        HVItemDecoration itemDecoration = new HVItemDecoration(this, HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        itemDecoration.setLeftMargin(UIUtil.getDimen(R.dimen.common_border_margin));
        srRecently.addItemDecoration(itemDecoration);
        mAdapter = new RecentlyAdapter();
        srRecently.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);

        mPresenter.setRootView(this);
        mPresenter.getRecentlyContactList();
    }

    @OnClick(R2.id.sr_tvb_search)
    public void onViewClicked() {
        WWFriendSearchActivity.start(this, title, content, imageUrl, mShareSource, objectId);
    }

    @Override
    public void showContacts(List<UIConversation> conversations) {
        mAdapter.refreshData(conversations);
    }

    @Override
    public void onShareComplete() {
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onItemClick(UIConversation uiConversation, int position) {
        mPresenter.sendMessage(uiConversation.getIdentify(), uiConversation.getConversationType(), title, content, imageUrl, objectId, mShareSource);
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENT_NOTIFY_PROFILE_UPDATED) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
