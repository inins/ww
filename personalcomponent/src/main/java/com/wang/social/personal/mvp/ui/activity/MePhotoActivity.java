package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frame.component.helper.QiNiuManager;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.common.GridSpacingItemDecoration;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.helper.PhotoHelper;
import com.wang.social.personal.helper.PhotoHelperEx;
import com.wang.social.personal.mvp.entities.photo.Photo;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterMePhoto;
import com.wang.social.personal.mvp.ui.dialog.DialogSure;
import com.wang.social.personal.mvp.ui.view.TitleView;
import com.wang.social.personal.net.helper.NetPhotoHelper;
import com.wang.social.personal.utils.viewutils.ViewUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MePhotoActivity extends BasicAppActivity implements IView, PhotoHelper.OnPhotoCallback {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R2.id.recycler)
    RecyclerView recycler;

    @Inject
    NetPhotoHelper netPhotoHelper;
    @Inject
    QiNiuManager qiNiuManager;
    @Inject
    ImageLoader mImageLoader;

    private PhotoHelperEx photoHelperEx;
    private RecycleAdapterMePhoto adapter;
    private Photo modifyPhoto;

    private int MAXCOUNT = 3;//最多只能有三张

    public static void start(Context context) {
        Intent intent = new Intent(context, MePhotoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_mephoto;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        photoHelperEx = PhotoHelperEx.newInstance(this, this);
        adapter = new RecycleAdapterMePhoto(mImageLoader);
        adapter.setOnPhotoClickListener(onPhotoClickListener);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtils.dp2px(7), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        netGetPhotoList();
    }

    private void setTitleviewCount() {
        titleview.setSubTitle("(" + adapter.getResultsCount() + "/" + MAXCOUNT + ")");
    }

    private RecycleAdapterMePhoto.OnPhotoClickListener onPhotoClickListener = new RecycleAdapterMePhoto.OnPhotoClickListener() {
        @Override
        public void onAdd(View v) {
            if (adapter.getResultsCount() < 3) {
                modifyPhoto = null;
                photoHelperEx.showDefaultDialog();
            } else {
                ToastUtil.showToastLong("最多只能上传3张图片");
            }
        }

        @Override
        public void onDel(View v, Photo photo, int position) {
            DialogSure.showDialog(v.getContext(), "确定要删除该照片", () -> {
                netDelPhoto(photo.getUserPhotoId());
            });
        }

        @Override
        public void onModify(View v, Photo photo, int position) {
            modifyPhoto = photo;
            photoHelperEx.showDefaultDialog();
        }

        @Override
        public void onItemClick(View v, Photo photo, int position) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelperEx.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(String path) {
        qiNiuManager.uploadFile(this, path, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                if (modifyPhoto != null) {
                    netModifyPhoto(modifyPhoto.getUserPhotoId(), url);
                } else {
                    netAddPhoto(url);
                }
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("上传失败");
            }
        });
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

    //////////////  网络请求(photo的CRUD操作)  /////////////////

    private void netGetPhotoList() {
        netPhotoHelper.netGetPhotoList(this, new NetPhotoHelper.OnPhotoListApiCallBack() {
            @Override
            public void onSuccess(List<Photo> photoList) {
                adapter.refreshData(photoList);
                setTitleviewCount();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastShort(e.getMessage());
            }
        });
    }

    private void netAddPhoto(String url) {
        netPhotoHelper.netAddPhoto(this, url, new NetPhotoHelper.OnPhotoAddApiCallBack() {
            @Override
            public void onSuccess(Photo photo) {
                adapter.insertItem(adapter.getItemCount() - 1, photo);
                setTitleviewCount();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastLong(e.getMessage());
            }
        });
    }

    private void netDelPhoto(int userPhotoId) {
        netPhotoHelper.netDelPhoto(this, userPhotoId, new NetPhotoHelper.OnPhotoModifyApiCallBack() {
            @Override
            public void onSuccess() {
                adapter.removeItemById(userPhotoId);
                setTitleviewCount();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastLong(e.getMessage());
            }
        });
    }

    private void netModifyPhoto(int userPhotoId, String url) {
        netPhotoHelper.netModifyPhoto(this, userPhotoId, url, new NetPhotoHelper.OnPhotoModifyApiCallBack() {
            @Override
            public void onSuccess() {
                Photo photo = adapter.getData().get(adapter.getPositionById(userPhotoId));
                photo.setPhotoUrl(url);
                adapter.modifyItemById(userPhotoId);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastLong(e.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
