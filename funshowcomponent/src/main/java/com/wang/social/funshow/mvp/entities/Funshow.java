package com.wang.social.funshow.mvp.entities;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Funshow implements Serializable{


    /**
     * talkSupportNum : 2
     * city : null
     * talkCommentNum : 8
     * isSupport : 0
     * userName : 飘飘飘香
     * talkImage : ["http://1.jpg","http://2.gif"]
     * userId : 10000
     * talkImageNum : 2
     * content : 这是一个趣晒，很无趣
     * talkShareNum : null
     * province : null
     * isFree : 1
     * createTime : 1521787669000
     * price : 1000
     * talkId : 1
     * userCover : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * isShopping : 1
     */

    private int talkSupportNum;
    private String city;
    private int talkCommentNum;
    private int isSupport;
    private String userName;
    private int userId;
    private int talkImageNum;
    private String content;
    private int talkShareNum;
    private String province;
    private int isFree;
    private long createTime;
    private int price;
    private int talkId;
    private String userCover;
    private int isShopping;
    private List<String> talkImage;
}
