package com.wang.social.topic.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class SearchResults {
    private int total;
    private int size;
    private int pages;
    private int current;
    List<SearchResult> list;
}
