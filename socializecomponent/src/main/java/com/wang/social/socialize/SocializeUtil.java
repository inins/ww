package com.wang.social.socialize;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.v4.app.FragmentManager;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wang.social.socialize.widget.DialogShare;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class SocializeUtil {
    public final static String TAG = SocializeUtil.class.getSimpleName().toString();
    /**
     * Wx app id
     */
    public static final String WX_APP_ID = "wxb6c49c8240e7d9f2";

    /**
     * 第三方登录平台
     */
    public static final int LOGIN_PLATFORM_UNKNOWN = -1;
    public static final int LOGIN_PLATFORM_WEIXIN = 2;      // 微信
    public static final int LOGIN_PLATFORM_QQ = 3;          // QQ
    public static final int LOGIN_PLATFORM_SINA_WEIBO = 4;  // 新浪微博

    @IntDef({LOGIN_PLATFORM_UNKNOWN, LOGIN_PLATFORM_WEIXIN, LOGIN_PLATFORM_QQ, LOGIN_PLATFORM_SINA_WEIBO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginPlatform {
    }

    /**
     * 第三方分享平台
     */
    public final static int SHARE_PLATFORM_UNKNOWN = -1;
    public final static int SHARE_PLATFORM_WX = 0;          // 微信好友
    public final static int SHARE_PLATFORM_WX_CIRCLE = 1;   // 微信朋友圈
    public final static int SHARE_PLATFORM_QQ = 2;          // QQ好友
    public final static int SHARE_PLATFORM_QQ_ZONE = 3;     // QQ空间
    public final static int SHARE_PLATFORM_SINA_WEIBO = 4;  // 新浪微博
    public final static int SHARE_PLATFORM_WW_FRIEND = 5;   // 往往好友

    @IntDef({
            SHARE_PLATFORM_UNKNOWN,
            SHARE_PLATFORM_WX,
            SHARE_PLATFORM_WX_CIRCLE,
            SHARE_PLATFORM_QQ,
            SHARE_PLATFORM_QQ_ZONE,
            SHARE_PLATFORM_SINA_WEIBO
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SharePlatform {
    }

    public interface WWShareListener {
        void onWWShare(String url, String title, String content, String imageUrl);
    }

    public interface LoginListener {
        /**
         * 开始授权登录
         *
         * @param platform
         */
        void onStart(@LoginPlatform int platform);

        /**
         * 登录成功回调，解析了友盟数据，返回我们需要的数据
         *
         * @param platform 用户绑定数据类型，2.微信注册 3.qq注册 4新浪微博
         * @param uid      对应平台ID
         * @param nickname 昵称
         * @param sex      性别 0 男 1女
         * @param headUrl  头像
         */
        void onComplete(@LoginPlatform int platform, Map<String, String> data, String uid, String nickname, int sex, String headUrl);

        /**
         * 授权登录异常
         *
         * @param platform 登录平台
         * @param t
         */
        void onError(@LoginPlatform int platform, Throwable t);

        /**
         * 用户取消授权登录
         *
         * @param platform 登录平台
         */
        void onCancel(@LoginPlatform int platform);
    }

    public interface ShareListener {
        /**
         * 分享开始的回调
         *
         * @param platform 登录平台
         */
        void onStart(@SharePlatform int platform);

        /**
         * 分享成功的回调
         *
         * @param platform 登录平台
         */
        void onResult(@SharePlatform int platform);

        /**
         * 分享失败的回调
         *
         * @param platform 登录平台
         * @param t
         */
        void onError(@SharePlatform int platform, Throwable t);

        /**
         * 分享取消的回调
         *
         * @param platform 登录平台
         */
        void onCancel(@SharePlatform int platform);
    }

    public static class SimpleShareListener implements ShareListener {
        @Override
        public void onStart(int platform) {
        }

        @Override
        public void onResult(int platform) {
        }

        @Override
        public void onError(int platform, Throwable t) {
        }

        @Override
        public void onCancel(int platform) {
        }
    }


    public static void init(Application application) {
        Timber.i("友盟初始化...");
        /*
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

        /*
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

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWeiboInstalled(Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
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

    private static LoginListener loginListener;
    private static UMAuthListener umAuthListener =
            new UMAuthListener() {
                /**
                 * 授权开始的回调
                 * @param platform 平台名称
                 */
                @Override
                public void onStart(SHARE_MEDIA platform) {
                    if (null != loginListener) {
                        loginListener.onStart(getLoginType(platform));
                    }
                }

                /**
                 * 授权成功的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 * @param data 用户资料返回
                 */
                @Override
                public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                    if (null != loginListener) {
                        int gender = 0;
                        if (data.get("gender") != null) {
                            gender = data.get("gender").equals("0") ? 0 : 1;
                        }
                        loginListener.onComplete(getLoginType(platform),
                                data,
                                data.get("uid"),
                                data.get("name"),
                                gender,
                                data.get("iconurl"));

                        loginListener = null;
                    }
                }

                /**
                 * 授权失败的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 * @param t 错误原因
                 */
                @Override
                public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                    if (null != loginListener) {
                        loginListener.onError(getLoginType(platform), t);

                        loginListener = null;
                    }
                }

                /**
                 * 授权取消的回调
                 * @param platform 平台名称
                 * @param action 行为序号，开发者用不上
                 */
                @Override
                public void onCancel(SHARE_MEDIA platform, int action) {
                    if (null != loginListener) {
                        loginListener.onCancel(getLoginType(platform));

                        loginListener = null;
                    }
                }
            };


    private static ShareListener shareListener;
    private static UMShareListener umShareListener = new UMShareListener() {
        /**
         * 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if (null != shareListener) {
                shareListener.onStart(toSharePlatform(platform));
            }
        }

        /**
         * 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (null != shareListener) {
                shareListener.onResult(toSharePlatform(platform));

                shareListener = null;
            }
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (null != shareListener) {
                shareListener.onError(toSharePlatform(platform), t);

                shareListener = null;
            }
        }

        /**
         * 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (null != shareListener) {
                shareListener.onCancel(toSharePlatform(platform));

                shareListener = null;
            }
        }
    };

    /**
     * LoginPlatform 转换为 友盟的数据格式
     *
     * @param platform 分享平台
     * @return 友盟定义平台
     */
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

    /**
     * 调用友盟第三方登录接口
     *
     * @param platform 登录平台
     * @param activity activity
     * @param listener 回调
     */
    private static void thirdPartyLogin(@LoginPlatform int platform, Activity activity, LoginListener listener) {
        boolean installed = UMShareAPI.get(activity).isInstall(activity, toUMShareMedia(platform));
        String msg = "";
        // 先判断是否安装了客户端
        switch (platform) {
            case LOGIN_PLATFORM_WEIXIN:
                msg = activity.getString(R.string.socialize_wx_not_install);
                break;
            case LOGIN_PLATFORM_QQ:
                msg = activity.getString(R.string.socialize_qq_not_install);
                break;
            case LOGIN_PLATFORM_SINA_WEIBO:
                msg = activity.getString(R.string.socialize_wb_not_install);
                break;
        }

        if (!installed) {
            if (null != listener) {
                listener.onError(platform, new Throwable(msg));
            }

            return;
        }

        loginListener = listener;

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);

        UMShareAPI.get(activity).setShareConfig(config);

        UMShareAPI.get(activity).
                getPlatformInfo(activity,
                        getUMShareMedia(platform), umAuthListener);
    }

    /**
     * 微信登录
     *
     * @param activity activity
     * @param listener 回调
     */
    public static void wxLogin(Activity activity, LoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_WEIXIN, activity, listener);
    }

    /**
     * QQ登录
     *
     * @param activity activity
     * @param listener 回调
     */
    public static void qqLogin(Activity activity, LoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_QQ, activity, listener);
    }

    /**
     * 新浪微博登录
     *
     * @param activity activity
     * @param listener 回调
     */
    public static void sinaLogin(Activity activity, LoginListener listener) {
        thirdPartyLogin(LOGIN_PLATFORM_SINA_WEIBO, activity, listener);
    }

//    public static void share(FragmentManager fragmentManager, ShareListener listener) {
//        shareListener = listener;
//        new DialogShare().show(fragmentManager, TAG);
//    }

    /**
     * 分享网页
     *
     * @param fragmentManager FragmentDialog 需要
     * @param listener        回调
     * @param url             分享链接
     * @param title           标题
     * @param content         内容
     * @param imageUrl        图标
     */
    public static void shareWeb(FragmentManager fragmentManager, ShareListener listener,
                                String url, String title, String content, String imageUrl) {
        shareListener = listener;

        DialogShare.shareWeb(fragmentManager, url, title, content, imageUrl);
    }

    public static void shareWithWW(FragmentManager fragmentManager, ShareListener listener,
                                   String url, String title, String content, String imageUrl,
                                   WWShareListener wwShareListener) {
        shareListener = listener;

        DialogShare.shareWithWW(fragmentManager, url, title, content, imageUrl, wwShareListener);
    }

    /**
     * 分享网页链接
     *
     * @param activity activity
     * @param platform 分享平台
     * @param url      链接
     * @param title    标题
     * @param content  内容
     * @param imageUrl 图标地址
     */
    public static void umShareWeb(Activity activity, @SharePlatform int platform,
                                  String url, String title, String content, String imageUrl) {
        // 先判断是否安装了对应的客户端
        boolean installed = UMShareAPI.get(activity).isInstall(activity, toUMShareMedia(platform));

        String msg = "";
        // 先判断是否安装了客户端
        switch (platform) {
            case SHARE_PLATFORM_WX:
            case SHARE_PLATFORM_WX_CIRCLE:
                msg = activity.getString(R.string.socialize_share_wx_not_install);
                break;
            case SHARE_PLATFORM_QQ:
            case SHARE_PLATFORM_QQ_ZONE:
                msg = activity.getString(R.string.socialize_share_qq_not_install);
                break;
            case SHARE_PLATFORM_SINA_WEIBO:
                msg = activity.getString(R.string.socialize_share_wb_not_install);
                break;
            case SHARE_PLATFORM_WW_FRIEND:
                break;
        }

        if (!installed) {
            if (null != shareListener) {
                shareListener.onError(platform, new Throwable(msg));
                shareListener = null;
            }

            return;
        }


//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//
//        UMShareAPI.get(activity).setShareConfig(config);

        new ShareAction(activity)
                .setPlatform(toUMShareMedia(platform))//传入平台
                .withMedia(new UMWeb(url, title, content, new UMImage(activity, imageUrl))) //分享内容
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    public static void umShare(Activity activity, @SharePlatform int platform) {
        new ShareAction(activity)
                .setPlatform(toUMShareMedia(platform))//传入平台
                .withText("hello")//分享内容
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    private static @SharePlatform
    int toSharePlatform(SHARE_MEDIA platform) {
        switch (platform) {
            case WEIXIN:
                return SHARE_PLATFORM_WX;
            case WEIXIN_CIRCLE:
                return SHARE_PLATFORM_WX_CIRCLE;
            case QQ:
                return SHARE_PLATFORM_QQ;
            case QZONE:
                return SHARE_PLATFORM_QQ_ZONE;
            case SINA:
                return SHARE_PLATFORM_SINA_WEIBO;
        }

        return SHARE_PLATFORM_UNKNOWN;
    }

    private static SHARE_MEDIA toUMShareMedia(@SharePlatform int platform) {
        switch (platform) {
            case SHARE_PLATFORM_WX:
                return SHARE_MEDIA.WEIXIN;
            case SHARE_PLATFORM_WX_CIRCLE:
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case SHARE_PLATFORM_QQ:
                return SHARE_MEDIA.QQ;
            case SHARE_PLATFORM_QQ_ZONE:
                return SHARE_MEDIA.QZONE;
            case SHARE_PLATFORM_SINA_WEIBO:
                return SHARE_MEDIA.SINA;
            case SHARE_PLATFORM_UNKNOWN:
            default:
                return SHARE_MEDIA.MORE;

        }


    }
}
