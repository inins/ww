package com.frame.component.entities;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 10:10
 * ============================================
 */
@NoArgsConstructor
public class FriendInfo {

    public static final int gender_male = 0x001;
    public static final int gender_female = 0x001;

    @Getter
    @Setter
    private String friendId;
    @Getter
    @Setter
    private String nickname;
    @Getter
    @Setter
    private String portrait;
    @Getter
    @Setter
    private Gender gender;
    @Getter
    @Setter
    private int age;
    @Getter
    @Setter
    private String constellation;
    @Getter
    @Setter
    private List<Tag> tags;
}
