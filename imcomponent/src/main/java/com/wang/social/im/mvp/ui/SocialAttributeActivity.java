package com.wang.social.im.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.common.InputPositiveIntegerFilter;
import com.wang.social.im.mvp.model.entities.SocialAttribute;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 趣聊属性设置
 */
public class SocialAttributeActivity extends BasicAppActivity {

    public static final String EXTRA_ATTR = "attr";

    @BindView(R2.id.sa_toolbar)
    SocialToolbar saToolbar;
    @BindView(R2.id.sa_iv_public)
    ImageView saIvPublic;
    @BindView(R2.id.sa_tv_public)
    TextView saTvPublic;
    @BindView(R2.id.sa_tv_public_tip)
    TextView saTvPublicTip;
    @BindView(R2.id.sa_iv_private)
    ImageView saIvPrivate;
    @BindView(R2.id.sa_tv_private)
    TextView saTvPrivate;
    @BindView(R2.id.sa_tv_private_tip)
    TextView saTvPrivateTip;
    @BindView(R2.id.sa_rg_join)
    RadioGroup saRgJoin;
    @BindView(R2.id.sa_rg_gem)
    RadioGroup saRgGem;
    @BindView(R2.id.sa_tv_custom_gem)
    TextView saTvCustomGem;
    @BindView(R2.id.sa_et_gem)
    EditText saEtGem;
    @BindView(R2.id.sa_tv_gem)
    TextView saTvGem;
    @BindView(R2.id.sa_rg_gender)
    RadioGroup saRgGender;
    @BindView(R2.id.sa_rb_gender_unlimited)
    RadioButton saRbGenderUnlimited;
    @BindView(R2.id.sa_rb_male)
    RadioButton saRbMale;
    @BindView(R2.id.sa_rb_female)
    RadioButton saRbFemale;
    @BindView(R2.id.sa_rb_gem_100)
    RadioButton saRbGem100;
    @BindView(R2.id.sa_rb_gem_300)
    RadioButton saRbGem300;
    @BindView(R2.id.sa_rb_gem_500)
    RadioButton saRbGem500;
    @BindView(R2.id.sa_cb_age_unlimited)
    AppCompatCheckBox saCbAgeUnlimited;
    @BindView(R2.id.sa_cb_90)
    AppCompatCheckBox saCb90;
    @BindView(R2.id.sa_cb_95)
    AppCompatCheckBox saCb95;
    @BindView(R2.id.sa_cb_00)
    AppCompatCheckBox saCb00;
    @BindView(R2.id.sa_cb_age_other)
    AppCompatCheckBox saCbAgeOther;

    private SocialAttribute mAttribute;

    private boolean mFromTextChange;

    public static void start(Activity activity, int requestCode, SocialAttribute attribute) {
        Intent intent = new Intent(activity, SocialAttributeActivity.class);
        intent.putExtra(EXTRA_ATTR, attribute);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_attribute;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mAttribute = getIntent().getParcelableExtra(EXTRA_ATTR);
        if (mAttribute == null) {
            mAttribute = new SocialAttribute();

            mAttribute.setOpen(true);
            mAttribute.setGenderLimit(SocialAttribute.GenderLimit.UNLIMITED);
            mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);
        }
        toggleOpen();

        if (mAttribute.isCharge()) {
            saRgJoin.check(R.id.sa_rb_pay_join);

            if (mAttribute.getGem() == 100) {
                saRbGem100.setChecked(true);
            } else if (mAttribute.getGem() == 300) {
                saRbGem300.setChecked(true);
            } else if (mAttribute.getGem() == 500) {
                saRbGem500.setChecked(true);
            } else {
                saEtGem.setText(String.valueOf(mAttribute.getGem()));
            }
        } else {
            saRgJoin.check(R.id.sa_rb_join_free);
        }

        switch (mAttribute.getGenderLimit()) {
            case UNLIMITED:
                saRbGenderUnlimited.setChecked(true);
                break;
            case MALE:
                saRbMale.setChecked(true);
                break;
            case FEMALE:
                saRbFemale.setChecked(true);
                break;
        }
        for (SocialAttribute.AgeLimit ageLimit : mAttribute.getAgeLimit()) {
            switch (ageLimit) {
                case UNLIMITED:
                    saCbAgeUnlimited.setChecked(true);
                    break;
                case AFTER_90:
                    saCb90.setChecked(true);
                    break;
                case AFTER_95:
                    saCb95.setChecked(true);
                    break;
                case AFTER_00:
                    saCb00.setChecked(true);
                    break;
                case OTHER:
                    saCbAgeOther.setChecked(true);
                    break;
            }
        }
        toggleGemInput();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        saEtGem.setFilters(new InputFilter[]{new InputPositiveIntegerFilter()});

        saToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        if (mAttribute.isCharge()) {
                            if (mAttribute.getGem() > IMConstants.SOCIAL_CHARGE_LIMIT_MAX || mAttribute.getGem() < IMConstants.SOCIAL_CHARGE_LIMIT_MIN) {
                                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_gem_limit_tip));
                                return;
                            }
                        }
                        if (mAttribute.getAgeLimit().size() == 0) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_age_limit));
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_ATTR, mAttribute);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });

        saRgJoin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sa_rb_join_free) {
                    mAttribute.setCharge(false);

                } else if (checkedId == R.id.sa_rb_pay_join) {
                    mAttribute.setCharge(true);

                }
                toggleGemInput();
            }
        });

        saRgGem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sa_rb_gem_100) {
                    mAttribute.setGem(100);

                } else if (checkedId == R.id.sa_rb_gem_300) {
                    mAttribute.setGem(300);

                } else if (checkedId == R.id.sa_rb_gem_500) {
                    mAttribute.setGem(500);

                }
                if (!mFromTextChange) {
                    saEtGem.setText("");
                }
                mFromTextChange = false;
            }
        });

        saEtGem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    saRgGem.clearCheck();
                    if (TextUtils.isEmpty(saEtGem.getText().toString())) {
                        mAttribute.setGem(0);
                    }
                }
                return false;
            }
        });

        saEtGem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mFromTextChange = true;
                    saRgGem.clearCheck();
                    mAttribute.setGem(Integer.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saRgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sa_rb_gender_unlimited) {
                    mAttribute.setGenderLimit(SocialAttribute.GenderLimit.UNLIMITED);

                } else if (checkedId == R.id.sa_rb_male) {
                    mAttribute.setGenderLimit(SocialAttribute.GenderLimit.MALE);

                } else if (checkedId == R.id.sa_rb_female) {
                    mAttribute.setGenderLimit(SocialAttribute.GenderLimit.FEMALE);

                }
            }
        });

        saCbAgeUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saCb95.setChecked(false);
                    saCb90.setChecked(false);
                    saCb00.setChecked(false);
                    saCbAgeOther.setChecked(false);

                    mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.UNLIMITED);
                }
            }
        });

        saCb90.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saCbAgeUnlimited.setChecked(false);

                    mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_90);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_90);
                }
            }
        });

        saCb95.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saCbAgeUnlimited.setChecked(false);

                    mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_95);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_95);
                }
            }
        });

        saCb00.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saCbAgeUnlimited.setChecked(false);

                    mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_00);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_00);
                }
            }
        });

        saCbAgeOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saCbAgeUnlimited.setChecked(false);

                    mAttribute.getAgeLimit().add(SocialAttribute.AgeLimit.OTHER);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.OTHER);
                }
            }
        });
    }

    @OnClick({R2.id.sa_cl_public, R2.id.sa_cl_private, R2.id.sa_tv_lin_rule, R2.id.sa_tv_notice})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sa_cl_public || view.getId() == R.id.sa_cl_private) { //是否公开群
            mAttribute.setOpen(!mAttribute.isOpen());
            toggleOpen();
        } else if (view.getId() == R.id.sa_tv_lin_rule) {//快活林规则

        } else if (view.getId() == R.id.sa_tv_notice) {//付费须知

        }
    }

    private void toggleOpen() {
        int unselectedColor = ContextCompat.getColor(this, R.color.common_text_blank);
        int unselectedColorGray = ContextCompat.getColor(this, R.color.common_text_dark);
        int selectColor = ContextCompat.getColor(this, R.color.common_colorAccent);
        if (mAttribute.isOpen()) {
            saIvPrivate.setImageResource(R.drawable.common_ic_check);
            saIvPublic.setImageResource(R.drawable.common_ic_check_hot);
            saTvPrivate.setTextColor(unselectedColor);
            saTvPrivateTip.setTextColor(unselectedColorGray);
            saTvPublic.setTextColor(selectColor);
            saTvPublicTip.setTextColor(selectColor);
        } else {
            saIvPublic.setImageResource(R.drawable.common_ic_check);
            saIvPrivate.setImageResource(R.drawable.common_ic_check_hot);
            saTvPublic.setTextColor(unselectedColor);
            saTvPublicTip.setTextColor(unselectedColorGray);
            saTvPrivate.setTextColor(selectColor);
            saTvPrivateTip.setTextColor(selectColor);
        }
    }

    private void toggleGemInput() {
        saRgGem.setVisibility(mAttribute.isCharge() ? View.VISIBLE : View.GONE);
        saTvCustomGem.setVisibility(mAttribute.isCharge() ? View.VISIBLE : View.GONE);
        saEtGem.setVisibility(mAttribute.isCharge() ? View.VISIBLE : View.GONE);
        saTvGem.setVisibility(mAttribute.isCharge() ? View.VISIBLE : View.GONE);
    }

    private void removeAgeLimit(SocialAttribute.AgeLimit ageLimit) {
        for (SocialAttribute.AgeLimit limit : mAttribute.getAgeLimit()) {
            if (ageLimit == limit) {
                mAttribute.getAgeLimit().remove(limit);
                break;
            }
        }
    }
}
