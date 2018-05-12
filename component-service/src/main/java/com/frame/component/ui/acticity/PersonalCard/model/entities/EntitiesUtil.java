package com.frame.component.ui.acticity.PersonalCard.model.entities;

public class EntitiesUtil {
    public static int assertNotNull(Integer v) {
        return v == null ? 0 : v;
    }

    public static long assertNotNull(Long v) {
        return v == null ? 0L : v;
    }

    public static String assertNotNull(String v) {
        return v == null ? "" : v;
    }

    public static boolean assertNotNull(Boolean v) { return v == null ? false : v; }
}
