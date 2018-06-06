package com.wang.social.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.MsgHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.XRadioGroup;
import com.frame.entities.EventBean;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterHome;
import com.wang.social.mvp.ui.dialog.DialogHomeAdd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.os.Build;

import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

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

    public static void start(Context context, String target, String targetId) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("target", target);
        intent.putExtra("targetId", targetId);
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
                        showText = "99+";
                    } else {
                        showText = String.valueOf(count);
                    }
                    textDot.setText(showText);
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
        StatusBarUtil.setStatusBarColor(HomeActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? R.color.common_white : R.color.common_dark);
        StatusBarUtil.setTextDark(this);
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

        remoteCall();
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
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? R.color.common_white : R.color.common_dark);
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_HOME));
                    break;
                case R.id.tab_2:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? R.color.common_white : R.color.common_dark);
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_TAB_WL));
                    break;
                case R.id.tab_3:
                    StatusBarUtil.setStatusBarColor(HomeActivity.this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? R.color.common_white : R.color.common_dark);
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            remoteCall();
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Timber.i("onNewIntent");
    }

    /**
     * 远程跳转页面
     */
    private void remoteCall() {
        String target = getIntent().getStringExtra("target");
        String id = getIntent().getStringExtra("targetId");

        performRemoteCall(target, id);
    }

    public void performRemoteCall(String target, String id) {
        int intId = -1;
        try {
            intId = Integer.parseInt(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(target)) {
            switch (target) {
                case AppConstant.Share.SHARE_TOPIC_OPEN_TARGET:
                    CommonHelper.TopicHelper.startTopicDetail(this, intId);
                    break;
                case AppConstant.Share.SHARE_FUN_SHOW_OPEN_TARGET:
                    CommonHelper.FunshowHelper.startDetailActivity(this, intId);
                    break;
                case AppConstant.Share.SHARE_GROUP_OPEN_TARGET:
                    CommonHelper.ImHelper.startGroupInviteBrowse(this, intId);
                    break;
                case AppConstant.Share.SHARE_GAME_TREE_OPEN_TARGET:
                    CommonHelper.GameHelper.startGameRoom(this, intId);
                    break;
                case AppConstant.Key.OPEN_TARGET_SYS_MESSAGE: //系统消息
                    CommonHelper.ImHelper.startNotifySysMsgActivity(this);
                    break;
                case AppConstant.Key.OPEN_TARGET_FRIEND_APPLY: //好友申请
                    CommonHelper.ImHelper.startNotifyFriendRequestActivity(this);
                    break;
                case AppConstant.Key.OPEN_TARGET_GROUP_APPLY: //群申请
                    CommonHelper.ImHelper.startNotifyGroupRequestActivity(this);
                    break;
                case AppConstant.Key.OPEN_TARGET_GROUP_INVITE: //群邀请
                    CommonHelper.ImHelper.startNotifyGroupJoinActivity(this);
                    break;
                case AppConstant.Key.OPEN_TARGET_DYNAMIC_FUN_SHOW: //趣晒
                    CommonHelper.FunshowHelper.startDetailActivity(this, intId);
                    break;
                case AppConstant.Key.OPEN_TARGET_DYNAMIC_TOPIC: //话题
                    CommonHelper.TopicHelper.startTopicDetail(this, intId);
                    break;
                case AppConstant.Share.SHARE_PROFIT_OPEN_TARGET: //代言收益
                    CommonHelper.PersonalHelper.startProfitActivity(this);
                    break;
            }
        }
    }
}
