package com.frame.component.entities.dto;

import com.frame.component.entities.QiNiu;
import com.frame.http.api.Mapper;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-04 9:52
 * ======================================
 */
public class QiNiuDTO implements Mapper<QiNiu> {

    private String token;

    @Override
    public QiNiu transform() {
        QiNiu qiNiu = new QiNiu();
        qiNiu.setToken(token == null ? "" : token);
        return qiNiu;
    }
}
