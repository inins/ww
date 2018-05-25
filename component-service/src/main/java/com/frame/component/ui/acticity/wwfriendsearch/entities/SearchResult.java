package com.frame.component.ui.acticity.wwfriendsearch.entities;

import java.util.List;

import lombok.Data;

@Data
public class SearchResult {
    List<SearchFriend> userFriend;
    List<SearchGroup> group;
}
