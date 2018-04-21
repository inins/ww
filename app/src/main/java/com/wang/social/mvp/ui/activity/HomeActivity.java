package com.wang.social.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.frame.component.service.personal.PersonalFragmentInterface;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterHome;
import com.wang.social.mvp.ui.dialog.DialogHomeAdd;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

@RouteNode(path = "/main", desc = "首页")
public class HomeActivity extends BasicAppActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.group_tab)
    RadioGroup groupTab;
    @BindView(R.id.pager)
    ViewPager pager;

    private DialogHomeAdd dialogHomeAdd;
    private PagerAdapterHome pagerAdapter;
    private int[] tabsId = new int[]{R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4};

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_LOGOUT:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(this, R.color.common_white);
        setNeedDoubleClickExit(true);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        dialogHomeAdd = new DialogHomeAdd(this);
        groupTab.setOnCheckedChangeListener(this);
        pagerAdapter = new PagerAdapterHome(getSupportFragmentManager());
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(onPageChangeListener);
    }

    //tab动作，切换viewpager
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < tabsId.length; i++) {
            if (tabsId[i] == checkedId) {
                pager.setCurrentItem(i, false);
            }
        }
    }

    //页面切换事件
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (tabsId[position]) {
                case R.id.tab_1:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_white);
                    break;
                case R.id.tab_2:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_white);
                    break;
                case R.id.tab_3:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_white);
                    break;
                case R.id.tab_4:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_blue_deep);
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_USER));
                    break;
            }
        }
    };

    //中间按钮点击事件
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                dialogHomeAdd.show();
                break;
        }
    }

    //友盟回调需要此处进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currentFragment = pagerAdapter.getCurrentFragment();
        if (currentFragment instanceof PersonalFragmentInterface) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
