package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerCreateTeamComponent;
import com.wang.social.im.di.modules.CreateTeamModule;
import com.wang.social.im.helper.ImageSelectHelper;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.TeamAttribute;
import com.wang.social.im.mvp.presenter.CreateTeamPresenter;
import com.wang.social.pictureselector.helper.PhotoHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建觅聊
 */
public class CreateTeamActivity extends BaseAppActivity<CreateTeamPresenter> implements CreateTeamContract.View, PhotoHelper.OnPhotoCallback {

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

    @Autowired
    String socialId;

    private String mCoverPath;
    private ArrayList<Tag> mSelectTags;
    private String mTag;
    private TeamAttribute mAttribute;

    private ImageSelectHelper mImageSelectHelper;

    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context, String socialId) {
        Intent intent = new Intent(context, CreateTeamActivity.class);
        intent.putExtra("socialId", socialId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
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

    private void init() {
        ctToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        String name = ctEtName.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_team_name));
                            return;
                        }
                        if (TextUtils.isEmpty(mCoverPath)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_cover));
                            return;
                        }
                        if (TextUtils.isEmpty(mTag)) {
                            ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_tag));
                            return;
                        }
                        mPresenter.checkCreateStatus(socialId, name, mCoverPath, mTag, mAttribute);
                        break;
                }
            }
        });
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @OnClick({R2.id.ct_tv_what, R2.id.ct_cl_attribute, R2.id.ct_cl_tags, R2.id.ct_iv_cover})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.ct_tv_what) { //什么是觅聊
            WebActivity.start(this, AppConstant.Url.REFERRAL_TEAM);
        } else if (view.getId() == R.id.ct_cl_attribute) { //觅聊属性
            TeamAttributeActivity.start(this, REQUEST_CODE_ATTR, mAttribute);
        } else if (view.getId() == R.id.ct_cl_tags) { //觅聊标签
            TagSelectionActivity.startForTagList(this, mSelectTags, 1);
        } else if (view.getId() == R.id.ct_iv_cover) { //封面
            mImageSelectHelper = ImageSelectHelper.newInstance(this, this);
            mImageSelectHelper.showDialog();
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
                    mAttribute = data.getParcelableExtra(TeamAttributeActivity.EXTRA_ATTR);
                    showAttrInfo();
                    break;
            }
        }
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {
            mSelectTags = (ArrayList<Tag>) event.get(TagSelectionActivity.NAME_SELECTED_LIST);

            if (mSelectTags != null && mSelectTags.size() > 0) {
                mTag = String.valueOf(mSelectTags.get(0).getId());
                ctTvTagsTip.setText("#" + mSelectTags.get(0).getTagName());
            } else {
                mTag = null;
                ctTvTagsTip.setText(R.string.im_team_select_tag);
            }
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

    @Override
    public void onCreateComplete(CreateGroupResult result) {
        finish();
        //通知有觅聊列表的地方刷新数据
        EventBus.getDefault().post(new EventBean(EventBean.EVENT_NOTIFY_CREATE_TEAM));
    }
}
