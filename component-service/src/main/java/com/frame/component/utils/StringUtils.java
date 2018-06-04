package com.frame.component.utils;

import android.text.TextUtils;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.RegexUtils;

import java.util.List;

import timber.log.Timber;

public class StringUtils {

    public static String formatTagIds(List<Tag> list) {
        String tagIds = "";

        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagIds = tagIds + tag.getId();
            if (i < list.size() - 1) {
                tagIds = tagIds + ",";
            }
        }

        return tagIds;
    }

    public static String formatTagNames(List<Tag> list) {
        String tagNames = "";

        for (int i = 0; i < list.size(); i++) {
            Tag tag = list.get(i);
            Timber.i(tag.getId() + " " + tag.getTagName());
            tagNames = tagNames + " #" + tag.getTagName();
            if (i < list.size() - 1) {
                tagNames = tagNames + ",";
            }
        }

        return tagNames;
    }


    public static boolean isVerifyCode(String code) {
        String regex = "^\\d{4}$";

        return regexMatch(regex, code);
    }

    /**
     * 检测密码格式
     * 6-18位，必须是字母和数字
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
//        String regex = "^[0-9A-Za-z]{6,18}$";
        String regex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()\\-_=+<,>.?/;:'\"\\\\|{\\[\\]~·！@#￥%……&*（）——+|{}《》【】‘；：”“'。，、？]){6,18}$";

        return regexMatch(regex, password);
    }


    /**
     * 验证手机格式
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileNO(String mobile) {
        /**
         手机号开头集合
         166，
         176，177，178
         180，181，182，183，184，185，186，187，188，189
         145，147
         130，131，132，133，134，135，136，137，138，139
         150，151，152，153，155，156，157，158，159
         198，199

         13开头的后面跟0-9的任意8位数；

         15开头的后面跟除了4以外的0-9的任意8位数；

         18开头的后面跟0-9的任意8位数；

         17开头的后面跟0-8的任意8位数，或者17[^9]；

         147，145开头后面跟任意8位数；

         166开头的后面跟任意8位数；

         198，199开头后面跟任意8位数；
         */
//        String telRegex =  "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
//
//        return regexMatch(telRegex, mobile);

        return RegexUtils.isMobileSimple(mobile);
    }

    public static boolean regexMatch(String regex, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            return str.matches(regex);
        }
    }
}
