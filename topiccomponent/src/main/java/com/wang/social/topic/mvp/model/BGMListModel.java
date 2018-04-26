package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.BGMListContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.MusicsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

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
                .obtainRetrofitService(TopicService.class)
                .musicList(param);
    }
}
