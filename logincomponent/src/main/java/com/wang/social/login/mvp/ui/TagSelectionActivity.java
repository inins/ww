package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.squareup.leakcanary.RefWatcher;
import com.wang.social.login.R;
import com.wang.social.login.R2;
import com.wang.social.login.di.component.DaggerTagSelectionComponent;
import com.wang.social.login.di.module.TagSelectionModule;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.Tags;
import com.wang.social.login.mvp.presenter.TagSelectionPresenter;
import com.wang.social.login.mvp.ui.widget.TagListFragment;
import com.wang.social.login.utils.Keys;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.wang.social.login.utils.Keys.MODE_CONFIRM;
import static com.wang.social.login.utils.Keys.MODE_SELECTION;
import static com.wang.social.login.utils.Keys.NAME_FROM_LOGIN;
import static com.wang.social.login.utils.Keys.NAME_MODE;
import static com.wang.social.login.utils.Keys.NAME_SELECTED_LIST;
import static com.wang.social.login.utils.Keys.NAME_TAG_TYPE;
import static com.wang.social.login.utils.Keys.TAG_TYPE_INTEREST;
import static com.wang.social.login.utils.Keys.TAG_TYPE_PERSONAL;

@RouteNode(path = "/login_tag_selection", desc = "标签选择")
public class TagSelectionActivity extends BaseAppActivity<TagSelectionPresenter> implements
        TagSelectionContract.View {

    private @Keys.TagType int mTagType = TAG_TYPE_PERSONAL; // 标签类型，个人中心模块需要调用，所以默认为个人标签

    /**
     * 启动标签选择页面
     * @param context context
     * @param mode 启动模式 @MODE_SELECTION 对应标签选择页面 @MODE_CONFIRM 对应标签确认页面
     * @param selectedList 已选标签列表
     * @param fromLogin 是否是从Login页面跳转过来的
     * @param type 类型，兴趣标签或者个人标签，加载已选和提交更改时需要调用不同的接口
     */
    private static void start(Context context, String mode, ArrayList<Tag> selectedList, boolean fromLogin, @Keys.TagType int type) {
        Intent intent = new Intent(context, TagSelectionActivity.class);
        intent.putExtra(NAME_MODE, mode);
        intent.putExtra(NAME_FROM_LOGIN, fromLogin);
        intent.putExtra(NAME_TAG_TYPE, type);
        if (null != selectedList) {
            intent.putParcelableArrayListExtra(NAME_SELECTED_LIST, selectedList);
        }
        context.startActivity(intent);
    }


    /**
     * 从登录页面启动标签选择页面
     *
     * 从登录页面启动的一定是兴趣标签
     */
    public static void startSelectionFromLogin(Context context) {
        start(context, MODE_SELECTION, null, true, TAG_TYPE_INTEREST);
    }

    /**
     * 启动标签选择页面
     */
    public static void startSelection(Context context, @Keys.TagType int type) {
        start(context, MODE_SELECTION, null, false, type);
    }

    /**
     * 开始确认标签选择
     */
    public static void startConfirm(Context context, ArrayList<Tag> selectedList, boolean fromLogin, @Keys.TagType int type) {
        start(context, MODE_CONFIRM, selectedList, fromLogin, type);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;
    @BindView(R2.id.selected_count_hint_text_view)
    TextView selectedCountHintTV;
    @BindView(R2.id.selected_count_text_view)
    TextView selectedCountTV;
    @BindView(R2.id.ts_content_layout)
    LinearLayout contentLayout;
    @BindView(R2.id.title_text_view)
    TextView titleTV;
    @BindView(R2.id.title_hint_text_view)
    TextView titleHintTV;

    String mode = MODE_SELECTION;
    // 是否是从登录页面跳转过来的,为了个人中心调用，默认设置为不是从登录页面跳转过来
    boolean fromLogin = false;

    @Inject
    AppManager appManager;

    /**
     * 选中数量文字初始化
     */
    private void resetCountTV() {
        // 如果是兴趣大杂烩，则显示确定
        if (mode.equals(MODE_CONFIRM)) {
            selectedCountHintTV.setText(getString(R.string.login_confirm));
        }

        // 如果是个人标签，则显示 选定
        if (mTagType == TAG_TYPE_PERSONAL) {
            selectedCountHintTV.setText(getString(R.string.login_selected));
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

        if (count > 0) {
            selectedCountHintTV.setVisibility(View.VISIBLE);
            selectedCountTV.setVisibility(View.VISIBLE);
            selectedCountTV.setText("(" + count + ")");
        } else {
            selectedCountHintTV.setVisibility(View.INVISIBLE);
            selectedCountTV.setVisibility(View.INVISIBLE);
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
        return R.layout.login_activity_tag_selection;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // 读取调用模式
        if (getIntent().hasExtra(NAME_MODE)) {
            mode = getIntent().getStringExtra(NAME_MODE);
        }
        // 是否是从登录页面跳转过来的
        fromLogin = getIntent().getBooleanExtra(NAME_FROM_LOGIN, false);
        // 读取标签类型,默认为个人标签
        mTagType = getIntent().getIntExtra(NAME_TAG_TYPE, TAG_TYPE_PERSONAL);

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

        switch (mode) {
            case MODE_SELECTION:
                initSelectionData();
                break;
            case MODE_CONFIRM:
                initConfirmData();
                break;
        }

        // 根据不同模式确定ToolBar右边文字
        resetCountTV();
    }

    /**
     * 初始化 兴趣大杂烩 UI和数据
     */
    private void initConfirmData() {
        titleTV.setText(getString(R.string.login_tag_confirm_title));
        titleHintTV.setText(getString(R.string.login_tag_confirm_title_hint));

        // 移除选择模式时需要的View
        contentLayout.removeAllViews();

        // 读取传入的 选中列表 参数
        if (getIntent().hasExtra(NAME_SELECTED_LIST)) {
            mPresenter.setSelectedTagList(
                    getIntent().getParcelableArrayListExtra(NAME_SELECTED_LIST));
        }

        // 这里不需要parentId，所以传入-1
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ts_content_layout,
                        TagListFragment.newConfirmMode(mPresenter.getSelectedList(), mTagType))
                .commit();
    }

    private void initSelectionData() {
        if (mTagType == TAG_TYPE_PERSONAL) {
            // 个人标签模式下，需要现价在已经选了多少个了
//            mPresenter.getParentTagList();
            mPresenter.findMyTagCount();
        } else {
            // 兴趣标签模式下，需要现价在已经的兴趣标签
            mPresenter.myRecommendTag();
        }
    }

    @Override
    public void resetTabView(Tags tags) {
        if (tags == null) return;
        if (tags.getList().size() <= 0) return;

        tabLayout.removeAllTabs();

        for (Tag tag : tags.getList()) {
            tabLayout.addTab(tabLayout.newTab().setText(tag.getTagName()));
        }

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Tag parentTag = tags.getList().get(position);
                return TagListFragment.newSelectionMode(parentTag.getId(), mPresenter.getSelectedList(), mTagType);
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
                } else {
                    // 跳转到兴趣大杂烩
                    startConfirm(TagSelectionActivity.this, mPresenter.getSelectedList(), fromLogin, mTagType);
                }
                break;
            case MODE_CONFIRM:
                // 确认模式，提交选择
                if (TAG_TYPE_INTEREST == mTagType) {
                    // 提交兴趣标签
                    mPresenter.updateRecommendTag();
                } else {
                    // 提交个人标签
                    mPresenter.addPersonalTag();
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
        if (event.get(Keys.EVENTBUS_TAG_ENTITY) instanceof Tag) {
            tag = (Tag) event.get(Keys.EVENTBUS_TAG_ENTITY);
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

        if (FrameUtils.obtainAppComponentFromContext(this).extras().get(RefWatcher.class.getName())
            instanceof RefWatcher) {
            Timber.i("Watch this!");
            RefWatcher refWatcher = (RefWatcher)FrameUtils.obtainAppComponentFromContext(this).extras().get(RefWatcher.class.getName());

            refWatcher.watch(this);
        }
    }

    /**
     * 退出
     * 如果是从登录过来的，则需要跳转到首页，否则直接返回
     */
    private void quit() {
        if (fromLogin) {
            // 如果是从登录页面跳转过来的，则需要跳转到首页
            gotoMainPage();
        } else {
            finish();
        }
    }

    /**
     * 跳转到首页
     */
    private void gotoMainPage() {
        // 跳转到首页
        if (fromLogin) {
            // 路由跳转
            CommonHelper.HomeHelper.startHomeActivity(this);
        }

        finish();

        appManager.killActivity(TagSelectionActivity.class);
    }

    /**
     * 拦截返回键，执行退出操作
     * @param keyCode 按键
     * @param event 按键
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
}
