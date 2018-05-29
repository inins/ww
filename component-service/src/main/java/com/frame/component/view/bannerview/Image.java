package com.frame.component.view.bannerview;

import com.frame.utils.RegexUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by liaoinstan on 15/9/2.
 * BannerView需要的一个实体类，可以考虑放在BannerView作为一个内部类，不要让它参与网络数据的转换，否则你的混淆配置又要加入这些实体类了
 */
@Data
public class Image implements Serializable {

    private int id;
    private String img;
    private String url;

    public Image() {
    }

    public Image(String img) {
        this.img = img;
    }

    //img 可能是网络图片也可能是本地图片，也可能是工程res资源图片，如果是资源图片，则返回资源id，否则返回0
    public int getLocalSrc() {
        return isLocalSrc() ? Integer.parseInt(img) : 0;
    }

    public boolean isLocalSrc() {
        return RegexUtils.isInteger(img);
    }
}
