package com.frame.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeUtilsTest {

    @Test
    public void getAgeByBirth() {
        /**
         * 将时间字符串转为时间戳
         * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
         *
         * @param time 时间字符串
         * @return 毫秒时间戳
         */
        Assert.assertTrue(TimeUtils.getAgeByBirth(
                TimeUtils.string2Millis("1990-01-01 00:00:00")) == 28);
        Assert.assertTrue(TimeUtils.getAgeByBirth(
                TimeUtils.string2Millis("1990-06-05 00:00:00")) == 27);
        Assert.assertTrue(TimeUtils.getAgeByBirth(
                TimeUtils.string2Millis("1990-06-06 00:00:00")) == 27);
        Assert.assertTrue(TimeUtils.getAgeByBirth(
                TimeUtils.string2Millis("1990-06-04 00:00:00")) == 28);
        Assert.assertTrue(TimeUtils.getAgeByBirth(
                TimeUtils.string2Millis("1990-06-03 00:00:00")) == 28);
    }
}