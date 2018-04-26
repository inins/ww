package com.wang.social.funshow.mvp.entities.post;

import java.util.List;

import lombok.Data;

@Data
public class FunshowAddPost {

    /**
     * adCode : 510107
     * address : 详细地址
     * authority : 0
     * city : 成都市
     * content : 阿萨大大啊实打实大苏打
     * creatorId : 10000
     * isAnonymous : 0
     * latitude : 30.566729
     * longitude : 104.063642
     * mediaType : 3
     * province : 四川
     * gemstone : 100
     * relateState : 1
     * resources : [{"mediaType":1,"picOrder":1,"picType":2,"url":"音乐地址"}]
     * state : 0
     * tags : [{"tagId":1,"tagName":"娱乐"}]
     * users : [{"userId":10000}]
     */

    private String adCode;
    private String address;
    private int authority;
    private String city;
    private String content;
    private int creatorId;
    private String isAnonymous;
    private String latitude;
    private String longitude;
    private int mediaType;
    private String province;
    private int gemstone;
    private int relateState;
    private int state;
    private List<ResourcePost> resources;
    private List<UserPost> users;
}
