package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.model.entities.GameElemData;
import com.wang.social.im.mvp.model.entities.UIMessage;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-18 11:33
 * ============================================
 */
public class GameViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_tv_game_message)
    TextView msgTvGameMessage;
    @BindView(R2.id.msg_cl_game)
    LinearLayout msgClGame;
    @Nullable
    @BindView(R2.id.msg_iv_error)
    ImageView msgIvError;
    @Nullable
    @BindView(R2.id.msg_pb_progress)
    ContentLoadingProgressBar msgPbProgress;

    Gson gson;

    public GameViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
        gson = FrameUtils.obtainAppComponentFromContext(context).gson();
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        super.bindData(itemValue, position, onItemClickListener);

        GameElemData elemData = (GameElemData) itemValue.getCustomMessageElemData(CustomElemType.GAME, GameElemData.class, gson);
        if (elemData != null) {
            String message = UIUtil.getString(R.string.im_game_message_format, itemValue.getNickname(conversationType), elemData.getDiamond());
            msgTvGameMessage.setText(message);
        }

        if (itemValue.getTimMessage().isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgClGame, itemValue, position, true, true);
    }
}