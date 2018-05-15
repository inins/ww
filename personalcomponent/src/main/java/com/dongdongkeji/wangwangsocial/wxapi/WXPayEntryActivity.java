package com.dongdongkeji.wangwangsocial.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frame.entities.EventBean;
import com.frame.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.social.personal.R;
import com.wang.social.personal.utils.PayUtil;
import com.wang.social.socialize.SocializeUtil;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler, View.OnClickListener {

    private IWXAPI wxapi;
    private int type;
    private TextView text_payresult;

    public static void startPaySuccess(Context context) {
        Intent intent = new Intent(context, WXPayEntryActivity.class);
        intent.putExtra("type", 0);
        context.startActivity(intent);
    }

    public static void startPayFail(Context context) {
        Intent intent = new Intent(context, WXPayEntryActivity.class);
        intent.putExtra("type", -1);
        context.startActivity(intent);
    }

    public static void startPayCancel(Context context) {
        Intent intent = new Intent(context, WXPayEntryActivity.class);
        intent.putExtra("type", -2);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", -1);
        }
        setPayData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_payresult);
        wxapi = WXAPIFactory.createWXAPI(this, SocializeUtil.WX_APP_ID);
        wxapi.handleIntent(getIntent(), this);
        initBase();
        initView();
        initCtrl();
        initData();
    }

    private void initBase() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", -1);
        }
    }

    private void initView() {
        text_payresult = findViewById(R.id.text_payresult);
    }

    private void initCtrl() {
    }

    private void initData() {
        setPayData();
    }

    private void setPayData() {
//        EventBus.getDefault().post(new EventBean(EventBean.EVENT_PAYRESULT));
        switch (type) {
            case 0:
                text_payresult.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.personal_ic_pay_success, 0, 0);
                text_payresult.setText(R.string.personal_account_payresult_success);
                break;
            case -1:
                text_payresult.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.personal_ic_pay_fail, 0, 0);
                text_payresult.setText(R.string.personal_account_payresult_faild);
                break;
            case -2:
                text_payresult.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.personal_ic_pay_cancle, 0, 0);
                text_payresult.setText(R.string.personal_account_payresult_cancel);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                finish();
                break;
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Timber.e("onPayFinish, errCode = " + resp.errCode);
        Timber.e("onPayFinish, errStr = " + resp.errStr);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            type = resp.errCode;
            setPayData();
            switch (resp.errCode) {
                case 0:
                    //personal_ic_pay_success
                    break;
                case -1:
                    //personal_ic_pay_fail
                    break;
                case -2:
                    //用户取消
                    break;
                case -3:
                    //发送失败
                    ToastUtil.showToastShort("微信支付：发送失败");
                    break;
                case -4:
                    //授权失败
                    ToastUtil.showToastShort("微信支付：授权失败");
                    break;
                case -5:
                    //微信不支持
                    ToastUtil.showToastShort("微信支付：微信版本不支持");
                    break;
                case -6:
                    //BAN
                    ToastUtil.showToastShort("微信支付：BAN");
                    break;
            }
        }
    }
}
