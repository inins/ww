package com.frame.component.entities.funshow;

import android.text.TextUtils;

import lombok.Data;

@Data
public class FunshowBean {
    //趣晒id
    private int id;
    //用户相关：id，头像昵称创建时间
    private int userId;
    private String avatar;
    private String nickname;
    private long createTime;

    //内容相关
    private String content;
    //图片封面
    private String showPic;
    //图片总数
    private int picNum;
    //是否免费
    private boolean isFree;
    //是否已经支付
    private boolean isPay;
    //查看需付费价格（宝石）
    private int price;
    //是否是视频趣晒
    private boolean isVideo;

    //点赞数
    private int supportTotal;
    //评论数
    private int commentTotal;
    //分享数
    private int shareTotal;
    //是否已经点赞
    private boolean isSupport;
    //城市
    private String cityName;
    //省份
    private String provinceName;

    //视频链接：非必需//如果是视频趣晒，并且又没有封面，则会去解析视频第一帧图像作为封面
    private String videoUrl;



    //获取省+市
    public String getPositionText() {
        String positionText = "";
        if (!TextUtils.isEmpty(provinceName)) {
            positionText += provinceName;
        }
        if (!TextUtils.isEmpty(cityName)) {
            positionText += cityName;
        }
        return positionText;
    }
}
