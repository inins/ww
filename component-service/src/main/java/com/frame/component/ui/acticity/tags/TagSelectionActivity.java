package com.frame.component.ui.acticity.tags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

@RouteNode(path = "/login_tag_selection", desc = "标签选择")
public class TagSelectionActivity extends BaseAppActivity<TagSelectionPresenter> implements
        TagSelectionContract.View, TagListFragment.MaxListener {


    public final static String EVENTBUS_TAG_DELETE = "EVENTBUS_TAG_DELETE";
    public final static String EVENTBUS_TAG_ENTITY = "EVENTBUS_TAG_ENTITY";

    public final static String NAME_MODE = "NAME_MODE";
    public final static String NAME_FROM_LOGIN = "NAME_FROM_LOGIN";
    public final static String NAME_TAG_TYPE = "NAME_TAG_TYPE";
    public final static String NAME_SELECTED_LIST = "NAME_SELECTED_LIST";
    public final static String NAME_PARENT_ID = "NAME_PARENT_ID";
    public final static String NAME_MAX_SELECTION = "NAME_MAX_SELECTION";
    public final static String NAME_TITLE = "NAME_TITLE";
    public final static String NAME_SUBTITLE = "NAME_SUBTITLE";
    // 标签选择
    public final static String MODE_SELECTION = "MODE_SELECTION";
    // 确认标签选择
    public final static String MODE_CONFIRM = "MODE_CONFIRM";

    /**
     * 标签类型定义
     */
    public final static int TAG_TYPE_UNKNOWN = -1;  // 标签类型未知
    public final static int TAG_TYPE_INTEREST = 0;  // 兴趣标签
    public final static int TAG_TYPE_PERSONAL = 1;  // 个人标签
    public final static int TAG_TYPE_TAG_LIST = 3;  // 直接返回选中的标签列表

    @Override
    public boolean checkMax() {
        if (mPresenter.getSelectedList().size() >= mMax) {
            ToastUtil.showToastLong(String.format("最多可以选择%d个标签", mMax));
            return true;
        }

        return false;
    }

    @IntDef({
            TAG_TYPE_UNKNOWN,
            TAG_TYPE_INTEREST,
            TAG_TYPE_PERSONAL,
            TAG_TYPE_TAG_LIST
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TagType {
    }

    private @TagType
    int mTagType = TAG_TYPE_PERSONAL; // 标签类型，个人中心模块需要调用，所以默认为个人标签

    /**
     * 启动标签选择页面
     *
     * @param context      context
     * @param mode         启动模式 @MODE_SELECTION 对应标签选择页面 @MODE_CONFIRM 对应标签确认页面
     * @param selectedList 已选标签列表
     * @param fromLogin    是否是从Login页面跳转过来的
     * @param type         类型，兴趣标签或者个人标签，加载已选和提交更改时需要调用不同的接口
     * @param title 标题
     * @param subtitle 副标题
     */
    private static void start(Context context,
                              String mode,
                              ArrayList<Tag> selectedList,
                              boolean fromLogin,
                              @TagType int type,
                              int max,
                              String title,
                              String subtitle) {
        Intent intent = new Intent(context, TagSelectionActivity.class);
        intent.putExtra(NAME_MODE, mode);
        intent.putExtra(NAME_FROM_LOGIN, fromLogin);
        intent.putExtra(NAME_TAG_TYPE, type);
        intent.putExtra(NAME_MAX_SELECTION, max);
        intent.putExtra(NAME_TITLE, title);
        intent.putExtra(NAME_SUBTITLE, subtitle);
        if (null != selectedList) {
            intent.putParcelableArrayListExtra(NAME_SELECTED_LIST, selectedList);
        }
        context.startActivity(intent);
    }

    private static void start(Context context,
                              String mode,
                              ArrayList<Tag> selectedList,
                              boolean fromLogin,
                              @TagType int type,
                              int max) {
        start(context,
                mode,
                selectedList,
                fromLogin,
                type,
                max,
                "",
                "");
    }

    /**
     * 返回选择的标签 id列表 和 mame列表
     *
     * @param context context
     *                //     * @param ids 已选标签ID列表，以逗号隔开 id1,id2,id3.....
     */
    public static void startForTagList(Context context, ArrayList<Tag> list, int max) {
        startForTagListWithTitle(context, list,  max, "", "");
    }

    public static void startForTagListWithTitle(Context context, ArrayList<Tag> list, int max, String title, String subtitle) {
        start(context, MODE_SELECTION, list, false, TAG_TYPE_TAG_LIST, max, title, subtitle);
    }

    public static void startForTagList(Context context, ArrayList<Tag> list) {
        startForTagList(context, list, Integer.MAX_VALUE);
    }

    /**
     * 从登录页面启动标签选择页面
     * <p>
     * 从登录页面启动的一定是兴趣标签
     */
    public static void startSelectionFromLogin(Context context, int max) {
        start(context, MODE_SELECTION, null, true, TAG_TYPE_INTEREST, max);
    }

    public static void startSelectionFromLogin(Context context) {
        startSelectionFromLogin(context, Integer.MAX_VALUE);
    }

    /**
     * 启动标签选择页面
     */
    public static void startSelection(Context context, @TagType int type, int max) {
        start(context, MODE_SELECTION, null, false, type, max);
    }

    public static void startSelection(Context context, @TagType int type) {
        startSelection(context, type, Integer.MAX_VALUE);
    }

    /**
     * 开始确认标签选择
     */
    public static void startConfirm(Context context, ArrayList<Tag> selectedList, boolean fromLogin, @TagType int type, int max) {
        start(context, MODE_CONFIRM, selectedList, fromLogin, type, max);
    }

    public static void startConfirm(Context context, ArrayList<Tag> selectedList, boolean fromLogin, @TagType int type) {
        startConfirm(context, selectedList, fromLogin, type, Integer.MAX_VALUE);
    }

    // 标题栏
    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    // 一级标签列表
    @BindView(R2.id.tab_layout)
    SmartTabLayout tabLayout;
    // ViewPager 二级标签页面
    @BindView(R2.id.view_pager)
    ViewPager viewPager;
    // 已选 选定 等文字
    @BindView(R2.id.selected_count_hint_text_view)
    TextView selectedCountHintTV;
    // 数量
    @BindView(R2.id.selected_count_text_view)
    TextView selectedCountTV;
    //
    @BindView(R2.id.ts_content_layout)
    LinearLayout contentLayout;
    @BindView(R2.id.title_view)
    TitleView mTitleView;
    // 右上角选中数量区域
    @BindView(R2.id.selected_count_layout)
    View selectedCountLayout;
    // 大量知识
    @BindView(R2.id.tag_all_layout)
    View mTatAllLayout;

    String mode = MODE_SELECTION;
    // 是否是从登录页面跳转过来的,为了个人中心调用，默认设置为不是从登录页面跳转过来
    boolean fromLogin = false;
    // 最多可以选择多少个
    private int mMax = Integer.MAX_VALUE;

    // 标题和副标题
    private String mTitle;
    private String mSubtitle;

    @Inject
    AppManager appManager;

    /**
     * 选中数量文字初始化
     */
    private void resetCountTV() {
        // 如果是兴趣大杂烩，则显示确定
        if (mode.equals(MODE_CONFIRM)) {
            selectedCountHintTV.setText(getString(R.string.tags_confirm));
        }

        // 如果是个人标签，则显示 选定
        if (mTagType == TAG_TYPE_PERSONAL) {
            selectedCountHintTV.setText(getString(R.string.tags_selected));
        }

        refreshCountTV();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 选中数量格文字式化
     */
    @Override
    public void refreshCountTV() {
        int count = mMyTagCount + mPresenter.getSelectedTagCount();

        // 兴趣标签模式下，用户可能会在确认页面删除掉所有的标签，这个时候也需要提交清空所有的标签
        if (count > 0 || (mTagType == TAG_TYPE_INTEREST)) {
            selectedCountHintTV.setVisibility(View.VISIBLE);
            selectedCountTV.setVisibility(View.VISIBLE);
            selectedCountTV.setText("(" + count + ")");
        } else {
            selectedCountHintTV.setVisibility(View.GONE);
            selectedCountTV.setVisibility(View.GONE);
        }

        if (mPresenter.getSelectedTagCount() <= 0 && mTagType != TAG_TYPE_INTEREST) {
            selectedCountHintTV.setTextColor(getResources().getColor(R.color.common_text_dark));
            selectedCountTV.setTextColor(getResources().getColor(R.color.common_text_dark));
            selectedCountLayout.setEnabled(false);
        } else {
            selectedCountHintTV.setTextColor(getResources().getColor(R.color.common_text_blank_dark));
            selectedCountTV.setTextColor(getResources().getColor(R.color.common_blue_deep));
            selectedCountLayout.setEnabled(true);
        }
    }

    /**
     * 更新已选标签成功，直接跳转到首页
     */
    @Override
    public void onUpdateTagSuccess() {
        EventBus.getDefault().post(new EventBean(EventBean.EVENTBUS_TAG_UPDATED));

        gotoMainPage();
    }

    private int mMyTagCount = 0;

    @Override
    public void setMyTagCount(int count) {
        mMyTagCount = count;

        refreshCountTV();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTagSelectionComponent.builder()
                .appComponent(appComponent)
                .tagSelectionModule(new TagSelectionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.tags_activity_tag_selection;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mMax = getIntent().getIntExtra(NAME_MAX_SELECTION, Integer.MAX_VALUE);

        // 读取调用模式
        if (getIntent().hasExtra(NAME_MODE)) {
            mode = getIntent().getStringExtra(NAME_MODE);
        }
        // 是否是从登录页面跳转过来的
        fromLogin = getIntent().getBooleanExtra(NAME_FROM_LOGIN, false);
        // 读取标签类型,默认为个人标签
        mTagType = getIntent().getIntExtra(NAME_TAG_TYPE, TAG_TYPE_PERSONAL);
        // 标题和副标题
        mTitle = getIntent().getStringExtra(NAME_TITLE);
        mSubtitle = getIntent().getStringExtra(NAME_SUBTITLE);

        if (fromLogin  && mode != MODE_CONFIRM) {
            toolbar.setLeftIcon(R.drawable.common_ic_close);
        }

        // ToolBar左边按钮，返回
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    // 退出
                    quit();
                }
            }
        });

        // 只有兴趣标签，且不是从登录跳转的，才有大量知识
        if (mTagType != TAG_TYPE_INTEREST || fromLogin) {
            mTatAllLayout.setVisibility(View.GONE);
        } else {
            mTatAllLayout.setVisibility(View.VISIBLE);
        }

        switch (mode) {
            case MODE_SELECTION:
                initSelectionData();
                break;
            case MODE_CONFIRM:
                initConfirmData();
                break;
        }

        // 是否设置了标题和副标题
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView.setTitle(mTitle);
        }
        if (!TextUtils.isEmpty(mSubtitle)) {
            mTitleView.setNote(mSubtitle);
        }

        // 根据不同模式确定ToolBar右边文字
        resetCountTV();
    }

    /**
     * 初始化 兴趣大杂烩 UI和数据
     */
    private void initConfirmData() {
        mTitleView.setTitle(getString(R.string.tags_confirm_title));
        mTitleView.setNote(getString(R.string.tags_confirm_title_hint));

        // 移除选择模式时需要的View
        contentLayout.removeAllViews();

        // 读取传入的 选中列表 参数
        if (getIntent().hasExtra(NAME_SELECTED_LIST)) {
            mPresenter.setSelectedTagList(
                    getIntent().getParcelableArrayListExtra(NAME_SELECTED_LIST));
        }

        // 这里不需要parentId，所以传入-1
        TagListFragment fragment = TagListFragment.newConfirmMode(mPresenter.getSelectedList(), mTagType);
        fragment.setMaxListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ts_content_layout, fragment)
                .commit();
    }

    private void initSelectionData() {
        if (mTagType == TAG_TYPE_PERSONAL) {
            // 个人标签模式下，需要现价在已经选了多少个了
//            mPresenter.getParentTagList();
            mPresenter.findMyTagCount();
        } else if (mTagType == TAG_TYPE_INTEREST) {
            // 兴趣标签模式下，需要现价在已经的兴趣标签
            mPresenter.myRecommendTag();
        } else if (mTagType == TAG_TYPE_TAG_LIST) {
            // 获取选择标签模式下，需要从bundle中获取数据
//            String idsStr = getIntent().getStringExtra("ids");
//            String namesStr = getIntent().getStringExtra("names");
//            mPresenter.setSelectedListFromBundle(idsStr, namesStr);

            mPresenter.setSelectedTagList(
                    getIntent().getParcelableArrayListExtra(NAME_SELECTED_LIST));

            // 加载父标签列表
            mPresenter.getParentTagList();
        }
    }

    @Override
    public void resetTabView(Tags tags) {
        if (tags == null) return;
        if (tags.getList().size() <= 0) return;

//        tabLayout.removeAllTabs();
//
//        for (Tag tag : tags.getList()) {
//            tabLayout.addTab(tabLayout.newTab().setText(tag.getTagName()));
//        }
//
//        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Tag parentTag = tags.getList().get(position);
                TagListFragment fragment =
                        TagListFragment.newSelectionMode(parentTag.getId(), mPresenter.getSelectedList(), mTagType);
                fragment.setMaxListener(TagSelectionActivity.this);
                return fragment;
            }

            @Override
            public int getCount() {
                return tags.getList().size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tags.getList().get(position).getTagName();
            }
        });

        tabLayout.setViewPager(viewPager);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /**
     * 大量知识
     */
    @OnClick(R2.id.tag_all_layout)
    public void tagAll() {
        // 发送通知
        EventBus.getDefault().post(new EventBean(EventBean.EVENTBUS_TAG_ALL));
        // 直接退出
        quitFinish();
    }

    /**
     * 右上角文字区域点击
     */
    @OnClick(R2.id.selected_count_layout)
    public void selectedCountClicked() {
        switch (mode) {
            case MODE_SELECTION:
                // 标签选择模式
                if (mTagType == TAG_TYPE_PERSONAL) {
                    // 个人标签选择，直接调用添加个人标签接口
                    mPresenter.addPersonalTag();
                } else if (mTagType == TAG_TYPE_TAG_LIST) {
                    // 不需要确认，直接返回
                    EventBean bean = new EventBean(EventBean.EVENTBUS_TAG_SELECTED_LIST);
                    bean.put(NAME_SELECTED_LIST, mPresenter.getSelectedList());
                    EventBus.getDefault().post(bean);

                    quitFinish();
                } else {
                    // 跳转到兴趣大杂烩
                    startConfirm(TagSelectionActivity.this, mPresenter.getSelectedList(), fromLogin, mTagType);
                }
                break;
            case MODE_CONFIRM:
                // 确认模式，提交选择
                if (TAG_TYPE_INTEREST == mTagType) { // 兴趣标签
                    // 提交兴趣标签
                    mPresenter.updateRecommendTag();
                } else if (TAG_TYPE_PERSONAL == mTagType) { // 个人标签
                    // 提交个人标签
                    mPresenter.addPersonalTag();
                } else if (TAG_TYPE_TAG_LIST == mTagType) { // 话题发布选择标签，直接返回标签列表文字
//                    String ids = TagUtils.formatTagIds(mPresenter.getSelectedList());
//                    String names = TagUtils.formatTagNames(mPresenter.getSelectedList());

                    EventBean bean = new EventBean(EventBean.EVENTBUS_TAG_SELECTED_LIST);
                    bean.put(NAME_SELECTED_LIST, mPresenter.getSelectedList());
//                    bean.put("ids", ids);
//                    bean.put("names", names);
                    EventBus.getDefault().post(bean);

                    quitFinish();
                }
                break;
        }
    }

    @Override
    public void onCommonEvent(EventBean event) {
        // 只接收标签相关事件
        if (event.getEvent() != EventBean.EVENTBUS_TAG_SELECTED &&
                event.getEvent() != EventBean.EVENTBUS_TAG_UNSELECT &&
                event.getEvent() != EventBean.EVENTBUS_TAG_DELETE) {
            return;
        }

        Tag tag;
        if (event.get(EVENTBUS_TAG_ENTITY) instanceof Tag) {
            tag = (Tag) event.get(EVENTBUS_TAG_ENTITY);
        } else {
            throw new ClassCastException("EventBus 返回数据类型不符，需要 Tag");
        }

        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_SELECTED:
                // 将Tag加入已选列表
                mPresenter.selectTag(tag);

                refreshCountTV();
                break;
            case EventBean.EVENTBUS_TAG_UNSELECT:
                mPresenter.unselectTag(tag);

                refreshCountTV();
                break;
            case EventBean.EVENTBUS_TAG_DELETE:
                mPresenter.unselectTag(tag);

                refreshCountTV();
                break;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 退出
     * 如果是从登录过来的，则需要跳转到首页，否则直接返回
     */
    private void quit() {
        if (fromLogin && (mode.equals(MODE_SELECTION))) {
            // 如果是从登录页面跳转过来的，则需要跳转到首页
            gotoMainPage();
        } else {
            finish();
        }
    }

    private void quitFinish() {
        finish();

        appManager.killActivity(TagSelectionActivity.class);
    }

    /**
     * 跳转到首页
     */
    private void gotoMainPage() {
        // 跳转到首页
        if (fromLogin) {
            // 路由跳转
            CommonHelper.AppHelper.startHomeActivity(this);
        }

        finish();

        appManager.killActivity(TagSelectionActivity.class);
    }

    /**
     * 拦截返回键，执行退出操作
     *
     * @param keyCode 按键
     * @param event   按键
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            quit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String formatTagIds(List<Tag> list) {
        String tagIds = "";

        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                Tag tag = list.get(i);
                Timber.i(tag.getId() + " " + tag.getTagName());
                tagIds = tagIds + tag.getId();
                if (i < list.size() - 1) {
                    tagIds = tagIds + ",";
                }
            }
        }

        return tagIds;
    }

    public static String formatTagNames(List<Tag> list) {
        String tagNames = "";

        if (null != list) {
            for (int i = 0; i < list.size(); i++) {
                Tag tag = list.get(i);
                Timber.i(tag.getId() + " " + tag.getTagName());
                tagNames = tagNames + " #" + tag.getTagName();
                if (i < list.size() - 1) {
                    tagNames = tagNames + ",";
                }
            }
        }

        return tagNames;
    }
}
