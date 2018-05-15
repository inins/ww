package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Topic;
import com.frame.component.ui.acticity.PersonalCard.ui.adapter.TopicListAdapter;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.topic.TopicMe;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class MeTopicFragment extends BasicFragment implements IView{

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private TopicListAdapter adapter;

    public static MeTopicFragment newInstance() {
        Bundle args = new Bundle();
        MeTopicFragment fragment = new MeTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_fragment_topic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new TopicListAdapter(this,recycler,new ArrayList<Topic>());
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetFunshowList(true);
            }

            @Override
            public void onLoadmore() {
                netGetFunshowList(false);
            }
        });

        springView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetFunshowList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(UserService.class).getTopicList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopicMe>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<TopicMe>> basejson) {
                        BaseListWrap<TopicMe> warp = basejson.getData();
                        List<Topic> list = TopicMe.tans2TopicList(warp.getList());
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
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
}
