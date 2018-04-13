package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.thirdlogin.BindHistory;
import com.wang.social.socialize.SocializeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * liaoinstan
 * 第三方账户绑定弹出
 */
public class DialogBottomThirdLoginBind extends BaseDialog {
    @BindView(R2.id.btn_weixin)
    TextView btn_weixin;
    @BindView(R2.id.btn_weibo)
    TextView btn_weibo;
    @BindView(R2.id.btn_qq)
    TextView btn_qq;

    private String TEXT_BINDED = "已绑定";

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

    @OnClick({R2.id.btn_weixin, R2.id.btn_weibo, R2.id.btn_qq, R2.id.btn_cancel})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_weixin) {
            if (onThirdLoginDialogListener != null)
                onThirdLoginDialogListener.onWeiXinClick(view, btn_weixin.getText().toString().equals(TEXT_BINDED));
            dismiss();
        } else if (i == R.id.btn_weibo) {
            if (onThirdLoginDialogListener != null)
                onThirdLoginDialogListener.onWeiBoClick(view, btn_weibo.getText().toString().equals(TEXT_BINDED));
            dismiss();
        } else if (i == R.id.btn_qq) {
            if (onThirdLoginDialogListener != null)
                onThirdLoginDialogListener.onQQClick(view, btn_qq.getText().toString().equals(TEXT_BINDED));
            dismiss();
        } else if (i == R.id.btn_cancel) {
            dismiss();
        }
    }

    //设置绑定状态
    public void setBindHistories(List<BindHistory> bindHistories) {
        btn_weixin.setText(getContext().getResources().getString(R.string.personal_setting_dialog_weixin));
        btn_weibo.setText(getContext().getResources().getString(R.string.personal_setting_dialog_weibo));
        btn_qq.setText(getContext().getResources().getString(R.string.personal_setting_dialog_qq));
        if (StrUtil.isEmpty(bindHistories)) return;
        for (BindHistory history : bindHistories) {
            switch (history.getBindType()) {
                case SocializeUtil.LOGIN_PLATFORM_WEIXIN:
                    btn_weixin.setText(TEXT_BINDED);
                    break;
                case SocializeUtil.LOGIN_PLATFORM_SINA_WEIBO:
                    btn_weibo.setText(TEXT_BINDED);
                    break;
                case SocializeUtil.LOGIN_PLATFORM_QQ:
                    btn_qq.setText(TEXT_BINDED);
                    break;
            }
        }
    }

    /////////////////////////////////////////////////////////

    OnThirdLoginDialogListener onThirdLoginDialogListener;

    public void setOnThirdLoginDialogListener(OnThirdLoginDialogListener onThirdLoginDialogListener) {
        this.onThirdLoginDialogListener = onThirdLoginDialogListener;
    }

    public interface OnThirdLoginDialogListener {
        void onWeiXinClick(View v, boolean binded);

        void onWeiBoClick(View v, boolean binded);

        void onQQClick(View v, boolean binded);
    }
}
