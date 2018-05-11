package com.wang.social.personal.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frame.component.helper.NetLoginTestHelper;
import com.frame.utils.BarUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.mvp.ui.fragment.PersonalFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);

        BarUtils.setColor(this, ContextCompat.getColor(this, R.color.common_blue), 0);

        Fragment personalFragment = PersonalFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!personalFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, personalFragment, "personalFragment");
        }
        fragmentTransaction.commit();

        findViewById(R.id.btn_test_login).setOnClickListener(v -> NetLoginTestHelper.newInstance().loginTest());
    }
}
