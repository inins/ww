package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comments {
    private int offset;
    private long limit;
    private int total;
    private int size;
    private int pages;
    private int current;
    private boolean searchCount;
    private boolean openSort;
    private String orderByField;
    private List<Comment> records;
    private String condition;
    private boolean asc;
    private int offsetCurrent;
}
