package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.frame.component.common.AppConstant;
import com.frame.component.common.InputLengthFilter;
import com.frame.component.enums.Gender;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.PayDialog;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerShadowSettingComponent;
import com.wang.social.im.di.modules.ShadowSettingModule;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.presenter.ShadowSettingPresenter;
import com.wang.social.im.widget.RulesDialog;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.pictureselector.helper.PhotoHelperEx;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分身设置
 */
public class ShadowSettingActivity extends BaseAppActivity<ShadowSettingPresenter> implements ShadowSettingContract.View, PhotoHelper.OnPhotoCallback {

    public static final String EXTRA_INFO = "shadow_info";

    @BindView(R2.id.ss_toolbar)
    SocialToolbar ssToolbar;
    @BindView(R2.id.ss_iv_portrait)
    ImageView ssIvPortrait;
    @BindView(R2.id.ss_et_nickname)
    EditText ssEtNickname;
    @BindView(R2.id.ss_rg_gender)
    RadioGroup ssRgGender;

    ShadowInfo shadowInfo;

    private PhotoHelperEx mPhotoHelper;

    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context, @NonNull ShadowInfo shadowInfo) {
        Intent intent = new Intent(context, ShadowSettingActivity.class);
        intent.putExtra(EXTRA_INFO, shadowInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoHelper = null;
        mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerShadowSettingComponent
                .builder()
                .appComponent(appComponent)
                .shadowSettingModule(new ShadowSettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_shadow_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        shadowInfo = getIntent().getExtras().getParcelable(EXTRA_INFO);

        if (!TextUtils.isEmpty(shadowInfo.getNickname())) {
            ssEtNickname.setText(shadowInfo.getNickname());
        }
        if (!TextUtils.isEmpty(shadowInfo.getPortrait())) {
            mImageLoader.loadImage(this, ImageConfigImpl
                    .builder()
                    .errorPic(R.drawable.im_round_image_placeholder)
                    .placeholder(R.drawable.im_round_image_placeholder)
                    .imageView(ssIvPortrait)
                    .url(shadowInfo.getPortrait())
                    .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                    .build());
        } else {
            ssIvPortrait.setImageResource(R.drawable.im_round_image_placeholder);
        }
        if (shadowInfo.getGender() != null) {
            switch (shadowInfo.getGender()) {
                case MALE:
                    ssRgGender.check(R.id.ss_rb_male);
                    break;
                case FEMALE:
                    ssRgGender.check(R.id.ss_rb_female);
                    break;
                case SECRET:
                    ssRgGender.check(R.id.ss_rb_secret);
                    break;
            }
        }
    }

    private void init() {
        ssEtNickname.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!RegexUtils.isUsernameMe(source)) {
                    return "";
                }
                return source;
            }
        }, new InputLengthFilter(12, false)});

        ssToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @OnClick({R2.id.ss_iv_portrait, R2.id.ss_tv_sure, R2.id.ss_tv_shadow})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ss_iv_portrait) {
            mPhotoHelper = PhotoHelperEx.newInstance(this, this);
            mPhotoHelper.showDefaultDialog();
        } else if (view.getId() == R.id.ss_tv_sure) {
            String nickname = ssEtNickname.getText().toString();
            int genderCheckId = ssRgGender.getCheckedRadioButtonId();
            if (TextUtils.isEmpty(nickname)) {
                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_nickname));
                return;
            }
            shadowInfo.setNickname(nickname);
            if (TextUtils.isEmpty(shadowInfo.getPortrait())) {
                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_portrait));
                return;
            }
            if (genderCheckId == R.id.ss_rb_female) {
                shadowInfo.setGender(Gender.FEMALE);

            } else if (genderCheckId == R.id.ss_rb_male) {
                shadowInfo.setGender(Gender.MALE);

            } else if (genderCheckId == R.id.ss_rb_secret) {
                shadowInfo.setGender(Gender.SECRET);

            } else {
                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_gender));
                return;
            }
            mPresenter.updateShadowInfo(shadowInfo);
        } else if (view.getId() == R.id.ss_tv_shadow) {
            new RulesDialog(this, getString(R.string.im_shadow_rules_title), AppConstant.Url.REFERRAL_SHADOW).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoHelper != null) {
            mPhotoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResult(String path) {
        shadowInfo.setPortrait(path);
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .errorPic(R.drawable.im_round_image_placeholder)
                .placeholder(R.drawable.im_round_image_placeholder)
                .imageView(ssIvPortrait)
                .url(path)
                .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .build());
    }

    @Override
    public void showPayDialog(String applyId, ShadowInfo info) {
        String message = UIUtil.getString(R.string.im_team_pay_message, IMConstants.CREATE_GROUP_PRICE);
        PayDialog payDialog = new PayDialog(this, new PayDialog.OnPayListener() {
            @Override
            public void onPay() {
                mPresenter.payForUpdate(applyId, info);
            }
        }, message, String.valueOf(IMConstants.CREATE_GROUP_PRICE));
        payDialog.show();
    }

    @Override
    public void onUpdateComplete(ShadowInfo info) {
        info.setStatus(ShadowInfo.STATUS_OPEN);
        EventBus.getDefault().post(info);
        finish();
    }
}