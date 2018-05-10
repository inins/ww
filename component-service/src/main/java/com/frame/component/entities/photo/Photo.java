package com.frame.component.entities.photo;

import java.io.Serializable;

public class Photo implements Serializable{

    /**
     * photoUrl : fdsafdsaf
     * userPhotoId : 1
     * state : 1
     * createDate : 1521702369000
     */

    private String photoUrl;
    private int userPhotoId;
    private String state;
    private long createDate;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(int userPhotoId) {
        this.userPhotoId = userPhotoId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
