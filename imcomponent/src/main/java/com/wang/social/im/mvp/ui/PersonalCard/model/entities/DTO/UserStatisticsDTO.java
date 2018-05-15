package com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.EntitiesUtil;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.UserStatistics;

import lombok.Data;

@Data
public class UserStatisticsDTO implements Mapper<UserStatistics> {
    private Integer friendCount;
    private Integer groupCount;
    private Integer talkCount;
    private Integer topicCount;

    @Override
    public UserStatistics transform() {
        UserStatistics object = new UserStatistics();

        object.setFriendCount(EntitiesUtil.assertNotNull(friendCount));
        object.setGroupCount(EntitiesUtil.assertNotNull(groupCount));
        object.setTalkCount(EntitiesUtil.assertNotNull(talkCount));
        object.setTopicCount(EntitiesUtil.assertNotNull(topicCount));

        return object;
    }
}
