package com.wang.social.personal.mvp.model;

import com.frame.di.scope.FragmentScope;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import javax.inject.Inject;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:32
 * ========================================
 */
@FragmentScope
public class UserModel extends BaseModel {

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

//    public Observable<BaseJson<UserWrap>> login(String mobile, String password) {
//        return mRepositoryManager
//                .obtainRetrofitService(UserService.class)
//                .login(mobile, password);
//    }
}