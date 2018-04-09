package com.wang.social.personal.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.lable.LableWrap;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface LableContract {

    interface View extends IView {

        void freshTagList(List<Lable> lables);

        void freshFlagList(List<Lable> lables);

        void freshParentTagList(List<Lable> lables);
    }

    interface Model extends IModel {

        Observable<BaseJson<LableWrap>> getShowtag();

        Observable<BaseJson<LableWrap>> getSelftags(int parentId);

        Observable<BaseJson<LableWrap>> getParentTags();

        Observable<BaseJson<Object>> deltag(int tagId);
    }
}
