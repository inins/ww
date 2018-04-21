package com.wang.social.topic.mvp.model.entities.dto;


import com.frame.http.api.Mapper;
import com.wang.social.topic.StringUtil;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import java.util.ArrayList;
import java.util.List;


public class TopicDetailDTO implements Mapper<TopicDetail> {
    private long birthday;
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
    private long createTime;
    private String shareTotal;
    private String nickname;

    @Override
    public TopicDetail transform() {
        TopicDetail object = new TopicDetail();

        object.setBirthday(birthday);
        object.setBackgroundMusicId(StringUtil.assertNotNull(backgroundMusicId));
        object.setBackgroundMusicName(StringUtil.assertNotNull(backgroundMusicName));
        object.setBackgroundMusicUrl(StringUtil.assertNotNull(backgroundMusicUrl));
        object.setBackgroundImage(StringUtil.assertNotNull(backgroundImage));
        object.setSupportTotal(supportTotal);
        object.setIsSupport(isSupport);
        object.setSex(sex);
        object.setCreatorId(creatorId);
        object.setCommentTotal(commentTotal);
        object.setAvatar(StringUtil.assertNotNull(avatar));
        object.setReadTotal(readTotal);
        object.setTitle(StringUtil.assertNotNull(title));
        object.setTemplateId(templateId);
        object.setContent(StringUtil.assertNotNull(content));
        object.setRelateState(relateState);
        object.setGemstone(gemstone);
        object.setTags(tags == null ? new ArrayList<>() : tags);
        object.setTopicId(topicId);
        object.setCreateTime(createTime);
        object.setShareTotal(shareTotal);
        object.setNickname(StringUtil.assertNotNull(nickname));

        return object;
    }
}