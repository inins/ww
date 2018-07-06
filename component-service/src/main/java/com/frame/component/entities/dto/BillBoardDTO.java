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
    private Integer linkType;

    @Override
    public BillBoard transform() {
        BillBoard object = new BillBoard();

        object.setBillboardId(EntitiesUtil.assertNotNull(billboardId));
        object.setPicUrl(EntitiesUtil.assertNotNull(picUrl));
        object.setLinkUrl(EntitiesUtil.assertNotNull(linkUrl));
        object.setLinkType(EntitiesUtil.assertNotNull(linkType));

//        // 内部浏览器
//        object.setBillboardId(EntitiesUtil.assertNotNull(billboardId));
//        object.setPicUrl(EntitiesUtil.assertNotNull("http://pic21.photophoto.cn/20111019/0034034837110352_b.jpg"));
//        object.setLinkUrl(EntitiesUtil.assertNotNull("https://blog.csdn.net/chengliang0315/article/details/53895131"));
//        object.setLinkType(EntitiesUtil.assertNotNull(1));
//
//
//        // 外部浏览器
//        object.setBillboardId(EntitiesUtil.assertNotNull(billboardId));
//        object.setPicUrl(EntitiesUtil.assertNotNull("http://pic21.photophoto.cn/20111019/0034034837110352_b.jpg"));
//        object.setLinkUrl(EntitiesUtil.assertNotNull("https://blog.csdn.net/chengliang0315/article/details/53895131"));
//        object.setLinkType(EntitiesUtil.assertNotNull(4));

        return object;
    }
}
