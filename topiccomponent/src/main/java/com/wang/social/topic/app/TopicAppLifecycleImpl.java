package com.wang.social.topic.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.frame.base.delegate.AppDelegate;
import com.frame.component.helper.AppDataHelper;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 16:55
 * ========================================
 */

public class TopicAppLifecycleImpl implements AppDelegate {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {
        Log.e("notice", "这里进行初始化工作");

        AppDataHelper.saveToken("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzgyMzE1MDQyMCIsImNyZWF0ZWQiOjE1MjM5MzE5NTkxMDMsImlkIjoxMDAyNSwiZXhwIjoxNTI0NTM2NzU5fQ.bWW8YlE0u_1vBvnHw4AtO4QIZXkvrxTocPKZ1Z0-sS7sABvYqUVPbTULDOllkZxiVFH1Q9ge4GR6p6HBf6yeqg");
    }

    @Override
    public void onTerminate(Application application) {

    }
}
