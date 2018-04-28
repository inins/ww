package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.TagListContract;
import com.wang.social.login.mvp.model.api.TagService;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

import com.wang.social.login.BuildConfig;
@FragmentScope
public class TagListModel extends BaseModel implements TagListContract.Model {
    @Inject
    public TagListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     *
     * @param size 每页条数
     * @param current 当前页码
     * @param parentId 父标签id
     * @return
     */
    @Override
    public Observable<BaseJson<TagsDTO>> taglist(int size, int current, int parentId) {

        Map<String, Object> param = new NetParam()
                .put("size", size)
                .put("current", current)
                .put("parentId", parentId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .taglist(param);
    }
}
