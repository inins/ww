package com.frame.component.ui.acticity.PersonalCard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.entities.Topic;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.PersonalCard.model.api.PersonalCardService;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.TopicDTO;
import com.frame.component.ui.acticity.PersonalCard.ui.adapter.TopicListAdapter;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class TopicListFragment extends BasicFragment implements IView, TopicListAdapter.ClickListener {

    public static TopicListFragment newInstance(int userid) {
        TopicListFragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private RxErrorHandler mErrorHandler;
    private int mCurrent = 0;
    private int mSize = 10;
    private List<Topic> mList = new ArrayList<>();
    private int mUserId;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_card_fragment_list_side_14;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();

        if (getArguments() != null) {
            mUserId = getArguments().getInt("userid");
        }

        mAdapter = new TopicListAdapter(this, mRecyclerView, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadTopicList(true);
            }

            @Override
            public void onLoadmore() {
                loadTopicList(false);
            }
        });
        mSpringView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void loadTopicList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
        mApiHelper.execute(this,
                getTopicList(mUserId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<Topic>>() {
                    @Override
                    public void onNext(PageList<Topic> pageList) {
                        if (null != pageList) {
                            mCurrent = pageList.getCurrent();
                            if (null != pageList.getList()) {
                                mList.addAll(pageList.getList());
                            }
                        }

                        if (null != mAdapter) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        mSpringView.onFinishFreshAndLoadDelay();
                    }
                });
    }

    private Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getTopicList(int queryUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("queryUserId", queryUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getFriendTopicList(param);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }

    @Override
    public void onTopicClick(Topic topic) {
        // 跳转到详情
        ToastUtil.showToastLong("话题详情");
    }
}
