package com.frame.component.entities.dto;

import com.frame.component.entities.Topic;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.frame.component.utils.EntitiesUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicDTO implements Mapper<Topic> {

    private Integer topicId;
    private Integer creatorId;
    private String firstStrff;
    private String backgroundImage;
    private String title;
    private Integer readTotal;
    private Integer supportTotal;
    private Long createTime;
    private String address;
    private Integer commentTotal;
    private Integer shareTotal;
    private Integer relateState;
    private Integer gemstone;
    private List<Tag> tags;
    private Boolean shopping;
    private Boolean support;
    private String avatar;
    private String nickname;
    private Integer isOfficial;
    private Integer isTop;

    @Override
    public Topic transform() {
        Topic object = new Topic();

        object.setTopicId(EntitiesUtil.assertNotNull(topicId));
        object.setCreatorId(EntitiesUtil.assertNotNull(creatorId));
        object.setFirstStrff(EntitiesUtil.assertNotNull(firstStrff));
        object.setBackgroundImage(EntitiesUtil.assertNotNull(backgroundImage));
        object.setTitle(EntitiesUtil.assertNotNull(title));
        object.setReadTotal(EntitiesUtil.assertNotNull(readTotal));
        object.setSupport(EntitiesUtil.assertNotNull(support));
        object.setSupportTotal(EntitiesUtil.assertNotNull(supportTotal));
        object.setCreateTime(EntitiesUtil.assertNotNull(createTime));
        object.setAddress(EntitiesUtil.assertNotNull(address));
        object.setCommentTotal(EntitiesUtil.assertNotNull(commentTotal));
        object.setShareTotal(EntitiesUtil.assertNotNull(shareTotal));
        object.setRelateState(EntitiesUtil.assertNotNull(relateState));
        object.setRelateMoney(EntitiesUtil.assertNotNull(gemstone));
        object.setTags(null == tags ? new ArrayList<>() : tags);
        object.setShopping(EntitiesUtil.assertNotNull(shopping));
        object.setAvatar(EntitiesUtil.assertNotNull(avatar));
        object.setNickname(EntitiesUtil.assertNotNull(nickname));
        object.setTop(EntitiesUtil.assertNotNull(isTop) == 1);
        isOfficial = 1;
        object.setOfficial(EntitiesUtil.assertNotNull(isOfficial) == 1);

        return object;
    }
}
