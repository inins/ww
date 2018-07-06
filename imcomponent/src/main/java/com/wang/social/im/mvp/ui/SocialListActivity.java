package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.frame.component.entities.AutoPopupItemModel;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.AutoPopupWindow;
import com.frame.di.component.AppComponent;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerSocialListComponent;
import com.wang.social.im.di.modules.SocialListModule;
import com.wang.social.im.mvp.contract.SocialListContract;
import com.wang.social.im.mvp.model.entities.SocialListLevelOne;
import com.wang.social.im.mvp.presenter.SocialListPresenter;
import com.wang.social.im.mvp.ui.adapters.socials.SocialListAdapter;
import com.wang.social.im.mvp.ui.fragments.ContactsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 趣聊列表
 */
public class SocialListActivity extends BaseAppActivity<SocialListPresenter> implements SocialListContract.View, AutoPopupWindow.OnItemClickListener {

    @BindView(R2.id.sl_iv_add)
    ImageView slIvAdd;
    @BindView(R2.id.sl_rlv_socials)
    RecyclerView slRlvSocials;

    private AutoPopupWindow popupWindow;

    private SocialListAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SocialListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialListComponent
                .builder()
                .appComponent(appComponent)
                .socialListModule(new SocialListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getSocials(true);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showSocials(List<SocialListLevelOne> socials) {
        List list = new ArrayList(socials);
        if (mAdapter != null) {
            mAdapter.updateData(list);
        } else {
            slRlvSocials.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new SocialListAdapter(list);
            slRlvSocials.setAdapter(mAdapter);
        }
    }

    @OnClick({R2.id.sl_iv_search, R2.id.sl_iv_add})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sl_iv_search) {
            SearchActivityV2.start(this);
        } else if (view.getId() == R.id.sl_iv_add) {
            if (popupWindow == null) {
                popupWindow = new AutoPopupWindow(this, getMenuItems(), AutoPopupWindow.POINT_TO_RIGHT);
                popupWindow.setItemClickListener(this);
            }
            if (!popupWindow.isShowing()) {
                int showX = ScreenUtils.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.popup_auto_width) - SizeUtils.dp2px(5);
                popupWindow.showAsDropDown(slIvAdd, showX, -SizeUtils.dp2px(15));
            }
        }
    }

    private List<AutoPopupItemModel> getMenuItems() {
        List<AutoPopupItemModel> items = new ArrayList<>();
        AutoPopupItemModel createModel = new AutoPopupItemModel(0, R.string.im_create_social);
        AutoPopupItemModel createTeamModel = new AutoPopupItemModel(0, R.string.im_create_team);
        AutoPopupItemModel scanModel = new AutoPopupItemModel(0, R.string.im_scan);
        AutoPopupItemModel contactsModel = new AutoPopupItemModel(0, R.string.im_contacts);
        items.add(createModel);
        items.add(createTeamModel);
        items.add(scanModel);
        items.add(contactsModel);
        return items;
    }

    @Override
    public void onItemClick(AutoPopupWindow popupWindow, int resId) {
        popupWindow.dismiss();
        if (resId == R.string.im_create_social) {
            CreateSocialActivity.start(this);
        } else if (resId == R.string.im_scan) {
            ScanActivity.start(this);
        } else if (resId == R.string.im_contacts) {
            PhoneBookActivity.start(this);
        } else if (resId == R.string.im_create_team) {
            CTSocialListActivity.start(this);
        }
    }
}
