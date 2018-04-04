package com.frame.component.entities;

import java.io.Serializable;

/**
 * Created by liaoinstan on 2018/4/3.
 * 用户实体
 */

public class User implements Serializable {

    /**
     * id : 1
     * userId : 10000
     * nickname : 平东
     * avatar : http://resouce.dongdongwedding.com/Fuqg4sWTwi-D-7oqmqIkPa3ARBzu
     * sex : 1
     * createTime : 1505232000000
     * phone : 15528801837
     * birthday : 1990-12-31
     * constellation : 射手座
     */

    private int id;
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long createTime;
    private String phone;
    private String birthday;
    private String province;
    private String city;
    private String constellation;
    private String autograph;

    /////////////////////////////////

    public String getSexText() {
        if (sex == 0) {
            return "男";
        } else if (sex == 1) {
            return "女";
        } else {
            return "";
        }
    }

    /////////////////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {

        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }
}
