package com.frame.component.ui.acticity.BGMList;

import android.content.Context;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.frame.component.ui.acticity.BGMList.MusicsDTO;

import io.reactivex.Observable;

public interface BGMListContract {
    interface View extends IView {
        Context getViewContext();
        void onNotifyDataSetChanged();
        void onLoadBGMListCompleted();
        String getDefaultMusicName();
        void resetStateAnim(boolean playing);
    }


    interface Model extends IModel {
        Observable<BaseJson<MusicsDTO>> musicList(int size, int current);
    }
}
