package com.wang.social.im.mvp.model.entities.notify;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FindChatRequest {


    /**
     * msgId : 22
     * pass : 0
     * groupId : 4
     * groupName : gougou
     * userId : 10000
     * nickname : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * sex : 1
     * birthday : 725904000000
     * constellation : 射手座
     * tags : [{"id":2,"tagId":3,"tagName":"电影","isIndexShow":0},{"id":3,"tagId":4,"tagName":"电视剧","isIndexShow":1},{"id":6,"tagId":6,"tagName":"直播","isIndexShow":0},{"id":4,"tagId":5,"tagName":"综艺","isIndexShow":0}]
     * reason : 伸手党
     * createTime : 1525228106000
     */

    private int msgId;
    private int pass;
    private int groupId;
    private String groupName;
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String constellation;
    private String reason;
    private long createTime;
    private List<Tag> tags;
    private int readState;

    public boolean isRead() {
        return readState == 1;
    }

    public RequestBean trans2RequestBean() {
        RequestBean requestBean = new RequestBean();
        requestBean.setMsgId(msgId);
        requestBean.setUserId(userId);
        requestBean.setStatus(pass);
        requestBean.setNickname(nickname);
        requestBean.setAvatar(avatar);
        requestBean.setSex(sex);
        requestBean.setBirthday(birthday);
        requestBean.setReason(reason);
        requestBean.setCreateTime(createTime);
        requestBean.setGroupId(groupId);
        requestBean.setReadState(readState);
        requestBean.setTags(tags);
        return requestBean;
    }

    public static List<RequestBean> tans2RequestBeanList(List<FindChatRequest> groupRequests) {
        List<RequestBean> requestBeans = new ArrayList<>();
        if (!StrUtil.isEmpty(groupRequests)) {
            for (FindChatRequest groupRequest : groupRequests) {
                requestBeans.add(groupRequest.trans2RequestBean());
            }
        }
        return requestBeans;
    }
}
