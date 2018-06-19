package com.wang.social.home.mvp.entities;

import com.frame.component.entities.funpoint.Funpoint;
import com.google.gson.annotations.SerializedName;
import com.wang.social.home.mvp.entities.topic.TopicHome;

import java.io.Serializable;

import lombok.Data;

@Data
public class FunpointAndTopic implements Serializable{

    private Funpoint funpoint;
    private TopicHome topic;

    public FunpointAndTopic(Funpoint funpoint) {
        this.funpoint = funpoint;
    }

    public FunpointAndTopic(TopicHome topic) {
        this.topic = topic;
    }

    ////////////////////////////////

    public boolean isFunpoint() {
        return funpoint != null;
    }

    public boolean isTopic() {
        return topic != null;
    }
}
