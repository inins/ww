package com.wang.social.login202.mvp.model.entities;

import lombok.Data;

@Data
public class CheckVerifyCode {
    private String mobile;
    private boolean isOK;
    private String message;
}
