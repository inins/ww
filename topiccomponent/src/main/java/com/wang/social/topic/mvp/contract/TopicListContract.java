package com.wang.social.topic.mvp.contract;

import com.frame.component.entities.Topic;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TopicDTO;

import io.reactivex.Observable;

public interface TopicListContract {
    interface View extends IView {
        void showToast(String msg);

        /**
         * 话题列表加载成功
         */
        void onTopicLoadSuccess();

        /**
         * 话题列表下载完成（成功或失败）
         */
        void onTopicLoadCompleted();
        void refreshTopicList();
    }


    interface Model extends IModel {
        /**
         * 最新话题列表
         * @param isCondition 是否为大量知识(0:不是，1：是)
         * @param size 每页条数
         * @param current 当前页码
         */
        Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getNewsList(String isCondition, int size, int current);

        /**
         * 最热话题列表
         * @param size 每页条数
         * @param current 当前页码
         */
        Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getHotList(int size, int current);

        /**
         * 无人问津话题列表
         * @param size 每页条数
         * @param current 当前页码
         */
        Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getLowList(int size, int current);
    }
}
