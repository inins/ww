package com.wang.social.home.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frame.component.helper.NetLoginTestHelper;
import com.frame.utils.BarUtils;
import com.wang.social.home.R;
import com.wang.social.home.mvp.ui.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_main);

        Fragment homeFragment = HomeFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!homeFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, homeFragment, "homeFragment");
        }
        fragmentTransaction.commit();

        findViewById(R.id.btn_funshow_login).setOnClickListener(v -> NetLoginTestHelper.newInstance().loginTest());
    }
}
