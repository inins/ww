package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;

import com.frame.component.ui.base.BaseController;
import com.wang.social.funshow.mvp.ui.activity.FunshowAddActivity;

//添加趣晒controller鸡肋。提供几个相互获取对象的方法
public abstract class FunshowAddBaseController extends BaseController{

    public FunshowAddBaseController(View root) {
        super(root);
    }

    protected FunshowAddEditController getEditController() {
        if (getContext() instanceof FunshowAddActivity) {
            return ((FunshowAddActivity) getContext()).getEditController();
        } else {
            return null;
        }
    }

    protected FunshowAddMusicBoardController getMusicBoardController() {
        if (getContext() instanceof FunshowAddActivity) {
            return ((FunshowAddActivity) getContext()).getMusicBoardController();
        } else {
            return null;
        }
    }

    protected FunshowAddBundleController getBundleController() {
        if (getContext() instanceof FunshowAddActivity) {
            return ((FunshowAddActivity) getContext()).getBundleController();
        } else {
            return null;
        }
    }

    protected FunshowAddBottomBarController getBottomBarController() {
        if (getContext() instanceof FunshowAddActivity) {
            return ((FunshowAddActivity) getContext()).getBottomBarController();
        } else {
            return null;
        }
    }

    protected FunshowAddTagController getTagController() {
        if (getContext() instanceof FunshowAddActivity) {
            return ((FunshowAddActivity) getContext()).getTagController();
        } else {
            return null;
        }
    }
}
