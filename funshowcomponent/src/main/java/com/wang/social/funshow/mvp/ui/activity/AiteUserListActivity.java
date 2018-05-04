package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.helper.SpringViewHelper;
import com.wang.social.funshow.mvp.entities.user.Friend;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterAiteUserList;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHotUserList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AiteUserListActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterAiteUserList adapter;


    public static void start(Context context) {
        Intent intent = new Intent(context, AiteUserListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_aiteuserlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterAiteUserList();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetTopUserList();
            }

            @Override
            public void onLoadmore() {
                springView.onFinishFreshAndLoadDelay(1000);
            }
        });
        springView.callFreshDelay();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            List<Friend> friends = adapter.getSelectItems();
            if (!StrUtil.isEmpty(friends)) {
                EventBean eventBean = new EventBean(EventBean.EVENT_CTRL_FUNSHOW_ADD_USER);
                eventBean.put("users", friends);
                EventBus.getDefault().post(eventBean);
                finish();
            } else {
                ToastUtil.showToastShort("请选择要@的好友");
            }
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /////////////////////////////////////

    public void netGetTopUserList() {
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(FunshowService.class).friendList(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Friend>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Friend>> basejson) {
                        BaseListWrap<Friend> warp = basejson.getData();
                        if (warp != null) {
                            List<Friend> list = warp.getList();
                            adapter.refreshData(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, null, () -> {
                    springView.onFinishFreshAndLoadDelay();
                });
    }
}
