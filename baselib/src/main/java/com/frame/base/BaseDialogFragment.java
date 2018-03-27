package com.frame.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.frame.R;
import com.frame.utils.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 弹框基类
 * Created by CJ on 2016/12/26 0026.
 *
 * @version 1.3
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Context context;
    protected View contentView;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.base_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        configDialog(dialog);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resource = initLayoutResId();
        contentView = inflater.inflate(resource, container, false);

        unbinder = ButterKnife.bind(this, contentView);

        initView();
        setListener();
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(getWidth(), getHeight());
            dialog.getWindow().setGravity(getGravity());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
        context = null;
        unbinder.unbind();
    }

    /**
     * 配置Dialog
     * @param dialog
     */
    protected void configDialog(Dialog dialog) {
    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 设置相关事件
     */
    protected void setListener() {
    }

    protected int getWidth(){
        return (int) (ScreenUtils.getScreenWidth() * 0.85);
    }

    protected int getHeight(){
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getGravity(){
        return Gravity.BOTTOM;
    }

    /**
     * 指定布局文件
     *
     * @return
     */
    protected abstract int initLayoutResId();
}