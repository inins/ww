package com.frame.component.ui.acticity.BGMList;

import com.frame.component.common.NetParam;
import com.frame.component.service.BuildConfig;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class BGMListModel extends BaseModel implements BGMListContract.Model {

    @Inject
    public BGMListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 歌曲列表（发布添加歌曲）（话题/趣晒）
     * @param size 每页条数
     * @param current 当前页数
     * @return
     */
    @Override
    public Observable<BaseJson<MusicsDTO>> musicList(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", BuildConfig.VERSION_NAME)
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MusicService.class)
                .musicList(param);
    }
}
