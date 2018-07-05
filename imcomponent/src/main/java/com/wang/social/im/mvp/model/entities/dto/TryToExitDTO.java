package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.TryToExit;

public class TryToExitDTO implements Mapper<TryToExit> {
    private String result;
    @Override
    public TryToExit transform() {
        TryToExit object = new TryToExit();

        object.setHasTeam("yes".equals(result));

        return object;
    }
}
