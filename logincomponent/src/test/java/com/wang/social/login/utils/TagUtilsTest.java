package com.wang.social.login.utils;

import com.frame.component.utils.StringUtils;

import junit.framework.Assert;

import org.junit.Test;

public class TagUtilsTest {

    @Test
    public void isMobileNO() {
        Assert.assertTrue(StringUtils.isMobileNO("13981920420"));
        Assert.assertTrue(StringUtils.isMobileNO("13823150420"));
        Assert.assertTrue(StringUtils.isMobileNO("15855686364"));


        Assert.assertFalse(StringUtils.isMobileNO("139819204201"));
        Assert.assertFalse(StringUtils.isMobileNO("1398192042"));
    }

    @Test
    public void isPassword() {
        Assert.assertTrue(StringUtils.isPassword("123456789"));
        Assert.assertTrue(StringUtils.isPassword("aaaaaa"));
        Assert.assertTrue(StringUtils.isPassword("123asd"));
        Assert.assertTrue(StringUtils.isPassword("~`!@#$%^&*()_-+="));
        Assert.assertTrue(StringUtils.isPassword("<,>.?/;:'\"\\|"));
        Assert.assertTrue(StringUtils.isPassword("{}[]123456"));
        Assert.assertTrue(StringUtils.isPassword("~·！@#￥%……&*（）-——=+"));
        Assert.assertTrue(StringUtils.isPassword("~~！@#￥%……&*（）——+；，、"));
    }

    @Test
    public void isVerifyCode() {
        Assert.assertTrue(StringUtils.isVerifyCode("1234"));
        Assert.assertTrue(StringUtils.isVerifyCode("8647"));

        Assert.assertFalse(StringUtils.isVerifyCode("1"));
        Assert.assertFalse(StringUtils.isVerifyCode("16"));
        Assert.assertFalse(StringUtils.isVerifyCode("168"));
        Assert.assertFalse(StringUtils.isVerifyCode("12345"));
        Assert.assertFalse(StringUtils.isVerifyCode("1234568"));
        Assert.assertFalse(StringUtils.isVerifyCode("12qd"));
        Assert.assertFalse(StringUtils.isVerifyCode("adfe"));
        Assert.assertFalse(StringUtils.isVerifyCode("12_1"));
        Assert.assertFalse(StringUtils.isVerifyCode("12+_"));
    }
}