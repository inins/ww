package com.wang.social.funpoint.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.ConerEditText;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.di.component.DaggerSingleActivityComponent;
import com.wang.social.funpoint.helper.SpringViewHelper;
import com.wang.social.funpoint.mvp.entities.Funpoint;
import com.wang.social.funpoint.mvp.model.api.FunpointService;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_right)
    TextView btnRight;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;
    @BindView(R2.id.edit_search)
    ConerEditText editSearch;

    private RecycleAdapterSearch adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funpoint_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        adapter = new RecycleAdapterSearch();
        adapter.setEditSearch(editSearch);
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                search(true);
            }

            @Override
            public void onLoadmore() {
                search(false);
            }
        });
        editSearch.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                search(true);
                return true;
            }
            return false;
        });
        loadingview.showLackView();
        //延迟0.1秒后弹出软键盘
        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(editSearch), 100);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_right) {
            finish();
        }
    }

    @Override
    public void onItemClick(Funpoint bean, int position) {
        netReadFunpoint(bean.getNewsId());
        WebActivity.start(this, bean.getUrl());
    }

    private void search(boolean isFresh) {
        String tags = editSearch.getTagsStr();
        String key = editSearch.getKey();
        if (!TextUtils.isEmpty(tags) || !TextUtils.isEmpty(key)) {
            netGetSearchList(tags, key, isFresh);
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(this);
        super.finish();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
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

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSearchList(String tags, String key, boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(FunpointService.class).getSearchFunpointList(tags, key, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> basejson) {
                        BaseListWrap<Funpoint> warp = basejson.getData();
                        List<Funpoint> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            loadingview.showOut();
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
                            if (isFresh) {
                                loadingview.showLackView();
                            }
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }

    public void netReadFunpoint(int newsId) {
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(FunpointService.class).readFunpoint(newsId),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        adapter.reFreshReadCountById(newsId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
