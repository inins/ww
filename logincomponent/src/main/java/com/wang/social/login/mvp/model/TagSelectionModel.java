package com.wang.social.login.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.api.TagService;
import com.wang.social.login.mvp.model.entities.PersonalTagCount;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.PersonalTagCountDTO;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

@ActivityScope
public class TagSelectionModel extends BaseModel implements TagSelectionContract.Model {
    @Inject
    public TagSelectionModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TagsDTO>> parentTagList() {
        Map<String, Object> param = new NetParam()
                .put("v","2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .parentTagList(param);
    }

    @Override
    public Observable<BaseJson> updateRecommendTag(List<Tag> list) {
        String tagIds = "";
        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagIds = tagIds + tag.getId();
            if (i < list.size() - 1) {
                tagIds = tagIds + ",";
            }
        }
        Map<String, Object> param = new NetParam()
                .put("tagIds", tagIds)
                .put("v","2.0.0")
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
                .put("v","2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .myRecommendTag(param);
    }

    @Override
    public Observable<BaseJson> addPersonalTag(List<Tag> list) {
        String tagIds = "";
        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagIds = tagIds + tag.getId();
            if (i < list.size() - 1) {
                tagIds = tagIds + ",";
            }
        }
        Map<String, Object> param = new NetParam()
                .put("tagIds", tagIds)
                .put("v","2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .addPersonalTag(param);
    }

    @Override
    public Observable<BaseJson<PersonalTagCountDTO>> findMyTagCount() {
        Map<String, Object> param = new NetParam()
                .put("v","2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .findMyTagCount(param);
    }
}
