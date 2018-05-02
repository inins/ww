package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.TemplateContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class TemplateModel extends BaseModel implements TemplateContract.Model {

    @Inject
    public TemplateModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    /**
     * 模板列表
     * @param type 模板类型（1 话题）
     * @param size 每页条数
     * @param current 当前页码
     * @return
     */
    @Override
    public Observable<BaseJson<TemplatesDTO>> templeList(int type, int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("type", type)
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .templeList(param);
    }
}
