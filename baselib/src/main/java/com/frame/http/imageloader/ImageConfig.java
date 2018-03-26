package com.frame.http.imageloader;

import android.widget.ImageView;

/**
 * ========================================
 * 这是图片加载配置定义的基类，定义所有图片加载框架都可以用的通用参数
 * <p>
 * Create by ChenJing on 2018-03-16 13:28
 * ========================================
 */

public class ImageConfig {

    protected String url;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
