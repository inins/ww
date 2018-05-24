package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.OfficialImage;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:32
 * ============================================
 */
public class OfficialImageDTO implements Mapper<OfficialImage> {

    private String picId;
    private String picUrl;

    @Override
    public OfficialImage transform() {
        OfficialImage officialImage = new OfficialImage();
        officialImage.setImageUrl(picUrl == null ? "" : picUrl);
        return officialImage;
    }
}
