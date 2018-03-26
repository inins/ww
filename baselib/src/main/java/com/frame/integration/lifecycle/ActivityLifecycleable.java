package com.frame.integration.lifecycle;

import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * ======================================
 * 让{@link android.app.Activity} 实现此接口，即可正常使用{@link com.trello.rxlifecycle2.RxLifecycle}
 * <p>
 * Create by ChenJing on 2018-03-12 17:11
 * ======================================
 */

public interface ActivityLifecycleable extends Lifecycleable<ActivityEvent>{
}
