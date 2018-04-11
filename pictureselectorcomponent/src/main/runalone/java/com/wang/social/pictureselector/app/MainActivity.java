package com.wang.social.pictureselector.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.wang.social.pictureselector.ActivityPictureClip;
import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.ui.widget.DialogActionSheet;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    EditText spanCountET;
    ToggleButton toggleButton;
    Button singleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getSupportActionBar().hide();// 隐藏ActionBar

        setContentView(R.layout.ps_activity_main);

        imageView = findViewById(R.id.image_view);
        editText = findViewById(R.id.max_selection_et);
        spanCountET = findViewById(R.id.span_count_et);
        toggleButton = findViewById(R.id.is_clip_btn);

        singleBtn = findViewById(R.id.start_image_selector);
        singleBtn.setOnClickListener(buttonListener);

        findViewById(R.id.pop_dialog_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogActionSheet.newInstance()
                                .addItem("官方图库", new DialogActionSheet.ClickListener() {
                                    @Override
                                    public void onClick(DialogActionSheet dialog, View v) {

                                    }
                                })
                                .addItem("拍摄", new DialogActionSheet.ClickListener() {
                                    @Override
                                    public void onClick(DialogActionSheet dialog, View v) {

                                    }
                                })
                                .addItem("相册", new DialogActionSheet.ClickListener() {
                                    @Override
                                    public void onClick(DialogActionSheet dialog, View v) {

                                    }
                                })
                                .show(getSupportFragmentManager(), "");
                    }
                });
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            startPictureClip();
//            startPicturePreview();
            startPictureSelection();
        }
    };

    private void startPictureClip() {
        startActivity(new Intent(this, ActivityPictureClip.class));
    }

    private void startPicturePreview() {
        startActivity(new Intent(this, ActivityPicturePreview.class));
    }

    private void startPictureSelection() {
        int max = 1;
        int span = 2;

        try {
            max = Integer.parseInt(editText.getText().toString());
            span = Integer.parseInt(spanCountET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        max = Math.max(1, max);
        span = Math.max(2, span);

        PictureSelector.from(MainActivity.this)
                .maxSelection(max)
                .spanCount(span)
                .isClip(toggleButton.isChecked())
                .forResult(11111);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (11111 == requestCode) {

                String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);

                Intent intent = new Intent(MainActivity.this, ActivityPicturePreview.class);
                intent.putExtra(PictureSelector.NAME_FILE_PATH_LIST, list);
                startActivity(intent);
            }
        }
    }
}
