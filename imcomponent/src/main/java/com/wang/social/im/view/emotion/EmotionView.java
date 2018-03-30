package com.wang.social.im.view.emotion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wang.social.im.R;
import com.wang.social.im.view.emotion.adapter.PageSetAdapter;
import com.wang.social.im.view.emotion.data.PageSetEntity;
import com.wang.social.im.view.emotion.widget.EmoticonsFuncView;
import com.wang.social.im.view.emotion.widget.EmoticonsIndicatorView;
import com.wang.social.im.view.emotion.widget.EmoticonsToolBarView;

import java.util.ArrayList;

/**
 * 表情键盘
 */
public class EmotionView extends FrameLayout implements EmoticonsFuncView.OnEmoticonsPageViewListener, EmoticonsToolBarView.OnToolBarItemClickListener{

    private EmoticonsFuncView mEmoticonsFuncView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;

    public EmotionView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public EmotionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmotionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.im_input_emotion, this);

        mEmoticonsFuncView = findViewById(R.id.ie_emotions);
        mEmoticonsIndicatorView = findViewById(R.id.ie_indicator);
        mEmoticonsToolBarView = findViewById(R.id.ie_toolbar);

        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return mEmoticonsFuncView;
    }

    public void setAdapter(PageSetAdapter pageSetAdapter){
        if (pageSetAdapter != null){
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null){
                for (PageSetEntity pageSetEntity : pageSetEntities){
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
            mEmoticonsFuncView.setAdapter(pageSetAdapter);
        }
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }
}