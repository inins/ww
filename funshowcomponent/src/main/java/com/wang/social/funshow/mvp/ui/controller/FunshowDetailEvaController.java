package com.wang.social.funshow.mvp.ui.controller;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BaseController;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.entities.user.ZanUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterEva;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FunshowDetailEvaController extends BaseController {

    int layout = R.layout.funshow_lay_detail_eva;

    @BindView(R2.id.recycler_eva)
    RecyclerView recyclerEva;
    SpringView springView;

    private RecycleAdapterEva adapterEva;

    private int talkId;

    public FunshowDetailEvaController(View root, SpringView springView, int talkId) {
        super(root);
        this.springView = springView;
        this.talkId = talkId;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        adapterEva = new RecycleAdapterEva();
        recyclerEva.setNestedScrollingEnabled(false);
        recyclerEva.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerEva.setAdapter(adapterEva);
        recyclerEva.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> springView.onFinishFreshAndLoadDelay(), 1000);
            }

            @Override
            public void onLoadmore() {
                netLoadEvaList();
            }
        });
        springView.callFreshDelay();
        netLoadEvaList();
    }

    @Override
    protected void onInitData() {
    }


    //////////////////////分页查询////////////////////
    private int current = 0;
    private int size = 20;

    public void netLoadEvaList() {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(FunshowService.class).funshowEvaList(talkId, ++current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Comment>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Comment>> basejson) {
                        BaseListWrap<Comment> warp = basejson.getData();
                        List<Comment> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            adapterEva.addItem(list);
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, null, () -> {
                    springView.onFinishFreshAndLoadDelay();
                });
    }
}
