package com.frame.integration.lifecycle;

import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * ======================================
 * 让{@link android.support.v4.app.Fragment} 实现此接口，即可正常使用{@link com.trello.rxlifecycle2.RxLifecycle}
 * <p>
 * Create by ChenJing on 2018-03-12 17:15
 * ======================================
 */

public interface FragmentLifecycleable extends Lifecycleable<FragmentEvent>{
}
