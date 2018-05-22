package com.wang.social.funshow.mvp.entities.post;

import lombok.Data;

@Data
public class ResourcePost {
    /**
     * mediaType : 1
     * picOrder : 1
     * picType : 2
     * url : 音乐地址
     */

    private int mediaType;
    private int picOrder;
    private String url;
}
