package com.wang.social.im.mvp.model.entities.dto;

import android.text.TextUtils;

import com.frame.component.entities.dto.TagDTO;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.model.entities.SocialInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 9:29
 * ============================================
 */
public class SocialDTO implements Mapper<SocialInfo> {

    private String groupId;
    private String groupName;
    private String groupDesc;
    private String groupCoverPlan;
    private String headUrl;
    private int isOpen; //是否公开: 0.封闭; 1.公开
    private int isFree;//是否免费: 0.收费; 1.免费用
    private int gemstone;
    private Integer gender;//性别限制 （null表示男女都可以） 0男 1女
    private String ageRange; //年代区间  90,00
    private int isCreateMi;
    private List<TagDTO> tags;

    @Override
    public SocialInfo transform() {
        SocialInfo social = new SocialInfo();
        social.setSocialId(groupId == null ? "" : groupId);
        social.setName(groupName == null ? "" : groupName);
        social.setCover(groupCoverPlan == null ? "" : groupCoverPlan);
        social.setDesc(groupDesc == null ? "" : groupDesc);
        SocialAttribute attr = new SocialAttribute();
        attr.setOpen(isOpen == 1);
        attr.setCharge(isFree == 0);
        attr.setGem(gemstone);
        if (gender == null) {
            attr.setGenderLimit(SocialAttribute.GenderLimit.UNLIMITED);
        } else if (gender == 0) {
            attr.setGenderLimit(SocialAttribute.GenderLimit.MALE);
        } else if (gender == 1) {
            attr.setGenderLimit(SocialAttribute.GenderLimit.FEMALE);
        }
        if (TextUtils.isEmpty(ageRange)) {
            String[] limit = ageRange.split(",");
            for (String age : limit) {
                if (age.equals("90")) {
                    attr.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_90);
                } else if (age.equals("95")) {
                    attr.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_95);
                } else if (age.equals("00")) {
                    attr.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_95);
                } else if (age.equals("other")) {
                    attr.getAgeLimit().add(SocialAttribute.AgeLimit.OTHER);
                }
            }
        } else {
            attr.getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);
        }
        social.setAttr(attr);
        social.setCreateTeam(isCreateMi == 1);
        List<Tag> tagList = new ArrayList<>();
        if (tags != null) {
            for (TagDTO tagDTO : tags) {
                tagList.add(tagDTO.transform());
            }
        }
        social.setTags(tagList);
        return social;
    }
}
