package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;

import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.view.MusicBoard;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import java.util.Random;

import butterknife.BindView;

public class FunshowAddMusicBoardController extends FunshowAddBaseController implements View.OnClickListener {

    @BindView(R2.id.musicboard)
    MusicBoard musicboard;
    @BindView(R.id.btn_del)
    ImageView btnDel;
    private Music music;

    public FunshowAddMusicBoardController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_add_musicboard;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        btnDel.setOnClickListener(this);
    }

    @Override
    protected void onInitData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_del) {
            DialogSure.showDialog(getContext(), "确定要删除音频？", () -> {
                music = null;
                musicboard.onStop();
                getRoot().setVisibility(View.GONE);
            });
        }
    }

    /////////////////////////////////////////

    public void setMusicPath(Music music) {
        this.music = music;

        musicboard.resetMusic(music);

        getRoot().setVisibility(View.VISIBLE);
    }

    public String getMusicPath() {
        return music != null ? music.getUrl() : "";
    }
}
