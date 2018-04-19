package com.frame.component.entities.config;

import java.io.Serializable;

import lombok.Data;

//消息设置
@Data
public class MsgConfig implements Serializable{
    //默认全部为开启
    private boolean enableMsg = true;
    private boolean showDetail = true;
    private boolean msgVoice = true;
    private boolean msgVibration = true;
}
