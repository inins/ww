package com.frame.integration.lifecycle;

import android.support.annotation.NonNull;

import io.reactivex.subjects.Subject;

/**
 * ======================================
 * 让{@link android.app.Activity} / {@link android.support.v4.app.Fragment} 实现此接口，即可正常使用{@link com.trello.rxlifecycle2.RxLifecycle}
 * <p>
 * Create by ChenJing on 2018-03-12 17:12
 * ======================================
 */

public interface Lifecycleable<E> {

    @NonNull
    Subject<E> provideLifecycleSubject();
}
