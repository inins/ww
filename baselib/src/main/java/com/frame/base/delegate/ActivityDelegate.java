package com.frame.base.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * ====================================
 * {@link android.app.Activity} 代理类，用于在每个{@link android.app.Activity} 对应的生命周期中插入需要的逻辑
 *
 * @see ActivityDelegateImp
 * Created by Bo on 2018-03-13.
 * ====================================
 */

public interface ActivityDelegate {

    String ACTIVITY_DELEGATE_CACHE_KEY = "activity_delegate";

    void onCreate(@Nullable Bundle savedInstanceSate);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
