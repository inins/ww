package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.FocusUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

//权限 0全部 1自己 2好友
public class LockActivity extends BasicAppActivity {

    @BindView(R2.id.radiogroup)
    RadioGroup radiogroup;

    private int lock;

    public static final int LOCK_ALL = 0;
    public static final int LOCK_FRIEND = 2;
    public static final int LOCK_SELF = 1;

    public static void start(Context context, int lock) {
        Intent intent = new Intent(context, LockActivity.class);
        intent.putExtra("lock", lock);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_lock;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        lock = getIntent().getIntExtra("lock", 0);
        switch (lock) {
            case LOCK_ALL:
                radiogroup.check(R.id.radio_showall);
                break;
            case LOCK_FRIEND:
                radiogroup.check(R.id.radio_showfriend);
                break;
            case LOCK_SELF:
                radiogroup.check(R.id.radio_showselt);
                break;
        }
        FocusUtil.focusToTop(toolbar);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            EventBean eventBean = new EventBean(EventBean.EVENT_CTRL_FUNSHOW_ADD_LOCK);
            int checkedId = radiogroup.getCheckedRadioButtonId();
            if (checkedId == R.id.radio_showall) {
                eventBean.put("lock", LOCK_ALL);
            } else if (checkedId == R.id.radio_showfriend) {
                eventBean.put("lock", LOCK_FRIEND);
            } else if (checkedId == R.id.radio_showselt) {
                eventBean.put("lock", LOCK_SELF);
            } else {
                ToastUtil.showToastShort("请选择权限");
                return;
            }
            EventBus.getDefault().post(eventBean);
            finish();
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
