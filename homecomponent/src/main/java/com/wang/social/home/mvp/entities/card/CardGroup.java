package com.wang.social.home.mvp.entities.card;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CardGroup implements Serializable{

    /**
     * groupCoverPlan : http://resouce.dongdongwedding.com/FlUnlBWYWeg
     * groupDesc : 大吉大利，今晚吃鸡
     * groupId : 26
     * groupName : 口乞又鸟
     * isFree : 1
     * groupTags : [{"tagId":4,"tagName":"电视剧"},{"tagId":5,"tagName":"综艺"}]
     * memberNum : 28
     * gemstone  : 0
     * validation : 1
     */

    private String groupCoverPlan;
    private String groupDesc;
    private int groupId;
    private String groupName;
    private int isFree;
    private int memberNum;
    private int gemstone;
    private int validation;
    private List<Tag> groupTags;

    public String getTagText() {
        if (StrUtil.isEmpty(groupTags)) return "";
        String tagText = "# ";
        for (Tag tag : groupTags) {
            tagText += tag.getTagName() + " ";
        }
        return tagText.trim();
    }

    public boolean isFree() {
        return isFree == 1;
    }
}
