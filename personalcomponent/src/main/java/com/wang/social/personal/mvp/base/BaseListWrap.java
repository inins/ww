package com.wang.social.personal.mvp.base;

import java.util.List;

import lombok.Data;

@Data
public class BaseListWrap<T> {
    private List<T> list;
}
