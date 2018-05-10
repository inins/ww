package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.component.entities.User;
import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.MemberList;

import java.util.ArrayList;
import java.util.List;

public class MemberListDTO implements Mapper<MemberList> {
    List<User> list;

    @Override
    public MemberList transform() {
        MemberList object = new MemberList();

        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
