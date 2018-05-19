package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.api.CommonService;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Topic;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.topic.TopicMe;
import com.frame.component.helper.NetCommonFunshowHelper;
import com.frame.component.helper.NetCommonTopicHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.adapter.TopicListAdapter;
import com.frame.component.ui.base.BasicNoDiFragment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class MeTopicFragment extends BasicNoDiFragment {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private TopicListAdapter adapter;

    private int groupId;
    private boolean isGroup;

    public static MeTopicFragment newInstance() {
        Bundle args = new Bundle();
        MeTopicFragment fragment = new MeTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MeTopicFragment newInstanceForGroup(int groupId) {
        Bundle args = new Bundle();
        args.putInt("groupId", groupId);
        MeTopicFragment fragment = new MeTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getArguments().getInt("groupId");
        isGroup = groupId != 0;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.fragment_topic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new TopicListAdapter(this, recycler, new ArrayList<Topic>());
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetTopicList(true);
            }

            @Override
            public void onLoadmore() {
                netGetTopicList(false);
            }
        });

        springView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetTopicList(boolean isFresh) {
        if (isFresh) current = 0;
        NetCommonTopicHelper.newInstance().netGetTopicList(this, false, groupId, current + 1, size,
                isGroup ? NetCommonTopicHelper.TYPE_GROUP : NetCommonTopicHelper.TYPE_ME,
                new NetCommonTopicHelper.OnTopicCallback() {
                    @Override
                    public void onSuccess(List<Topic> list) {
                        if (!StrUtil.isEmpty(list)) {
                            current++;
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
