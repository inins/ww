package com.wang.social.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布话题、趣晒弹窗
 */
public class DialogHomeAdd extends BaseDialog {

    public DialogHomeAdd(Context context) {
        super(context,R.style.common_PopupDialog);
    }

    @Override
    protected int getView() {
        return R.layout.dialog_home_add;
    }

    @Override
    protected void intView(View root) {
    }


    @OnClick({R.id.btn_qs, R.id.btn_topic})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_qs:
                if (onAddDialogClickListener != null) onAddDialogClickListener.onQsClick(v);
                dismiss();
                break;
            case R.id.btn_topic:
                if (onAddDialogClickListener != null) onAddDialogClickListener.onTopicClick(v);
                dismiss();
                break;
        }
    }

    private OnAddDialogClickListener onAddDialogClickListener;

    public void setOnAddDialogClickListener(OnAddDialogClickListener onAddDialogClickListener) {
        this.onAddDialogClickListener = onAddDialogClickListener;
    }

    public interface OnAddDialogClickListener {
        void onQsClick(View v);

        void onTopicClick(View v);
    }
}
