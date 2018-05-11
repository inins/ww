package com.frame.component.view.bannerview;

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
    @SerializedName("PromotionName")
    private String title;
    private String content;
    @SerializedName("imgPath")
    private String img;
    @SerializedName("URL")
    private String url;

    public Image() {
    }

    public Image(int id, String img) {
        this.id = id;
        this.img = img;
    }
    public Image(String img) {
        this.img = img;
    }
    public Image(int id, String title, String img) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

}
