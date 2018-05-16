package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.DistributionAge;
import com.wang.social.im.mvp.model.entities.DistributionGroup;
import com.wang.social.im.mvp.model.entities.DistributionSex;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DistributionGroupDTO implements Mapper<DistributionGroup> {
    List<DistributionAge> age;
    List<DistributionSex> sex;

    @Override
    public DistributionGroup transform() {
        DistributionGroup object = new DistributionGroup();

        object.setAge(null == age ? new ArrayList<>() : age);
        object.setSex(null == sex ? new ArrayList<>() : sex);

        return object;
    }
}
