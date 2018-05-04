package com.frame.component.entities.dto;

import com.frame.component.entities.DiamondNum;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class DiamondNumDTO implements Mapper<DiamondNum> {
    Integer diamondNum;

    @Override
    public DiamondNum transform() {
        DiamondNum object = new DiamondNum();

        object.setDiamondNum(null == diamondNum ? 0 : diamondNum);

        return object;
    }
}
