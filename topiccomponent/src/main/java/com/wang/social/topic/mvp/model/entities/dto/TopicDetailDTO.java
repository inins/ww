package com.wang.social.topic.mvp.model.entities.dto;


import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import java.util.ArrayList;
import java.util.List;


public class TopicDetailDTO implements Mapper<TopicDetail> {
    private String title;
    private String content;
    private int templateId;
    private String backgroundImage;
    private int creatorId;
    private int birthday;
    private String avatar;
    private String nickname;
    private int shareTotal;
    private int commentTotal;
    private int supportTotal;
    private List<String> tags;

    @Override
    public TopicDetail transform() {
        TopicDetail detail = new TopicDetail();

        detail.setTitle(title == null ? "" : title);
        detail.setContent(content == null ? "" : content);
        detail.setTemplateId(templateId);
        detail.setBackgroundImage(backgroundImage == null ? "" : backgroundImage);
        detail.setCreatorId(creatorId);
        detail.setBirthday(birthday);
        detail.setAvatar(avatar == null ? "" : avatar);
        detail.setNickname(nickname == null ? "" : nickname);
        detail.setShareTotal(shareTotal);
        detail.setCommentTotal(commentTotal);
        detail.setSupportTotal(supportTotal);
        detail.setTags(tags == null ? new ArrayList<>() : tags);

        return detail;
    }
}