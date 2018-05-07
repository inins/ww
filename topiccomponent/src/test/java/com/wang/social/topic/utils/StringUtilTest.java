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

    @Test
    public void findBetween() {
        Assert.assertEquals(StringUtil.findBetween(
                "1234567890", "123", "9", true),
                "45678");
        Assert.assertEquals(StringUtil.findBetween(
                "12345678", "123", "90", false),
                "45678");
    }

    @Test
    public void findLocalAudio() {
        String content = "<img onclick=\"playAudio('http:/storage/emulated/0/Android/data/com.wang.social.topic/cache/Sound/1525695264483temp.voice');\" id=\"audioImg\" align=\"center\" \n" +
                "src=\"/storage/emulated/0/WW/AudioPics/1525695270605.jpg\" width=\"118\" height=\"41\" alt=\"录音\"><br>\n" +
                "<img onclick=\"playAudio('/storage/emulated/0/Android/data/com.wang.social.topic/cache/Sound/1525695288072temp.voice');\" id=\"audioImg\" align=\"center\" \n" +
                "src=\"/storage/emulated/0/WW/AudioPics/1525695290748.jpg\" width=\"108\" height=\"41\" alt=\"录音\">\n" +
                "<br>合肥好的好的合富辉煌奖学金超级富豪记得记得就减肥减肥那边超久而俱化吃减肥减肥姐姐\n" +
                "<img align=\"center\" src=\"/storage/emulated/0/下载/Photo/u=124709217,1692928771&amp;fm=27&amp;gp=0.jpg\" alt=\"图片\"><br>\n" +
                "<img align=\"center\" src=\"/storage/emulated/0/下载/Photo/u=2466726528,3311401849&amp;fm=27&amp;gp=0.jpg\" alt=\"图片\"><br><br>";

        Assert.assertEquals(StringUtil.findLocalAudio(content), "/storage/emulated/0/Android/data/com.wang.social.topic/cache/Sound/1525695288072temp.voice");
    }
}