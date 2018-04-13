package com.wang.social.funpoint.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frame.utils.BarUtils;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.mvp.ui.fragment.FunPointFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funpoint_activity_main);

        Fragment funPointFragment = FunPointFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!funPointFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, funPointFragment, "funPointFragment");
        }
        fragmentTransaction.commit();
    }
}
