package com.frame.component.ui.acticity.BGMList;

import com.frame.http.api.BaseJson;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MusicService {
    /**
     * 歌曲列表（发布添加歌曲）
     */
    @GET("app/common/musicList")
    Observable<BaseJson<MusicsDTO>> musicList(@QueryMap Map<String, Object> param);


}
