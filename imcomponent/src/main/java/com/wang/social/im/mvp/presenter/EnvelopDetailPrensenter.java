package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.EnvelopDetailContract;

import javax.inject.Inject;

/**
 * ============================================
 * 为{@link com.wang.social.im.mvp.ui.EnvelopDetailActivity}提供逻辑处理
 * <p>
 * Create by ChenJing on 2018-04-24 13:50
 * ============================================
 */
@ActivityScope
public class EnvelopDetailPrensenter extends BasePresenter<EnvelopDetailContract.Model, EnvelopDetailContract.View> {

    @Inject
    public EnvelopDetailPrensenter(EnvelopDetailContract.Model model, EnvelopDetailContract.View view) {
        super(model, view);
    }
}
