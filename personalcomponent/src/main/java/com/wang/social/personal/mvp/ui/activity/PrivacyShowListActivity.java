package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.common.NetParam;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.ShowListCate;
import com.wang.social.personal.mvp.entities.ShowListGroup;
import com.wang.social.personal.mvp.entities.privates.PrivateDetail;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterPrivacyShowList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyShowListActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<ShowListCate> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.titleview)
    TitleView titleview;
    private RecycleAdapterPrivacyShowList adapter;

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;

    private int type;

    public static void start(Context context, int type) {
        Intent intent = new Intent(context, PrivacyShowListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_showlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 1:
                titleview.setTitle(getResources().getString(R.string.personal_privacy_showlist_title_friend));
                break;
            case 2:
                titleview.setTitle(getResources().getString(R.string.personal_privacy_showlist_title_ql));
                break;
            case 3:
                titleview.setTitle(getResources().getString(R.string.personal_privacy_showlist_title_qs));
                break;
            case 4:
                titleview.setTitle(getResources().getString(R.string.personal_privacy_showlist_title_topic));
                break;
        }

        adapter = new RecycleAdapterPrivacyShowList();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        //测试数据
        List<ShowListCate> results = new ArrayList<ShowListCate>() {{
            add(new ShowListCate("公开", "openAll", new ArrayList<ShowListGroup>() {{
//                add(new ShowListGroup("群组名称一"));
//                add(new ShowListGroup("群组名称二"));
//                add(new ShowListGroup("群组名称三"));
//                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("仅自己可见", "onlySelf", new ArrayList<ShowListGroup>() {{
//                add(new ShowListGroup("群组名称一"));
//                add(new ShowListGroup("群组名称二"));
//                add(new ShowListGroup("群组名称三"));
//                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("好友可见", "friend", new ArrayList<ShowListGroup>() {{
//                add(new ShowListGroup("群组名称一"));
//                add(new ShowListGroup("群组名称二"));
//                add(new ShowListGroup("群组名称三"));
//                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("趣聊/觅聊可见", "groupFriend", new ArrayList<ShowListGroup>() {{
//                add(new ShowListGroup("群组名称一"));
//                add(new ShowListGroup("群组名称二"));
//                add(new ShowListGroup("群组名称三"));
//                add(new ShowListGroup("群组名称四"));
            }}));
        }};
        adapter.refreshData(results);

        netGetDetail();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_right) {
            netCommit();
        }
    }

    @Override
    public void onItemClick(ShowListCate testEntity, int position) {

    }

    private void setData(PrivateDetail privateDetail) {
        if (privateDetail != null) {
            if (privateDetail.isOpenAll()) {
                adapter.getData().get(0).setSelect(true);
            } else if (privateDetail.isOnlySelf()) {
                adapter.getData().get(1).setSelect(true);
            } else if (privateDetail.isFriend()) {
                adapter.getData().get(2).setSelect(true);
            } else if (privateDetail.isGroupFriend()) {
                adapter.getData().get(3).setSelect(true);
            }
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    ////////////////////////

    private void netCommit() {
        Map<String, Object> param = NetParam.newInstance()
                .put(adapter.getParamMap())
                .put("type", type)
                .build();
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).updatePrivate(param),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastLong("修改成功");
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_PRIVATE_UPDATE));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    private void netGetDetail() {
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).privateDetail(type),
                new ErrorHandleSubscriber<BaseJson<PrivateDetail>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<PrivateDetail> basejson) {
                        PrivateDetail privateDetail = basejson.getData();
                        setData(privateDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
