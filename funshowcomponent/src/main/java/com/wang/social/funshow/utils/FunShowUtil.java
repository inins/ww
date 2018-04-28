package com.wang.social.funshow.utils;

import android.view.View;
import android.widget.TextView;

import com.frame.utils.RegexUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.funshow.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FunShowUtil {

    public static String getFunshowTimeStr(long time) {
        return TimeUtils.date2String(new Date(time), new SimpleDateFormat("MM-dd HH:mm"));
    }

    //如果view是textView则根据isAdd把其文字（如果是数字）+1或-1
    public static int addSubTextViewCount(View view, boolean isAdd) {
        if (view instanceof TextView) {
            String numStr = ((TextView) view).getText().toString();
            if (RegexUtils.isIntegerWhithZero(numStr)) {
                int num = Integer.parseInt(numStr);
                ((TextView) view).setText(String.valueOf(isAdd ? ++num : --num));
                return num;
            }
        }
        return 0;
    }

    //详情页，解析“xxx个赞”，把xxx +1 或 -1
    public static int addSubTextViewCountForEnd(TextView textView, boolean isAdd, String endStr) {
        String numStr = textView.getText().toString();
        if (numStr.contains(endStr)) {
            numStr = StrUtil.subLastChart(numStr, endStr);
            if (RegexUtils.isIntegerWhithZero(numStr)) {
                int num = Integer.parseInt(numStr);
                textView.setText(String.valueOf(isAdd ? ++num : --num) + endStr);
                return num;
            }
        }
        return 0;
    }
}
