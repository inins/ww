package com.frame.component.entities.funshow;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowGroup {

    public FunshowBean tans2FunshowBean() {
        FunshowBean funshowBean = new FunshowBean();

//        funshowBean.setId(id);
//        funshowBean.setUserId(creatorId);
//        funshowBean.setNickname(nickname);
//        funshowBean.setCreateTime(createTime);
//        funshowBean.setContent(content);
//        funshowBean.setShowPic(showPic);
//        funshowBean.setPicNum(picNum);
//        funshowBean.setFree(isFree());
//        funshowBean.setVideo(hasVideo());
//        funshowBean.setSupportTotal(supportTotal);
//        funshowBean.setCommentTotal(commentTotal);
//        funshowBean.setShareTotal(share_total);
//        funshowBean.setSupport(isSupportBool());
//        funshowBean.setCityName(cityName);
//        funshowBean.setProvinceName(provinceName);
//        funshowBean.setPrice(gemstone);
//        funshowBean.setPay(true);   //自己的趣晒不需要支付，默认为已经支付
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<FunshowGroup> list) {
        List<FunshowBean> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (FunshowGroup bean : list) {
                toList.add(bean.tans2FunshowBean());
            }
        }
        return toList;
    }
}
