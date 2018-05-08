package com.wang.social.funpoint.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.ConerEditText;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.di.component.DaggerSingleActivityComponent;
import com.frame.component.entities.funpoint.Funpoint;
import com.wang.social.funpoint.mvp.model.api.FunpointService;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterSearch;
import com.wang.social.funpoint.mvp.ui.fragment.SearchFunpointFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.btn_right)
    TextView btnRight;
    @BindView(R2.id.edit_search)
    ConerEditText editSearch;

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
        SearchFunpointFragment searchFragment = SearchFunpointFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!searchFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, searchFragment, "searchFragment");
        }
        fragmentTransaction.commit();

        editSearch.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                search();
                return true;
            }
            return false;
        });
        //延迟0.1秒后弹出软键盘
        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(editSearch), 100);
    }

    private void search() {
        String tags = editSearch.getTagsStr();
        String key = editSearch.getKey();
        if (!TextUtils.isEmpty(tags) || !TextUtils.isEmpty(key)) {
            EventBean eventBean = new EventBean(EventBean.EVENT_APP_SEARCH);
            eventBean.put("tags",tags);
            eventBean.put("key",key);
            EventBus.getDefault().post(eventBean);
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_right) {
            finish();
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
}
