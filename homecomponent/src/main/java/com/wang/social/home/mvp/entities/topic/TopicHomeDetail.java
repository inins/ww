package com.wang.social.home.mvp.entities.topic;

import com.frame.component.entities.Tag;

import java.util.List;

import lombok.Data;

@Data
public class TopicHomeDetail {


    /**
     * id : 1
     * title : 不要跑，继续刚
     * creatorId : 10000
     * readTotal : 4
     * commentTotal : 11
     * supportTotal : 1
     * createTime : 1521785005000
     * content : <!DOCTYPE html><html lang="zh"><head><meta charset="utf-8" /><title>wwrichtextTittle</title><meta name="HandheldFriendly" content="True" /><meta name="MobileOptimized" content="320" /><meta name="viewport" content="width=device-width, initial-scale=1" /><script>function playAudio(url){var musicPlay=document.getElementById('musicPlay');musicPlay.setAttribute('src',url);musicPlay.play();}</script><style>img{max-width: 100%;-webkit-border-radius: 20px;border-radius: 20px;border: 0px solid black; p{line-height: 1.6rem !important;}}</style></head><body><audio src="" id="musicPlay"> </audio>摸索着人转切转切住用肉在人转切转切转切住人做人做最清晰
     * <div><font face="STSongti-SC-Regular, st">额婆婆嘴是您是要人转切</font><font face="DFWaWaSC-W5, ww">滴哦婆媳所以</font>
     * <img src="http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-04-21-EbasjXZW.png" alt="hello">
     * </div></body></html>
     * nickname : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * topicTag : [{"tagId":1,"tagName":"娱乐"},{"tagId":3,"tagName":"电影"},{"tagId":4,"tagName":"电视剧"}]
     */

    private int id;
    private String firstStrff;
    private String backgroundImage;
    private String title;
    private int creatorId;
    private int readTotal;
    private int commentTotal;
    private int supportTotal;
    private long createTime;
    private String content;
    private String nickname;
    private String avatar;
    private List<Tag> topicTag;
    private boolean shopping;

    private boolean support;
    private int relateState;

    public TopicHome tans2TopicHome() {
        TopicHome topicHome = new TopicHome();
        topicHome.setTopicId(id);
        topicHome.setTitle(title);
        topicHome.setFirstStrff(content);
        topicHome.setCreateTime(createTime);
        topicHome.setTopicImage("xxx");
        topicHome.setTopicSupportNum(supportTotal);
        topicHome.setTopicCommentNum(commentTotal);
        topicHome.setTopicReadNum(readTotal);
        topicHome.setTopicTag(topicTag);
        topicHome.setUserCover(avatar);
        topicHome.setUserName(nickname);
        topicHome.setFirstStrff(firstStrff);
        topicHome.setUserCover(backgroundImage);
        topicHome.setIsShopping(shopping ? 1 : 0);
        topicHome.setIsSupport(support ? 1 : 0);
        topicHome.setIsFree(relateState);
        return topicHome;
    }

}
