package com.wang.social.funshow.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frame.component.helper.NetLoginTestHelper;
import com.frame.entities.EventBean;
import com.wang.social.funshow.R;
import com.wang.social.funshow.mvp.ui.activity.FunshowAddActivity;
import com.wang.social.funshow.mvp.ui.fragment.FunShowFragment;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funshow_activity_main);

        Fragment funShowFragment = FunShowFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!funShowFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, funShowFragment, "funShowFragment");
        }
        fragmentTransaction.commit();

        //测试跳转代码
        findViewById(R.id.btn_funshow_add).setOnClickListener(v -> FunshowAddActivity.start(this));
        findViewById(R.id.btn_funshow_login).setOnClickListener(v -> NetLoginTestHelper.newInstance().loginTest());
        findViewById(R.id.btn_funshow_type).setOnClickListener(v -> {
            int typein = type == 0 ? 1 : 0;
            EventBean eventBean = new EventBean(EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE);
            eventBean.put("type", typein);
            EventBus.getDefault().post(eventBean);
        });
    }
}
