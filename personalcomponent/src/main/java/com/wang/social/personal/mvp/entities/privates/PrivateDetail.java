package com.wang.social.personal.mvp.entities.privates;

import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PrivateDetail implements Serializable {

    /**
     * id : 1
     * userId : 10001
     * type : 1
     * openAll : true
     * onlySelf : false
     * friend : false
     * groupFriend : false
     * groupIdList : []
     * showAge : false
     * createTime : 2018-03-26 11:25:25
     * updateTime : 2018-03-26 11:25:25
     */

    private int id;
    private int userId;
    private int type;
    private boolean openAll;
    private boolean onlySelf;
    private boolean friend;
    private boolean groupFriend;
    private String showAge;

    public String getShowText() {
        if (openAll) {
            return "公开";
        } else if (onlySelf) {
            return "仅自己可见";
        } else if (friend) {
            return "好友可见";
        } else if (groupFriend) {
            return "仅自己可见";
        } else {
            return "趣聊/觅聊可见";
        }
    }

    public static boolean getShowAgeBool(List<PrivateDetail> privateDetailList) {
        if (StrUtil.isEmpty(privateDetailList)) return false;
        for (PrivateDetail privateDetail : privateDetailList) {
            if (privateDetail.getShowAgeBool()) return true;
        }
        return false;
    }

    public boolean getShowAgeBool() {
        return "1".equals(showAge);
    }

    public void setShowAgeBool(boolean isShowAge) {
        this.showAge = isShowAge ? "1" : "0";
    }
}
