package com.wang.social.topic.mvp.ui.widget.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * 富文本编辑器，基于WebView
 * Created by Chao on 2017/6/21.
 */

public class RichEditor extends WebView {

    public enum Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        BLOCKQUOTE,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6
    }

    public interface OnTextChangeListener {

        void onTextChange(String text);
    }

    public interface OnDecorationStateListener {

        void onStateChangeListener(String text, List<Type> types);
    }

    public interface AfterInitialLoadListener {

        void onAfterInitialLoad(boolean isReady);
    }

    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String STATE_SCHEME = "re-state://";
    private boolean isReady = false;
    private String mContents;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private AfterInitialLoadListener mLoadListener;
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        // 设置与Js交互的权限
        getSettings().setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(createWebviewClient());
        loadUrl(SETUP_HTML);
        //loadDataWithBaseURL("file:///android_asset/",SETUP_HTML,"text/html","utf-8",null);
        applyAttributes(context, attrs);
    }

    protected EditorWebViewClient createWebviewClient() {
        return new EditorWebViewClient();
    }

    public synchronized void setOnTextChangeListener(OnTextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public synchronized void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        mDecorationStateListener = listener;
    }

    public synchronized void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        mLoadListener = listener;
    }

    private void callback(String text) {
        mContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChange(mContents);
        }
        return;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }

    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public synchronized void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }


    private void stateCheck(String text) {

        if (!text.contains("@_@")) {
            String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
            List<Type> types = new ArrayList<>();
            for (Type type : Type.values()) {
                if (TextUtils.indexOf(state, type.name()) != -1) {
                    types.add(type);
                }
            }

            if (mDecorationStateListener != null) {
                mDecorationStateListener.onStateChangeListener(state, types);
            }
            return;
        }

        String state = text.replaceFirst(STATE_SCHEME, "").split("@_@")[0].toUpperCase(Locale.ENGLISH);
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.add(type);
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChangeListener(state, types);
        }

        if (text.replaceFirst(STATE_SCHEME, "").split("@_@").length > 1) {
            mContents = text.replaceFirst(STATE_SCHEME, "").split("@_@")[1];
            if (mTextChangeListener != null) {
                mTextChangeListener.onTextChange(mContents);
            }
        }
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        final int[] attrsArray = new int[]{
                android.R.attr.gravity
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int gravity = ta.getInt(0, NO_ID);
        switch (gravity) {
            case Gravity.LEFT:
                exec("javascript:RE.setTextAlign(\"left\")");
                break;
            case Gravity.RIGHT:
                exec("javascript:RE.setTextAlign(\"right\")");
                break;
            case Gravity.TOP:
                exec("javascript:RE.setVerticalAlign(\"top\")");
                break;
            case Gravity.BOTTOM:
                exec("javascript:RE.setVerticalAlign(\"bottom\")");
                break;
            case Gravity.CENTER_VERTICAL:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                break;
            case Gravity.CENTER_HORIZONTAL:
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
            case Gravity.CENTER:
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
        }

        ta.recycle();
    }


    public synchronized void setHtml(String contents) {
        if (contents == null) {
            contents = "";
        }
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        } catch (UnsupportedEncodingException e) {
            // No handling
        }
        mContents = contents;
    }


    public String getHtml() {
        return mContents;
    }

    public synchronized void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public synchronized void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    @Override
    public synchronized void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override
    public synchronized void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public synchronized void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public synchronized void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public synchronized void setBackgroundResource(int resid) {
        Bitmap bitmap = Utils.decodeResource(getContext(), resid);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override
    public synchronized void setBackground(Drawable background) {
        Bitmap bitmap = Utils.toBitmap(background);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public synchronized void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    public synchronized void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public synchronized void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }

    public synchronized void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public synchronized void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public synchronized void undo() {
        exec("javascript:RE.undo();");
    }

    public synchronized void redo() {
        exec("javascript:RE.redo();");
    }

    public synchronized void setBold() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setBold();");
    }

    public synchronized void setItalic() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setItalic();");
    }

    public synchronized void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    public synchronized void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    public synchronized void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    public synchronized void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    public synchronized void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    public synchronized void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public synchronized void setFontSize(int fontSize) {
        if (fontSize > 7 || fontSize < 1) {
            Log.e("RichEditor", "Font size should have a value between 1-7");
        }
        exec("javascript:RE.setFontSize('" + fontSize + "');");
    }

    public synchronized void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    public synchronized void setHeading(int heading) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setHeading('" + heading + "'," + true + ");");
    }

    public synchronized void setHeading(int heading, boolean b, boolean isItalic, boolean isBold, boolean isStrikeThrough) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setHeading('" + heading + "'," + b + ");");
    }

    public synchronized void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    public synchronized void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    public synchronized void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    public synchronized void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    public synchronized void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    public synchronized void setBlockquote() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setBlockquote(" + true + ");");
    }

    public synchronized void setBlockquote(boolean b, boolean isItalic, boolean isBold, boolean isStrikeThrough) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setBlockquote(" + b + ");");
    }

    public synchronized void setOrderdisc() {
        Timber.i("");
        exec("javascript:RE.setOrderdisc();");
    }//实心圆

    public synchronized void setOrderNumbers() {
        Timber.i("javascript:RE.setOrderNumbers();");
        exec("javascript:RE.setOrderNumbers();");
    }//数字

    public synchronized void setOrderCircle() {
        Timber.i("javascript:RE.setOrderCircle();");
        exec("javascript:RE.setOrderCircle();");
    }//空心圆

    public synchronized void setOrderSquare() {
        Timber.i("javascript:RE.setOrderSquare();");
        exec("javascript:RE.setOrderSquare();");
    }//方块

    public synchronized void setNumberRoman() {
        Timber.i("javascript:RE.setNumberRoman();");
        exec("javascript:RE.setNumberRoman();");
    }//罗马数字

    public synchronized void setNumbersLower() {
        Timber.i("javascript:RE.setNumbersLower();");
        exec("javascript:RE.setNumbersLower();");
    }//小写字母

    public synchronized void setNumbersUpper() {
        Timber.i("javascript:RE.setNumbersUpper();");
        exec("javascript:RE.setNumbersUpper();");
    }//大写字母

    public synchronized void setOrderCjk() {
        Timber.i("javascript:RE.setOrderCjk();");
        exec("javascript:RE.setOrderCjk();");
    }//一

    public synchronized void setNumbersNone() {
        exec("javascript:RE.setNumbersNone();");
    }

    public synchronized void setFont(String font) {
        exec("javascript:RE.setFont('" + font + "');");
    }

    public synchronized void insertImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', '" + alt + "');");
    }
    public synchronized void initAudioPlay() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.initAudioPlay();");
    }

    public synchronized void insertAudioImage(String url, String img, int w, int h, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertAudioImage('" + url + "', '" + img + "', '" + w + "', '" + h + "', '" + alt + "');");
    }

    public synchronized void insertHr() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHr();");
    }

    public synchronized void insertHtml(String html) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertHtml(" + html + ");");
    }

    public synchronized void insertText(String text) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertText('" + text + "');");
    }

    public synchronized void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    public synchronized void insertTodo() {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.setTodo('" + Utils.getCurrentTime() + "');");
    }

    public synchronized void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public synchronized void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    protected void exec(final String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            postDelayed(new Runnable() {
                @Override
                public synchronized void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }

    protected class EditorWebViewClient extends WebViewClient {
        @Override
        public synchronized void onPageFinished(WebView view, String url) {
            isReady = url.equalsIgnoreCase(SETUP_HTML);
            if (mLoadListener != null) {
                mLoadListener.onAfterInitialLoad(isReady);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String decode;
            try {
                decode = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // No handling
                return false;
            }

            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode);
                return true;
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode);
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}