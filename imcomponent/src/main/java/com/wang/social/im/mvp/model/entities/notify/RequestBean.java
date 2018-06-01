package com.wang.social.im.mvp.model.entities.notify;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RequestBean implements Serializable{

    private int msgId;
    private int userId;
    private int status;     //0 未处理 1通过 2过期 3拒绝
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String reason;
    private long createTime;
    private int readState;

    private List<Tag> tags;
    private int groupId;

    public boolean isRead() {
        return readState == 1;
    }

    public boolean isMale() {
        return sex == 0;
    }

    public boolean isDeal() {
        return status != 0;
    }

    public String getStatusText() {
        switch (status) {
            case 0:
                return "同意";
            case 1:
                return "已同意";
            case 2:
                return "已过期";
            case 3:
                return "已拒绝";
            default:
                return "";
        }
    }

    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }
}
