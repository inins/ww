package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.ListUtil;
import com.frame.di.component.AppComponent;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.SimpleTextWatcher;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.frame.component.helper.AppValiHelper;
import com.frame.http.api.ApiHelperEx;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.personal.helper.PicPhotoHelperEx;
import com.wang.social.personal.mvp.model.api.UserService;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FeedbackActivity extends BasicAppActivity implements PhotoHelper.OnPhotoCallback, IView {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.bundleview)
    BundleImgView bundleview;
    @BindView(R2.id.edit_suggest)
    EditText editSuggest;
    @BindView(R2.id.text_count)
    TextView textCount;
    @BindView(R2.id.edit_phone)
    EditText editPhone;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    QiNiuManager qiNiuManager;
    PicPhotoHelperEx photoHelperEx;

    private int MaxPhotoCount = 3;

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_feedback;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        bundleview.setMaxcount(MaxPhotoCount);
        photoHelperEx = PicPhotoHelperEx.newInstance(this, this);
        photoHelperEx.setClip(false);
        bundleview.setOnBundleLoadImgListener(new BundleImgView.OnBundleLoadImgListener() {
            @Override
            public void onloadImg(ImageView imageView, String imgurl, int defaultSrc) {
                mImageLoader.loadImage(FeedbackActivity.this, ImageConfigImpl.builder()
                        .imageView(imageView)
                        .url(imgurl)
                        .build());
            }
        });
        bundleview.setOnBundleClickListener(new BundleImgView.OnBundleSimpleClickListener() {
            @Override
            public void onPhotoAddClick(View v) {
                int count = MaxPhotoCount - bundleview.getResultCount();
                photoHelperEx.setMaxSelectCount(count);
                photoHelperEx.showDefaultDialog();
            }
        });
        editSuggest.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                textCount.setText(text.length() + "/500");
            }
        });

        //如果用户已有手机号自动填充

        User user = AppDataHelper.getUser();
        if (user != null && !TextUtils.isEmpty(user.getPhone())) {
            editPhone.setText(user.getPhone());
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            int picCount = bundleview.getResultCount();
            String suggest = editSuggest.getText().toString();
            String phone = editPhone.getText().toString();
            String msg = AppValiHelper.feedback(phone, suggest, picCount);
            if (msg != null) {
                ToastUtil.showToastLong(msg);
            } else {
                netUploadCommit(phone, suggest);
            }

        } else if (i == R.id.btn_question) {
            WebActivity.start(this, AppConstant.Url.proposal);
        }
    }

    @Override
    public void onResult(String path) {
        String[] pathArray = PhotoHelper.format2Array(path);
        if (StrUtil.isEmpty(pathArray)) return;
        List<BundleImgEntity> pathList = new ArrayList<>();
        for (String str : pathArray) {
            pathList.add(new BundleImgEntity(str));
        }
        bundleview.addPhotos(pathList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelperEx.onActivityResult(requestCode, resultCode, data);
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

    //////////////  网络请求 /////////////////

    //上传图片
    private void netUploadCommit(String phone, String content) {
        List<String> paths = bundleview.getResultsStrArray();
        qiNiuManager.uploadFiles(this, ListUtil.transArrayList(paths), new QiNiuManager.OnBatchUploadListener() {
            @Override
            public void onSuccess(ArrayList<String> urls) {
                netGetPhotoList(phone, content, StrUtil.getStrDotByList(urls));
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("上传失败");
            }
        });
    }

    //提交内容
    private void netGetPhotoList(String phone, String content, String pictures) {
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).feedback(phone, content, pictures),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastLong("感谢您的建议");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
