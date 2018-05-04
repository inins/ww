package com.wang.social.topic.utils;

import android.content.Context;
import android.text.TextUtils;

import com.frame.utils.TimeUtils;
import com.wang.social.topic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtil {
    public static String assertNotNull(String str) {
        return str == null ? "" : str;
    }

    public static int getAgeFromBirthTime(Date birthday) {
        // 先截取到字符串中的年、月、日
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthday);
        int selectYear = birth.get(Calendar.YEAR);;
        int selectMonth = birth.get(Calendar.MONTH) + 1;
        int selectDay = birth.get(Calendar.DATE);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    public static String formatCreateDate(Context context, long mills) {
        String dateString;
        Date date = new Date();
//        long mills = TimeUtils.string2Millis(topic.getCreateTime());
        date.setTime(mills);
        if (TimeUtils.isToday(date)) {
            dateString = context.getString(R.string.topic_today) +
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        } else {
            dateString = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(date);
        }

        return dateString;
    }

    public static String getBirthYears(long mills) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mills);
        int year = c.get(Calendar.YEAR);
        year = year - (year % 10);
        year = year % 100;
        if (year == 0) return "00";
        return Integer.toString(year);
    }

    public static boolean isURL(String str){
        if (TextUtils.isEmpty(str)) return false;
        str = str.toLowerCase();
        return str.startsWith("http:");
    }



    public final static String TAG_IMG = "<img";
    public final static String TAG_END = ">";
    public final static String TAG_SRC = "src=";
    public final static String TAG_COLON = "\"";
    public final static String TAG_BLANK = " ";
    public static String findLocalImg(String content) {
        if (null == content || content.length() <= 0) return "";

        int index = 0;

        int startPos = content.indexOf(TAG_IMG);
        if (startPos < 0) return "";

        index += startPos;

        String temp = content.substring(startPos);

        int endPos = temp.indexOf(TAG_END);

        if (endPos < 0) return "";

        index += endPos;

        temp = temp.substring(0, endPos);

        startPos = temp.indexOf(TAG_SRC);

        if (startPos < 0) return "";

        temp = temp.substring(startPos + TAG_SRC.length());

        endPos = temp.indexOf(TAG_BLANK);

        temp = temp.substring(0, endPos);

        temp = temp.replaceAll("\"", "");
        temp = temp.replaceAll("'", "");
        temp = temp.replaceAll(" ", "");

        if (isURL(temp)) {
            return findLocalImg(content.substring(index));
        }

        return temp;
    }
}
