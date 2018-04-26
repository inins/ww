package com.wang.social.funshow.mvp.entities.funshow;

import com.frame.component.ui.acticity.BGMList.Music;

import lombok.Data;

@Data
public class FunshowDetailVideoRsc {
    /**
     * musicId : 0
     * type : 1
     * musicName : null
     * url : http://resouce.dongdongwedding.com/wangwang_2018-04-26_7de80a3d-3a62-4237-bf1d-7e3bd6a65d52.voice
     */

    private int musicId;
    private int type;
    private String musicName;
    private String url;

    //////////////////////////////

    public boolean isVidoe() {
        return type == 2;
    }

    public boolean isVoice() {
        return type == 1;
    }

    public Music trans2Music() {
        Music music = new Music();
        music.setMusicId(musicId);
        music.setUrl(url);
        if (musicId != 0) {
            music.setMusicName(musicName);
        } else {
            music.setMusicName("我的录音");
        }
        return music;
    }
}
