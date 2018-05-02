package com.wang.social.pictureselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.wang.social.pictureselector.ui.FragmentPicturePreview;

/**
 * Created by King on 2018/3/28.
 */

public class ActivityPicturePreview extends AppCompatActivity {

    public static void start(Context context, String... pics) {
        Intent intent = new Intent(context, ActivityPicturePreview.class);
        intent.putExtra(PictureSelector.NAME_FILE_PATH_LIST, pics);
        context.startActivity(intent);
    }

    public static void start(Context context, int current, String... pics) {
        Intent intent = new Intent(context, ActivityPicturePreview.class);
        intent.putExtra(PictureSelector.NAME_FILE_PATH_LIST, pics);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getSupportActionBar().hide();// 隐藏ActionBar

        setContentView(R.layout.ps_activity_picture_preview);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_view, new FragmentPicturePreview())
                .commit();
    }

}
