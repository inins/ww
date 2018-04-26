package com.wang.social.topic.mvp.model.entities;

import android.content.Intent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Music {
    private int musicId;
    private String musicName;
    private String url;

    public boolean isDefault(Music music) {
        return music.getMusicId() == -1;
    }

    public static Music defaultMusic() {
        return new Music(-1, "无音乐", "");
    }

    public static Music newInstance(Intent intent) {
        if (null == intent) return null;

        return new Music(
                intent.getIntExtra("id", -1),
                intent.getStringExtra("name"),
                intent.getStringExtra("url"));
    }

    public static Intent newIntent(Music music) {
        Intent intent = new Intent();

        if (null != music) {
            intent.putExtra("id", music.getMusicId());
            intent.putExtra("name", music.getMusicName());
            intent.putExtra("url", music.getUrl());
        }

        return intent;
    }
}
