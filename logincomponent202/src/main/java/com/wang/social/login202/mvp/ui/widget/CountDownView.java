package com.wang.social.login202.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.frame.utils.RxLifecycleUtils;
import com.wang.social.login202.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class CountDownView extends AppCompatTextView {

    // 倒计时时间
    final int count = 60;
    CharSequence charSequence;
    private int mTextColor;
    boolean hasBackground = true;
    private Disposable mDisposable;

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

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//
//        Timber.i("onAttachedToWindow : " + getId());
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        Timber.i("onDetachedFromWindow : " + getId());
//        super.onDetachedFromWindow();
//        if (null != mDisposable) {
//            mDisposable.dispose();
//        }
//    }

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
        mTextColor = getCurrentTextColor();
        Observable.intervalRange(0, count + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        setTextBackground(R.drawable.login202_shape_rect_corner_stroke_gray);
                        setTextColor(getContext().getResources().getColor(R.color.common_text_dark));
                    }

                    @Override
                    public void onNext(Long aLong) {
//                        Timber.i("onNext : " + Long.toString(aLong));
                        CountDownView.this.setText(Long.toString(count - aLong) + "s" +
                                getResources().getString(R.string.login202_send_again));
                    }

                    @Override
                    public void onError(Throwable e) {
                        enableView();
                    }

                    @Override
                    public void onComplete() {
                        enableView();
                    }
                });

//        mDisposable = Flowable.intervalRange(0, count + 1, 0, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
////                        Timber.i("onNext : " + getId() + " " + Long.toString(aLong));
//                        CountDownView.this.setText(Long.toString(count - aLong) + "s" +
//                                getResources().getString(R.string.login202_send_again));
//                    }
//                })
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        enableView();
//                    }
//                })
//                .subscribe();
    }

    private void enableView() {
        setEnabled(true);
        CountDownView.this.setText(charSequence);
        CountDownView.this.setTextColor(mTextColor);

        setTextBackground(R.drawable.tags_shape_rect_corner_stroke_blue_deep);
    }
}
