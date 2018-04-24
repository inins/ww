package com.frame.component.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.WindowInsets;

/**
 * ============================================
 * 解决设置状态栏透明后键盘弹起adjustResize无效问题
 * <p>
 * Create by ChenJing on 2018-04-23 19:08
 * ============================================
 */
public class FullScreenConstraintLayout extends ConstraintLayout {

    private int[] mInsets = new int[4];

    public FullScreenConstraintLayout(Context context) {
        super(context);
    }

    public FullScreenConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    /**
     * Api 20 及以上
     * @param insets
     * @return
     */
    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        } else {
            return super.onApplyWindowInsets(insets);
        }
    }

    /**
     * Api 20以下
     * @param insets
     * @return
     */
    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.left;
            mInsets[1] = insets.top;
            mInsets[2] = insets.right;
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }
}
