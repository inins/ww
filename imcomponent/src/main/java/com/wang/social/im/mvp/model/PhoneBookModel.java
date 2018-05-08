package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.PhoneBookContract;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 15:48
 * ============================================
 */
@ActivityScope
public class PhoneBookModel extends BaseModel implements PhoneBookContract.Model{

    @Inject
    public PhoneBookModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
