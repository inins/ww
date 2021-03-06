package com.wang.social.im.view.emotion;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.view.emotion.adapter.EmoticonsAdapter;
import com.wang.social.im.view.emotion.adapter.PageSetAdapter;
import com.wang.social.im.view.emotion.data.EmoticonEntity;
import com.wang.social.im.view.emotion.data.EmoticonPageEntity;
import com.wang.social.im.view.emotion.data.EmoticonPageSetEntity;
import com.wang.social.im.view.emotion.data.PageSetEntity;
import com.wang.social.im.view.emotion.listener.EmoticonClickListener;
import com.wang.social.im.view.emotion.listener.EmoticonDisplayListener;
import com.wang.social.im.view.emotion.listener.ImageBase;
import com.wang.social.im.view.emotion.listener.PageViewInstantiateListener;
import com.wang.social.im.view.emotion.widget.EmoticonPageView;
import com.wang.social.im.view.emotion.widget.EmoticonsEditText;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleCommonUtils {

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new EmojiFilter());
    }

    public static EmoticonClickListener getCommonEmoticonClickListener(final EditText editText) {
        return new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    SimpleCommonUtils.delClick(editText);
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
                        int index = editText.getSelectionStart();
                        Editable editable = editText.getText();
                        editable.insert(index, content);
                    }
                }
            }
        };
    }

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, EmoticonClickListener emoticonClickListener) {

        if (sCommonPageSetAdapter != null) {
            return sCommonPageSetAdapter;
        }

        PageSetAdapter pageSetAdapter = new PageSetAdapter();

        addEmojiPageSetEntity(pageSetAdapter, context, emoticonClickListener);

        addFaceOne(pageSetAdapter, context, emoticonClickListener);

        addFaceTwo(pageSetAdapter, context, emoticonClickListener);

        return pageSetAdapter;
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param emoticonClickListener
     */
    public static void addEmojiPageSetEntity(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener emoticonClickListener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sEmojiArray);
        EmoticonPageSetEntity emojiPageSetEntity =
                new EmoticonPageSetEntity.Builder()
                        .setLine(3)
                        .setRow(7)
                        .setEmoticonList(emojiArray)
                        .setIPageViewInstantiateItem(getDefaultEmoticonPageViewInstantiateItem(new EmoticonDisplayListener<Object>() {
                            @Override
                            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                                final EmojiBean emojiBean = (EmojiBean) object;
                                if (emojiBean == null && !isDelBtn) {
                                    return;
                                }

                                if (isDelBtn) {
                                    viewHolder.iv_emoticon.setImageResource(R.drawable.im_emotion_del_icon);
                                } else {
                                    viewHolder.iv_emoticon.setImageResource(emojiBean.icon);
                                }

//                                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (emoticonClickListener != null) {
//                                            emoticonClickListener.onEmoticonClick(emojiBean, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
//                                        }
//                                    }
//                                });
                            }
                        }, emoticonClickListener))
                        .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("emoji_0x1f603"))
                        .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param faceClickListener
     */
    public static void addFaceOne(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener faceClickListener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sFaceArray);
        EmoticonPageSetEntity emojiPageSetEntity =
                new EmoticonPageSetEntity.Builder()
                        .setLine(2)
                        .setRow(5)
                        .setItemHeight(SizeUtils.dp2px(50))
                        .setEmoticonList(emojiArray)
                        .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(EmoticonsAdapter.class, faceClickListener, Constants.EMOTICON_CLICK_BIGIMAGE, new EmoticonDisplayListener<Object>() {

                            @Override
                            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object o, boolean isDelBtn) {
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.iv_emoticon.getLayoutParams();
                                lp.width = SizeUtils.dp2px(40);
                                lp.height = SizeUtils.dp2px(40);
                                viewHolder.iv_emoticon.setImageResource(((EmojiBean) o).icon);
//                                viewHolder.iv_emoticon.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (faceClickListener != null) {
//                                            faceClickListener.onEmoticonClick(o, Constants.EMOTICON_CLICK_BIGIMAGE, false);
//                                        }
//                                    }
//                                });
                            }
                        }))
                        .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("im_ww_emoji_001"))
                        .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }

    /**
     * 插入emoji表情集
     *
     * @param pageSetAdapter
     * @param context
     * @param faceClickListener
     */
    public static void addFaceTwo(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener faceClickListener) {
        ArrayList<EmojiBean> emojiArray = new ArrayList<>();
        Collections.addAll(emojiArray, DefEmoticons.sFaceTwoArray);
        EmoticonPageSetEntity emojiPageSetEntity =
                new EmoticonPageSetEntity.Builder()
                        .setLine(2)
                        .setRow(5)
                        .setItemHeight(SizeUtils.dp2px(50))
                        .setEmoticonList(emojiArray)
                        .setIPageViewInstantiateItem(getEmoticonPageViewInstantiateItem(EmoticonsAdapter.class, faceClickListener, Constants.EMOTICON_CLICK_BIGIMAGE, new EmoticonDisplayListener<Object>() {

                            @Override
                            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object o, boolean isDelBtn) {
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.iv_emoticon.getLayoutParams();
                                lp.width = SizeUtils.dp2px(50);
                                lp.height = SizeUtils.dp2px(50);
                                viewHolder.iv_emoticon.setImageResource(((EmojiBean) o).icon);
//                                viewHolder.iv_emoticon.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (faceClickListener != null) {
//                                            faceClickListener.onEmoticonClick(o, Constants.EMOTICON_CLICK_BIGIMAGE, false);
//                                        }
//                                    }
//                                });
                            }
                        }))
                        .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("im_ww_emoji_101"))
                        .build();
        pageSetAdapter.add(emojiPageSetEntity);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, Object... args) throws Exception {
        return newInstance(_Class, 0, args);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class _Class, int constructorIndex, Object... args) throws Exception {
        Constructor cons = _Class.getConstructors()[constructorIndex];
        return cons.newInstance(args);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getDefaultEmoticonPageViewInstantiateItem(final EmoticonDisplayListener<Object> emoticonDisplayListener, EmoticonClickListener clickListener) {
        return getEmoticonPageViewInstantiateItem(EmoticonsAdapter.class, clickListener, Constants.EMOTICON_CLICK_TEXT, emoticonDisplayListener);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, EmoticonClickListener onEmoticonClickListener) {
        return getEmoticonPageViewInstantiateItem(_class, onEmoticonClickListener, Constants.EMOTICON_CLICK_TEXT, null);
    }

    public static PageViewInstantiateListener<EmoticonPageEntity> getEmoticonPageViewInstantiateItem(final Class _class, final EmoticonClickListener onEmoticonClickListener, int type, final EmoticonDisplayListener<Object> emoticonDisplayListener) {
        return new PageViewInstantiateListener<EmoticonPageEntity>() {
            @Override
            public View instantiateItem(ViewGroup container, int position, EmoticonPageEntity pageEntity) {
                if (pageEntity.getRootView() == null) {
                    EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                    pageView.setNumColumns(pageEntity.getRow());
                    pageEntity.setRootView(pageView);
                    try {
                        EmoticonsAdapter adapter = (EmoticonsAdapter) newInstance(_class, container.getContext(), pageEntity, onEmoticonClickListener);
                        if (emoticonDisplayListener != null) {
                            adapter.setOnDisPlayListener(emoticonDisplayListener);
                        }
                        pageView.getEmoticonsGridView().setAdapter(adapter);

                        pageView.getEmoticonsGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (onEmoticonClickListener != null) {
                                    onEmoticonClickListener.onEmoticonClick(pageEntity.getEmoticonList().get(position), type, false);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return pageEntity.getRootView();
            }
        };
    }

    public static EmoticonDisplayListener<Object> getCommonEmoticonDisplayListener(final EmoticonClickListener onEmoticonClickListener, final int type) {
        return new EmoticonDisplayListener<Object>() {
            @Override
            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {

                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
                if (emoticonEntity == null && !isDelBtn) {
                    return;
                }
//                viewHolder.ly_root.setBackgroundResource(R.drawable.bg_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.drawable.im_emotion_del_icon);
                } else {
                    try {
                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onEmoticonClickListener != null) {
                            onEmoticonClickListener.onEmoticonClick(emoticonEntity, type, isDelBtn);
                        }
                    }
                });
            }
        };
    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

//    public static void spannableEmoticonFilter(TextView tv_content, String content) {
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
//
//        Spannable spannable = EmojiDisplay.spannableFilter(tv_content.getContext(),
//                spannableStringBuilder,
//                content,
//                EmoticonsKeyboardUtils.getFontHeight(tv_content));
//
//        tv_content.setText(spannable);
//    }
}
