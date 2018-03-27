package com.wang.social.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.component.router.Router;
import com.frame.component.service.personal.PersonalService;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.R;

@RouteNode(path = "/main", desc = "首页")
public class MainActivity extends BasicActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();;
        if (fragment != null){
            transaction.remove(fragment).commit();
            fragment = null;
        }
        Router router = Router.getInstance();
        PersonalService personalService = (PersonalService) router.getService(PersonalService.class.getName());
        if (personalService != null){
             fragment = personalService.getPersonalFragment();
             transaction.add(R.id.fragment, fragment, fragment.getClass().getName());
             transaction.commitAllowingStateLoss();
        }
    }
}
