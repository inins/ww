package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.app.Constant;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.PageList;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.Autowired;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.mvp.model.api.GroupService;
import com.frame.component.entities.ShareUserInfo;
import com.wang.social.im.mvp.model.api.ShareWoodService;
import com.wang.social.im.mvp.ui.adapters.ShareListAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 快活林分享排行
 */
public class ShareListActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.sl_list)
    RecyclerView slList;
    @BindView(R2.id.sl_loader)
    SpringView slLoader;

    @Inject
    ApiHelper mApiHelper;
    @Inject
    IRepositoryManager mRepositoryManager;

    @Autowired
    String objectId;
    @Autowired
    int shareType;

    private int mCurrentPage = 0;
    private String mType;
    private ShareListAdapter mAdapter;

    public static void start(Context context, int shareType, String objectId) {
        Intent intent = new Intent(context, ShareListActivity.class);
        intent.putExtra("shareType", shareType);
        intent.putExtra("objectId", objectId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_share_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        switch (shareType) {
            case Constant.SHARE_WOOD_TYPE_GROUP:
                mType = GroupService.SHARE_WOOD_GROUP;
                break;
            case Constant.SHARE_WOOD_TYPE_TALK:
                mType = GroupService.SHARE_WOOD_TALK;
                break;
            case Constant.SHARE_WOOD_TYPE_TOPIC:
                mType = GroupService.SHARE_WOOD_TOPIC;
                break;
        }
        getData();
    }

    private void init() {
        slLoader.setEnableHeader(false);
        slLoader.setFooter(new AliFooter(this, false));

        slList.setLayoutManager(new LinearLayoutManager(this));
        slList.addItemDecoration(new HVItemDecoration(this, HVItemDecoration.LINEAR_DIVIDER_VERTICAL));
        mAdapter = new ShareListAdapter();
        slList.setAdapter(mAdapter);

        slLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadmore() {
                getData();
            }
        });
    }

    private void getData() {
        mCurrentPage += 1;
        mApiHelper.execute(this, mRepositoryManager
                        .obtainRetrofitService(ShareWoodService.class)
                        .getShareList("2.0.0", mType, objectId, mCurrentPage, 20),
                new ErrorHandleSubscriber<PageList<ShareUserInfo>>() {
                    @Override
                    public void onNext(PageList<ShareUserInfo> shareUserInfoPageList) {
                        slLoader.onFinishFreshAndLoad();
                        if (shareUserInfoPageList.getCurrent() == 1) {
                            mAdapter.refreshData(shareUserInfoPageList.getList());
                        } else {
                            mAdapter.addItem(shareUserInfoPageList.getList());
                        }
                        if (shareUserInfoPageList.getCurrent() >= shareUserInfoPageList.getMaxPage()) {
                            slLoader.setEnableFooter(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        slLoader.onFinishFreshAndLoad();
                        mCurrentPage -= 1;
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoadingDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
