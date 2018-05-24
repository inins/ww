package com.wang.social.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.frame.component.helper.MsgHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.XRadioGroup;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterHome;
import com.wang.social.mvp.ui.dialog.DialogHomeAdd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

@RouteNode(path = "/main", desc = "首页")
public class HomeActivity extends BasicAppNoDiActivity implements XRadioGroup.OnCheckedChangeListener {

    @BindView(R.id.group_tab)
    XRadioGroup groupTab;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.img_dot)
    ImageView imgDot;
    @BindView(R.id.text_dot)
    TextView textDot;


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
            case EventBean.EVENT_MSG_READALL:
                //消息已经全部阅读
                imgDot.setVisibility(View.GONE);
                break;
            case EventBean.EVENT_NOTIFY_MESSAGE_UNREAD:
                int count = (int) event.get("count");
                if (count > 0) {
                    textDot.setVisibility(View.VISIBLE);
                    String showText;
                    if (count > 99) {
                        showText = UIUtil.getString(com.wang.social.im.R.string.im_cvs_unread_max);
                    } else {
                        showText = String.valueOf(count);
                    }
                    textDot.setText(String.valueOf(showText));
                } else {
                    textDot.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(SystemMessage msg) {
        imgDot.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(DynamicMessage msg) {
        imgDot.setVisibility(View.VISIBLE);
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

        imgDot.setVisibility(MsgHelper.hasReadAllNotify() ? View.GONE : View.VISIBLE);
    }

    //tab动作，切换viewpager
    @Override
    public void onCheckedChanged(XRadioGroup radioGroup, int checkedId) {
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
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_HOME));
                    break;
                case R.id.tab_2:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_white);
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_WL));
                    break;
                case R.id.tab_3:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, R.color.common_white);
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_PLAZA));
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
        Fragment personalFragment = pagerAdapter.getPersonalFragment();
        if (personalFragment != null)
            personalFragment.onActivityResult(requestCode, resultCode, data);
    }
}
