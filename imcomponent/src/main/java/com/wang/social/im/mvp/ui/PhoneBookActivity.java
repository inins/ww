package com.wang.social.im.mvp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerPhoneBookComponent;
import com.wang.social.im.di.modules.PhoneBookModule;
import com.wang.social.im.mvp.contract.PhoneBookContract;
import com.wang.social.im.mvp.model.entities.PhoneContact;
import com.wang.social.im.mvp.presenter.PhoneBookPresenter;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.adapters.PhoneContactsAdapter;
import com.wang.social.im.view.indexlist.IndexableAdapter;
import com.wang.social.im.view.indexlist.IndexableHeaderAdapter;
import com.wang.social.im.view.indexlist.IndexableLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 手机通讯录
 */
public class PhoneBookActivity extends BaseAppActivity<PhoneBookPresenter> implements PhoneBookContract.View, PhoneContactsAdapter.OnHandleListener, IndexableAdapter.OnItemContentClickListener<PhoneContact> {

    @BindView(R2.id.pb_toolbar)
    SocialToolbar pbToolbar;
    @BindView(R2.id.pb_fl_friends)
    IndexableLayout pbFlFriends;
    @BindView(R2.id.pb_progress)
    ContentLoadingProgressBar pbProgress;

    @SuppressLint("CheckResult")
    public static void start(Activity activity) {
        new RxPermissions(activity)
                .requestEach(Manifest.permission.READ_CONTACTS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Intent intent = new Intent(activity, PhoneBookActivity.class);
                            activity.startActivity(intent);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtil.showToastShort(activity.getString(R.string.im_toast_contact_permission));
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        pbToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPhoneBookComponent
                .builder()
                .appComponent(appComponent)
                .phoneBookModule(new PhoneBookModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_photo_book;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getContacts(PhoneBookActivity.this);
    }

    @Override
    public void showLoading() {
        pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbProgress.setVisibility(View.GONE);
    }

    @Override
    public void showContacts(List<PhoneContact> contacts) {
        pbFlFriends.setLayoutManager(new LinearLayoutManager(this));
        PhoneContactsAdapter adapter = new PhoneContactsAdapter(this, this);
        pbFlFriends.setAdapter(adapter);
        adapter.setDatas(contacts);
        pbFlFriends.showAllLetter(false);
        pbFlFriends.setOverlayStyle_MaterialDesign(ContextCompat.getColor(this, R.color.common_colorAccent));
        pbFlFriends.setCompareMode(IndexableLayout.MODE_CHINESE);

        adapter.setOnItemContentClickListener(this);
        pbFlFriends.addHeaderAdapter(new HeaderAdapter(Arrays.asList("")));
    }

    @Override
    public void onHandle(PhoneContact contact) {
        if (!contact.isJoined()) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + contact.getPhoneNumber()));
            String body = String.format(IMConstants.CONTENT_INVITE_JOIN_APP, AppDataHelper.getUser().getNickname());
            intent.putExtra("sms_body", body);
            startActivity(intent);
        } else {
            DialogValiRequest.showDialog(this, new DialogValiRequest.OnSureCallback() {
                @Override
                public void onOkClick(String content) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToastShort("请输入申请理由");
                        return;
                    }
                    mPresenter.friendRequest(contact.getUserId(), content);
                }
            });
        }
    }

    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, PhoneContact entity) {
        if (entity.isJoined()) {
            PersonalCardActivity.start(this, Integer.parseInt(entity.getUserId()));
        }
    }

    private class HeaderAdapter extends IndexableHeaderAdapter {

        public HeaderAdapter(List datas) {
            super(null, null, datas);
        }

        @Override
        public int getItemViewType() {
            return 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(PhoneBookActivity.this).inflate(R.layout.im_view_header_phone_book, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {

        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}