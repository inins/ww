package com.frame.component.view.barview;

import java.io.Serializable;

import lombok.Data;

@Data
public class BarUser implements Serializable {
    private String imgUrl;

    public BarUser(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
