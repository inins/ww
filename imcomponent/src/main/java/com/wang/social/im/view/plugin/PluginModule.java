package com.wang.social.im.view.plugin;

/**
 * Created by Bo on 2018-03-29.
 */

public class PluginModule {

    public enum PluginType{
        IMAGE, //图片
        SHOOT, //拍摄
        LOCATION,//位置
        REDPACKET,//红包
        GAME,//游戏
        SHADOW //分身
    }

    private int drawableResId;
    private int pluginName;
    private PluginType pluginType;

    public PluginModule(int drawableResId, int pluginName, PluginType pluginType) {
        this.drawableResId = drawableResId;
        this.pluginName = pluginName;
        this.pluginType = pluginType;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public int getPluginName() {
        return pluginName;
    }

    public PluginType getPluginType() {
        return pluginType;
    }
}
