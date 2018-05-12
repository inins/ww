package com.wang.social.home.mvp.entities.funshow;

import lombok.Data;

@Data
public class FunshowHomeDetail {


    /**
     * talkId : 1
     * content : 这是一个趣晒，很无趣
     * relateState : 1
     * gemstone : 1000
     * commentTotal : 45
     * supportTotal : 1
     * shareTotal : 1
     * address : null
     * thumbnail : http://resouce.dongdongwedding.com/wangwang_2017-06-16_6d7da9a7-e8dd-4552-b8eb-5c49e4df8665.jpg
     * picNum : 1
     * mediaType : null
     * userId : 10000
     * nickname : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * createTime : 1521787669000
     */

    private int talkId;
    private String content;
    private int relateState;
    private int gemstone;
    private int commentTotal;
    private int supportTotal;
    private int shareTotal;
    private String address;
    private String thumbnail;
    private int picNum;
    private int mediaType;
    private int creatorId;
    private String nickname;
    private String avatar;
    private long createTime;

    public FunshowHome tans2FunshowHome(){
        FunshowHome funshowHome = new FunshowHome();
        funshowHome.setTalkId(talkId);
        funshowHome.setContent(content);
        funshowHome.setUrls(picNum);
        funshowHome.setProvince("xx");
        funshowHome.setCity("xx");
        funshowHome.setSupportTotal(supportTotal);
        funshowHome.setCommentTotal(commentTotal);
        funshowHome.setShareTotal(shareTotal);
        funshowHome.setMediaType(mediaType);
        funshowHome.setNickname(nickname);
        funshowHome.setHeadImg(avatar);
        funshowHome.setCreateTime(createTime);
        funshowHome.setCreatorId(creatorId);
        funshowHome.setUrl(thumbnail);
        return funshowHome;
    }
}
