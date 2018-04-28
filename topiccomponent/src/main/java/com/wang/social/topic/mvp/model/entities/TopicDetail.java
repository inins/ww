package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class TopicDetail {

    /**
     *
     参数名字	参数类型	说明
     title	String	话题标题
     content	String	正文
     createTime	Long	创建时间
     templateId	int	模板ID
     relateState	int	收费状态 0不收费 1收费
     gemstone	int	收费宝石
     backgroundImage	String	话题背景图片
     backgroundMusicId	String	话题背景音乐ID
     backgroundMusicName	String	话题背景音乐名
     backgroundMusicUrl	String	话题背景音乐URL
     creatorId	int	创建者ID
     birthday	Long	出生日期
     avatar	String	头像
     nickname	String	昵称
     shareTotal	int	分享统计
     commentTotal	int	回复统计
     supportTotal	int	占赞统计
     tags	数组	标签数组
     */

    private String title;
    private List<String> tags;
    private Long createTime;
    private String avatar;
    private String nickname;
    private Integer sex;
    private String backgroundImage;
    private String backgroundMusicId;
    private String backgroundMusicName;
    private String backgroundMusicUrl;
    private Long birthday;
    private Integer supportTotal;
    private Integer isSupport;
    private Integer creatorId;
    private Integer commentTotal;
    private Integer readTotal;
    private Integer templateId;
    private String content;
    private Integer relateState;
    private Integer gemstone;
    private Integer topicId;
    private Integer shareTotal;
}
