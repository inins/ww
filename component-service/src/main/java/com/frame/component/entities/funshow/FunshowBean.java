package com.frame.component.entities.funshow;

import android.text.TextUtils;

import java.io.Serializable;

import lombok.Data;

@Data
public class FunshowBean implements Serializable {
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
    //是否匿名
    private boolean isHideName;
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

    //是否置顶
    private boolean isTop;
    //是否官方号
    private boolean isOfficial;

    //视频链接：非必需//如果是视频趣晒，并且又没有封面，则会去解析视频第一帧图像作为封面
    //现在这个字段不再使用，解析在线视频太耗费性能，统一加载封面图
    private String videoUrl;


    //是否拥有查看权限
    //收费且没有付费 没有权限，其他情况均可查看
    public boolean hasAuth() {
        return isFree || isPay;
    }

    public String getNickname() {
        return !isHideName() ? nickname : "匿名用户";
    }

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
