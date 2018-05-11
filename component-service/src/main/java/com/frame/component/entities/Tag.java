package com.frame.component.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tag implements Serializable{

    /**
     * id : 4
     * tagId : 5
     * tagName : 综艺
     * isIndexShow : 0
     */

    private int id;
    private int tagId;
    private String tagName;
    private int isIndexShow;

}
