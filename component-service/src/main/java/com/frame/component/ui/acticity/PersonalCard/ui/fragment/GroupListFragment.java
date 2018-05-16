package com.frame.component.ui.acticity.PersonalCard.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.PersonalCard.model.api.PersonalCardService;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.GroupBeanDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.GroupBean;
import com.frame.component.ui.acticity.PersonalCard.ui.adapter.TalkAdapter;
import com.frame.component.ui.acticity.PersonalCard.ui.widget.ThumbnailDecoration;
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
import com.frame.utils.SizeUtils;
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

public class GroupListFragment extends BasicFragment implements IView {

    public static GroupListFragment newInstance(int userId) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private TalkAdapter mAdapter;

    private int mUserId;
    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private RxErrorHandler mErrorHandler;
    private int mCurrent = 0;
    private int mSize = 10;
    private List<GroupBean> mList = new ArrayList<>();

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_card_fragment_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();

        mUserId = getArguments().getInt("userid");

        mAdapter = new TalkAdapter(mRecyclerView, mList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(
                new ThumbnailDecoration(SizeUtils.dp2px(10),
                        Color.TRANSPARENT,
                        true));
        mRecyclerView.setAdapter(mAdapter);

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadGroupList(true);
            }

            @Override
            public void onLoadmore() {
                loadGroupList(false);
            }
        });
        mSpringView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void loadGroupList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
        mApiHelper.execute(this,
                getGroupList(mUserId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<GroupBean>>() {
                    @Override
                    public void onNext(PageList<GroupBean> list) {
                        if (null != list && null != list.getList()) {
                            mList.addAll(list.getList());
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

    private Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> getGroupList(int queryUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("queryUserId", queryUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getGroupList(param);
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
}
