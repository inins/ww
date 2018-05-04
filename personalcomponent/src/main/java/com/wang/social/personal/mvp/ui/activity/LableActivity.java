package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BaseAdapter;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.GridSpacingItemDecoration;
import com.wang.social.personal.common.OnSimpleTabSelectedListener;
import com.wang.social.personal.di.component.DaggerLableActivityComponent;
import com.wang.social.personal.di.module.LableModule;
import com.wang.social.personal.mvp.contract.LableContract;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.presonter.LablePresonter;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterLable;
import com.wang.social.personal.mvp.ui.dialog.DialogFragmentLable;
import com.frame.component.ui.dialog.DialogSure;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class LableActivity extends BaseAppActivity<LablePresonter> implements LableContract.View, RecycleAdapterLable.OnLableDelClickListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.btn_right)
    TextView btn_right;
    @BindView(R2.id.recycler_show)
    RecyclerView recycler_show;
    @BindView(R2.id.recycler_me)
    RecyclerView recycler_me;
    @BindView(R2.id.tablayout)
    TabLayout tablayout;

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;

    private RecycleAdapterLable adapter_show;
    private RecycleAdapterLable adapter_me;

    private DialogFragmentLable dialogLable;

    public static void start(Context context) {
        if (CommonHelper.LoginHelper.isLogin()) {
            Intent intent = new Intent(context, LableActivity.class);
            context.startActivity(intent);
        } else {
            CommonHelper.LoginHelper.startLoginActivity(context);
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_UPDATED:
                TabLayout.Tab tab = tablayout.getTabAt(tablayout.getSelectedTabPosition());
                mPresenter.getSelftags((int) tab.getTag());
                mPresenter.getShowtag();
                break;
        }
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_lable;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        dialogLable = new DialogFragmentLable();

        adapter_show = new RecycleAdapterLable();
        adapter_show.setOnLableDelClickListener(this);
        recycler_show.setNestedScrollingEnabled(false);
        recycler_show.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_show.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
        recycler_show.setAdapter(adapter_show);

        adapter_me = new RecycleAdapterLable();
        adapter_me.setOnLableDelClickListener(this);
        adapter_me.setOnItemClickListener(onMeItemClickListener);
        recycler_me.setNestedScrollingEnabled(false);
        recycler_me.setLayoutManager(ChipsLayoutManager.newBuilder(this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        recycler_me.addItemDecoration(new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        recycler_me.setAdapter(adapter_me);

        tablayout.addOnTabSelectedListener(new OnSimpleTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPresenter.getSelftags((int) tab.getTag());
            }
        });

        //请求一级标签和个性标签
        mPresenter.getShowtag();
        mPresenter.getParentTags();
    }


    private BaseAdapter.OnItemClickListener<Lable> onMeItemClickListener = (lable, position) -> {
        //非编辑状态下不能添加标签
        if (!adapter_me.isDeleteEnable()) return;
        if (adapter_show.getItemCount() < 4) {
            String ids = adapter_show.getIdsByAdd(lable);
            mPresenter.updateShowtag(ids);
        } else {
            ToastUtil.showToastLong("名片中只能展示4个标签");
        }
    };

    @Override
    public void OnDelClick(RecycleAdapterLable adapter, Lable lable, int position) {
        if (adapter == adapter_show && adapter_show.getItemCount() <= 1) {
            ToastUtil.showToastLong("至少展示一个标签");
            return;
        }
        DialogSure.showDialog(this, "确认要删除该标签？", () -> {
            if (adapter == adapter_show) {
                //删除个性标签
                String ids = adapter_show.getIdsByDel(lable);
                mPresenter.updateShowtag(ids);
            } else if (adapter == adapter_me) {
                //删除个人标签
                mPresenter.deltag(lable.getId());
                adapter.removeItem(position);
            }
        });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            if (!adapter_show.isDeleteEnable()) {
                btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_finish));
                adapter_show.setDeleteEnable(true);
                adapter_show.notifyDataSetChanged();
                adapter_me.setDeleteEnable(true);
                adapter_me.notifyDataSetChanged();
            } else {
                btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_edit));
                adapter_show.setDeleteEnable(false);
                adapter_show.notifyDataSetChanged();
                adapter_me.setDeleteEnable(false);
                adapter_me.notifyDataSetChanged();
            }
        } else if (i == R.id.btn_add) {
//            CommonHelper.LoginHelper.startTagSelectActivity(this);
            TagSelectionActivity.startSelection(this, TagSelectionActivity.TAG_TYPE_PERSONAL);
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLableActivityComponent
                .builder()
                .appComponent(appComponent)
                .lableModule(new LableModule(this))
                .build()
                .inject(this);
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
    public void freshTagList(List<Lable> lables) {
        adapter_show.refreshData(lables);
    }

    @Override
    public void freshFlagList(List<Lable> lables) {
        adapter_me.refreshData(lables);
    }

    @Override
    public void freshParentTagList(List<Lable> lables) {
        if (StrUtil.isEmpty(lables)) return;
        tablayout.removeAllTabs();
        for (Lable lable : lables) {
            tablayout.addTab(tablayout.newTab().setText(lable.getName()).setTag(lable.getId()));
        }
    }
}
