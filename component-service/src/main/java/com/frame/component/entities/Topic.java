package com.frame.component.entities;

import android.text.TextUtils;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Topic {

    private int topicId;
    private int creatorId;
    private String firstStrff;
    private String backgroundImage;
    private String title;
    private int readTotal;
    private int supportTotal;
    private long createTime;
    private String address;
    private int commentTotal;
    private int shareTotal;
    private int relateState;
    private int relateMoney;
    private List<Tag> tags;
    private boolean isShopping;
    private boolean isSupport;
    private String avatar;
    private String nickname;
    //是否置顶
    private boolean isTop;
    //是否官方号
    private boolean isOfficial;

    public String getTagStr() {
        String ret = "";
        if (null != tags) {
            for (Tag tag : tags) {
                String name = "";
                if (!TextUtils.isEmpty(tag.getTagName())) {
                    name = tag.getTagName();
                } else if (!TextUtils.isEmpty(tag.getTopicTagName())) {
                    name = tag.getTopicTagName();
                }
                ret += name + ",";
            }
        }
        return StrUtil.subLastChart(ret, ",");
    }
}
