package com.wang.social.personal.mvp.entities.recommend;

import com.frame.component.entities.BaseSelectBean;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RecommendGroup extends BaseSelectBean implements Serializable {
    private int id;
    private String groupName;
    private String headUrl;
    private String label;   //学生，老师
}
