package com.wang.social.im.mvp.model.entities.notify;

import com.frame.component.entities.Tag;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FriendRequest implements Serializable{

    /**
     * msgId : 72
     * pass : 0
     * userId : 10034
     * nickname : 哄哄你
     * avatar : http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-04-27-6H2H1lIk.png
     * sex : 1
     * birthday : 631123200000
     * constellation : 射手座
     * tags : []
     * reason : 12312321
     * createTime : 1526006791000
     */

    private int msgId;
    private int pass;
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String constellation;
    private String reason;
    private long createTime;
    private List<Tag> tags;

    public boolean isMale() {
        return sex == 0;
    }

    public boolean isAgreed() {
        return pass == 1;
    }

    public RequestBean trans2RequestBean() {
        RequestBean requestBean = new RequestBean();
        requestBean.setMsgId(msgId);
        requestBean.setUserId(userId);
        requestBean.setAgree(isAgreed());
        requestBean.setNickname(nickname);
        requestBean.setAvatar(avatar);
        requestBean.setSex(sex);
        requestBean.setBirthday(birthday);
        requestBean.setReason(reason);
        requestBean.setCreateTime(createTime);
        return requestBean;
    }

    public static List<RequestBean> tans2RequestBeanList(List<FriendRequest> friendRequests) {
        List<RequestBean> requestBeans = new ArrayList<>();
        if (!StrUtil.isEmpty(friendRequests)) {
            for (FriendRequest friendRequest : friendRequests) {
                requestBeans.add(friendRequest.trans2RequestBean());
            }
        }
        return requestBeans;
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
