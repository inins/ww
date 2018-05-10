package com.wang.social.im.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.model.entities.SocialInfo;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-10 15:10
 * ============================================
 */
public class SocialJoinLimitDialog extends BaseDialog {

    @BindView(R2.id.djl_tv_limit_gender)
    TextView djlTvLimitGender;
    @BindView(R2.id.djl_rb_gender_unlimited)
    RadioButton djlRbGenderUnlimited;
    @BindView(R2.id.djl_rb_male)
    RadioButton djlRbMale;
    @BindView(R2.id.djl_rb_female)
    RadioButton djlRbFemale;
    @BindView(R2.id.djl_rg_gender)
    RadioGroup djlRgGender;
    @BindView(R2.id.djl_tv_limit_age)
    TextView djlTvLimitAge;
    @BindView(R2.id.djl_cb_age_unlimited)
    AppCompatCheckBox djlCbAgeUnlimited;
    @BindView(R2.id.djl_cb_90)
    AppCompatCheckBox djlCb90;
    @BindView(R2.id.djl_cb_95)
    AppCompatCheckBox djlCb95;
    @BindView(R2.id.djl_cb_00)
    AppCompatCheckBox djlCb00;
    @BindView(R2.id.djl_cl_content)
    ConstraintLayout djlClContent;
    @BindView(R2.id.djl_tvb_cancel)
    TextView djlTvbCancel;

    private SocialInfo social;

    public SocialJoinLimitDialog(Context context, SocialInfo social) {
        super(context);
        this.social = social;
    }

    @Override
    protected int getView() {
        return R.layout.im_dialog_join_limit;
    }

    @Override
    protected void intView(View root) {
        djlCbAgeUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    djlCb90.setChecked(false);
                    djlCb90.setChecked(false);
                    djlCb00.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.UNLIMITED);
                }
            }
        });

        djlCb90.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    djlCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_90);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_90);
                }
            }
        });

        djlCb95.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    djlCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_95);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_95);
                }
            }
        });

        djlCb00.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    djlCbAgeUnlimited.setChecked(false);

                    social.getAttr().getAgeLimit().add(SocialAttribute.AgeLimit.AFTER_00);
                } else {
                    removeAgeLimit(SocialAttribute.AgeLimit.AFTER_00);
                }
            }
        });
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        switch (social.getAttr().getGenderLimit()) {
            case UNLIMITED:
                djlRbGenderUnlimited.setChecked(true);
                break;
            case MALE:
                djlRbMale.setChecked(true);
                break;
            case FEMALE:
                djlRbFemale.setChecked(true);
                break;
        }
        for (SocialAttribute.AgeLimit ageLimit : social.getAttr().getAgeLimit()) {
            switch (ageLimit) {
                case UNLIMITED:
                    djlCbAgeUnlimited.setChecked(true);
                    break;
                case AFTER_90:
                    djlCb90.setChecked(true);
                    break;
                case AFTER_95:
                    djlCb95.setChecked(true);
                    break;
                case AFTER_00:
                    djlCb00.setChecked(true);
                    break;
            }
        }
    }

    private void removeAgeLimit(SocialAttribute.AgeLimit ageLimit) {
        for (SocialAttribute.AgeLimit limit : social.getAttr().getAgeLimit()) {
            if (ageLimit == limit) {
                social.getAttr().getAgeLimit().remove(limit);
                break;
            }
        }
    }
}