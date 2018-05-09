package com.wang.social.home.mvp.entities;

import com.frame.component.entities.funpoint.Funpoint;

import lombok.Data;

@Data
public class FunpointAndTopic {

    private Funpoint funpoint;
    private Topic topic;

    public FunpointAndTopic(Funpoint funpoint) {
        this.funpoint = funpoint;
    }

    public FunpointAndTopic(Topic topic) {
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
