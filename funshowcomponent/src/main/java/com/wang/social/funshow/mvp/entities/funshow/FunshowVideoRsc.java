package com.wang.social.funshow.mvp.entities.funshow;

import lombok.Data;

@Data
public class FunshowVideoRsc {

    /**
     * mediaType : 2
     * url : http://resouce.dongdongwedding.com/wangwang_2018-04-26_3369fa70-0904-4c91-9a73-b038a3e56f61.voice
     */

    private int mediaType;
    private String url;

    //////////////////////////////

    public boolean isVidoe() {
        return mediaType == 2;
    }
}
