package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.BaseListJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.PhoneBookContract;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.model.entities.ContactCheckResult;
import com.wang.social.im.mvp.model.entities.dto.ContactCheckResultDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 15:48
 * ============================================
 */
@ActivityScope
public class PhoneBookModel extends BaseModel implements PhoneBookContract.Model {

    @Inject
    public PhoneBookModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<ContactCheckResultDTO, ContactCheckResult>>> checkPhoneBook(String phones) {
        return mRepositoryManager
                .obtainRetrofitService(ChainService.class)
                .checkPhoneBook("2.0.0", phones);
    }
}