package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicFragment;
import com.frame.component.entities.AutoPopupItemModel;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.dialog.AutoPopupWindow;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.ConnectionStatus;
import com.wang.social.im.helper.FriendShipHelper;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.ui.ConversationListFragment;
import com.wang.social.im.mvp.ui.CreateSocialActivity;
import com.wang.social.im.mvp.ui.PhoneBookActivity;
import com.wang.social.im.mvp.ui.ScanActivity;
import com.wang.social.im.mvp.ui.adapters.FragmentAdapter;
import com.wang.social.im.view.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * 往来
 * <p>
 * Create by ChenJing on 2018-05-07 17:42
 * ============================================
 */
public class ContactsFragment extends BasicFragment implements AutoPopupWindow.OnItemClickListener {

    @BindView(R2.id.fc_iv_search)
    ImageView fcIvSearch;
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

        fcTabLayout.setViewPager(fcViewpager);

        ((TextView) fcTabLayout.getTabAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        fcTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        ((TextView) fcTabLayout.getTabAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    } else {
                        ((TextView) fcTabLayout.getTabAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
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

    @Override
    public void onItemClick(AutoPopupWindow popupWindow, int resId) {
        popupWindow.dismiss();
        if (resId == R.string.im_create_social) {
            CreateSocialActivity.start(getContext());
        } else if (resId == R.string.im_scan) {
            ScanActivity.start(getActivity());
        } else if (resId == R.string.im_contacts) {
            PhoneBookActivity.start(getActivity());
        }
    }
}
