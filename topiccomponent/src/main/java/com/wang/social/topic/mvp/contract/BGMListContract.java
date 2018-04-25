package com.wang.social.topic.mvp.contract;

import android.content.Context;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.MusicsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

import io.reactivex.Observable;

public interface BGMListContract {
    interface View extends IView {
        Context getViewContext();
        void onNotifyDataSetChanged();
        String getDefaultMusicName();
        void resetStateAnim(boolean playing);
    }


    interface Model extends IModel {
        Observable<BaseJson<MusicsDTO>> musicList(int size, int current);
    }
}
