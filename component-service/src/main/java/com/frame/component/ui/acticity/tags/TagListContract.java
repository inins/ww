package com.frame.component.ui.acticity.tags;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.frame.component.ui.acticity.tags.TagsDTO;

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
