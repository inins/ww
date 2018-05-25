package com.frame.component.ui.acticity.wwfriendsearch.entities.dto;

import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchFriend;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchGroup;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchResult;
import com.frame.http.api.Mapper;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDTO implements Mapper<SearchResult> {
    List<SearchFriend> userFriend;
    List<SearchGroup> group;

    @Override
    public SearchResult transform() {
        SearchResult object = new SearchResult();

        object.setUserFriend(null == userFriend ? new ArrayList<>() : userFriend);
        object.setGroup(null == group ? new ArrayList<>() : group);

        return object;
    }
}
