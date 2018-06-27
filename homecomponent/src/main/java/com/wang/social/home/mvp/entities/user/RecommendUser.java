package com.wang.social.home.mvp.entities.user;

import com.frame.component.entities.Tag;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RecommendUser implements Serializable {

    /**
     * birthday : 1482321894
     * constellation : 射手座
     * city : 成都市
     * nickname : 小明
     * photos : http://static.wangsocial.com/image/group/%E7%A4%BE%E4%BC%9A.jpg
     * province : 四川省
     * sex : 0
     * userId : 22
     * userTags : [{"id":103,"tagId":4,"tagName":"电视剧","isIndexShow":0},{"id":104,"tagId":5,"tagName":"综艺","isIndexShow":0},{"id":105,"tagId":8,"tagName":"音乐","isIndexShow":0}]
     */

    private long birthday;
    private String constellation;
    private String city;
    private String nickname;
    private String avatar;
    private String province;
    private int sex;        //0 男 1女 -1 未知
    private int userId;
    private List<Tag> userTags;

    public String getTagText() {
        return Tag.getTagText(userTags);
    }

    //是否是男性
    public boolean isMale() {
        return sex == 0;
    }
}
