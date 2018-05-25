package com.frame.component.ui.acticity.wwfriendsearch.entities;

import lombok.Data;

@Data
public class SearchFriend extends SearchBase {
    private String nickname;
    private String avatar;
    private int userId;
}
