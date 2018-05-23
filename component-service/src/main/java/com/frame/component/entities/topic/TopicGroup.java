package com.frame.component.entities.topic;

import com.frame.component.entities.Topic;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicGroup {


    /**
     * firstStrff : 哦1哦哦1哦1哦哦1哦1你敏敏1哦1哦1哦哦1哦哦1民工敏魔
     * birthday : 730310400000
     * backgroundImage : http://resouce.dongdongwedding.com/2018-05-17_bc48b0f7-4207-4c82-9979-c654f1553b6c.jpg
     * supportTotal : 1
     * isSupport : 0
     * creatorId : 10012
     * commentTotal : 5
     * readTotal : 13
     * avatar : http://resouce.dongdongwedding.com/wangwang_2018-04-17_a697c09b-d9a9-4535-9624-cb9420d5a3a3.jpg
     * title : 话题3333
     * templateId : 0
     * relateDiamond : 0.0
     * relateState : 0
     * topicId : 62
     * shareTotal : null
     * createTime : 1526551374000
     * nickname : _JJ
     */

    private String firstStrff;
    private long birthday;
    private String backgroundImage;
    private int supportTotal;
    private int isSupport;
    private int creatorId;
    private int commentTotal;
    private int readTotal;
    private String avatar;
    private String title;
    private int templateId;
    private float relateDiamond;
    private int relateState;
    private int topicId;
    private int shareTotal;
    private long createTime;
    private String nickname;

    public Topic tans2Topic() {
        Topic topic = new Topic();

        topic.setTopicId(topicId);
        topic.setCreatorId(creatorId);
        topic.setFirstStrff(firstStrff);
        topic.setBackgroundImage(backgroundImage);
        topic.setTitle(title);
        topic.setReadTotal(readTotal);
        topic.setSupportTotal(supportTotal);
        topic.setCreateTime(createTime);
        //topic.setAddress();
        topic.setCommentTotal(commentTotal);
        topic.setShareTotal(shareTotal);
        topic.setRelateState(relateState);
        topic.setRelateMoney((int) relateDiamond);
//        topic.setTags(tans2TagList(tags));
        //topic.setShopping();
        topic.setSupport(isSupport == 1);
        topic.setAvatar(avatar);
        topic.setNickname(nickname);
        return topic;
    }

    public static List<Topic> tans2TopicList(List<TopicGroup> list) {
        List<Topic> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (TopicGroup bean : list) {
                toList.add(bean.tans2Topic());
            }
        }
        return toList;
    }

}
