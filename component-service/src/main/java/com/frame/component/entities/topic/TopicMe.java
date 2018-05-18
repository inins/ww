package com.frame.component.entities.topic;

import com.frame.component.entities.Tag;
import com.frame.component.entities.Topic;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicMe {
    /**
     * firstStrff : 就等你呢潮男潮女难道你没吃呢潮男潮女呢你才能出门潮男潮女道具
     * birthday : 788889600000
     * supportTotal : 0
     * backgroundImage :
     * isSupport : 0
     * commentTotal : 4
     * readTotal : 8
     * avatar : http://resouce.dongdongwedding.com/wangwang_2018-04-24_307d23fd-7cfc-4af7-87b1-9b48329516c5.jpg
     * title : 减肥减肥呢
     * relateState : 0
     * gemstone : 0
     * tags : [{"tagId":6,"tagName":"美剧"}]
     * shareTotal : 0
     * createTime : 1525743682000
     * nickname : W-NewType
     * id : 61
     */

    private String firstStrff;
    private long birthday;
    private int supportTotal;
    private String backgroundImage;
    private int isSupport;
    private int commentTotal;
    private int readTotal;
    private String avatar;
    private String title;
    private int relateState;
    private int gemstone;
    private int shareTotal;
    private long createTime;
    private String nickname;
    private int id;
    private List<Tag> tags;

    ////////////////////////////

    public boolean isSupport() {
        return isSupport != 0;
    }

    /**
     * private int topicId;
     * private int creatorId;
     * private String firstStrff;
     * private String backgroundImage;
     * private String title;
     * private int readTotal;
     * private int supportTotal;
     * private long createTime;
     * private String address;
     * private int commentTotal;
     * private int shareTotal;
     * private int relateState;
     * private int gemstone;
     * private List<Tag> tags;
     * private boolean shopping;
     * private boolean support;
     * private String avatar;
     * private String nickname;
     *
     * @return
     */

    public Topic tans2Topic() {
        //TODO：以下注释掉的属性，接口没有提供，需要后台配合处理
        Topic topic = new Topic();
        topic.setTopicId(id);
        //topic.setCreatorId();
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
        topic.setRelateMoney(gemstone);
        topic.setTags(tans2TagList(tags));
        //topic.setShopping();
        topic.setSupport(isSupport());
        topic.setAvatar(avatar);
        topic.setNickname(nickname);

        return topic;
    }

    public static List<Topic> tans2TopicList(List<TopicMe> topicMes) {
        List<Topic> topics = new ArrayList<>();
        if (!StrUtil.isEmpty(topicMes)) {
            for (TopicMe topicMe : topicMes) {
                topics.add(topicMe.tans2Topic());
            }
        }
        return topics;
    }

    public static List<com.frame.component.ui.acticity.tags.Tag> tans2TagList(List<Tag> tags) {
        List<com.frame.component.ui.acticity.tags.Tag> topicTags = new ArrayList<>();
        if (!StrUtil.isEmpty(tags)) {
            for (Tag tag : tags) {
                com.frame.component.ui.acticity.tags.Tag topicTag = new com.frame.component.ui.acticity.tags.Tag();
                topicTag.setId(tag.getTagId());
                topicTag.setTagName(tag.getTagName());
                topicTags.add(topicTag);
            }
        }
        return topicTags;
    }
}
