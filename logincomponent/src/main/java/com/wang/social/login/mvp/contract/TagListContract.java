package com.wang.social.login.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import io.reactivex.Observable;

public interface TagListContract {
    interface View extends IView {
        void showToast(String msg);

        void tagListChanged();
        void resetTagListView();
    }

    interface Model extends IModel {
        Observable<BaseJson<TagsDTO>> taglist(int size, int current, int parentId );
    }
}
