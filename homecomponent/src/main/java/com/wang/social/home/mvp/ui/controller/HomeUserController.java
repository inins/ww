package com.wang.social.home.mvp.ui.controller;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.ListUtil;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.helper.CatchHelper;
import com.wang.social.home.mvp.entities.funshow.FunshowHome;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterHomeUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeUserController extends BaseController {

    @BindView(R2.id.recycler_user)
    RecyclerView recycler;
    @BindView(R2.id.img_user_show)
    ImageView imgUserShow;
    @BindView(R2.id.img_user_learn)
    ImageView imgUserLearn;
    @BindView(R2.id.img_user_new)
    ImageView imgUserNew;
    private RecycleAdapterHomeUser adapter;

    public HomeUserController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_user;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        adapter = new RecycleAdapterHomeUser();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onInitData() {
        //加载缓存
//        adapter.refreshData(CatchHelper.getFunshowHome());
//        netGetUsers(false);

        adapter.refreshData(new ArrayList<TestEntity>(){{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }



//    public void netGetUsers(boolean needLoading) {
//        ApiHelperEx.execute(getIView(), needLoading,
//                ApiHelperEx.getService(HomeService.class).getNewFunshow(),
//                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHome>>>() {
//                    @Override
//                    public void onNext(BaseJson<BaseListWrap<FunshowHome>> basejson) {
//                        BaseListWrap<FunshowHome> wrap = basejson.getData();
//                        List<FunshowHome> list = wrap.getList();
//                        if (!StrUtil.isEmpty(list)) {
//                            List<FunshowBean> funshowBeans = FunshowHome.tans2FunshowBeanList(list);
//                            //只加载前5条
//                            funshowBeans = ListUtil.getFirst(funshowBeans, 5);
//                            //加入缓存
//                            CatchHelper.saveFunshowHome(funshowBeans);
//                            List<FunshowBean> testList = CatchHelper.getFunshowHome();
//                            adapter.refreshData(funshowBeans);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showToastLong(e.getMessage());
//                    }
//                }, () -> {
//                    if (needLoading) getLoadingLayout().showLoadingView();
//                }, () -> {
//                    if (needLoading) getLoadingLayout().showOut();
//                });
//    }
}
