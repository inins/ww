package com.wang.social.personal.mvp.entities.recommend;

import com.frame.component.entities.BaseSelectBean;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RecommendUser extends BaseSelectBean implements Serializable {
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
}
