package com.frame.component.entities.dto;


import com.frame.component.entities.BillBoard;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

public class BillBoardDTO implements Mapper<BillBoard> {
    /**
     * 参数名字	参数类型	说明	是否必须
     billboardId	Integer	广告ID	√
     picUrl	String	图片URL	√
     linkUrl	String	链接URL	√
     linkType	Integer	链接类型	√
     */
    private Integer billboardId;
    private String picUrl;
    private String linkUrl;
    private String linkType;

    @Override
    public BillBoard transform() {
        BillBoard object = new BillBoard();

        object.setBillboardId(EntitiesUtil.assertNotNull(billboardId));
        object.setPicUrl(EntitiesUtil.assertNotNull(picUrl));
        object.setLinkUrl(EntitiesUtil.assertNotNull(linkUrl));
        object.setLinkType(EntitiesUtil.assertNotNull(linkType));

        return object;
    }
}
