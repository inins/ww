package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.User;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterEva;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterZan;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunshowDetailActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.recycler_zan)
    RecyclerView recyclerZan;
    @BindView(R2.id.recycler_eva)
    RecyclerView recyclerEva;
    @BindView(R2.id.bundleview)
    BundleImgView bundleview;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_title)
    TextView textTitle;
    private RecycleAdapterZan adapterZan;
    private RecycleAdapterEva adapterEva;

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
        FontUtils.boldText(textName);
        FontUtils.boldText(textTitle);

        adapterZan = new RecycleAdapterZan();
        adapterZan.setOnMoreClickListener(v -> {
        });
        recyclerZan.setNestedScrollingEnabled(false);
        recyclerZan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerZan.setAdapter(adapterZan);
        recyclerZan.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));

        adapterEva = new RecycleAdapterEva();
        adapterEva.setOnItemClickListener(this);
        recyclerEva.setNestedScrollingEnabled(false);
        recyclerEva.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerEva.setAdapter(adapterEva);
        recyclerEva.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        //测试数据

        adapterZan.refreshData(new ArrayList<User>() {{
            add(new User());
            add(new User());
            add(new User());
            add(new User());
            add(new User());
            add(new User());
        }});

        adapterEva.refreshData(new ArrayList() {{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});

        bundleview.setPhotos(new ArrayList<BundleImgEntity>() {{
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
        }});
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            switch (bundleview.getColcount()) {
                case 4:
                    bundleview.setColcountWihi(3, 0.87f);
                    break;
                case 3:
                    bundleview.setColcountWihi(2, 0.87f);
                    break;
                case 2:
                    bundleview.setColcountWihi(1, 1.76f);
                    break;
                case 1:
                    bundleview.setColcountWihi(4, 1f);
                    break;
            }
        }
    }

    @Override
    public void onItemClick(TestEntity testEntity, int position) {

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
