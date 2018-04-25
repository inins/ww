package com.wang.social.funshow.utils;

import com.frame.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FunShowUtil {

    public static String getFunshowTimeStr(long time) {
        return TimeUtils.date2String(new Date(time), new SimpleDateFormat("MM-dd HH:mm"));
    }
}
