package com.wang.social.pictureselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.wang.social.pictureselector.ui.FragmentPictureClip;

/**
 * Created by King on 2018/3/28.
 */

public class ActivityPictureClip extends AppCompatActivity {

    FragmentPictureClip fragmentPictureClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getSupportActionBar().hide();// 隐藏ActionBar

        setContentView(R.layout.ps_activity_picture_clip);

        findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        findViewById(R.id.confirm_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentPictureClip.clip();
                    }
                });

        fragmentPictureClip = new FragmentPictureClip();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.clip_view_layout, fragmentPictureClip)
                .commit();
    }

}
