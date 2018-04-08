package com.wang.social.login.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.api.TagService;
import com.wang.social.login.mvp.model.entities.dto.Tags;
import com.wang.social.login.mvp.model.entities.dto.TagsDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class TagSelectionModel extends BaseModel implements TagSelectionContract.Model {
    @Inject
    public TagSelectionModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TagsDTO>> passwordLogin() {
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .parentTagList();
    }
}
