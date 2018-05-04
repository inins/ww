package com.wang.social.im.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.BaseImageLoaderStrategy;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerCreateTeamComponent;
import com.wang.social.im.di.modules.CreateTeamModule;
import com.wang.social.im.helper.ImageSelectHelper;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.model.entities.TeamAttribute;
import com.wang.social.im.mvp.presenter.CreateTeamPrensenter;
import com.wang.social.pictureselector.helper.PhotoHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建觅聊
 */
public class CreateTeamActivity extends BaseAppActivity<CreateTeamPrensenter> implements CreateTeamContract.View, PhotoHelper.OnPhotoCallback {

    private final int REQUEST_CODE_ATTR = 1000;

    @BindView(R2.id.ct_toolbar)
    SocialToolbar ctToolbar;
    @BindView(R2.id.ct_iv_cover)
    ImageView ctIvCover;
    @BindView(R2.id.ct_et_name)
    EditText ctEtName;
    @BindView(R2.id.ct_tv_attribute_intro)
    TextView ctTvAttributeIntro;
    @BindView(R2.id.ct_tv_tags_tip)
    TextView ctTvTagsTip;

    private String mCoverPath;
    private String mTags;
    private TeamAttribute mAttribute;

    private ImageSelectHelper mImageSelectHelper;

    @Inject
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCreateTeamComponent
                .builder()
                .appComponent(appComponent)
                .createTeamModule(new CreateTeamModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_create_team;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mAttribute = new TeamAttribute();

        showAttrInfo();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.ct_tv_what, R2.id.ct_cl_attribute, R2.id.ct_cl_tags, R2.id.ct_iv_cover})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ct_tv_what) { //什么是觅聊

        } else if (view.getId() == R.id.ct_cl_attribute) { //觅聊属性
            TeamAttributeActivity.start(this, REQUEST_CODE_ATTR, mAttribute);
        } else if (view.getId() == R.id.ct_cl_tags) { //觅聊标签
            TagSelectionActivity.startForTagList(this, mTags);
        } else if (view.getId() == R.id.ct_iv_cover) { //封面
            mImageSelectHelper = ImageSelectHelper.newInstance(this, this);
            mImageSelectHelper.showDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImageSelectHelper != null){
            mImageSelectHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ATTR:
                    mAttribute = data.getParcelableExtra(TeamAttributeActivity.EXTRA_ATTR);
                    showAttrInfo();
                    break;
            }
        }
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {
            mTags = (String) event.get("ids");
            String names = (String) event.get("names");

            ctTvTagsTip.setText(names);
        }
    }

    private void showAttrInfo() {
        if (mAttribute.isCharge()) {
            ctTvAttributeIntro.setText("付费/直接加入");
        } else {
            ctTvAttributeIntro.setText("免费/管理员审核");
        }
    }

    @Override
    public void onResult(String path) {
        mCoverPath = path;
        mImageLoader.loadImage(this, ImageConfigImpl
                .builder()
                .imageView(ctIvCover)
                .url(path)
                .placeholder(R.drawable.im_round_image_placeholder)
                .errorPic(R.drawable.im_round_image_placeholder)
                .transformation(new RoundedCornersTransformation(getResources().getDimensionPixelSize(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .build());
    }
}
