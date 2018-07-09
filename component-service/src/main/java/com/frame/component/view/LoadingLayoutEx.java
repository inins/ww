package com.frame.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frame.component.helper.LoadingViewHelper;
import com.frame.component.service.R;

import timber.log.Timber;

/**
 * Created by liaoinstan on 2018/5/31.
 * {@link LoadingLayout} 拓展，提供符合APP设计的统一加载个状态样式，和便捷的外部接口及调用方式
 * 使用：
 * 加载异常调用 {@link #showFailView(int, String)} 或者 showFailViewXXXX  展示异常页面
 * 加载成功调用 {@link #showOut()}            消除异常页面
 * 可选：
 * 加载中调用 {@link #showLoadingView()}      加载中动画
 */

public class LoadingLayoutEx extends LoadingLayout {

    public LoadingLayoutEx(Context context) {
        this(context, null);
    }

    public LoadingLayoutEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayoutEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs) {
        //无
    }

    private void initView() {
        setLoadingViewSrc(R.layout.layout_loading);
        setFailViewSrc(R.layout.layout_fail_common);
    }

    public void showFailView(int imgSrc, String text) {
        if (failViewSrc == 0) {
            Timber.e("没有设置lackViewSrc");
        }
        showin = LoadingViewHelper.showin(this, failViewSrc, showin);
        TextView text_notice = showin.findViewById(R.id.text_notice);
        if (text_notice != null) {
            text_notice.setCompoundDrawablesWithIntrinsicBounds(0, imgSrc, 0, 0);
            text_notice.setText(text);
        }
    }

    /////////////  加载错误  //////////////

    public void showFailViewNoNet() {
        showFailView(R.drawable.common_ic_default_nonet, getContext().getString(R.string.common_default_nonet));
    }

    public void showFailViewNoMsg() {
        showFailView(R.drawable.common_ic_default_nomsg, getContext().getString(R.string.common_default_nomsg));
    }

    public void showFailViewNoNotify() {
        showFailView(R.drawable.common_ic_default_nonotify, getContext().getString(R.string.common_default_nonotify));
    }

    public void showFailViewNoFriend() {
        showFailView(R.drawable.common_ic_default_nofriend, getContext().getString(R.string.common_default_nofriend));
    }

    public void showFailViewNoGroup() {
        showFailView(R.drawable.common_ic_default_nogroup, getContext().getString(R.string.common_default_nogroup));
    }

    public void showFailViewNoSupport() {
        showFailView(R.drawable.common_ic_default_nosupport, getContext().getString(R.string.common_default_nosupport));
    }

    public void showFailViewNoTopic() {
        showFailView(R.drawable.common_ic_default_notopic, getContext().getString(R.string.common_default_notopic));
    }

    public void showFailViewNoComment() {
        showFailView(R.drawable.common_ic_default_nocomment, getContext().getString(R.string.common_default_nocomment));
    }

    public void showFailViewNoPersonal() {
        showFailView(R.drawable.common_ic_default_nopersonal, getContext().getString(R.string.common_default_nopersonal));
    }

    public void showFailViewNoFunshow() {
        showFailView(R.drawable.common_ic_default_nofunshow, getContext().getString(R.string.common_default_nofunshow));
    }

    public void showFailViewNoSearch() {
        showFailView(R.drawable.common_ic_default_nosearch, getContext().getString(R.string.common_default_nosearch));
    }

    public void showFailViewNoFindChat() {
        showFailView(R.drawable.common_ic_default_nofriend, getContext().getString(R.string.common_default_nofindchat));
    }

    public void showFailViewNoAite() {
        showFailView(R.drawable.common_ic_default_nofriend, getContext().getString(R.string.common_default_noaite));
    }

    public void showFailViewNoMoneyTree() {
        showFailView(R.drawable.common_ic_default_nomoneytree, getContext().getString(R.string.common_default_nomoneytree));
    }

    public void showFailViewSearchFunpoint() {
        showFailView(R.drawable.common_ic_default_search_funpoint, getContext().getString(R.string.common_default_search_funpoint));
    }

    public void showFailViewSearchFunshow() {
        showFailView(R.drawable.common_ic_default_search_topic, getContext().getString(R.string.common_default_search_funshow));
    }

    public void showFailViewSearchTopic() {
        showFailView(R.drawable.common_ic_default_search_funpoint, getContext().getString(R.string.common_default_search_topic));
    }

    public void showFailViewSearchFunChat() {
        showFailView(R.drawable.common_ic_default_search_topic, getContext().getString(R.string.common_default_search_qu));
    }

    public void showFailViewSearchUser() {
        showFailView(R.drawable.common_ic_default_search_funpoint, getContext().getString(R.string.common_default_search_user));
    }

    //通用：没有数据 （UI没有标识的页面空数据状态用这个）
    public void showFailViewNoData() {
        showFailView(R.drawable.common_ic_default_nomsg, getContext().getString(R.string.common_default_nomoneytree));
    }

    public void showFailViewCustomize(int icon, String text) {
        showFailView(icon, text);
    }
}
