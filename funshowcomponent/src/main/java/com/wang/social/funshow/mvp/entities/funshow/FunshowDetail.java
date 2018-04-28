package com.wang.social.funshow.mvp.entities.funshow;

import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowDetail {

    /**
     * birthday : 725904000000
     * city :
     * isSupport : 1
     * musicUrl : null
     * sex : 1
     * userNickname : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * talkContent : 这是一个趣晒，很无趣
     * userId : 10000
     * shareNum : null
     * commentNum : 8
     * isAnonymous : 0
     * isFree : 1
     * constellation : 射手座
     * province :
     * videoUrl : null
     * createTime : 1521787669000
     * price : null
     * isShopping : 1
     * pics : [{"picOrder":1,"picType":null,"url":"http://resouce.dongdongwedding.com/wangwang_2017-06-16_6d7da9a7-e8dd-4552-b8eb-5c49e4df8665.jpg"},{"picOrder":2,"picType":null,"url":"http://resouce.dongdongwedding.com/wangwang_2017-06-16_d6a7f812-ad3a-46e1-96a9-5b92e3c50b2c.jpg"}]
     * age : null
     * supportNum : 2
     */

    private long birthday;
    private String city;
    private int isSupport;
    private int sex;
    private String userNickname;
    private String avatar;
    private String talkContent;
    private int userId;
    private int shareNum;
    private int commentNum;
    private String isAnonymous;
    private int isFree;
    private String constellation;
    private String province;
    private long createTime;
    private float price;
    private int isShopping;
    private int age;
    private int supportNum;
    private List<Pic> pics;
    private FunshowDetailVideoRsc resourceUrl;

    ////////////////////////////////////

    public FunshowDetailVideoRsc getMusicRsc() {
        if (resourceUrl != null && resourceUrl.isVoice()) {
            return resourceUrl;
        } else {
            return null;
        }
    }

    public FunshowDetailVideoRsc getVideoRsc() {
        if (resourceUrl != null && resourceUrl.isVidoe()) {
            return resourceUrl;
        } else {
            return null;
        }
    }

    public int getPicCount() {
        return pics != null ? pics.size() : 0;
    }

    public List<BundleImgEntity> getBundleImgEntities() {
        List<BundleImgEntity> list = new ArrayList<>();
        if (!StrUtil.isEmpty(pics)) {
            for (Pic pic : pics) {
                list.add(new BundleImgEntity(pic.getUrl()));
            }
        }
        return list;
    }

    //////////////////////////////

    public boolean isSupport() {
        return isSupport == 1;
    }

    public void setIsSupport(boolean isSupport) {
        this.isSupport = isSupport ? 1 : 0;
    }

    public boolean isShopping() {
        return isShopping == 1;
    }

    public void setIsShopping(boolean isShopping) {
        this.isShopping = isShopping ? 1 : 0;
    }
}
