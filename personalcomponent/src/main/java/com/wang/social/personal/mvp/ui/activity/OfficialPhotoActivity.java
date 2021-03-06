package com.wang.social.personal.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.frame.base.BaseAdapter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.GridSpacingItemDecoration;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.component.entities.BaseListWrap;
import com.wang.social.personal.mvp.entities.photo.OffiPic;
import com.frame.component.entities.photo.Photo;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterOfficialPhoto;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

@RouteNode(path = "/officialPhoto", desc = "官方图库")
public class OfficialPhotoActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<OffiPic> {

    public static final String NAME_URL = "url";

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ImageLoader mImageLoader;

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private RecycleAdapterOfficialPhoto adapter;

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, OfficialPhotoActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_officialphoto;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterOfficialPhoto(mImageLoader);
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtils.dp2px(7), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        netGetPhotoList();
    }

    @Override
    public void onItemClick(OffiPic offiPic, int position) {
        Intent intent = new Intent();
        intent.putExtra(NAME_URL, offiPic.getPicUrl());
        setResult(RESULT_OK, intent);
        finish();
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

    //////////////  网络请求  /////////////////

    private void netGetPhotoList() {
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).getOfficialPhotoList(1),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<OffiPic>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<OffiPic>> baseJson) {
                        BaseListWrap<OffiPic> wrap = baseJson.getData();
                        if (wrap != null) {
                            List<OffiPic> offiPicList = wrap.getList();
                            adapter.refreshData(offiPicList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
