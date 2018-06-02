package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerInviteFriendComponent;
import com.wang.social.im.di.modules.InviteFriendModule;
import com.wang.social.im.mvp.contract.InviteFriendContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.presenter.InviteFriendPresenter;
import com.wang.social.im.mvp.ui.adapters.InviteFriendAdapter;
import com.wang.social.im.view.indexlist.IndexableAdapter;
import com.wang.social.im.view.indexlist.IndexableLayout;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.frame.utils.Utils.getContext;

/**
 * 邀请好友
 */
public class InviteFriendActivity extends BaseAppActivity<InviteFriendPresenter> implements InviteFriendContract.View, IndexableAdapter.OnItemContentClickListener<IndexFriendInfo> {

    @BindView(R2.id.if_tv_invite)
    TextView ifTvInvite;
    @BindView(R2.id.if_il_friends)
    IndexableLayout ifIlFriends;

    @Autowired
    String socialId;

    private InviteFriendAdapter mAdapter;

    public static void start(Context context, String socialId) {
        Intent intent = new Intent(context, InviteFriendActivity.class);
        intent.putExtra("socialId", socialId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInviteFriendComponent
                .builder()
                .appComponent(appComponent)
                .inviteFriendModule(new InviteFriendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_invite_friend;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getInviteFriendList(socialId);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @OnClick(R2.id.if_tv_invite)
    public void onViewClicked() {
        List<IndexFriendInfo> selectItems = mAdapter.getSelectItems();
        mPresenter.sendInvite(socialId, selectItems);
    }

    @Override
    public void showFriends(List<IndexFriendInfo> friends) {
        ifIlFriends.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InviteFriendAdapter(this);
        mAdapter.setOnItemContentClickListener(this);
        ifIlFriends.setAdapter(mAdapter);
        mAdapter.setDatas(friends);
        ifIlFriends.showAllLetter(false);
        ifIlFriends.setOverlayStyle_MaterialDesign(ContextCompat.getColor(getContext(), R.color.common_colorAccent));
        ifIlFriends.setCompareMode(IndexableLayout.MODE_FAST);
    }

    @Override
    public void inviteComplete() {
        ToastUtil.showToastShort("邀请成功");
        finish();
    }

    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, IndexFriendInfo entity) {
        entity.setSelected(!entity.isSelected());
        View view = v.findViewById(R.id.iif_cb_check);
        if (view instanceof AppCompatCheckBox) {
            ((AppCompatCheckBox) view).setChecked(entity.isSelected());
        } else {
            mAdapter.notifyDataSetChanged();
        }

        showSelectSize();
    }

    private void showSelectSize() {
        int selectSize = mAdapter.getSelectItems().size();
        if (selectSize > 0) {
            String text = UIUtil.getString(R.string.im_send_invite) + String.format(Locale.getDefault(), "(%d)", selectSize);
            ifTvInvite.setText(text);
            ifTvInvite.setEnabled(true);
        } else {
            ifTvInvite.setEnabled(false);
            ifTvInvite.setText(UIUtil.getString(R.string.im_send_invite));
        }
    }
}