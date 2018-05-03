package com.wang.social.funshow.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.utils.VideoCoverUtil;

import java.io.File;

public class CameraPlayActivity extends AppCompatActivity {

    private VideoView videoview;
    private ImageView img_cover;
    private View btn_video_play;
    private View btn_video_restart;

    private String path;

    public static void start(Activity activity, String path, int requsetCode) {
        Intent intent = new Intent(activity, CameraPlayActivity.class);
        intent.putExtra("path", path);
        activity.startActivityForResult(intent, requsetCode);
        activity.overridePendingTransition(R.anim.funshow_scale_in_scale, R.anim.funshow_scale_stay);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.funshow_scale_out_scale);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funshow_activity_camera_play);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        path = getIntent().getStringExtra("path");

        initView();
        initCtrl();
    }

    private void initView() {
        videoview = findViewById(R.id.video_view);
        img_cover = findViewById(R.id.img_cover);
        btn_video_play = findViewById(R.id.btn_video_play);
        btn_video_restart = findViewById(R.id.btn_video_restart);
    }

    private void initCtrl() {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "路径不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                btn_video_play.setVisibility(View.VISIBLE);
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn_video_play.setSelected(false);
            }
        });
        videoview.setVideoURI(Uri.parse(path));
        Bitmap coverbitmap = VideoCoverUtil.createVideoThumbnail(path);
        img_cover.setImageBitmap(coverbitmap);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_video_restart) {
            onBackPressed();
        } else if (id == R.id.btn_video_play) {
            if (videoview.isPlaying()) {
                btn_video_play.setSelected(false);
                videoview.pause();
            } else {
                btn_video_play.setSelected(true);
                videoview.start();
                img_cover.setVisibility(View.GONE);
            }
        } else if (id == R.id.btn_right) {
            if (!TextUtils.isEmpty(path)) {
                Intent intent = new Intent();
                intent.putExtra(CameraActivity.RESULT_KEY_PATH, path);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.showToastShort("no video");
            }
        }
    }

    //删除视频
    private void deleteVideo() {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) file.delete();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteVideo();
    }
}
