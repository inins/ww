package com.frame.component.entities;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-07 11:00
 * ============================================
 */
@Data
public class VersionInfo {

    private String apkUrl;
    private int versionCode;
    private String versionName;
    private int minVersionCode;
    private String date;
    private String updateLog;
}
