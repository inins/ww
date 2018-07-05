package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.Topic;
import com.frame.component.entities.dto.MyTalkBeanDTO;
import com.frame.component.entities.dto.TalkBeanDTO;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.adapter.RecycleAdapterCommonFunshow;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;

public class TalkListFragment extends BasicFragment implements IView {

    public static TalkListFragment newInstance(int userid) {
        TalkListFragment fragment = new TalkListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private RecycleAdapterCommonFunshow mAdapter;

    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;


    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;
    private int mCurrent = 0;
    private final static int mSize = 10;
    private List<Topic> mList = new ArrayList<>();
    private int mUserId;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_spring_recycler;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(getContext()).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(getContext()).repoitoryManager();

        if (getArguments() != null) {
            mUserId = getArguments().getInt("userid");
        }

        mAdapter = new RecycleAdapterCommonFunshow();
        mAdapter.registEventBus();
        mAdapter.setShowMoreBtn(false);
        mAdapter.setShowHeader(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }

            @Override
            public void onLoadmore() {
                loadData(false);
            }
        });
        mSpringView.callFreshDelay();
    }

    private void loadData(boolean refresh) {
        if (null != AppDataHelper.getUser() &&
                mUserId == AppDataHelper.getUser().getUserId()) {
            getMyTalkList(refresh);
        } else {
            getFriendTalkList(refresh);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void updateList(boolean refresh, PageList<FunshowBean> list) {
        if (null != list) {
            mCurrent = list.getCurrent();

            if (refresh) {
                mAdapter.refreshData(list.getList());
            } else {
                mAdapter.addItem(list.getList());
            }

            loadingviewEx.showOut();

            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        }

        if (refresh && (null == list || list.getList().size() <= 0)) {
            loadingviewEx.showFailViewCustomize(R.drawable.common_ic_default_nofunshow,
                    "还没有发布趣晒");
        }
    }

    /**
     * 搜索用户趣聊列表
     *
     * @param refresh 是否刷新
     */
    private void getFriendTalkList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
        mApiHelper.execute(this,
                netGetFriendTalkList(mUserId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<FunshowBean>>() {
                    @Override
                    public void onNext(PageList<FunshowBean> list) {
                        updateList(refresh, list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    private Observable<BaseJson<PageListDTO<TalkBeanDTO, FunshowBean>>> netGetFriendTalkList(int queryUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("queryUserId", queryUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .getFriendTalkList(param);
    }


    /**
     * 用户趣晒列表-我的个人名片
     *
     * @param refresh 是否刷新
     */
    private void getMyTalkList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
        mApiHelper.execute(this,
                netGetMyTalkList(mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<FunshowBean>>() {
                    @Override
                    public void onNext(PageList<FunshowBean> list) {
                        updateList(refresh, list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    /**
     * 用户趣晒列表-我的个人名片
     */
    private Observable<BaseJson<PageListDTO<MyTalkBeanDTO, FunshowBean>>> netGetMyTalkList(int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .getMyTalkList(param);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiHelper = null;
        if (null != mAdapter) {
            mAdapter.unRegistEventBus();
        }
    }
}
