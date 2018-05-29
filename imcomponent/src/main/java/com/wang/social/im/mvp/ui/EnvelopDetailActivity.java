package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.common.HVItemDecoration;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.BarUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerEnvelopDetailComponent;
import com.wang.social.im.di.modules.EnvelopDetailModule;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.presenter.EnvelopDetailPresenter;
import com.wang.social.im.mvp.ui.adapters.EnvelopAdoptInfoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 红包详情
 */
public class EnvelopDetailActivity extends BaseAppActivity<EnvelopDetailPresenter> implements EnvelopDetailContract.View, DialogInterface.OnCancelListener {

    @BindView(R2.id.ed_rlv_records)
    RecyclerView edRlvRecords;
    @BindView(R2.id.ed_tv_out_of_date)
    TextView edTvOutOfDate;
    @BindView(R2.id.s_toolbar)
    SocialToolbar sToolbar;
    @BindView(R2.id.ed_load)
    SpringView edLoad;

    private EnvelopAdoptInfoAdapter mAdapter;

    private EnvelopInfo mEnvelopInfo;

    public static void start(Context context, EnvelopInfo envelopInfo) {
        Intent intent = new Intent(context, EnvelopDetailActivity.class);
        intent.putExtra("envelopInfo", envelopInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarUtils.setColor(this, ContextCompat.getColor(this, R.color.im_envelop_theme), 0);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEnvelopDetailComponent
                .builder()
                .appComponent(appComponent)
                .envelopDetailModule(new EnvelopDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_envelop_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mEnvelopInfo = getIntent().getParcelableExtra("envelopInfo");

        init();

        if (mEnvelopInfo.getStatus() == EnvelopInfo.Status.OVERDUE) {
            edTvOutOfDate.setVisibility(View.VISIBLE);
        }

        edRlvRecords.setLayoutManager(new LinearLayoutManager(this));
        HVItemDecoration itemDecoration = new HVItemDecoration(this, HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        itemDecoration.setLeftMargin(getResources().getDimensionPixelSize(R.dimen.common_border_margin));
        edRlvRecords.addItemDecoration(itemDecoration);
        mAdapter = new EnvelopAdoptInfoAdapter();
        edRlvRecords.setAdapter(mAdapter);

        if ((mEnvelopInfo.getType() == EnvelopInfo.EnvelopType.PRIVATE && !mEnvelopInfo.isSelf()) ||
                (mEnvelopInfo.getType() == EnvelopInfo.EnvelopType.EQUAL && !mEnvelopInfo.isSelf())) {
            mAdapter.refreshData(Arrays.asList(mEnvelopInfo));
            edLoad.setEnableFooter(false);
            edRlvRecords.setBackgroundColor(ContextCompat.getColor(this, R.color.im_bg_envelop_main));
        } else {
            mPresenter.getEnvelopAdoptInfo(mEnvelopInfo.getEnvelopId());
        }
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
        dialogLoading.get().setOnCancelListener(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void init() {
        edLoad.setEnableHeader(false);
        edLoad.setFooter(new AliFooter(this, false));

        sToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:

                        break;
                }
            }
        });

        edLoad.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                mPresenter.getEnvelopAdoptInfo(mEnvelopInfo.getEnvelopId());
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finish();
    }

    @Override
    public void showList(List<EnvelopAdoptInfo> list, boolean hasMore) {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            List<Object> datas = new ArrayList<>();
            datas.add(mEnvelopInfo);
            datas.addAll(list);
            mAdapter.refreshData(datas);
        } else {
            mAdapter.addItem(list);
        }
        edLoad.onFinishFreshAndLoad();
        if (!hasMore) {
            edLoad.setEnableFooter(false);
        }
    }

    @Override
    public void onLoadListError() {
        edLoad.onFinishFreshAndLoad();
    }
}