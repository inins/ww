package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.frame.component.common.ConerBkSpan;
import com.wang.social.funshow.R;
import com.wang.social.funshow.common.StringColorSpan;
import com.wang.social.funshow.mvp.entities.user.Friend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AiteEditText extends android.support.v7.widget.AppCompatEditText {

    private List<Friend> friends = new ArrayList<>();

    public AiteEditText(Context context) {
        super(context);
    }

    public AiteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AiteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                StringColorSpan[] spans = s.getSpans(0, s.length(), StringColorSpan.class);
                for (int i = 0; i < spans.length; i++) {
                    int start = s.getSpanStart(spans[i]);
                    int end = s.getSpanEnd(spans[i]);
                    String spanStr = s.toString().substring(start, end);
                    String spanKey = spans[i].keyWords().trim();
                    if (!spanStr.equals(spanKey)) {
                        s.removeSpan(spans[i]);
                        s.replace(start, end, "");
                        removeFriendByName(spanKey);
                    }
                }
            }
        });
    }

    private void removeFriendByName(String name) {
        Iterator<Friend> iterator = friends.iterator();
        while (iterator.hasNext()) {
            Friend next = iterator.next();
            if (name.endsWith(next.getNickName())) {
                iterator.remove();
            }
        }
    }

    private void appendAiteStr(String name, int id) {
        String aiteStr = "@" + name;
        int color = ContextCompat.getColor(getContext(), R.color.common_blue_deep);
        SpannableString aiteSpannable = new SpannableString(aiteStr);
        aiteSpannable.setSpan(new StringColorSpan(color, aiteStr), 0, aiteStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        append(aiteSpannable);
    }

    public void appendAiteStr(Friend friend) {
        appendAiteStr(friend.getNickName(), friend.getFriendId());
        friends.add(friend);
    }

    public void appendAiteStr(List<Friend> friends) {
        for (Friend friend : friends) {
            appendAiteStr(friend);
        }
    }

    /////////////////////////////


    public List<Friend> getFriends() {
        return friends;
    }
}
