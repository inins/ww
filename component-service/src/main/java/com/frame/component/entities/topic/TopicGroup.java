package com.frame.component.entities.topic;

import com.frame.component.entities.Topic;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TopicGroup {

    public Topic tans2Topic() {
        Topic topic = new Topic();

//        topic.setTopicId(id);
//        //topic.setCreatorId();
//        topic.setFirstStrff(firstStrff);
//        topic.setBackgroundImage(backgroundImage);
//        topic.setTitle(title);
//        topic.setReadTotal(readTotal);
//        topic.setSupportTotal(supportTotal);
//        topic.setCreateTime(createTime);
//        //topic.setAddress();
//        topic.setCommentTotal(commentTotal);
//        topic.setShareTotal(shareTotal);
//        topic.setRelateState(relateState);
//        topic.setRelateMoney(gemstone);
//        topic.setTags(tans2TagList(tags));
//        //topic.setShopping();
//        topic.setSupport(isSupport());
//        topic.setAvatar(avatar);
//        topic.setNickname(nickname);
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
