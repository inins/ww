package com.wang.social.funshow.mvp.entities.user;

import com.frame.component.entities.BaseSelectBean;
import com.frame.component.entities.Tag;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Friend extends BaseSelectBean implements Serializable {

    /**
     * friendId : 10000
     * nickName : 飘飘飘香
     * avatar : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * age : null
     * sex : 1
     * constellation : 射手座
     * birthday : 725904000000
     */

    private int friendId;
    @SerializedName("nickname")
    private String nickName;
    private String avatar;
    private int age;
    private int sex;
    private String constellation;
    private long birthday;
    private List<Tag> tagList;

}
