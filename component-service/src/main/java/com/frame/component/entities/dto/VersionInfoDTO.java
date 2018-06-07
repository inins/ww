package com.frame.component.entities.dto;

import com.frame.component.entities.VersionInfo;
import com.frame.http.api.Mapper;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-07 11:00
 * ============================================
 */
public class VersionInfoDTO implements Mapper<VersionInfo> {

    private String packUrl;
    private int versionCode;
    private String versionName;
    private int minSupportVersion;
    private String pubDate;
    private String remarks;

    @Override
    public VersionInfo transform() {
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setApkUrl(packUrl == null ? "" : packUrl);
        versionInfo.setVersionCode(versionCode);
        versionInfo.setVersionName(versionName == null ? "" : versionName);
        versionInfo.setMinVersionCode(minSupportVersion);
        versionInfo.setDate(pubDate == null ? "" : pubDate);
        versionInfo.setUpdateLog(remarks == null ? "" : remarks);
        return versionInfo;
    }
}
