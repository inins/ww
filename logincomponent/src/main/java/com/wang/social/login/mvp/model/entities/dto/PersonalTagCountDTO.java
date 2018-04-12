package com.wang.social.login.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.login.mvp.model.entities.PersonalTagCount;

public class PersonalTagCountDTO implements Mapper<PersonalTagCount> {
    int tagCount;

    @Override
    public PersonalTagCount transform() {
        PersonalTagCount o = new PersonalTagCount();
        o.setTagCount(tagCount);
        return o;
    }
}
