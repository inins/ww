package com.wang.social.topic.utils;

import junit.framework.Assert;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void findImg() {
        String content = "<font size=\"6\">就到家才能吃</font><img src=\"http:slafa.img \">" +
                "<img align=\"center\" src=\"/storage/emulated/0/WW/Pics/WW20180504170814137.jpg\" alt=\"图片\"><br><br><font size=\"1\">付就放假</font>";
        String img = "/storage/emulated/0/WW/Pics/WW20180504170814137.jpg";

        Assert.assertEquals(StringUtil.findLocalImg(content), img);

        content = "<img align=\"center\" " +
                "src=\"/storage/emulated/0/WW/Pics/WW20180504170814137.jpg\" alt=\"图片" +
                "\"><br><br><font size=\"1\">付就放假</font><font size=\"6\">就到家才能吃</font>";
        Assert.assertEquals(StringUtil.findLocalImg(content), img);
        content = "<br><br><font size=\"1\">付就放假</font><font size=\"6\">就到家才能吃</font><img align=\"center\" " +
                "src=\"/storage/emulated/0/WW/Pics/WW20180504170814137.jpg\" alt=\"图片" +
                "\">";
        Assert.assertEquals(StringUtil.findLocalImg(content), img);
        content = "<br><br><font size=\"1\">付就放假</font><font size=\"6\">就到家才能吃</font><g align=\"center\" " +
                "src=\"/storage/emulated/0/WW/Pics/WW20180504170814137.jpg\" alt=\"图片" +
                "\">";
        Assert.assertFalse(StringUtil.findLocalImg(content).equals(img));
    }
}