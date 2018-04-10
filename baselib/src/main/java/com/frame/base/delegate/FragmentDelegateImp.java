package com.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.frame.utils.FrameUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * ========================================
 * {@link FragmentDelegate} 实现类
 * <p>
 * Create by ChenJing on 2018-03-13 14:50
 * ========================================
 */

public class FragmentDelegateImp implements FragmentDelegate {

    private static final String PARAM_STATE_IS_HIDDEN = "is_hidden";

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;
    private Unbinder mUnbinder;

    public FragmentDelegateImp(FragmentManager fragmentManager, Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            //根据保存的状态，控制当前Fragment的显示隐藏，解决内存重启时多个Fragment重叠问题
            boolean isHidden = savedInstanceState.getBoolean(PARAM_STATE_IS_HIDDEN, false);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (isHidden) {
                transaction.hide(mFragment);
            } else {
                transaction.show(mFragment);
            }
            transaction.commit();
        }
        if (iFragment.useEventBus()){
            EventBus.getDefault().register(mFragment);
        }
        iFragment.setupFragmentComponent(FrameUtils.obtainAppComponentFromContext(mFragment.getActivity()));
    }

    @Override
    public void onCreateView(@Nullable View view, @Nullable Bundle onSavedInstanceState) {
        if (view != null){
            mUnbinder = ButterKnife.bind(mFragment, view);
        }
    }

    @Override
    public void onActivityCreate(@Nullable Bundle onSavedInstanceState) {
        iFragment.initData(onSavedInstanceState);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        outState.putBoolean(PARAM_STATE_IS_HIDDEN, mFragment.isHidden());
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY){
            try{
                mUnbinder.unbind();
            }catch (Exception e){
                e.printStackTrace();
                Timber.w("Fragment onDestroyView: "+e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        if (iFragment.useEventBus()){
            EventBus.getDefault().unregister(this);
        }
        this.iFragment = null;
        this.mFragmentManager = null;
        this.mFragment = null;
        this.mUnbinder = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
