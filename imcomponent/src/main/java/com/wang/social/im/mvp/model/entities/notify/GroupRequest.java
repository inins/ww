package com.wang.social.im.mvp.model.entities.notify;

import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GroupRequest {



    public RequestBean trans2RequestBean() {
        RequestBean requestBean = new RequestBean();
//        requestBean.setMsgId(msgId);
//        requestBean.setUserId(userId);
//        requestBean.setAgree(isAgreed());
//        requestBean.setNickname(nickname);
//        requestBean.setAvatar(avatar);
//        requestBean.setSex(sex);
//        requestBean.setBirthday(birthday);
//        requestBean.setReason(reason);
//        requestBean.setCreateTime(createTime);
        return requestBean;
    }

    public static List<RequestBean> tans2RequestBeanList(List<GroupRequest> groupRequests) {
        List<RequestBean> requestBeans = new ArrayList<>();
        if (!StrUtil.isEmpty(groupRequests)) {
            for (GroupRequest groupRequest : groupRequests) {
                requestBeans.add(groupRequest.trans2RequestBean());
            }
        }
        return requestBeans;
    }
}
