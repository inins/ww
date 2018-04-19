package com.wang.social.topic.mvp.contract;

import com.frame.component.view.barview.BarUser;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicTopUsersDTO;

import java.util.List;

import io.reactivex.Observable;

public interface TopicContract {
    interface View extends IView {
        void showToast(String msg);
        void refreshSelectedTagLise();
        void onTopicTopUserLoaded(List<BarUser> list);
    }


    interface Model extends IModel {
        /**
         * 知识魔列表
         * @param size 每页条数
         * @param current 当前页码
         * @param from 来源页面（square：广场；chat：聊天）
         */
        Observable<BaseJson<TopicTopUsersDTO>> getReleaseTopicTopUser(int size, int current, String from);

        /**
         * 已选推荐标签列表（首页/话题）
         * @param size 每页条数
         * @param current 当前页码
         */
        Observable<BaseJson<TagsDTO>> myRecommendTag(int size, int current);
    }
}
