package com.wang.social.topic.utils;

import android.text.Html;
import android.webkit.URLUtil;

import com.qiniu.android.utils.StringUtils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static org.junit.Assert.*;

public class FileUtilTest {


    @Test
    public void testFile() {
        String path = "/storage/emulated/0/下载/Photo/u=908483592,3964474959&amp;fm=11&amp;gp=0.jpg";
        File file = null;
        try {
            String dp = URLDecoder.decode(path, "utf-8");
            String ep = URLEncoder.encode(path, "utf-8");
            System.out.println(dp);
            System.out.println(ep);
            file = new File(dp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(file.exists());
    }
}