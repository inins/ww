package com.wang.social.login.mvp.model;

import com.wang.social.login.mvp.model.entities.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagEvent {
    String tag;
    Tag object;
}
