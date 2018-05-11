package com.frame.component.entities.photo;

import java.io.Serializable;

import lombok.Data;

@Data
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
}
