package com.wang.social.im.helper;

import com.frame.utils.FileUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-19 11:52
 * ============================================
 */
public class CommHelper {

    /**
     * 创建一个临时文件
     * @param parent
     * @return
     */
    public static File getTempFile(String parent){
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
}
