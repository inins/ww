package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.wang.social.funshow.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/3/29.
 * 个人中心的页面顶部标题样式都一样，使用titleView进行统一管理，方便修改和拓展
 */

public class VideoBtnView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private CircleProgressView progressView;
    private View btn_go;

    public VideoBtnView(@NonNull Context context) {
        this(context, null);
    }

    public VideoBtnView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoBtnView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
//            final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, 0, 0);
//            title = ta.getString(R.styleable.TitleView_personal_textTitle);
//            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.funshow_videobtn_view, this, true);
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        progressView = findViewById(R.id.progress);
        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnLongClickListener(this);
        btn_go.setOnTouchListener(this);
        btn_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onVideoBtnListener != null) onVideoBtnListener.onClick(view);
    }

    @Override
    public boolean onLongClick(View v) {
        startProgress();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopProgress();
                break;
        }
        return false;
    }

    ////////////////////// 计时

    private long maxTime = 10000;   //最多录制10秒
    private Disposable disposable;

    private void startProgress() {
        if (disposable == null) {
            if (onVideoBtnListener != null) onVideoBtnListener.onLongPressStart();
            disposable = Observable.interval(10, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            float now = aLong * 10;
                            float progress = now / maxTime * progressView.getMax();
                            progressView.setProgress(progress);
                            if (now > maxTime) stopProgress();
                        }
                    });
        }
    }

    private void stopProgress() {
        if (disposable != null) {
            if (onVideoBtnListener != null) {
                long time = (long) (progressView.getProgress() / progressView.getMax() * maxTime);
                onVideoBtnListener.onLongPressEnd(time);
            }
            progressView.setProgress(0);
            disposable.dispose();
            disposable = null;
        }
    }

    ///////////////////////////

    private OnVideoBtnListener onVideoBtnListener;

    public void setOnVideoBtnListener(OnVideoBtnListener onVideoBtnListener) {
        this.onVideoBtnListener = onVideoBtnListener;
    }

    public interface OnVideoBtnListener {
        void onClick(View v);

        void onLongPressStart();

        void onLongPressEnd(long time);
    }
}
