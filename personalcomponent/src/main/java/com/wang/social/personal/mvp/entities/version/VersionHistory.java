package com.wang.social.personal.mvp.entities.version;

import lombok.Data;

@Data
public class VersionHistory {
    private String versionName;
    private String versionText;

    public VersionHistory(String versionName, String versionText) {
        this.versionName = versionName;
        this.versionText = versionText;
    }
}
