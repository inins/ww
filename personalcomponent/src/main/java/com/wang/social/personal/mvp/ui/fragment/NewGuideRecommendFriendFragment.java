package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.User;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.utils.ListUtil;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recommend.RecommendUser;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.activity.NewGuideRecommendActivity;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterNewguideRecommendFriend;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */

public class NewGuideRecommendFriendFragment extends BasicNoDiFragment {

    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterNewguideRecommendFriend adapter;

    public static NewGuideRecommendFriendFragment newInstance() {
        Bundle args = new Bundle();
        NewGuideRecommendFriendFragment fragment = new NewGuideRecommendFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_fragment_newguide_recommend_friend;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterNewguideRecommendFriend();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(3, SizeUtils.dp2px(20), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        //请求数据
        netGetRecommendUsers();
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @OnClick({R2.id.btn_go})
    public void onViewClicked(View v) {
        if (getActivity() instanceof NewGuideRecommendActivity) {
            //发起添加好友
            netAddFriends(adapter.getSelectIdList());
            ((NewGuideRecommendActivity) getActivity()).changeBanner(1);
            ((NewGuideRecommendActivity) getActivity()).next();
        }
    }

    public void netGetRecommendUsers() {
        User user = AppDataHelper.getUser();
        if (user == null) return;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).getRecommendUsers(user.getSex()),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<RecommendUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<RecommendUser>> basejson) {
                        BaseListWrap<RecommendUser> wrap = basejson.getData();
                        List<RecommendUser> list = wrap != null ? wrap.getList() : null;
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

    //添加好友
    public void netAddFriends(List<Integer> idList) {
        if (StrUtil.isEmpty(idList)) return;
        for (Integer id : idList) {
            NetFriendHelper.newInstance().netSendFriendlyApply(this, id, "你好，交个朋友吧", false, null);
        }
    }
}
