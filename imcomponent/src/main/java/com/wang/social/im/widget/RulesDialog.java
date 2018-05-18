package com.wang.social.im.widget;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.frame.utils.ScreenUtils;
import com.wang.social.im.R;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-17 17:48
 * ============================================
 */
public class RulesDialog extends BaseDialog {

    private TextView tvTitle;
    private WebView webView;

    private String mTitle, mUrl;

    public RulesDialog(Context context, String title, String url) {
        super(context);
        this.mTitle = title;
        this.mUrl = url;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);

        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        tvTitle.setText(mTitle);
        webView.loadUrl(mUrl);

        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getView() {
        return R.layout.im_dialog_rules;
    }

    @Override
    protected void intView(View root) {
        tvTitle = root.findViewById(R.id.dr_tv_title);
        webView = root.findViewById(R.id.dr_web);

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

        root.findViewById(R.id.dr_tvb_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}