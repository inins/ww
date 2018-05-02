package com.wang.social.im.mvp.model.entities;

import java.util.List;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 9:43
 * ============================================
 */
@Data
public class ListData<T> {

    private List<T> list;
}
