package com.frame.component.entities;

import lombok.Data;

@Data
public class PersonalPic {
    private String photoUrl;
    private int userPhotoId;
    private String state;
    private long createDate;
}
