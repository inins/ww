package com.wang.social.im.mvp.ui.PersonalCard.model.entities.DTO;

import com.frame.http.api.Mapper;
import com.frame.component.utils.EntitiesUtil;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.FriendList;
import com.frame.component.entities.PersonalInfo;

import java.util.ArrayList;
import java.util.List;

public class FriendListDTO implements Mapper<FriendList> {
    private Integer total;
    private Integer current;
    private Integer pages;
    private Integer size;
    private Integer isPermission;
    private List<PersonalInfo> list;

    @Override
    public FriendList transform() {
        FriendList object = new FriendList();

        object.setTotal(EntitiesUtil.assertNotNull(total));
        object.setCurrent(EntitiesUtil.assertNotNull(current));
        object.setPages(EntitiesUtil.assertNotNull(pages));
        object.setSize(EntitiesUtil.assertNotNull(size));
        object.setIsPermission(EntitiesUtil.assertNotNull(isPermission));
        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
