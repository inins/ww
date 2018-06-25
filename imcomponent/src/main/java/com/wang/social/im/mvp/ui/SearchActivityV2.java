package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.utils.FocusUtil;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.controller.SearchFriendController;
import com.wang.social.im.mvp.controller.SearchGroupController;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivityV2 extends BasicAppNoDiActivity {


    @BindView(R2.id.edit_search)
    EditText editSearch;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;

    private SearchFriendController friendController;
    private SearchGroupController groupController;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivityV2.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_search_v2;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        //初始化控制器
        friendController = new SearchFriendController(this, findViewById(R.id.include_friend));
        groupController = new SearchGroupController(this, findViewById(R.id.include_group));

        //延迟0.1秒后弹出软键盘
        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(editSearch), 100);
    }

    private void search() {
        String key = editSearch.getText().toString();
        if (!TextUtils.isEmpty(key)) {
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(this);
        super.finish();
    }

    @OnClick(R2.id.btn_right)
    public void cancel() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        friendController.onDestory();
        groupController.onDestory();
    }
}
