package com.wang.social.im.mvp.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicFragment;
import com.frame.component.entities.AutoPopupItemModel;
import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.frame.component.helper.MsgHelper;
import com.frame.component.ui.dialog.AutoPopupWindow;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.ConversationListFragment;
import com.wang.social.im.mvp.ui.CreateSocialActivity;
import com.wang.social.im.mvp.ui.PhoneBookActivity;
import com.wang.social.im.mvp.ui.ScanActivity;
import com.wang.social.im.mvp.ui.SearchActivity;
import com.wang.social.im.mvp.ui.adapters.FragmentAdapter;
import com.wang.social.im.view.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

import static com.frame.entities.EventBean.EVENT_NOTIFY_SHOW_CONVERSATION_LIST;

/**
 * ============================================
 * 往来
 * <p>
 * Create by ChenJing on 2018-05-07 17:42
 * ============================================
 */
public class ContactsFragment extends BasicFragment implements AutoPopupWindow.OnItemClickListener {

    @BindView(R2.id.fc_iv_more)
    ImageView fcIvMore;
    @BindView(R2.id.fc_tab_layout)
    SmartTabLayout fcTabLayout;
    @BindView(R2.id.fc_viewpager)
    NoScrollViewPager fcViewpager;

    private AutoPopupWindow popupWindow;

    public static ContactsFragment newInstance() {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_contacts;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<String> titles = new ArrayList<>();
        titles.add(UIUtil.getString(R.string.im_chat));
        titles.add(UIUtil.getString(R.string.im_friend));
        titles.add(UIUtil.getString(R.string.im_notify));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ConversationListFragment.newInstance());
        fragments.add(FriendsFragment.newInstance());
        fragments.add(NotifyFragment.newInstance());

        fcViewpager.setAdapter(new FragmentAdapter(getFragmentManager(), fragments, titles));

        fcTabLayout.setCustomTabView(R.layout.im_view_contacts_tab, R.id.ct_tv_name);
        fcTabLayout.setViewPager(fcViewpager);

        setSelectStatus(fcTabLayout.getTabAt(0));
        toggleNotifyUnread(!MsgHelper.hasReadAllNotify());

        fcTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        setSelectStatus(fcTabLayout.getTabAt(i));
                    } else {
                        setUnSelectStatus(fcTabLayout.getTabAt(i));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Optional
    @OnClick({R2.id.fc_iv_more, R2.id.fc_iv_search})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.fc_iv_more) {
            if (popupWindow == null) {
                popupWindow = new AutoPopupWindow(getContext(), getMenuItems(), AutoPopupWindow.POINT_TO_RIGHT);
                popupWindow.setItemClickListener(ContactsFragment.this);
            }
            if (!popupWindow.isShowing()) {
                int showX = ScreenUtils.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.popup_auto_width) - SizeUtils.dp2px(5);
                popupWindow.showAsDropDown(fcIvMore, showX, -SizeUtils.dp2px(15));
            }
        } else if (view.getId() == R.id.fc_iv_search) {
            SearchActivity.start(getActivity());
        }
    }

    private List<AutoPopupItemModel> getMenuItems() {
        List<AutoPopupItemModel> items = new ArrayList<>();
        AutoPopupItemModel createModel = new AutoPopupItemModel(0, R.string.im_create_social);
        AutoPopupItemModel scanModel = new AutoPopupItemModel(0, R.string.im_scan);
        AutoPopupItemModel contactsModel = new AutoPopupItemModel(0, R.string.im_contacts);
        items.add(createModel);
        items.add(scanModel);
        items.add(contactsModel);
        return items;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onItemClick(AutoPopupWindow popupWindow, int resId) {
        popupWindow.dismiss();
        if (resId == R.string.im_create_social) {
            CreateSocialActivity.start(getContext());
        } else if (resId == R.string.im_scan) {
            new RxPermissions(getActivity())
                    .requestEach(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                ScanActivity.start(getActivity());
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                ToastUtil.showToastShort("请在设置中打开相机权限");
                            }
                        }
                    });
        } else if (resId == R.string.im_contacts) {
            PhoneBookActivity.start(getActivity());
        }
    }

    private void setSelectStatus(View view) {
        TextView tvName = view.findViewById(R.id.ct_tv_name);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvName.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = SizeUtils.dp2px(100);
        tvName.setLayoutParams(layoutParams);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
    }

    private void setUnSelectStatus(View view) {
        TextView tvName = view.findViewById(R.id.ct_tv_name);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvName.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvName.setLayoutParams(layoutParams);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }

    /**
     * 显示未读消息数量
     *
     * @param count
     */
    private void showMessageUnreadCount(int count) {
        TextView tvUnread = fcTabLayout.getTabAt(0).findViewById(R.id.ct_tv_unread);
        if (count > 0) {
            tvUnread.setVisibility(View.VISIBLE);
            String showText;
            if (count > 99) {
                showText = UIUtil.getString(R.string.im_cvs_unread_max);
            } else {
                showText = String.valueOf(count);
            }
            tvUnread.setText(String.valueOf(showText));
        } else {
            tvUnread.setVisibility(View.GONE);
        }
    }

    private void toggleNotifyUnread(boolean show) {
        ImageView ivUnread = fcTabLayout.getTabAt(2).findViewById(R.id.ct_iv_unread_point);
        if (show) {
            ivUnread.setVisibility(View.VISIBLE);
        } else {
            ivUnread.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENT_NOTIFY_MESSAGE_UNREAD) {
            int count = (int) event.get("count");
            showMessageUnreadCount(count);
        } else if (event.getEvent() == EventBean.EVENT_MSG_READALL) {
            toggleNotifyUnread(false);
        } else if (event.getEvent() == EVENT_NOTIFY_SHOW_CONVERSATION_LIST) {
            fcViewpager.setCurrentItem(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(SystemMessage msg) {
        toggleNotifyUnread(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent(DynamicMessage msg) {
        toggleNotifyUnread(true);
    }
}