package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseFragment;
import com.frame.component.ui.dialog.DialogLoading;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerFriendsComponent;
import com.wang.social.im.di.modules.FriendsModule;
import com.wang.social.im.mvp.contract.FriendsContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.presenter.FriendsPresenter;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.SocialListActivity;
import com.wang.social.im.mvp.ui.adapters.FriendsAdapter;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 19:50
 * ============================================
 */
public class FriendsFragment extends BaseFragment<FriendsPresenter> implements FriendsContract.View, IndexableAdapter.OnItemContentClickListener<IndexFriendInfo>, FriendsAdapter.OnHandleListener {

    @BindView(R2.id.ff_fl_friends)
    IndexableLayout ffFlFriends;
    @BindView(R2.id.ff_progress)
    ContentLoadingProgressBar ffProgress;

    private WeakReference<DialogLoading> mLoading;

    private FriendsAdapter mAdapter;

    public static FriendsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFriendsComponent
                .builder()
                .appComponent(appComponent)
                .friendsModule(new FriendsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_friends;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getFriendsList();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        ffProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        ffProgress.setVisibility(View.GONE);
    }

    @Override
    public void showFriends(List<IndexFriendInfo> friends) {
//        if (friends.isEmpty()) {
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.add(R.id.ff_fragment, NobodyFragment.newInstance(), NobodyFragment.class.getName());
//            transaction.commitAllowingStateLoss();
//        } else {
        if (mAdapter == null) {
            ffFlFriends.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new FriendsAdapter(getContext(), this);
            mAdapter.setOnItemContentClickListener(this);
            ffFlFriends.setAdapter(mAdapter);
            mAdapter.setDatas(friends);
            ffFlFriends.showAllLetter(false);
            ffFlFriends.setOverlayStyle_MaterialDesign(ContextCompat.getColor(getContext(), R.color.common_colorAccent));
            ffFlFriends.setCompareMode(IndexableLayout.MODE_FAST);

            ffFlFriends.addHeaderAdapter(new HeaderAdapter(Arrays.asList(""), friends.size()));
        } else {
            mAdapter.setDatas(friends);
            mAdapter.notifyDataSetChanged();
        }
//        }
    }

    @Override
    public void onFriendDeleted(IndexFriendInfo friendInfo) {
        mAdapter.getItems().remove(friendInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDialogLoading() {
        if (mLoading == null || mLoading.get() == null) {
            mLoading = new WeakReference<>(new DialogLoading(getActivity()));
        }
        mLoading.get().show();
    }

    @Override
    public void hideDialogLoading() {
        if (mLoading != null && mLoading.get() != null) {
            mLoading.get().dismiss();
        }
    }

    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, IndexFriendInfo entity) {
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENT_NOTIFY_FRIEND_ADD ||
                event.getEvent() == EventBean.EVENTBUS_FRIEND_DELETE) {
            mPresenter.getFriendsList();
        }
    }

    @Override
    public void onDelete(IndexFriendInfo friendInfo) {
        String showMessage = UIUtil.getString(R.string.im_friend_delete_tip, friendInfo.getNickname());
        DialogSure.showDialog(getContext(), showMessage, new DialogSure.OnSureCallback() {
            @Override
            public void onOkClick() {
                mPresenter.deleteFriend(friendInfo);
            }
        });
    }

    @Override
    public void onItemClick(IndexFriendInfo friendInfo) {
        PersonalCardActivity.start(getContext(), Integer.valueOf(friendInfo.getFriendId()));
    }

    private class HeaderAdapter extends IndexableHeaderAdapter {

        int friendSize;

        public HeaderAdapter(List datas, int friendsSize) {
            super("↑", null, datas);
            this.friendSize = friendsSize;
        }

        @Override
        public int getItemViewType() {
            return 2;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.im_view_header_friends, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            ((ViewHolder) holder).tvFriends.setText(UIUtil.getString(R.string.im_my_friend, friendSize));
            ((ViewHolder) holder).clLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SocialListActivity.start(getContext());
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout clLayout;
            TextView tvSize;
            TextView tvFriends;

            public ViewHolder(View itemView) {
                super(itemView);
                clLayout = itemView.findViewById(R.id.hf_cl_social);
                tvSize = itemView.findViewById(R.id.hf_tv_size);
                tvFriends = itemView.findViewById(R.id.hf_tv_friends);
            }
        }
    }
}