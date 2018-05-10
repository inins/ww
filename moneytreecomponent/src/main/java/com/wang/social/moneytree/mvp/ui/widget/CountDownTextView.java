package com.wang.social.moneytree.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CountDownTextView extends AppCompatTextView {

    public interface CountDownListener {
        void onCountDownFinished();
    }

    private Disposable mDisposable;
    private CountDownListener mCountDownListener;
    private long mDuration;
    private boolean mStarted = false;

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCountDownListener(CountDownListener countDownListener) {
        mCountDownListener = countDownListener;
    }

    public void start(int duration) {
        if (mStarted) return;
        mStarted = true;
        mDuration = duration;

        mDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mDuration = Math.max(0, --mDuration);

                        setText(countTimer((int)mDuration));

                        if (mDuration <= 0) {
                            if (null != mCountDownListener) {
                                mCountDownListener.onCountDownFinished();
                            }

                            mDisposable.dispose();
                        }
                    }
                });
    }

    private String countTimer(int countTime) {
        final int MINUTE = 60;
        final int HOUR = 60 * 60;
        int hh = (int)countTime / HOUR;
        int mm = (int)countTime % HOUR / MINUTE;
        int ss = countTime % MINUTE;

        return (hh > 0 ? String.format("%02d", hh) + ":" : "") +
                String.format("%02d", mm) + ":" +
                String.format("%02d", ss);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != mDisposable) {
            mDisposable.dispose();
        }
    }
}
