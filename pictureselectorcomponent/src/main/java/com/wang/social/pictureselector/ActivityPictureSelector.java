package com.wang.social.pictureselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.wang.social.pictureselector.ui.FragmentPictureSelector;

/**
 * Created by King on 2018/3/28.
 */

public class ActivityPictureSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getSupportActionBar().hide();// 隐藏ActionBar

        setContentView(R.layout.ps_activity_picture_selector);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_view, new FragmentPictureSelector())
                .commit();
    }
}
