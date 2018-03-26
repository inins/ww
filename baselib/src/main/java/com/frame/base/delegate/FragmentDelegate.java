package com.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * ========================================
 * {@link android.support.v4.app.Fragment} 代理类， 用于在每个{@link android.support.v4.app.Fragment} 对应的生命周期中插入需要的逻辑
 *
 * @see FragmentDelegateImp
 * Create by ChenJing on 2018-03-13 14:40
 * ========================================
 */

public interface FragmentDelegate {

    String FRAGMENT_DELEGATE_CACHE_KEY = "fragment_delegate";

    void onAttach(Context context);

    void onCreate(@Nullable Bundle onSavedInstanceState);

    void onCreateView(@Nullable View view, @Nullable Bundle onSavedInstanceState);

    void onActivityCreate(@Nullable Bundle onSavedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@Nullable Bundle outState);

    void onDestroyView();

    void onDestroy();

    void onDetach();

    /**
     * Return true if the fragment is currently added to its activity.
     */
    boolean isAdded();
}