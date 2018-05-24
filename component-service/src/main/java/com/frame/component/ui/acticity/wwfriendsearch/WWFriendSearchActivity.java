package com.frame.component.ui.acticity.wwfriendsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.wwfriendsearch.adapter.WWFriendSearchAdapter;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchBase;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchFriend;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchGroup;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchResult;
import com.frame.component.ui.acticity.wwfriendsearch.entities.dto.SearchResultDTO;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.EntitiesUtil;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class WWFriendSearchActivity extends BaseAppActivity implements IView,
        WWFriendSearchAdapter.ClickListener {

    @BindView(R2.id.search_edit_text)
    EditText mSearchET;
    @BindView(R2.id.friend_recycler_view)
    RecyclerView mFriendRV;
    WWFriendSearchAdapter mFriendAdapter;
    @BindView(R2.id.more_friend_text_view)
    TextView mMoreFriendTV;
    @BindView(R2.id.arrow_icon_image_view)
    ImageView mArrowIV;
    @BindView(R2.id.group_recycler_view)
    RecyclerView mGroupRV;
    WWFriendSearchAdapter mGroupAdapter;
    @BindView(R2.id.content_layout)
    View mContentLayout;
    @BindView(R2.id.group_title_text_view)
    View mGroupTitleTV;
    @BindView(R2.id.friend_title_text_view)
    View mFriendTitleTV;
    @BindView(R2.id.more_friend_layout)
    View mMoreFriendLayout;

    private List<SearchBase> mFriendList = new ArrayList<>();
    private List<SearchBase> mGroupList = new ArrayList<>();

    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_ww_friend_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(this).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(this).repoitoryManager();

        mFriendAdapter = new WWFriendSearchAdapter(this, mFriendList);
        mFriendAdapter.setClickListener(this);
        mFriendRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFriendRV.setAdapter(mFriendAdapter);

        mGroupAdapter = new WWFriendSearchAdapter(this, mGroupList);
        mGroupAdapter.setClickListener(this);
        mGroupRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mGroupRV.setAdapter(mGroupAdapter);

        mSearchET.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
            }

            return false;
        });

        mContentLayout.setVisibility(View.GONE);
    }

    @OnClick(R2.id.cancel_text_view)
    public void cancelTV() {
        KeyboardUtils.hideSoftInput(this);
        finish();
    }

    /**
     * 更多 收起
     */
    @OnClick(R2.id.more_friend_layout)
    public void showMore() {
        if (mFriendAdapter.getVisibleCount() <= 3) {
            mFriendAdapter.setVisibleCount(Integer.MAX_VALUE);
            mMoreFriendTV.setText("收起");
            mArrowIV.setImageResource(R.drawable.common_ic_up);
        } else {
            mFriendAdapter.setVisibleCount(3);
            mMoreFriendTV.setText("更多联系人");
            mArrowIV.setImageResource(R.drawable.common_ic_down);
        }
    }

    private void doSearch() {
        String keyword = mSearchET.getText().toString();

//        if (TextUtils.isEmpty(keyword)) {
//            ToastUtil.showToastShort("请输入搜索关键字");
//            return;
//        }

        listFriendAndGroup(EntitiesUtil.assertNotNull(keyword));
    }

    private void listFriendAndGroup(String keyword) {
        mApiHelper.execute(this,
                netListFriendAndGroup(keyword),
                new ErrorHandleSubscriber<SearchResult>() {
                    @Override
                    public void onNext(SearchResult searchResult) {
                        mFriendList.clear();
                        mFriendList.addAll(searchResult.getUserFriend());
                        mFriendAdapter.notifyDataSetChanged();

                        if (mFriendList.size() <= 0) {
                            mFriendTitleTV.setVisibility(View.GONE);
                            mMoreFriendLayout.setVisibility(View.GONE);
                        } else {
                            mFriendTitleTV.setVisibility(View.VISIBLE);
                            mMoreFriendLayout.setVisibility(View.VISIBLE);
                        }

                        mGroupList.clear();
                        mGroupList.addAll(searchResult.getGroup());
                        mGroupAdapter.notifyDataSetChanged();

                        if (mGroupList.size() <= 0) {
                            mGroupTitleTV.setVisibility(View.GONE);
                        } else {
                            mGroupTitleTV.setVisibility(View.VISIBLE);
                        }

                        mContentLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                },
                disposable -> showLoading(),
                () -> hideLoading());
    }

    private Observable<BaseJson<SearchResultDTO>> netListFriendAndGroup(String keyword) {
        Map<String, Object> param = new NetParam()
                .put("keyword", keyword)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .listFriendAndGroup(param);
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
    public void onFriendClick(SearchFriend friend) {
        ToastUtil.showToastShort(friend.getNickname() + " " + friend.getUserId());
    }

    @Override
    public void onGroupClick(SearchGroup group) {
        ToastUtil.showToastShort(group.getGroup_name() + " " + group.getGroup_id());
    }
}
