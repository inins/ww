package com.wang.social.im.mvp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.pictureselector.PictureSelector;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 二维码扫描
 */
@RouteNode(path = "/scan", desc = "二维码扫描页面")
public class ScanActivity extends BasicAppActivity implements QRCodeView.Delegate {

    private static final int REQUEST_CODE_ALBUM = 100;

    @BindView(R2.id.sc_zing)
    ZXingView scZing;
    @BindView(R2.id.sc_toolbar)
    SocialToolbar scToolbar;
    @BindView(R2.id.sc_ll_mask)
    LinearLayout scLlMask;

    private Disposable disposable;
    private boolean resumed;

    @SuppressLint("CheckResult")
    public static void start(Activity context) {
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_scan;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        scZing.showScanRect();
        scZing.setDelegate(this);

        scZing.startCamera();
        scZing.startSpot();

        init();
    }

    private void init() {
        scToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        finish();
                        break;
                    case RIGHT_TEXT:
                        PictureSelector.from(ScanActivity.this)
                                .maxSelection(1)
                                .isClip(false)
                                .spanCount(3)
                                .forResult(REQUEST_CODE_ALBUM);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (scZing != null) {
            scZing.onDestroy();
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result.contains("userId")) {
            String userId = result.substring(result.indexOf("userId=") + 7);
            if (userId.contains("&")) {
                userId = userId.substring(0, userId.indexOf("&"));
            }
            PersonalCardActivity.start(this, Integer.valueOf(userId));
            finish();
        } else if (result.contains("groupId")) {
            String groupId = result.substring(result.indexOf("groupId=") + 8);
            if (groupId.contains("&")) {
                groupId = groupId.substring(0, groupId.indexOf("&"));
            }
            GroupInviteDetailActivity.startForBrowse(this, Integer.valueOf(groupId));
            finish();
        } else {
            if (scZing != null) {
                scZing.stopSpot();
                scLlMask.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        if (resumed) {
            ToastUtil.showToastShort("请在设置中打开相机权限");
        }
    }

    @OnClick(R2.id.sc_ll_mask)
    public void onViewClicked() {
        scZing.startSpot();
        scLlMask.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ALBUM) {
                String[] path = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
                if (path != null && path.length > 0) {
                    resultCode(path[0]);
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void resultCode(String path) {
        Observable.just(path)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        ScanActivity.this.disposable = disposable;
                        showLoadingDialog();
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return QRCodeDecoder.syncDecodeQRCode(path);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingDialog();
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        onScanQRCodeSuccess(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        scLlMask.setVisibility(View.VISIBLE);
                    }
                });
    }
}
