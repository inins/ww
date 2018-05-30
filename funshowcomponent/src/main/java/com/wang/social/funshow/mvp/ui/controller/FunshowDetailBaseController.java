package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;

import com.frame.component.ui.base.BaseController;
import com.wang.social.funshow.mvp.ui.activity.FunshowAddActivity;
import com.wang.social.funshow.mvp.ui.activity.FunshowDetailActivity;

//趣晒详情controller鸡肋。提供几个相互获取对象的方法
public abstract class FunshowDetailBaseController extends BaseController{

    private FunshowDetailContentBoardController contentBoardController;
    private FunshowDetailZanController zanController;
    private FunshowDetailEvaController evaController;
    private FunshowDetailEditController editController;

    public FunshowDetailBaseController(View root) {
        super(root);
    }

    protected FunshowDetailContentBoardController getContentBoardController() {
        if (getContext() instanceof FunshowDetailActivity) {
            return ((FunshowDetailActivity) getContext()).getContentBoardController();
        } else {
            return null;
        }
    }

    protected FunshowDetailZanController getZanController() {
        if (getContext() instanceof FunshowDetailActivity) {
            return ((FunshowDetailActivity) getContext()).getZanController();
        } else {
            return null;
        }
    }

    protected FunshowDetailEvaController getEvaController() {
        if (getContext() instanceof FunshowDetailActivity) {
            return ((FunshowDetailActivity) getContext()).getEvaController();
        } else {
            return null;
        }
    }

    protected FunshowDetailEditController getEditController() {
        if (getContext() instanceof FunshowDetailActivity) {
            return ((FunshowDetailActivity) getContext()).getEditController();
        } else {
            return null;
        }
    }
}
