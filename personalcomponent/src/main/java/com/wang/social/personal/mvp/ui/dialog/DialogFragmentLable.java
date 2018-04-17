package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.utils.SizeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.TestEntity;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterLableUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 */
public class DialogFragmentLable extends DialogFragment {

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
        adapter.refreshData(new ArrayList<TestEntity>(){{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
