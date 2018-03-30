package com.wang.social.im.view.emotion;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wang.social.im.R;
import com.wang.social.im.view.emotion.data.EmoticonEntity;
import com.wang.social.im.view.emotion.listener.EmoticonClickListener;
import com.wang.social.im.view.emotion.widget.EmoticonsEditText;

public class EmotionAdapter {

    private EmotionView mEmotionView;

    private EmoticonsEditText mEditText;

    private boolean isInitialed;

    public EmotionAdapter(EmoticonsEditText editText) {
        this.mEditText = editText;
    }

    public void setVisibility(int visibility){
        if (mEmotionView != null){
            mEmotionView.setVisibility(visibility);
        }
    }

    public int getVisibility(){
        return mEmotionView == null ? View.GONE : mEmotionView.getVisibility();
    }

    public boolean isInitialed() {
        return isInitialed;
    }

    public void bindView(ViewGroup viewGroup){
        isInitialed = true;
        initView(viewGroup);
    }

    private void initView(ViewGroup viewGroup) {
        mEmotionView = new EmotionView(viewGroup.getContext());
        int height = viewGroup.getContext().getResources().getDimensionPixelSize(R.dimen.im_chat_input_plugin_board_height);
        mEmotionView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        viewGroup.addView(mEmotionView);
        mEmotionView.getEmoticonsFuncView().setAdapter(SimpleCommonUtils.getCommonAdapter(viewGroup.getContext(), emoticonClickListener));
    }

    private EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
            if (isDelBtn) {
                SimpleCommonUtils.delClick(mEditText);
            } else {
                if (o == null) {
                    return;
                }
                if (actionType == Constants.EMOTICON_CLICK_TEXT) {
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    } else if (o instanceof EmoticonEntity) {
                        content = ((EmoticonEntity) o).getContent();
                    }

                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    int index = mEditText.getSelectionStart();
                    Editable editable = mEditText.getText();
                    editable.insert(index, content);
                }
            }
        }
    };
}