package com.wang.social.home.mvp.entities.topic;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.util.List;

import lombok.Data;

@Data
public class TopicHome {

    /**
     * firstStrff : 好多好吃就放假经常坚持坚持姐姐飞机场忽大忽小不能吃就放假你才
     * topicImage :
     * isSupport : 0
     * title : 付黄飞鸿
     * userName : W-NewType
     * topicReadNum : 10
     * userId : 10027
     * topicId : 60
     * isFree : 0
     * createTime : 1525699831000
     * topicCommentNum : 0
     * price : 0
     * topicSupportNum : 0
     * topicTag : [{"tagId":3,"tagName":"电影"}]
     * userCover : http://resouce.dongdongwedding.com/wangwang_2018-04-24_307d23fd-7cfc-4af7-87b1-9b48329516c5.jpg
     * isShopping : 0
     */

    private String firstStrff;
    private String topicImage;
    private int isSupport;
    private String title;
    private String userName;
    private int topicReadNum;
    private int userId;
    private int topicId;
    private int isFree;
    private long createTime;
    private int topicCommentNum;
    private int price;
    private int topicSupportNum;
    private String userCover;
    private int isShopping;
    private List<Tag> topicTag;

    //////////////////////////

    public String getTagStr() {
        String ret = "";
        for (Tag tag : topicTag) {
            ret += tag.getTagName() + ",";
        }
        return StrUtil.subLastChart(ret, ",");
    }

    public boolean isFree() {
        return isFree == 1;
    }

    public boolean isSupport() {
        return isSupport != 0;
    }

    public void setIsSupportBool(boolean zan) {
        isSupport = zan ? 1 : 0;
    }
}
