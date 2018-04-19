package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseFragment;
import com.frame.component.path.LoginPath;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicComponent;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.presenter.TopicPresenter;
import com.wang.social.topic.mvp.ui.adapter.SelectedTagAdapter;
import com.wang.social.topic.mvp.ui.widget.AppBarStateChangeListener;
import com.wang.social.topic.mvp.ui.widget.GradualImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TopicFragment extends BaseFragment<TopicPresenter> implements TopicContract.View {

    @BindView(R2.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    // 选中标签列表
    @BindView(R2.id.selected_recycler_view)
    RecyclerView mRecyclerView;
    SelectedTagAdapter mSelectedTagAdapter;
    // 最新 最热 无人问津 话题列表类型 Tab
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    // TabLayout 后面的搜索图标，TabLayout停靠顶部时显示
    @BindView(R2.id.tab_layout_search_image_view)
    GradualImageView mTabLayoutSearchIV;

    @BindView(R2.id.view_pager)
    ViewPager mViewPager;
    final int[] TAB_TITLES = {
            R.string.topic_newest,
            R.string.topic_hottest,
            R.string.topic_no_interest
    };
    @BindView(R2.id.barview)
    BarView mBarView;


    public static TopicFragment newInstance() {
        Bundle args = new Bundle();
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.topic_fragment_topic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // AppBarLayout 变化监听
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onRateChanged(float rate) {
                // 蓝色搜索按钮显示隐藏
                mTabLayoutSearchIV.setRate(rate);
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mTabLayoutSearchIV.setEnabled(false);
                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    mTabLayoutSearchIV.setEnabled(true);
                } else {
                    //中间状态
                }
            }
        });

        initSelectedTagData();
        initTopicList();

        // 加载知识魔
        mPresenter.getReleaseTopicTopUser();

        // 加载标签数据
        mPresenter.myRecommendTag();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTopicComponent.builder()
                .appComponent(appComponent)
                .topicModule(new TopicModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R2.id.select_tag_image_view)
    public void selectTag() {
        UIRouter.getInstance().openUri(getActivity(), LoginPath.LOGIN_TAG_SELECTION_URL, null);
    }

    /**
     * 搜索
     */
    @OnClick({R2.id.search_layout, R2.id.tab_layout_search_image_view})
    public void search() {
        mPresenter.search();
    }


    /**
     * 初始化已选标签信息列表
     */
    private void initSelectedTagData() {
        mSelectedTagAdapter = new SelectedTagAdapter(getContext(), new SelectedTagAdapter.DataProvider() {
            @Override
            public int getItemCount() {
                return mPresenter.getSelectedTagCount();
            }

            @Override
            public String getName(int position) {
                return mPresenter.getSelectedTagName(position);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSelectedTagAdapter);
    }

    /**
     * 初始化话题列表
     */
    private void initTopicList() {
        mTabLayout.removeAllTabs();
        for (int resId : TAB_TITLES) {
            mTabLayout.addTab(mTabLayout.newTab().setText(getString(resId)));
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TopicListFragment.newInstance();
            }

            @Override
            public int getCount() {
                return TAB_TITLES.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(TAB_TITLES[position]);
            }
        });
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void refreshSelectedTagLise() {
        if (null != mSelectedTagAdapter) {
            mSelectedTagAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTopicTopUserLoaded(List<BarUser> list) {

//        mBarView.refreshData(new ArrayList<BarUser>() {{
//            add(new BarUser("http://i-7.vcimg.com/trim/48b866104e7efc1ffd7367e7423296c11060910/trim.jpg"));
//            add(new BarUser("https://tse3-mm.cn.bing.net/th?id=OIP.XzZcrXAIrxTtUH97rMlNGQHaEo&p=0&o=5&pid=1.1"));
//            add(new BarUser("http://photos.tuchong.com/23552/f/624083.jpg"));
//        }});
        mBarView.refreshData(list);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }
}
