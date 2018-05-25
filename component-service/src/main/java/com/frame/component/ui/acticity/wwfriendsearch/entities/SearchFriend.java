package com.frame.component.ui.acticity.wwfriendsearch.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchFriend extends SearchBase {
    private String nickname;
    private String avatar;
    private int userId;
}
