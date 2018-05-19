package com.frame.component.entities;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseCardListWrap<T> extends BaseListWrap<T> {
    private String orderByField;
    private String strategy;
    private String asc;
}
