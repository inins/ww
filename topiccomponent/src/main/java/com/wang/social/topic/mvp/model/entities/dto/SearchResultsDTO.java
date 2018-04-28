package com.wang.social.topic.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.topic.mvp.model.entities.SearchResult;
import com.wang.social.topic.mvp.model.entities.SearchResults;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsDTO implements Mapper<SearchResults> {
    private Integer total;
    private Integer size;
    private Integer pages;
    private Integer current;
    List<SearchResult> list;

    private int assertNotNull(Integer i) {
        return i == null ? 0 : i;
    }

    @Override
    public SearchResults transform() {
        SearchResults object = new SearchResults();

        object.setTotal(assertNotNull(total));
        object.setSize(assertNotNull(size));
        object.setPages(assertNotNull(pages));
        object.setCurrent(assertNotNull(current));
        object.setList(list == null ? new ArrayList<>() : list);

        return object;
    }
}
