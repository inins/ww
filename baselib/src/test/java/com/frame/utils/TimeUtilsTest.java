package com.frame.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeUtilsTest {

    @Test
    public void getAgeByBirth() {
        long b20100601 = 1275321600000L;
        long b20100602 = 1275408000000L;
        Assert.assertTrue(TimeUtils.getAgeByBirth(b20100601) == 7);
        Assert.assertTrue(TimeUtils.getAgeByBirth(b20100602) == 8);
    }
}