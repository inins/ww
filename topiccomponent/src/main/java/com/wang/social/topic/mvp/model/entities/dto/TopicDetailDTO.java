package com.wang.social.topic.mvp.model.entities.dto;


import com.frame.http.api.Mapper;
import com.wang.social.topic.StringUtil;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import java.util.ArrayList;
import java.util.List;


public class TopicDetailDTO implements Mapper<TopicDetail> {
    private Long birthday;
    private String backgroundMusicId;
    private String backgroundMusicName;
    private String backgroundMusicUrl;
    private String backgroundImage;
    private Integer supportTotal;
    private Integer isSupport;
    private Integer sex;
    private Integer creatorId;
    private Integer commentTotal;
    private String avatar;
    private Integer readTotal;
    private String title;
    private String templateId;
    private String content;
    private Integer relateState;
    private Integer gemstone;
    private List<String> tags ;
    private Integer topicId;
    private Long createTime;
    private String shareTotal;
    private String nickname;

    private int assertNotNull(Integer i) {
        return i == null ? 0 : i;
    }

    private long assertNotNull(Long l) {
        return l == null ? 0L : l;
    }

    @Override
    public TopicDetail transform() {
        TopicDetail object = new TopicDetail();

        object.setBirthday(birthday == null ? 0L : birthday);
        object.setBackgroundMusicId(StringUtil.assertNotNull(backgroundMusicId));
        object.setBackgroundMusicName(StringUtil.assertNotNull(backgroundMusicName));
        object.setBackgroundMusicUrl(StringUtil.assertNotNull(backgroundMusicUrl));
        object.setBackgroundImage(StringUtil.assertNotNull(backgroundImage));
        object.setSupportTotal(assertNotNull(supportTotal));
        object.setIsSupport(assertNotNull(isSupport));
        object.setSex(assertNotNull(sex));
        object.setCreatorId(assertNotNull(creatorId));
        object.setCommentTotal(assertNotNull(commentTotal));
        object.setAvatar(StringUtil.assertNotNull(avatar));
        object.setReadTotal(assertNotNull(readTotal));
        object.setTitle(StringUtil.assertNotNull(title));
        object.setTemplateId(StringUtil.assertNotNull(templateId));
        object.setContent(StringUtil.assertNotNull(content));
        object.setRelateState(assertNotNull(relateState));
        object.setGemstone(assertNotNull(gemstone));
        object.setTags(tags == null ? new ArrayList<>() : tags);
        object.setTopicId(assertNotNull(topicId));
        object.setCreateTime(assertNotNull(createTime));
        object.setShareTotal(StringUtil.assertNotNull(shareTotal));
        object.setNickname(StringUtil.assertNotNull(nickname));

        return object;
    }
}