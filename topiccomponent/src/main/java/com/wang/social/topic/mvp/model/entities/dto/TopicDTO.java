package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.component.entities.Topic;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicDTO implements Mapper<Topic> {

//    private Integer topicId;
//    private String firstStrff;
//    private String topicImage;
//    private String userCover;
//    private String title;
//    private Integer topicReadNum;
//    private Integer topicSupportNum;
//    private Long createTime;
//    private String address;
//    private Integer topicCommentNum;
//    private Integer shareTotal;
//    private Integer isFree;
//    private Integer gemstone;
//    private List<Tag> topicTag;
//    private Integer isShopping;
//    private Integer isSupport;
//    private String userName;
//    private Integer userId;
//    private Integer price;

    private String firstStrff;
    private String topicImage;
    private Integer isSupport;
    private String title;
    private String userName;
    private Integer topicReadNum;
    private Integer userId;
    private Integer topicId;
    private Integer isFree;
    private Long createTime;
    private Integer topicCommentNum;
    private Integer price;
    private Integer topicSupportNum;
    private List<Tag> topicTag;
    private String userCover;
    private Integer isShopping;
    private Integer isOfficial;
    private Integer isTop;

    @Override
    public Topic transform() {
        Topic object = new Topic();

        object.setTopicId(EntitiesUtil.assertNotNull(topicId));
        object.setCreatorId(EntitiesUtil.assertNotNull(userId));
        object.setFirstStrff(EntitiesUtil.assertNotNull(firstStrff));
        object.setBackgroundImage(EntitiesUtil.assertNotNull(topicImage));
        object.setTitle(EntitiesUtil.assertNotNull(title));
        object.setReadTotal(EntitiesUtil.assertNotNull(topicReadNum));
        object.setSupport(EntitiesUtil.assertNotNull(isSupport) != 0);
        object.setSupportTotal(EntitiesUtil.assertNotNull(topicSupportNum));
        object.setCreateTime(EntitiesUtil.assertNotNull(createTime));
        object.setCommentTotal(EntitiesUtil.assertNotNull(topicCommentNum));
        // 是否收费（0免费 1收费）
        object.setRelateState(EntitiesUtil.assertNotNull(isFree));
        object.setRelateMoney(EntitiesUtil.assertNotNull(price));
        object.setTags(null == topicTag ? new ArrayList<>() : topicTag);
        // 是否需要购买（0 无需购买 1 购买）
        object.setShopping(isShopping == 0);
        object.setAvatar(EntitiesUtil.assertNotNull(userCover));
        object.setNickname(EntitiesUtil.assertNotNull(userName));

        object.setTop(EntitiesUtil.assertNotNull(isTop) == 1);
        object.setOfficial(EntitiesUtil.assertNotNull(isOfficial) == 1);

        return object;
    }
}
