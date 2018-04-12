package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.Tags;
import com.wang.social.login.mvp.model.entities.dto.PersonalTagCountDTO;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.List;

import io.reactivex.Observable;

public interface TagSelectionContract {
    interface View extends IView {
        void showToast(String msg);

        void resetTabView(Tags tags);
        void refreshCountTV();
        // 更新已选标签成功
        void onUpdateTagSuccess();

        void setMyTagCount(int count);
    }


    interface Model extends IModel {
        Observable<BaseJson<TagsDTO>> parentTagList();
        Observable<BaseJson> updateRecommendTag(List<Tag> list);
        Observable<BaseJson<TagsDTO>> myRecommendTag(int size, int current);
        Observable<BaseJson> addPersonalTag(List<Tag> list);
        Observable<BaseJson<PersonalTagCountDTO>> findMyTagCount();
    }
}
