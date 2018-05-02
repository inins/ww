package com.wang.social.topic;

import com.frame.utils.TimeUtils;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void getAgeFromBirthTime() {
    }

    @Test
    public void getBirthYears() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2004-4-20").getTime()).equals("00"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("1982-4-20").getTime()).equals("80"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("1992-4-20").getTime()).equals("90"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2010-4-20").getTime()).equals("10"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2024-4-20").getTime()).equals("20"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2039-4-20").getTime()).equals("30"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2044-4-20").getTime()).equals("40"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2057-4-20").getTime()).equals("50"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("2160-4-20").getTime()).equals("60"));
            Assert.assertTrue(
                    StringUtil.getBirthYears(
                            format.parse("1074-4-20").getTime()).equals("70"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}