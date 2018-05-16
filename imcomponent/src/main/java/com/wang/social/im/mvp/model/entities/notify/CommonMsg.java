package com.wang.social.im.mvp.model.entities.notify;

import lombok.Data;

@Data
public class CommonMsg {
    private int id;
    private String avatar;
    private String picUrl;
    private String name;
    private long time;
    private String content;
}
