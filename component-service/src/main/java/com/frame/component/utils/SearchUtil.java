package com.frame.component.utils;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.frame.component.service.R;
import com.frame.utils.StrUtil;
import com.frame.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchUtil {

    public static SpannableString getHotText(String tags, String key, String content) {
        //设置搜索关键字高亮
        //遍历搜索tags和关键字，匹配成功则设置为高亮
        //获取关键字集合
        SpannableString spannableString = new SpannableString(content);
        List<String> keys = new ArrayList<>();
        if (!StrUtil.isEmpty(tags)) keys.addAll(Arrays.asList(tags.split(",")));
        if (!TextUtils.isEmpty(key)) keys.add(key);
        if (StrUtil.isEmpty(keys)) return spannableString;
        //遍历匹配关键字
        int color = ContextCompat.getColor(Utils.getContext(), R.color.common_blue_deep);
        for (String str : keys) {
            //匹配成功：设置高亮
            if (content.contains(str)) {
                int start = content.indexOf(str);
                int end = start + str.length();
                spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
