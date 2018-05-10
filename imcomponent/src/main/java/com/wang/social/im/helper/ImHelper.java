package com.wang.social.im.helper;

import android.text.TextUtils;

import com.frame.component.common.AppConstant;
import com.frame.utils.FileUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-19 11:52
 * ============================================
 */
public class ImHelper {

    /**
     * 创建一个临时文件
     *
     * @param parent
     * @return
     */
    public static File getTempFile(String parent) {
        File cacheFile = new File(FrameUtils.obtainAppComponentFromContext(Utils.getContext()).cacheFile(), parent);
        FileUtils.createOrExistsDir(cacheFile);
        try {
            File tmpFile = File.createTempFile(parent, "", cacheFile);
            tmpFile.deleteOnExit();
            return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片缓存地址
     *
     * @param fileName
     * @return
     */
    public static String getImageCachePath(String fileName) {
        File cacheFile = new File(FrameUtils.obtainAppComponentFromContext(Utils.getContext()).cacheFile(), AppConstant.Constant.IMAGE_CACHE_DIR);
        FileUtils.createOrExistsDir(cacheFile);
        if (TextUtils.isEmpty(fileName)) {
            return cacheFile.getPath();
        } else {
            return cacheFile.getPath() + "/" + fileName;
        }
    }

    /**
     * 判断缓存文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isCacheFileExit(String fileName) {
        File file = new File(getImageCachePath(fileName));
        return file.exists();
    }

    /**
     * 获取年龄段
     * @param birthdayMills
     * @return
     */
    public static String getAgeRange(long birthdayMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthdayMills);
        int year = calendar.get(Calendar.YEAR);
        if (year > 2000) {
            return "00后";
        } else if (year >= 1995 && year < 2000) {
            return "95后";
        } else if (year >= 1990 && year < 1995) {
            return "90后";
        } else {
            return "其他";
        }
    }
}
