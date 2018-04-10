package com.wang.social.socialize;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.IntDef;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public class SocializeUtil {
    /**
     * Wx app id
     */
    public static final String WX_APP_ID = "wxb6c49c8240e7d9f2";

    public static final int LOGIN_PLATFORM_UNKNOWN = -1;
    public static final int LOGIN_PLATFORM_WEIXIN = 2;
    public static final int LOGIN_PLATFORM_QQ = 3;
    public static final int LOGIN_PLATFORM_SINA_WEIBO = 4;

    @IntDef({LOGIN_PLATFORM_UNKNOWN, LOGIN_PLATFORM_WEIXIN, LOGIN_PLATFORM_QQ, LOGIN_PLATFORM_SINA_WEIBO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginPlatform {
    }

    public interface ThirdPartyLoginListener {

        void onStart(@LoginPlatform int platform);

        void onComplete(@LoginPlatform int platform, Map<String, String> data);

        /**
         * 登录成功返回数据
         * @param platform 用户绑定数据类型，2.微信注册 3.qq注册 4新浪微博
         * @param uid 对应平台ID
         * @param nickname 昵称
         * @param sex 性别 0 男 1女
         * @param headUrl 头像
         */
        void onComplete(@LoginPlatform int platform, String uid, String nickname, int sex, String headUrl);

        void onError(@LoginPlatform int platform, Throwable t);

        void onCancel(@LoginPlatform int platform);
    }


    public static void init(Application application) {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 AppKey
         * 参数3:友盟 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(application,
                "591bac697f2c7466b000010c",
                "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE,
                "");

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);

        // 平台配置
        // 微信 appid appsecret
        PlatformConfig.setWeixin(WX_APP_ID, "0f6f312e95cf369e209bf9a584c915f3");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1106099147", "GImOYfGQ1twdofyv");
        // 新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("2310796351", "d19680a11876ebdecceb1ac21a903076", "http://weibo.com");
    }

    private static @LoginPlatform
    int getLoginType(SHARE_MEDIA platform) {
        switch (platform) {
            case WEIXIN:
                return LOGIN_PLATFORM_WEIXIN;
            case QQ:
                return LOGIN_PLATFORM_QQ;
            case SINA:
                return LOGIN_PLATFORM_SINA_WEIBO;
        }

        return LOGIN_PLATFORM_UNKNOWN;
    }

    private static ThirdPartyLoginListener thirdPartyLoginListener;
    private static UMAuthListener umAuthListener =
            new UMAuthListener() {
                /**
                 * @desc 授权开始的回调
                 * @param platform 平台名称
                 */
                @Override
                public void onStart(SHARE_MEDIA platform) {
                    if (null != thirdPartyLoginListener) {
                        thirdPartyLoginListener.onStart(getLoginType(platform));
                    }
                }

                /**
                 * @desc 授权成功的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 * @param data 用户资料返回
                 */
                @Override
                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    if (null != thirdPartyLoginListener) {
                        thirdPartyLoginListener.onComplete(getLoginType(platform), data);
                        thirdPartyLoginListener.onComplete(getLoginType(platform),
                                data.get("uid"),
                                data.get("name"),
                                data.get("gender").equals("0") ? 1 : 0,
                                data.get("iconurl"));
                    }
                }

                /**
                 * @desc 授权失败的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 * @param t 错误原因
                 */
                @Override
                public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                    if (null != thirdPartyLoginListener) {
                        thirdPartyLoginListener.onError(getLoginType(platform), t);
                    }
                }

                /**
                 * @desc 授权取消的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 */
                @Override
                public void onCancel(SHARE_MEDIA platform, int action) {
                    if (null != thirdPartyLoginListener) {
                        thirdPartyLoginListener.onCancel(getLoginType(platform));
                    }
                }
            };

    private static SHARE_MEDIA getUMShareMedia(@LoginPlatform int platform) {
        switch (platform) {
            case SocializeUtil.LOGIN_PLATFORM_WEIXIN:
                return SHARE_MEDIA.WEIXIN;
            case SocializeUtil.LOGIN_PLATFORM_QQ:
                return SHARE_MEDIA.QQ;
            case SocializeUtil.LOGIN_PLATFORM_SINA_WEIBO:
                return SHARE_MEDIA.SINA;
            default:
                return SHARE_MEDIA.MORE;
        }
    }

    private static void thirdPartyLogin(@LoginPlatform int platform, Activity activity, ThirdPartyLoginListener listener) {
        thirdPartyLoginListener = listener;
        UMShareAPI.get(activity).getPlatformInfo(activity,
                getUMShareMedia(platform), umAuthListener);
    }

    /**
     * 微信登录
     * @param activity
     * @param listener
     */
    public static void wxLogin(Activity activity, ThirdPartyLoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_WEIXIN, activity, listener);
    }

    /**
     * QQ登录
     * @param activity
     * @param listener
     */
    public static void qqLogin(Activity activity, ThirdPartyLoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_QQ, activity, listener);
    }

    /**
     * 新浪微博登录
     * @param activity
     * @param listener
     */
    public static void sinaLogin(Activity activity, ThirdPartyLoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_SINA_WEIBO, activity, listener);
    }



}
