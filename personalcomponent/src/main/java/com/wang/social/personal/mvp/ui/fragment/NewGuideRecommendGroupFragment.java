package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recommend.RecommendGroup;
import com.wang.social.personal.mvp.entities.recommend.RecommendUser;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.activity.NewGuideRecommendActivity;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterNewguideRecommendFriend;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterNewguideRecommendGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */

public class NewGuideRecommendGroupFragment extends BasicNoDiFragment {

    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterNewguideRecommendGroup adapter;

    public static NewGuideRecommendGroupFragment newInstance() {
        Bundle args = new Bundle();
        NewGuideRecommendGroupFragment fragment = new NewGuideRecommendGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_fragment_newguide_recommend_group;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterNewguideRecommendGroup();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(3, SizeUtils.dp2px(20), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        //请求数据
        netGetRecommendGroups();
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @OnClick({R2.id.btn_go})
    public void onViewClicked(View v) {
        if (getActivity() instanceof NewGuideRecommendActivity) {
//            ((NewGuideRecommendActivity) getActivity()).changeBanner(0);
//            ((NewGuideRecommendActivity) getActivity()).last();
            //批量入群
            netAddGroups(adapter.getSelectIdList());
        }
    }

    public void netGetRecommendGroups() {
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(UserService.class).getRecommendGroups(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<RecommendGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<RecommendGroup>> basejson) {
                        BaseListWrap<RecommendGroup> wrap = basejson.getData();
                        List<RecommendGroup> list = wrap != null ? wrap.getList() : null;
                        if (!StrUtil.isEmpty(list)) {
                            adapter.refreshData(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    //申请入群
    public void netAddGroups(List<Integer> idList) {
        if (StrUtil.isEmpty(idList)) return;
        addGroup(idList, 0);
    }

    //递归调用加群接口
    private void addGroup(List<Integer> idList, int index) {
        NetGroupHelper.newInstance().addGroup(getContext(), this, getChildFragmentManager(), idList.get(index), isNeedValidation -> {
            if (idList.size() - 1 >= index + 1) {
                addGroup(idList, index + 1);
            } else {
                getActivity().finish();
                CommonHelper.AppHelper.startHomeActivity(getContext());
            }
        });
    }
}
