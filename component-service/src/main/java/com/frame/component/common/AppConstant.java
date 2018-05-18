package com.frame.component.common;

import com.frame.component.api.Api;

public class AppConstant {

    //常量
    public static class Constant {
        //录音缓存目录
        public static final String SOUND_CACHE_DIR = "Sound";
        //Glide缓存目录
        public static final String GLIDE_CACHE_DIR = "Glide";
        //图片缓存目录
        public static final String IMAGE_CACHE_DIR = "Image";
    }

    //key
    public static class Key {
        //暂无
    }

    //静态web页面url
    public static class Url {

        //##################################
        //############公共（文案）##########
        //##################################

        //快活林规则说明
        public static final String sharedTree = Api.WEB_BASE_URL + "about/common/sharedTree.html";
        //代言收益规则说明
        public static final String ruleDescription = "ttp://wangsocial.com/share/v_2.0/test/about/common/ruleDescription.html";
        //隐私协议
        public static final String privacyAgreement = Api.WEB_BASE_URL + "about/common/privacyAgreement.html";
        //建群须知
        public static final String notice = Api.WEB_BASE_URL + "about/common/notice.html";
        //邀请码
        public static final String InvitationCode = Api.WEB_BASE_URL + "about/common/InvitationCode.html";
        //付费Fan聊创建须知
        public static final String fanNotice = Api.WEB_BASE_URL + "about/common/fanNotice.html";
        //往往免责声明
        public static final String disclaimer = Api.WEB_BASE_URL + "about/common/disclaimer.html";
        //账户常见问题
        public static final String commonProblem = Api.WEB_BASE_URL + "about/common/commonProblem.html";
        //大佬撒钱规则介绍
        public static final String bigMoneyNotice = Api.WEB_BASE_URL + "about/common/bigMoneyNotice.html";
        //关于往往
        public static final String wwAbout = Api.WEB_BASE_URL + "about/common/wwAbout.html";
        //意见反馈：为什么希望得到你的意见
        public static final String proposal = Api.WEB_BASE_URL + "about/common/proposal.html";

        //##################################
        //#########android（文案）##########
        //##################################

        //关于账户
        public static final String aboutAccount = Api.WEB_BASE_URL + "about/android/aboutAccount.html";
        //关于Fan聊/Mi聊
        public static final String aboutFanliao = Api.WEB_BASE_URL + "about/android/aboutFanliao.html";
        //往往版本记录
        public static final String aboutHistory = Api.WEB_BASE_URL + "about/android/aboutHistory.html";
        //其它问题(下载与注册，关于我们)
        public static final String aboutOther = Api.WEB_BASE_URL + "about/android/aboutOther.html";
        //关于趣晒/话题
        public static final String aboutQushai = Api.WEB_BASE_URL + "about/android/aboutQushai.html";
        //往往用户注册协议
        public static final String userAgreement = Api.WEB_BASE_URL + "about/android/userAgreement.html";
        //v2.0
        public static final String currentVersion = Api.WEB_BASE_URL + "about/android/currentVersion.html";

        //##################################
        //############分享（文案）##########
        //##################################

        //摇钱树
        public static final String cashcowindex = Api.WEB_BASE_URL + "cashcow/index.html";
        //游戏玩法
        public static final String cashcowrule = Api.WEB_BASE_URL + "cashcow/rule.html";
        // 二维码
        public static final String ewmCode = Api.WEB_BASE_URL + "ewmCode/qrcode.html";
        //榜单
        public static final String overall = Api.WEB_BASE_URL + "overall/index.html";
        //下载
        public static final String appdownload = Api.WEB_BASE_URL + "app/index.html";
        //故事
        public static final String story = Api.WEB_BASE_URL + "contentShared/story/index.html";
        //话题
        public static final String topic = Api.WEB_BASE_URL + "contentShared/topic/index.html";
        /**
         * 趣聊
         * 参数:
         * groupId 群ID
         * userId 用户ID
         */
        public static final String SHARE_SOCIAL_URL = Api.WEB_BASE_URL + "contentShared/group/index.html";
        public static final String SHARE_SOCIAL_TITLE = "趣聊分享";

        //##################################
        //############  APP  ###############
        //##################################

        //给个好评，跳转页面
        public static final String eva = "http://sj.qq.com/myapp/detail.htm?apkName=com.dongdongkeji.wangwangsocial";

    }
}