package com.wang.social.funshow.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.social.funshow.R;
import com.wang.social.funshow.mvp.ui.fragment.FunShowFragment;

public class MainActivity extends AppCompatActivity {

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
    }
}
