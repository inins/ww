package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.TestEntity;
import com.frame.component.view.LoadingLayout;
import com.frame.mvp.IView;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.lable.MiChat;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterLableUser;
import com.wang.social.personal.net.helper.NetLableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 */
public class DialogFragmentLable extends DialogFragment {

    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;
    private Unbinder bind;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.img_tag)
    ImageView imgTag;
    @BindView(R2.id.img_del)
    ImageView imgDel;
    @BindView(R2.id.text_count)
    TextView textCount;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterLableUser adapter;

    private Lable lable;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.common_MyDialog);
        dialog.setContentView(R.layout.personal_dialog_lable);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.personal_dialog_lable, container);
        bind = ButterKnife.bind(this, root);
        initCtrl();
        initData();
        return root;
    }

    private void initCtrl() {
        imgDel.setVisibility(View.GONE);
        adapter = new RecycleAdapterLableUser();
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(3, SizeUtils.dp2px(7), GridLayoutManager.HORIZONTAL, true));
        recycler.setAdapter(adapter);
    }

    private void initData() {
        if (lable != null) {
            textName.setText(lable.getName());
            imgTag.setVisibility(lable.getShowTagBool() ? View.VISIBLE : View.GONE);
        }
        netGetMiList();
    }

    private void setMiData(List<MiChat> miList) {
        if (!StrUtil.isEmpty(miList)) {
            textCount.setText("加入的觅聊" + " (" + miList.size() + ")");
            adapter.refreshData(miList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    public void show(FragmentManager manager, Lable lable) {
        this.lable = lable;
        show(manager, "DialogFragmentLable");
    }

    private void netGetMiList() {
        if (lable == null) return;
        loadingview.showLoadingView();
        NetLableHelper.newInstance().netGetMiListByLable(getIView(), false, lable.getId(), lable.getName(), new NetLableHelper.OnMiChatListCallback() {
            @Override
            public void success(List<MiChat> miChats) {
                if (!StrUtil.isEmpty(miChats)) {
                    setMiData(miChats);
                    loadingview.showOut();
                } else {
                    loadingview.showLackView();
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastShort(e.getMessage());
                loadingview.showFailView();
            }
        });
    }

    private IView getIView() {
        if (getActivity() instanceof IView) {
            return (IView) getActivity();
        } else {
            return null;
        }
    }
}
