package com.wang.social.im.view.plugin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Bo on 2018-03-29.
 */

public class PluginModule {

    public enum PluginType {
        IMAGE, //图片
        SHOOT, //拍摄
        LOCATION,//位置
        REDPACKET,//红包
        GAME,//游戏
        SHADOW //分身
    }

    @Getter
    @Setter
    private int drawableResId;
    @Getter
    @Setter
    private int pluginName;
    @Getter
    private PluginType pluginType;

    public PluginModule(int drawableResId, int pluginName, PluginType pluginType) {
        this.drawableResId = drawableResId;
        this.pluginName = pluginName;
        this.pluginType = pluginType;
    }
}
