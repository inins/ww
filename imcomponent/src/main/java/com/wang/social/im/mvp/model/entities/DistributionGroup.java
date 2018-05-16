package com.wang.social.im.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class DistributionGroup {
    List<DistributionAge> age;
    List<DistributionSex> sex;
}
