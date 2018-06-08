package com.wang.social.im.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.Autowired;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.mvp.model.SocialHomeModel;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.model.entities.SocialInfo;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 趣聊加入限制
 */
public class SocialLimitActivity extends BasicAppActivity implements IView {

    public static final String EXTRA_SOCIAL = "social";

    @BindView(R2.id.sl_toolbar)
    SocialToolbar slToolbar;
    @BindView(R2.id.sl_tv_title)
    TextView slTvTitle;
    @BindView(R2.id.sl_tv_limit_gender)
    TextView slTvLimitGender;
    @BindView(R2.id.sl_rb_gender_unlimited)
    RadioButton slRbGenderUnlimited;
    @BindView(R2.id.sl_rb_male)
    RadioButton slRbMale;
    @BindView(R2.id.sl_rb_female)
    RadioButton slRbFemale;
    @BindView(R2.id.sl_rg_gender)
    RadioGroup slRgGender;
    @BindView(R2.id.sl_tv_limit_age)
    TextView slTvLimitAge;
    @BindView(R2.id.sl_cb_age_unlimited)
    AppCompatCheckBox slCbAgeUnlimited;
    @BindView(R2.id.sl_cb_90)
    AppCompatCheckBox slCb90;
    @BindView(R2.id.sl_cb_95)
    AppCompatCheckBox slCb95;
    @BindView(R2.id.sl_cb_00)
    AppCompatCheckBox slCb00;
    @BindView(R2.id.sl_cb_other)
    AppCompatCheckBox slCbOther;

    @Autowired
    SocialInfo social;

    @Inject
    ApiHelper mApiHelper;
    @Inject
    IRepositoryManager mRepositoryManager;

    public static void start(Activity activity, SocialInfo social, int requestCode) {
        Intent intent = new Intent(activity, SocialLimitActivity.class);
        intent.putExtra(EXTRA_SOCIAL, social);
        activity.startActivityForResult(intent, requestCode);
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
        return R.layout.im_ac_social_limit;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        switch (social.getAttr().getGenderLimit()) {
            case UNLIMITED:
                slRbGenderUnlimited.setChecked(true);
                break;
            case MALE:
                slRbMale.setChecked(true);
                break;
            case FEMALE:
                slRbFemale.setChecked(true);
                break;
        }
        for (SocialAttribute.AgeLimit ageLimit : social.getAttr().getAgeLimit()) {
            switch (ageLimit) {
                case UNLIMITED:
                    slCbAgeUnlimited.setChecked(true);
                    break;
                case AFTER_90:
                    slCb90.setChecked(true);
                    break;
                case AFTER_95:
                    slCb95.setChecked(true);
                    break;
                case AFTER_00:
                    slCb00.setChecked(true);
                    break;
                case OTHER:
                    slCbOther.setChecked(true);
                    break;
            }
        }
    }

    private void init() {
        slToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        updateChargeStatus();
                        break;
                }
            }
        });

        slRgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        slCbAgeUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slCb90.setChecked(false);
                    slCb90.setChecked(false);
                    slCb00.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.UNLIMITED);
                }
            }
        });

        slCb90.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_90);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_90);
                }
            }
        });

        slCb95.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_95);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_95);
                }
            }
        });

        slCb00.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_00);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_00);
                }
            }
        });

        slCbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    slCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.OTHER);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.OTHER);
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void removeAgeLimit(SocialAttribute.AgeLimit ageLimit) {
        for (SocialAttribute.AgeLimit limit : social.getAttr().getAgeLimit()) {
            if (ageLimit == limit) {
                social.getAttr().getAgeLimit().remove(limit);
                break;
            }
        }
    }

    private void updateChargeStatus() {
        if (slRgGender.getCheckedRadioButtonId() == R.id.sl_rb_gender_unlimited) {
            social.getAttr().setGenderLimit(SocialAttribute.GenderLimit.UNLIMITED);
        } else if (slRgGender.getCheckedRadioButtonId() == R.id.sl_rb_male) {
            social.getAttr().setGenderLimit(SocialAttribute.GenderLimit.MALE);
        } else if (slRgGender.getCheckedRadioButtonId() == R.id.sl_rb_female) {
            social.getAttr().setGenderLimit(SocialAttribute.GenderLimit.FEMALE);
        }
        mApiHelper.executeNone(this, new SocialHomeModel(mRepositoryManager)
                .updateSocialInfo(social), new ErrorHandleSubscriber<BaseJson>() {
            @Override
            public void onNext(BaseJson baseJson) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SOCIAL, social);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                showLoadingDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                dismissLoadingDialog();
            }
        });
    }
}
