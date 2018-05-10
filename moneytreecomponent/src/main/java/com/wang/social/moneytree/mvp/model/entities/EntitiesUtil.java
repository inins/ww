package com.wang.social.moneytree.mvp.model.entities;

public class EntitiesUtil {
    public static int assertNotNull(Integer v) {
        return v == null ? 0 : v;
    }

    public static String assertNotNull(String v) {
        return v == null ? "" : v;
    }
}
