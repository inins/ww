package com.wang.social.im.view.emotion.listener;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public interface EmoticonClickListener<T> {

    void onEmoticonClick(T t, int actionType, boolean isDelBtn);
}
