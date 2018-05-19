package com.wang.social.home.mvp.ui.controller;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.ui.activity.CardActivity;

import butterknife.BindView;

public class HomeNaviboardController extends BaseController implements View.OnClickListener {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.btn_search)
    TextView btnSearch;
    @BindView(R2.id.btn_samekind)
    View btnSamekind;
    @BindView(R2.id.btn_circle)
    View btnCircle;
    @BindView(R2.id.btn_active)
    View btnActive;

    public HomeNaviboardController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_naviboard;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textTitle);
        btnSearch.setOnClickListener(this);
        btnSamekind.setOnClickListener(this);
        btnCircle.setOnClickListener(this);
        btnActive.setOnClickListener(this);
    }

    @Override
    protected void onInitData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search) {
            CommonHelper.HomeHelper.startSearchActivity(getContext());
        } else if (id == R.id.btn_samekind) {
            CardActivity.startUser(getContext());
        } else if (id == R.id.btn_circle) {
            CardActivity.startGroup(getContext());
        } else if (id == R.id.btn_active) {
            CommonHelper.GameHelper.startMoneyTreeFromSquare(getContext());
        }
    }
}
