package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ShareModel;

import java.util.List;

/**
 * Created by Chao on 2017/8/21.
 */

public class ShareDTO implements Mapper<ShareModel> {
    private List<ShareModel> nodes;
    private String headerImage;
    private int isSelf;
    private int haveMoney;


    @Override
    public ShareModel transform() {
        ShareModel model = new ShareModel();
        model.setNodes(nodes);
        model.setHeaderImage(headerImage);
        model.setIsSelf(isSelf);
        model.setHaveMoney(haveMoney);
        return model;
    }
}
