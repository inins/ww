package com.frame.component.entities.config;

import java.io.Serializable;

import lombok.Data;

//消息设置
@Data
public class MsgConfig implements Serializable{
    private boolean enableMsg;
    private boolean showDetail;
    private boolean msgVoice;
    private boolean msgVibration;
}
