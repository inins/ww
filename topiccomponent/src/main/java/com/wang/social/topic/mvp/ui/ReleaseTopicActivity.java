package com.wang.social.topic.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frame.component.ui.acticity.BGMList.BGMListActivity;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.MusicBoard;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.ToastUtil;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerReleaseTopicComponent;
import com.wang.social.topic.di.module.ReleaseTopicModule;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.mvp.presenter.ReleaseTopicPresenter;
import com.wang.social.topic.mvp.ui.widget.DFSetPrice;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ReleaseTopicActivity extends BaseAppActivity<ReleaseTopicPresenter>
        implements ReleaseTopicContract.View {

    public final static int REQUEST_CODE_TEMPLATE = 1001;
    public final static int REQUEST_CODE_BGM = 1002;

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.title_edit_text)
    EditText mTitleET;
    @BindView(R2.id.title_count_text_view)
    TextView mTitleCountTV;
    // 标签
    @BindView(R2.id.topic_tags_text_view)
    TextView mTagsTV;
    // 背景音乐
    @BindView(R2.id.music_board_layout)
    MusicBoard mMusicBoard;

    // 底部栏
    @BindView(R2.id.bottom_layout)
    TabLayout mTabLayout;
    int[] BOTTOM_ICON_RES = {
            R.drawable.topic_ic_template,
            R.drawable.topic_ic_music,
            R.drawable.topic_ic_voice,
            R.drawable.topic_ic_font,
            R.drawable.topic_ic_image,
            R.drawable.topic_ic_charge
    };


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerReleaseTopicComponent.builder()
                .appComponent(appComponent)
                .releaseTopicModule(new ReleaseTopicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_release_topic;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                    addTopic();
                }
            }
        });
        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshTitleCount(s.length());
            }
        });

        refreshTitleCount(0);

        // 底部栏(直接使用tablayou不能响应每次点击)
        for (int i = 0; i < BOTTOM_ICON_RES.length; i++) {
            RelativeLayout layout = new RelativeLayout(mTabLayout.getContext());

            ImageView imageView = new ImageView(mTabLayout.getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            imageView.setImageDrawable(getResources().getDrawable(BOTTOM_ICON_RES[i]));
            imageView.setLayoutParams(params);
            layout.addView(imageView);

            layout.setTag(i);
            layout.setOnClickListener(mBottomBarListener);

            TabLayout.Tab tab = mTabLayout.newTab()
                    .setCustomView(layout);

            mTabLayout.addTab(tab);
        }

        mMusicBoard.setStateListener(new MusicBoard.StateListener() {
            @Override
            public void onStartPrepare() {
                showLoadingDialog();
            }

            @Override
            public void onPrepared() {
                dismissLoadingDialog();
            }
        });
    }

    private View.OnClickListener mBottomBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag() instanceof Integer) {
                int i = (int) v.getTag();
                Timber.i("底部点击 : " + i);
                switch (i) {
                    case 0: // 模板
                        TemplateActivity.start(ReleaseTopicActivity.this,
                                mPresenter.getTemplate(),
                                REQUEST_CODE_TEMPLATE);
                        break;
                    case 1: // 音乐
                        BGMListActivity.start(ReleaseTopicActivity.this,
                                mPresenter.getBGMusic(),
                                REQUEST_CODE_BGM);
                        break;
                    case 2: // 语音
                        break;
                    case 3: // 字体
                        break;
                    case 4: // 图片
                        break;
                    case 5: // 收费
                        DFSetPrice.showDialog(getSupportFragmentManager());
                        break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TEMPLATE: // 模板选择
                    Template template = data.getParcelableExtra("template");
                    Timber.i("onActivityResult : " + template.getId() + " " + template.getName());

                    mPresenter.setTemplate(template);

                    break;
                case REQUEST_CODE_BGM: // 背景音乐
                    Music music = Music.newInstance(data);
                    Timber.i("onActivityResult " + music.getMusicId() + " " + music.getMusicName());

                    mMusicBoard.resetMusic(music);
                    mPresenter.setBGMusic(music);

                    break;
            }
        }
    }

    private void addTopic() {
        mPresenter.resetNetParam()
                .setTitle(mTitleET.getText().toString())
                .setBackgroundImage("")
                .setTagIds(mTagIds)
                .setBackgroundMusicId(mPresenter.getBGMusic().getMusicId())
                .setContent("")
                .setTemplateId(mPresenter.getTemplate().getId());

        mPresenter.addTopic();
    }

    private void refreshTitleCount(int length) {
        mTitleCountTV.setText(String.format(getString(R.string.topic_title_length_format), length));
    }


    @OnClick(R2.id.cover_layout)
    public void selectCoverImage() {

    }

    private String mTagIds;
    private String mTagNames;

    // 标签
    @OnClick(R2.id.topic_tags_text_view)
    public void topicTags() {
//        Bundle bundle = new Bundle();
//        if (!TextUtils.isEmpty(mTagIds)) {
//            bundle.putString("ids", mTagIds);
//        }
//        bundle.putString("NAME_MODE", "MODE_SELECTION");
//        bundle.putInt("NAME_TAG_TYPE", 3);
//
//        Intent intent = new Intent(this, TagSelectionActivity.class);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
        TagSelectionActivity.startForTagList(this, mTagIds);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {

            String ids = (String) event.get("ids");
            String names = (String) event.get("names");

            Timber.i(ids);
            Timber.i(names);

            mTagIds = ids;
            mTagNames = names;

            mTagsTV.setText(mTagNames);
        }
//        else if (event.getEvent() == EventBean.EVENTBUS_BGM_SELECTED) {
//            if (null != event.get("BGM") && event.get("BGM") instanceof Music) {
//                Music music = (Music) event.get("BGM");
//                Timber.i("onCommonEvent : " + music.getMusicId() + " " + music.getMusicName());
//            }
//        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMusicBoard.onPause();
    }

    @Override
    public void toastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }
}
