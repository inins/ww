package com.wang.social.im.app;

import android.app.Application;
import android.content.Context;

import com.frame.base.App;
import com.frame.base.delegate.AppDelegate;
import com.frame.component.app.Constant;
import com.frame.utils.FrameUtils;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupSettings;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.message.TIMMessageRevokedListener;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;
import com.wang.social.im.BuildConfig;

import org.simple.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

/**
 * ======================================
 * {@link Application}代理实现类
 * <p>
 * Create by ChenJing on 2018-04-04 14:19
 * ======================================
 */
public class ImAppLifecycleImpl implements AppDelegate {

    private final String TAG = this.getClass().getCanonicalName();

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        //初始化IM相关配置，须在SDK登陆之前进行
        imSdkInit(application);
    }

    @Override
    public void onTerminate(Application application) {

    }

    /**
     * IM SDK初始化
     */
    private void imSdkInit(Application application) {
        //初始化SDK基本配置
        TIMSdkConfig config = new TIMSdkConfig(Constant.IM_APPID)
                .enableCrashReport(false) //是否开启Crash上报
                .enableLogPrint(BuildConfig.LOG_DEBUG) //设置是否打印日志
                .setLogLevel(TIMLogLevel.DEBUG) //设置日志打印级别
                .setLogPath(FrameUtils.obtainAppComponentFromContext(application).cacheFile().getPath() + "/imlog/"); //设置日志保存路径
        //初始化
        TIMManager.getInstance().init(application, config);

        imUserInit();
    }

    /**
     * 初始化用户配置
     */
    private void imUserInit() {
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new ImUserStatusListener())
                .setConnectionListener(new ImConnectionListener());
        //消息扩展配置
        userConfig = new TIMUserConfigMsgExt(userConfig)
                .enableStorage(true)//设置消息存储
                .setMessageRevokedListener(new MessageRevokeListener())
                .enableReadReceipt(false); //关闭消息已读回执
        //资料关系链扩展配置
        userConfig = new TIMUserConfigSnsExt(userConfig)
                .enableFriendshipStorage(true)
                .setFriendshipProxyListener(new ImFriendShipProxyListener());
        //群组管理扩展用户配置
        userConfig = new TIMUserConfigGroupExt(userConfig)
                .enableGroupStorage(true)
                .setGroupAssistantListener(new ImGroupAssistantListener());
        //将用户信息与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);
    }

    /**
     * 监听用户的登陆状态
     */
    private class ImUserStatusListener implements TIMUserStatusListener {

        /**
         * 被其他终端踢下线
         */
        @Override
        public void onForceOffline() {
            Timber.tag(TAG).d("onForceOffline");
        }

        /**
         * 用户签名过期，需重新登陆
         */
        @Override
        public void onUserSigExpired() {
            Timber.tag(TAG).d("onUserSigExpired");
        }
    }

    /**
     * 监听与Server之间的链接状态
     */
    private class ImConnectionListener implements TIMConnListener {

        @Override
        public void onConnected() {
            Timber.tag(TAG).d("im server onConnected");
        }

        @Override
        public void onDisconnected(int i, String s) {
            Timber.tag(TAG).d("im server onDisconnected");
        }

        @Override
        public void onWifiNeedAuth(String s) {
            Timber.tag(TAG).d("onWifiNeedAuth");
        }
    }

    /**
     * 监听资料关系链变化
     * 当关系链发生变化时，使用{@link org.simple.eventbus.EventBus}通知相关界面进行更新
     */
    private class ImFriendShipProxyListener implements TIMFriendshipProxyListener{

        @Override
        public void OnAddFriends(List<TIMUserProfile> list) {
            Timber.tag(TAG).d("OnAddFriends");
        }

        @Override
        public void OnDelFriends(List<String> list) {
            Timber.tag(TAG).d("OnDelFriends");
        }

        @Override
        public void OnFriendProfileUpdate(List<TIMUserProfile> list) {
            Timber.tag(TAG).d("OnFriendProfileUpdate");
        }

        @Override
        public void OnAddFriendReqs(List<TIMSNSChangeInfo> list) {
            Timber.tag(TAG).d("OnAddFriendReqs");
        }
    }

    /**
     * 群组相关信息变化监听
     * 当群组信息发生变化时，使用{@link org.simple.eventbus.EventBus}通知相关界面进行更新
     */
    private class ImGroupAssistantListener implements TIMGroupAssistantListener{

        @Override
        public void onMemberJoin(String s, List<TIMGroupMemberInfo> list) {
            Timber.tag(TAG).d("onMemberJoin");
        }

        @Override
        public void onMemberQuit(String s, List<String> list) {
            Timber.tag(TAG).d("onMemberQuit");
        }

        @Override
        public void onMemberUpdate(String s, List<TIMGroupMemberInfo> list) {
            Timber.tag(TAG).d("onMemberUpdate");
        }

        @Override
        public void onGroupAdd(TIMGroupCacheInfo timGroupCacheInfo) {
            Timber.tag(TAG).d("onGroupAdd");
        }

        @Override
        public void onGroupDelete(String s) {
            Timber.tag(TAG).d("onGroupDelete");
        }

        @Override
        public void onGroupUpdate(TIMGroupCacheInfo timGroupCacheInfo) {
            Timber.tag(TAG).d("onGroupUpdate");
        }
    }

    /**
     * 消息撤销监听
     */
    private class MessageRevokeListener implements TIMMessageRevokedListener{

        @Override
        public void onMessageRevoked(TIMMessageLocator timMessageLocator) {
            EventBus.getDefault().post(timMessageLocator);
        }
    }
}
