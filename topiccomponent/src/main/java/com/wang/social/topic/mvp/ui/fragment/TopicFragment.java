package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseFragment;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicComponent;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.TopicTopUser;
import com.wang.social.topic.mvp.presenter.TopicPresenter;
import com.wang.social.topic.mvp.ui.SearchActivity;
import com.wang.social.topic.mvp.ui.TopUserActivity;
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
    @BindView(R2.id.selected_tag_1_text_view)
    TextView mSelectedTag1TV;
    @BindView(R2.id.selected_tag_2_text_view)
    TextView mSelectedTag2TV;
    @BindView(R2.id.selected_tag_3_text_view)
    TextView mSelectedTag3TV;
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

        // 跳转到知识魔页面
        mBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopUserActivity.start(getActivity());
            }
        });

        initTopicList();

        // 加载知识魔
        mPresenter.getReleaseTopicTopUser(true);

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

    /**
     * 标签选择
     */
    @OnClick(R2.id.select_tag_image_view)
    public void selectTag() {
//        UIRouter.getInstance().openUri(getActivity(), LoginPath.LOGIN_TAG_SELECTION_URL, null);
        TagSelectionActivity.startSelection(getActivity(), TagSelectionActivity.TAG_TYPE_INTEREST);
    }

    /**
     * 搜索
     */
    @OnClick({R2.id.search_layout, R2.id.tab_layout_search_image_view})
    public void search() {
//        mPresenter.search();
        // 跳转到搜索页面
        SearchActivity.start(getActivity());
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
                return TopicListFragment.newInstance(position);
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
    public void onMyRecommendTagListLoad(List<Tag> list) {
        mSelectedTag1TV.setVisibility(View.GONE);
        mSelectedTag2TV.setVisibility(View.GONE);
        mSelectedTag3TV.setVisibility(View.GONE);

        for (int i = 0; i < Math.min(3, list.size()); i++) {
            Tag tag = list.get(i);
            switch (i) {
                case 0:
                    mSelectedTag1TV.setVisibility(View.VISIBLE);
                    mSelectedTag1TV.setText(tag.getTagName());
                    break;
                case 1:
                    mSelectedTag2TV.setVisibility(View.VISIBLE);
                    mSelectedTag2TV.setText(tag.getTagName());
                    break;
                case 2:
                    mSelectedTag3TV.setVisibility(View.VISIBLE);
                    mSelectedTag3TV.setText(tag.getTagName());
                    break;
            }
        }
    }

    @Override
    public void onTopicTopUserLoadSuccess() {

//        mBarView.refreshData(new ArrayList<BarUser>() {{
//            add(new BarUser("http://i-7.vcimg.com/trim/48b866104e7efc1ffd7367e7423296c11060910/trim.jpg"));
//            add(new BarUser("https://tse3-mm.cn.bing.net/th?id=OIP.XzZcrXAIrxTtUH97rMlNGQHaEo&p=0&o=5&pid=1.1"));
//            add(new BarUser("http://photos.tuchong.com/23552/f/624083.jpg"));
//        }});


        List<BarUser> list = new ArrayList<>();
        for (int i = 0; i < Math.min(5, mPresenter.getTopicTopUserList().size()); i++) {
            TopicTopUser user = mPresenter.getTopicTopUserList().get(i);
            list.add(new BarUser(user.getAvatar()));
        }
        mBarView.refreshData(list);
    }

    @Override
    public void onTopicTopUserLoadCompleted() {

    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_UPDATED:
                // 加载标签数据
                mPresenter.myRecommendTag();
                break;
        }
    }
}
