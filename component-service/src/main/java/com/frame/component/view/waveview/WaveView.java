package com.frame.component.view.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * liaoinstan
 * 录音波浪View，使用贝塞尔曲线绘制波形，波浪的波长、波峰、颜色、波动速度等都可修改参数
 * waveList：波浪集合(即多少层，每层可以设置不同属性达到不同的视差效果)
 */
public class WaveView extends View {

    private List<Wave> waveList = new ArrayList<>();

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制波浪
        for (Wave wave : waveList) {
            wave.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //添加3层波浪，设置不同颜色波长位移速度等属性
        waveList.add(new Wave(this).setColor(Color.parseColor("#88ff9497")).setWaweCount(1f).setOffsetLv(0f).setSpeed(1).setBaselineLv(0.12f));
        waveList.add(new Wave(this).setColor(Color.parseColor("#888ee0fe")).setWaweCount(2f).setOffsetLv(0.5f).setSpeed(1).setBaselineLv(0.12f));
        waveList.add(new Wave(this).setColor(Color.parseColor("#8898a2ff")).setWaweCount(1.5f).setOffsetLv(0.3f).setSpeed(1).setBaselineLv(0.12f));
        //开始波动
        for (Wave wave : waveList) {
            wave.startWave();
        }
    }

    //不可见时暂停动画，可见时恢复
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == VISIBLE) {
            resumeWave();
        } else {
            pauseWave();
        }
    }

    //销毁时结束并释放动画
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (waveList != null) {
            for (Wave wave : waveList) {
                wave.cancelWave();
            }
        }
    }

    /**
     * 设置额外的波形高度，可以为负
     */
    public void setExheight(int exheight) {
        if (waveList != null) {
            for (Wave wave : waveList) {
                wave.setExheight(exheight);
            }
        }
    }

    /**
     * 暂停波动动画
     */
    public void pauseWave() {
        if (waveList != null) {
            for (Wave wave : waveList) {
                wave.pauseWave();
            }
        }
    }

    /**
     * 继续动画
     */
    public void resumeWave() {
        if (waveList != null) {
            for (Wave wave : waveList) {
                wave.resumeWave();
            }
        }
    }
}
