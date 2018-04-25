package com.wang.social.funshow.mvp.entities.funshow;

import lombok.Data;

@Data
public class Pic {

    /**
     * url : 图片地址
     * picOrder : 图片顺序
     * picType : 图片类型（ 1.高清  2.普通）
     */

    private String url;
    private int picOrder;
    private int picType;
}
