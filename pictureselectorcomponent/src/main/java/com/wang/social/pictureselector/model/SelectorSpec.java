package com.wang.social.pictureselector.model;

import com.wang.social.pictureselector.engine.ImageEngine;
import com.wang.social.pictureselector.engine.impl.GlideEngine;

/**
 * Created by King on 2018/3/30.
 */

public class SelectorSpec {

    // 最多选择多少张图片
    int maxSelection = 1;
    // 预览时显示多少列
    int spanCount = 3;
    // 单张图片时是否跳转到裁切
    boolean clip = true;
    // 图片显示引擎
    ImageEngine imageEngine = new GlideEngine();

    private SelectorSpec() {
    }

    private static class SingletonHolder {
        public final static SelectorSpec instance = new SelectorSpec();
    }

    public static SelectorSpec getInstance() {
        return SingletonHolder.instance;
    }

    public static SelectorSpec getCleanInstance() {
        SelectorSpec instance = getInstance();
        instance.reset();
        return instance;
    }

    /**
     * 重置默认参数
     */
    private void reset() {
        maxSelection = 1;
        spanCount = 3;
        clip = true;
        imageEngine = new GlideEngine();
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public boolean isClip() {
        return clip;
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public ImageEngine getImageEngine() {
        return imageEngine;
    }

    public void setImageEngine(ImageEngine imageEngine) {
        this.imageEngine = imageEngine;
    }
}
