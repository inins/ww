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
import android.widget.TextView;

import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.PayDialog;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerCreateSocialComponent;
import com.wang.social.im.di.modules.CreateSocialModule;
import com.wang.social.im.helper.ImageSelectHelper;
import com.wang.social.im.mvp.contract.CreateSocialContract;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.presenter.CreateSocialPresenter;
import com.wang.social.pictureselector.helper.PhotoHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建趣聊
 */
public class CreateSocialActivity extends BaseAppActivity<CreateSocialPresenter> implements CreateSocialContract.View, PhotoHelper.OnPhotoCallback {

    private static final int REQUEST_CODE_ATTR = 1000;

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

    private String mCoverPath;
    private ArrayList<Tag> mSelectTags;
    private String mTags;
    private ImageSelectHelper mImageSelectHelper;

    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context) {
        Intent intent = new Intent(context, CreateSocialActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCreateSocialComponent
                .builder()
                .appComponent(appComponent)
                .createSocialModule(new CreateSocialModule(this))
                .build()
                .inject(this);
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
    public boolean useEventBus() {
        return true;
    }

    private void init() {
        scEtName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() + scEtName.getText().length() > 8) {
                    return "";
                }
                if (!RegexUtils.isUsernameMe(source)) {
                    return "";
                }
                return source;
            }
        }});

        csToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        String name = scEtName.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_name));
                            return;
                        }
                        if (TextUtils.isEmpty(mCoverPath)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_cover));
                            return;
                        }
                        if (TextUtils.isEmpty(mTags)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_tag));
                            return;
                        }
                        mPresenter.checkPayStatus(mAttr, mCoverPath, name, scSbTeam.isChecked(), mTags);
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

    @OnClick({R2.id.sc_cl_attribute, R2.id.sc_cl_tags, R2.id.sc_tv_create_tip, R2.id.sc_iv_cover})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sc_cl_attribute) {
            SocialAttributeActivity.start(this, REQUEST_CODE_ATTR, mAttr);
        } else if (view.getId() == R.id.sc_iv_cover) {
            mImageSelectHelper = ImageSelectHelper.newInstance(this, this);
            mImageSelectHelper.showDialog();
        } else if (view.getId() == R.id.sc_cl_tags) {
            TagSelectionActivity.startForTagList(this, mSelectTags, 5);
        } else if (view.getId() == R.id.sc_tv_create_tip) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImageSelectHelper != null) {
            mImageSelectHelper.onActivityResult(requestCode, resultCode, data);
        }
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
                if (i != mAttr.getAgeLimit().size() - 1) {
                    attrBuffer.append("、");
                }
            }
        }
        scTvAttributeIntro.setText(attrBuffer.toString());
    }

    @Override
    public void onResult(String path) {
        mCoverPath = path;
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .imageView(scIvCover)
                .url(path)
                .placeholder(R.drawable.im_round_image_placeholder)
                .errorPic(R.drawable.im_round_image_placeholder)
                .transformation(new RoundedCornersTransformation(getResources().getDimensionPixelSize(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .build());
    }

    @Override
    public void onCreateComplete(CreateGroupResult result) {
        finish();
    }

    @Override
    public void showPayDialog(String applyId, String cover, String name, boolean canCreateTeam, SocialAttribute socialAttribute, String tags) {
        String message = UIUtil.getString(R.string.im_team_pay_message, IMConstants.CREATE_GROUP_PRICE);
        PayDialog payDialog = new PayDialog(this, new PayDialog.OnPayListener() {
            @Override
            public void onPay() {
                mPresenter.payForCreate(applyId, cover, name, canCreateTeam, socialAttribute, tags);
            }
        }, message, String.valueOf(IMConstants.CREATE_GROUP_PRICE));
        payDialog.show();
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {
            mSelectTags = (ArrayList<Tag>) event.get(TagSelectionActivity.NAME_SELECTED_LIST);

            if (mSelectTags != null && mSelectTags.size() > 0) {
                StringBuilder idBuffer = new StringBuilder();
                StringBuilder nameBuffer = new StringBuilder();
                for (Tag tag : mSelectTags) {
                    idBuffer.append(tag.getId()).append(",");
                    nameBuffer.append("#").append(tag.getTagName()).append(" ");
                }
                idBuffer.deleteCharAt(idBuffer.length() - 1);
                mTags = idBuffer.toString();
                scTvTagsTip.setText(nameBuffer.toString());
            } else {
                mTags = null;
                scTvTagsTip.setText(R.string.im_social_tags_tip);
            }
        }
    }
}
