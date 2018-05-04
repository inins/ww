package com.wang.social.topic.utils;

import java.io.File;

public class FileUtil {
    public static long getFileSize(String path) {
        try {
            File file = new File(path);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
