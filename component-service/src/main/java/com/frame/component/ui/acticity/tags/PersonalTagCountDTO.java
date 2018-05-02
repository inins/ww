package com.frame.component.ui.acticity.tags;

import com.frame.http.api.Mapper;
import com.frame.component.ui.acticity.tags.PersonalTagCount;

public class PersonalTagCountDTO implements Mapper<PersonalTagCount> {
    int tagCount;

    @Override
    public PersonalTagCount transform() {
        PersonalTagCount o = new PersonalTagCount();
        o.setTagCount(tagCount);
        return o;
    }
}
