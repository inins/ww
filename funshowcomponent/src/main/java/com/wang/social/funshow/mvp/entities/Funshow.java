package com.wang.social.funshow.mvp.entities;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Funshow implements Serializable{

    /**
     * talkId : 趣晒ID
     * content : 趣晒内容
     * userId : 用户ID
     * userName : 用户昵称
     * userCover : 用户头像
     * talkImage : ["http：//xxxx.jpg"]
     * talkImageNum : 趣晒图片数量
     * createTime : 创建时间
     * province : 省份
     * city : 城市
     * talkSupportNum : 点赞次数
     * talkCommentNum : 评论次数
     * talkShareNum : 分享次数
     * isFree : 是否收费（0免费 1收费）
     * price : 收费价格
     * isShopping : 是否需要购买（0 无需购买 1 购买）
     */

    private String talkId;
    private String content;
    private String userId;
    private String userName;
    private String userCover;
    private String talkImageNum;
    private String createTime;
    private String province;
    private String city;
    private String talkSupportNum;
    private String talkCommentNum;
    private String talkShareNum;
    private String isFree;
    private String price;
    private String isShopping;
    private List<String> talkImage;

}
