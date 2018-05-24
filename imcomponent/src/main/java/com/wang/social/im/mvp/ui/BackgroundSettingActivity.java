package com.wang.social.im.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.frame.base.BaseAdapter;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.enums.ConversationType;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerBackgroundSettingComponent;
import com.wang.social.im.di.modules.BackgroundSettingModule;
import com.wang.social.im.mvp.contract.BackgroundSettingContract;
import com.wang.social.im.mvp.model.entities.OfficialImage;
import com.wang.social.im.mvp.presenter.BackgroundSettingPresenter;
import com.wang.social.im.mvp.ui.adapters.BackgroundImageAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 聊天背景设置
 */
public class BackgroundSettingActivity extends BaseAppActivity<BackgroundSettingPresenter> implements BackgroundSettingContract.View, BaseAdapter.OnItemClickListener<OfficialImage> {

    @BindView(R2.id.bs_rlv_images)
    RecyclerView bsRlv;
    @BindView(R2.id.bs_loader)
    SpringView bsLoader;

    @Autowired
    String targetId;
    @Autowired
    int typeOrdinal;

    private ConversationType mConversationType;

    private BackgroundImageAdapter mAdapter;

    public static void start(Context context, ConversationType conversationType, String targetId) {
        Intent intent = new Intent(context, BackgroundSettingActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("typeOrdinal", conversationType.ordinal());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversationType = ConversationType.values()[typeOrdinal];
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBackgroundSettingComponent
                .builder()
                .appComponent(appComponent)
                .backgroundSettingModule(new BackgroundSettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_background_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        bsLoader.setEnableHeader(false);
        bsLoader.setFooter(new AliHeader(this, false));

        bsRlv.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        bsRlv.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtils.dp2px(7), GridLayoutManager.VERTICAL, false));
        mAdapter = new BackgroundImageAdapter();
        mAdapter.setOnItemClickListener(this);
        bsRlv.setAdapter(mAdapter);

        mPresenter.getImages(true);

        bsLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                mPresenter.getImages(false);
            }
        });
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
    public void showImages(List<OfficialImage> images, boolean hasMore) {
        if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
            mAdapter.refreshData(images);
        }
        if (!hasMore) {
            bsLoader.setEnableFooter(false);
        }
    }

    @Override
    public void hideListLoading() {
        bsLoader.onFinishFreshAndLoadDelay();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onItemClick(OfficialImage officialImage, int position) {
        if (mConversationType != null && !TextUtils.isEmpty(targetId)) {
            mPresenter.downloadAndSetting(officialImage.getImageUrl(), mConversationType, targetId);
        }
    }
}