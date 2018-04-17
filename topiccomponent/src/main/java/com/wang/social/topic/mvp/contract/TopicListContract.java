package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

import io.reactivex.Observable;

public interface TopicListContract {
    interface View extends IView {
    }


    interface Model extends IModel {
        /**
         * 最新话题列表
         * @param isCondition 是否为大量知识(0:不是，1：是)
         * @param size 每页条数
         * @param current 当前页码
         * @return
         */
        Observable<BaseJson<TopicRspDTO>> getNewsList(String isCondition, int size, int current);
    }
}
