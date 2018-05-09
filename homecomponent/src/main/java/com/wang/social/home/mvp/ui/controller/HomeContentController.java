package com.wang.social.home.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.contract.HomeContract;
import com.wang.social.home.mvp.entities.FunpointAndTopic;
import com.wang.social.home.mvp.entities.Topic;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterHome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeContentController extends BaseController implements BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterHome adapter;

    public HomeContentController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_content;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textTitle);

        adapter = new RecycleAdapterHome();
//        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new ItemDecorationDivider(getContext(), LinearLayoutManager.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onInitData() {
//        adapter.refreshData(new ArrayList<Funpoint>() {{
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//            add(new Funpoint());
//        }});
        netGetFunpointAndTopicList(true);
    }

    @Override
    public void onItemClick(Funpoint funpoint, int position) {
    }

    ////////////////////////////////////////////////

    public void refreshData() {
        netGetFunpointAndTopicList(true);
    }

    public void loadmoreData() {
        netGetFunpointAndTopicList(false);
    }

    ////////////////////////////////////////////////

    private int current = 1;
    private int size = 10;

    public void netGetFunpointAndTopicList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getFunpointAndTopic(0, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<JsonObject>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<JsonObject>> basejson) {
                        BaseListWrap<JsonObject> warp = basejson.getData();
                        List<JsonObject> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            //封装实体
                            List<FunpointAndTopic> ftList = new ArrayList<>();
                            for (JsonObject jsonObject : list) {
                                if (jsonObject.has("newsId")) {
                                    ftList.add(new FunpointAndTopic(new Gson().fromJson(jsonObject, Funpoint.class)));
                                } else if (jsonObject.has("topicId")) {
                                    ftList.add(new FunpointAndTopic(new Gson().fromJson(jsonObject, Topic.class)));
                                }
                            }
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(ftList);
                            } else {
                                adapter.addItem(ftList);
                            }
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                        if (getIView()!=null && getIView() instanceof HomeContract.View){
                            ((HomeContract.View) getIView()).finishSpringView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        if (getIView()!=null && getIView() instanceof HomeContract.View){
                            ((HomeContract.View) getIView()).finishSpringView();
                        }
                    }
                });
    }
}
