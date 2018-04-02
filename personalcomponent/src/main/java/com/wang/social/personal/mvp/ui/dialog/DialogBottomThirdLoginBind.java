package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * liaoinstan
 * 第三方账户绑定弹出
 */
public class DialogBottomThirdLoginBind extends BaseDialog {
    @BindView(R.id.btn_weixin)
    TextView btn_weixin;
    @BindView(R.id.btn_weibo)
    TextView btn_weibo;
    @BindView(R.id.btn_qq)
    TextView btn_qq;

    public DialogBottomThirdLoginBind(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_thirdloginbind;
    }

    @Override
    protected void intView(View root) {
    }

    @OnClick({R.id.btn_weixin, R.id.btn_weibo, R.id.btn_qq, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_weixin:
            case R.id.btn_weibo:
            case R.id.btn_qq:
                ToastUtil.showToastShort("建设中...");
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
