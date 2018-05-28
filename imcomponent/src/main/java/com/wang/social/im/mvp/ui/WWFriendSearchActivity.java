package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.enums.ConversationType;
import com.frame.component.enums.ShareSource;
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
import com.frame.integration.AppManager;
import com.frame.integration.IRepositoryManager;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.contract.ShareContract;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.presenter.SharePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class WWFriendSearchActivity extends BaseAppActivity<SharePresenter> implements ShareContract.View,
        WWFriendSearchAdapter.ClickListener {

    // 搜索输入框
    @BindView(R2.id.search_edit_text)
    EditText mSearchET;
    // 好友列表
    @BindView(R2.id.friend_recycler_view)
    RecyclerView mFriendRV;
    WWFriendSearchAdapter mFriendAdapter;
    // 更多联系人（点击展开或收起好友列表）
    @BindView(R2.id.more_friend_layout)
    View mMoreFriendLayout;
    // 更多联系人文字
    @BindView(R2.id.more_friend_text_view)
    TextView mMoreFriendTV;
    // 上下箭头图片
    @BindView(R2.id.arrow_icon_image_view)
    ImageView mArrowIV;
    // 群组列表
    @BindView(R2.id.group_recycler_view)
    RecyclerView mGroupRV;
    WWFriendSearchAdapter mGroupAdapter;
    // 搜索输入框一下的内容，初始时隐藏
    @BindView(R2.id.content_layout)
    View mContentLayout;
    // 群组标题栏
    @BindView(R2.id.group_title_text_view)
    View mGroupTitleTV;
    // 好友标题栏
    @BindView(R2.id.friend_title_text_view)
    View mFriendTitleTV;

    @Autowired
    String title;
    @Autowired
    String content;
    @Autowired
    String imageUrl;
    @Autowired
    int source;
    @Autowired
    String objectId;

    @Inject
    ApiHelper mApiHelper;
    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    AppManager mAppManager;

    private List<SearchBase> mFriendList = new ArrayList<>();
    private List<SearchBase> mGroupList = new ArrayList<>();
    private ShareSource mShareSource;

    public static void start(Context context, String title, String content, String imageUrl, ShareSource source, String objectId) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("imageUrl", imageUrl);
        bundle.putInt("source", source == null ? -1 : source.ordinal());
        bundle.putString("objectId", objectId);
        Intent intent = new Intent(context, WWFriendSearchActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (source != -1) {
            mShareSource = ShareSource.values()[source];
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_share_friend_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
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

        mPresenter.setRootView(this);
    }

    /**
     * 右上角 取消 退出页面
     */
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

    /**
     * 搜索
     */
    private void doSearch() {
        String keyword = mSearchET.getText().toString();

        // 关键字格式判断
//        if (TextUtils.isEmpty(keyword)) {
//            ToastUtil.showToastShort("请输入搜索关键字");
//            return;
//        }

        listFriendAndGroup(EntitiesUtil.assertNotNull(keyword));
    }

    /**
     * 搜索数据
     *
     * @param keyword 关键字
     */
    private void listFriendAndGroup(String keyword) {
        mApiHelper.execute(this,
                netListFriendAndGroup(keyword),
                new ErrorHandleSubscriber<SearchResult>() {
                    @Override
                    public void onNext(SearchResult searchResult) {
                        // 更新好友列表数据
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

                        // 更新群组列表数据
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

    /**
     * 搜索数据
     *
     * @param keyword 关键字
     */
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

    /**
     * 好友选择
     *
     * @param friend 好友信息
     */
    @Override
    public void onFriendClick(SearchFriend friend) {
        mPresenter.sendMessage(String.valueOf(friend.getUserId()), ConversationType.PRIVATE, title, content, imageUrl, objectId, mShareSource);
    }

    /**
     * 群组选择
     *
     * @param group 群组信息
     */
    @Override
    public void onGroupClick(SearchGroup group) {
        String targetId = ImHelper.wangId2ImId(String.valueOf(group.getGroup_id()), ConversationType.SOCIAL);
        mPresenter.sendMessage(targetId, ConversationType.SOCIAL, title, content, imageUrl, objectId, mShareSource);
    }

    @Override
    public void showContacts(List<UIConversation> conversations) {

    }

    @Override
    public void onShareComplete() {
        finish();
        mAppManager.killActivity(ShareRecentlyActivity.class);
    }
}
