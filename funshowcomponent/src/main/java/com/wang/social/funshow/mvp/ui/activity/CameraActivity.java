package com.wang.social.funshow.mvp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frame.component.utils.BitmapUtil;
import com.frame.component.utils.FileUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.mvp.ui.view.TextureCameraPreview;
import com.wang.social.funshow.mvp.ui.view.VideoBtnView;


public class CameraActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PLAY = 1;
    public static final String RESULT_KEY_PATH = "key_path";

    private TextureCameraPreview camera_view;
    private View btn_camera_flash;
    private View btn_camera_switch;
    private TextView text_camera_notice;
    private VideoBtnView videobtn_camera;
    private ImageView img_pic;

    private boolean videoEnable;
    private String pathPhoto;

    public static void start(Activity activity, int requestCode) {
        start(activity, true, requestCode);
    }

    public static void startDisableVideo(Activity activity, int requestCode) {
        start(activity, false, requestCode);
    }

    public static void start(Activity activity, boolean videoEnable, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("videoEnable", videoEnable);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funshow_activity_camera);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initBase();
        initView();
        initCtrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            if (!camera_view.isStartCamera()) camera_view.start();
        } else {
            Toast.makeText(this, "请允许应用申请权限", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION);
        }
    }

    private static final int REQUEST_PERMISSION = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "您拒绝了使用相机录像权限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                camera_view.start();
                break;
        }
    }

    private void initBase() {
        videoEnable = getIntent().getBooleanExtra("videoEnable", false);
    }

    private void initView() {
        camera_view = findViewById(R.id.camera_view);
        btn_camera_flash = findViewById(R.id.btn_camera_flash);
        btn_camera_switch = findViewById(R.id.btn_camera_switch);
        videobtn_camera = findViewById(R.id.videobtn_camera);
        text_camera_notice = findViewById(R.id.text_camera_notice);

        img_pic = findViewById(R.id.img_pic);
        if (videoEnable) {
            text_camera_notice.setText("轻触拍照，长按摄像");
            videobtn_camera.setEnableLongPress(true);
        } else {
            text_camera_notice.setText("轻触拍照");
            videobtn_camera.setEnableLongPress(false);
        }
    }

    private void initCtrl() {
        camera_view.setRecordListener(new TextureCameraPreview.OnStartVideoListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onStop(String path) {
                CameraPlayActivity.start(CameraActivity.this, path, REQUEST_CODE_PLAY);
            }
        });

        videobtn_camera.setOnVideoBtnListener(new VideoBtnView.OnVideoBtnListener() {
            @Override
            public void onClick(final View v) {
                camera_view.takePicture(new TextureCameraPreview.OnPictureTakenListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        String toPath = FileUtil.getPhotoFullPath();
                        //TODO:目前8.0黑鲨手机拍照后会被旋转-90度，其他手机正常，目前测试机少无法确定原因，目前暂且根据高宽对图片进行校正，如果发现图片宽>高则旋转90度，具体原因有待后期排查
                        int degree = 0;
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            degree = 90;
                        }
                        bitmap = BitmapUtil.rotateBitmap(degree, bitmap);           //根据旋转角度进行旋转
                        pathPhoto = BitmapUtil.saveBitmap(bitmap, toPath);             //保存图片到指定路径
                        img_pic.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLongPressStart() {
                if (!camera_view.isRecording()) {
                    camera_view.startRecord();
                }
            }

            @Override
            public void onLongPressEnd(long time) {
                // 结束录像
                if (camera_view.isRecording()) {
                    camera_view.stopRecord();
                }
            }
        });
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_camera_flash) {
            camera_view.openFlash();
        } else if (i == R.id.btn_camera_switch) {
            camera_view.switchCamera();
        } else if (i == R.id.btn_right) {
            if (!TextUtils.isEmpty(pathPhoto)) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_KEY_PATH, pathPhoto);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastUtil.showToastShort("请拍照");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PLAY:
                    //接收视频路径，再次返回给上级
                    String videoPath = data.getStringExtra(RESULT_KEY_PATH);
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY_PATH, videoPath);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
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
}
