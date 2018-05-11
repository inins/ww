package com.frame.component.ui.acticity.tags;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class TagSelectionModel extends BaseModel implements TagSelectionContract.Model {
    @Inject
    public TagSelectionModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TagsDTO>> parentTagList() {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .parentTagList(param);
    }

    @Override
    public Observable<BaseJson> updateRecommendTag(List<Tag> list) {
        String tagIds = TagUtils.formatTagIds(list);
        Map<String, Object> param = new NetParam()
                .put("tagIds", tagIds)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .updateRecommendTag(param);
    }

    @Override
    public Observable<BaseJson<TagsDTO>> myRecommendTag(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .myRecommendTag(param);
    }

    @Override
    public Observable<BaseJson> addPersonalTag(List<Tag> list) {
        String tagIds = TagUtils.formatTagIds(list);
        Map<String, Object> param = new NetParam()
                .put("tagIds", tagIds)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .addPersonalTag(param);
    }

    @Override
    public Observable<BaseJson<PersonalTagCountDTO>> findMyTagCount() {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .findMyTagCount(param);
    }
}
