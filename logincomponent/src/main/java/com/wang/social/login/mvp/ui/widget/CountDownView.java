package com.wang.social.login.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wang.social.login.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CountDownView extends AppCompatTextView {

    // 倒计时时间
    final int count = 60;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    CharSequence charSequence;
    boolean hasBackground = true;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 透明度
        setAlpha(0.8f);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != compositeDisposable) {
            compositeDisposable.dispose();
        }
    }

    public void setHasBackground(boolean hasBackground) {
        this.hasBackground = hasBackground;
    }

    private void setTextBackground(int resId) {
        if (!hasBackground) return;

        setBackground(getContext()
                .getResources()
                .getDrawable(resId));
    }

    public void start() {
        // 不可点击
        setEnabled(false);
        charSequence = getText();
        Observable.intervalRange(0, count + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        setTextBackground(R.drawable.login_shape_rect_corner_stroke_gray);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        CountDownView.this.setText(Long.toString(count - aLong) + "s" +
                                getResources().getString(R.string.login_send_again));
                    }

                    @Override
                    public void onError(Throwable e) {
                        setEnabled(true);
                        CountDownView.this.setText(charSequence);
                        setTextBackground(R.drawable.tags_shape_rect_corner_stroke_blue_deep);
                    }

                    @Override
                    public void onComplete() {
                        setEnabled(true);
                        CountDownView.this.setText(charSequence);

                        setTextBackground(R.drawable.tags_shape_rect_corner_stroke_blue_deep);
                    }
                });
    }
}
