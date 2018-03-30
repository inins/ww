package com.frame.component.ui.acticity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.component.service.R;
import com.frame.di.component.AppComponent;

import butterknife.BindView;

public class WebActivity extends BasicActivity {

    WebView webView;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView text_title;
    TextView btn_right;

    private String url;
    private String title;

    public static void start(Context context) {
        start(context, "https://www.bing.com");
    }

    public static void start(Context context, String url) {
        start(context, "", url);
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_web;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initBase();
        initView();
        initWebView();
        setToolbar(title);
        webView.loadUrl(url);
    }

    private void initBase() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
    }

    private void initView() {
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress);
        toolbar = findViewById(R.id.toolbar);
        text_title = findViewById(R.id.text_toolbar_title);
        btn_right = findViewById(R.id.btn_right);
    }

    public void setToolbar(String title) {
        //设置toobar文字图标和返回事件
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //设置toobar居中文字
        if (text_title != null && !TextUtils.isEmpty(title)) {
            text_title.setText(title);
        }
    }

    private void initWebView() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setAllowFileAccess(true);
        setting.setAllowFileAccessFromFileURLs(true);
        setting.setAllowUniversalAccessFromFileURLs(true);
        setting.setAppCacheEnabled(true);
        setting.setDatabaseEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setting.setAllowFileAccessFromFileURLs(true);
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    protected TextView getRightBtn() {
        return btn_right;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
