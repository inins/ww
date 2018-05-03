package com.wang.social.im.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.contract.CreateSocialContract;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.presenter.CreateSocialPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建趣聊
 */
public class CreateSocialActivity extends BaseAppActivity<CreateSocialPresenter> implements CreateSocialContract.View {

    private static final int REQUEST_CODE_ATTR = 1000;
    private static final int REQUEST_CODE_TAGS = 1001;

    @BindView(R2.id.cs_toolbar)
    SocialToolbar csToolbar;
    @BindView(R2.id.sc_iv_cover)
    ImageView scIvCover;
    @BindView(R2.id.sc_et_name)
    EditText scEtName;
    @BindView(R2.id.sc_tv_attribute_intro)
    TextView scTvAttributeIntro;
    @BindView(R2.id.sc_tv_tags_tip)
    TextView scTvTagsTip;
    @BindView(R2.id.sc_sb_team)
    SwitchButton scSbTeam;

    private SocialAttribute mAttr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_create_social;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mAttr = new SocialAttribute();
        mAttr.setOpen(true);
        mAttr.setGenderLimit(SocialAttribute.GenderLimit.UNLIMITED);
        mAttr.getAgeLimit().add(SocialAttribute.AgeLimit.UNLIMITED);

        showAttrInfo();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.sc_cl_attribute, R2.id.sc_cl_tags, R2.id.sc_tv_create_tip})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sc_cl_attribute) {
            SocialAttributeActivity.start(this, REQUEST_CODE_ATTR, mAttr);
        } else if (view.getId() == R.id.sc_cl_tags) {

        } else if (view.getId() == R.id.sc_tv_create_tip) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ATTR:
                    mAttr = data.getParcelableExtra(SocialAttributeActivity.EXTRA_ATTR);
                    showAttrInfo();
                    break;
            }
        }
    }

    private void showAttrInfo() {
        StringBuffer attrBuffer = new StringBuffer();
        if (mAttr.isOpen()) {
            attrBuffer.append("任何人都能进入趣聊/");
        } else {
            attrBuffer.append("只允许通过邀请进入趣聊/");
        }
        if (mAttr.isCharge()) {
            attrBuffer.append("付费/");
        } else {
            attrBuffer.append("免费/");
        }
        switch (mAttr.getGenderLimit()) {
            case UNLIMITED:
                attrBuffer.append("不限男女/");
                break;
            case FEMALE:
                attrBuffer.append("女/");
                break;
            case MALE:
                attrBuffer.append("男/");
                break;
        }
        for (int i = 0; i < mAttr.getAgeLimit().size(); i++) {
            SocialAttribute.AgeLimit ageLimit = mAttr.getAgeLimit().get(i);
            if (ageLimit == SocialAttribute.AgeLimit.UNLIMITED) {
                attrBuffer.append("不限年龄");
                break;
            } else {
                switch (ageLimit) {
                    case AFTER_90:
                        attrBuffer.append("90后");
                        break;
                    case AFTER_95:
                        attrBuffer.append("95后");
                        break;
                    case AFTER_00:
                        attrBuffer.append("00后");
                        break;
                    case OTHER:
                        attrBuffer.append("其他");
                        break;
                }
                if (i != mAttr.getAgeLimit().size() - 1){
                    attrBuffer.append("、");
                }
            }
        }
        scTvAttributeIntro.setText(attrBuffer.toString());
    }
}
