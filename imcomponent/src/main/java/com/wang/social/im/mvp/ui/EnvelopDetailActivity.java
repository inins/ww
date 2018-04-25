package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.BarUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;
import com.wang.social.im.mvp.presenter.EnvelopDetailPrensenter;
import com.wang.social.im.mvp.ui.adapters.EnvelopAdoptInfoAdapter;

import butterknife.BindView;

/**
 * 红包详情
 */
public class EnvelopDetailActivity extends BaseAppActivity<EnvelopDetailPrensenter> implements EnvelopDetailContract.View {

    @BindView(R2.id.ed_rlv_records)
    RecyclerView edRlvRecords;
    @BindView(R2.id.ed_tv_out_of_date)
    TextView edTvOutOfDate;
    @BindView(R2.id.s_toolbar)
    SocialToolbar sToolbar;

    private EnvelopAdoptInfoAdapter mAdapter;

    public static void start(Context context, long envelopId) {
        Intent intent = new Intent(context, EnvelopDetailActivity.class);
        intent.putExtra("envelopId", envelopId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarUtils.setColor(this, ContextCompat.getColor(this, R.color.im_envelop_theme), 0);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_envelop_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        edRlvRecords.setLayoutManager(new LinearLayoutManager(this));
        edRlvRecords.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new EnvelopAdoptInfoAdapter();
        edRlvRecords.setAdapter(mAdapter);

        init();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void init() {
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
    }
}
