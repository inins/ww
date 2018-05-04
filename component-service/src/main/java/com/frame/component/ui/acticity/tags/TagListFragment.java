package com.frame.component.ui.acticity.tags;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BaseFragment;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

import static com.frame.component.ui.acticity.tags.TagSelectionActivity.EVENTBUS_TAG_ENTITY;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.MODE_CONFIRM;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.MODE_SELECTION;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.NAME_MODE;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.NAME_PARENT_ID;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.NAME_SELECTED_LIST;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.NAME_TAG_TYPE;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.TAG_TYPE_INTEREST;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.TAG_TYPE_PERSONAL;
import static com.frame.component.ui.acticity.tags.TagSelectionActivity.TAG_TYPE_TAG_LIST;

public class TagListFragment extends BaseFragment<TagListPresenter> implements
        TagListContract.View {

    public interface MaxListener {
        boolean checkMax();
    }

    /**
     * 返回TagListFragment
     *
     * @param list 选中列表(主要在 兴趣大杂烩时使用)
     */
    public static TagListFragment newSelectionMode(int parentId, ArrayList<Tag> list, @TagSelectionActivity.TagType int type) {
        TagListFragment fragment = new TagListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(NAME_MODE, MODE_SELECTION);
        bundle.putInt(NAME_PARENT_ID, parentId);
        bundle.putParcelableArrayList(NAME_SELECTED_LIST, list);
        bundle.putInt(NAME_TAG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 编辑模式
     *
     * @param list
     * @return
     */
    public static TagListFragment newConfirmMode(ArrayList<Tag> list, @TagSelectionActivity.TagType int type) {
        TagListFragment fragment = new TagListFragment();

        Bundle bundle = new Bundle();
        bundle.putString(NAME_MODE, MODE_CONFIRM);
        bundle.putParcelableArrayList(NAME_SELECTED_LIST, list);
        bundle.putInt(NAME_TAG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void tagListChanged() {
        if (null != tagAdapter) {
            tagAdapter.notifyDataSetChanged();
        }
    }

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;

    TagAdapter tagAdapter;
    // 标签列表模式（选择或者编辑）
    String mode = MODE_SELECTION;
    // 父标签id
    int parentId = 0;
    // 标签列表类型（个人标签还是兴趣标签）
    @TagSelectionActivity.TagType
    int tagListType = TAG_TYPE_PERSONAL;
    private MaxListener mMaxListener;

    public void setMaxListener(MaxListener maxListener) {
        mMaxListener = maxListener;
    }

    private TagAdapter.DataProvider tagDataProvider;

    {
        tagDataProvider = new TagAdapter.DataProvider() {
            @Override
            public int getItemCount() {
                return mPresenter.getTagCount();
            }

            @Override
            public Tag getItem(int position) {
                return mPresenter.getTag(position);
            }

            @Override
            public boolean isDeleteMode() {
                return mode.equals(MODE_CONFIRM);
            }

            /**
             * 是否选中，需要根据当前标签列表的模式来判断
             *
             * @param tag
             * @return
             */
            @Override
            public boolean isSelected(Tag tag) {
                // 编辑模式一定是选中
                if (isDeleteMode()) return true;

                switch (tagListType) {
                    case TAG_TYPE_PERSONAL:
                        return tag.isPersonalTag() || tag.isPersonalSelectedTag();
                    case TAG_TYPE_INTEREST:
                    case TAG_TYPE_TAG_LIST:
                        return tag.isInterest();
                }

                return false;
            }
        };
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private TagAdapter.TagClickListener tagClickListener = new TagAdapter.TagClickListener() {
        @Override
        public void onTagClick(Tag tag) {
            // 个人标签模式下，已经是个人标签的标签不可点击
            if (tagListType == TAG_TYPE_PERSONAL) {
                if (tag.isPersonalTag()) return;
            }

            boolean selected = tagListType == TAG_TYPE_PERSONAL ?
                    tag.isPersonalSelectedTag() | tag.isPersonalTag() : tag.isInterest();

            if (!selected && null != mMaxListener) {
                if (mMaxListener.checkMax()) {
                    Timber.i("标签数量够了");
                    return;
                }
            }

            // 执行 Tag 点击动作
            tag.clickTag();

            // 点击后 Tag 是否被选中了
            selected = tagListType == TAG_TYPE_PERSONAL ?
                    tag.isPersonalSelectedTag() | tag.isPersonalTag() : tag.isInterest();

            tagListChanged();

            EventBean bean = new EventBean(selected ? EventBean.EVENTBUS_TAG_SELECTED : EventBean.EVENTBUS_TAG_UNSELECT);
            bean.put(EVENTBUS_TAG_ENTITY, tag);
            EventBus.getDefault().post(bean);
        }

        @Override
        public void onDelete(Tag tag) {
            // 将tag从列表删除
            mPresenter.removeTag(tag);

            EventBean bean = new EventBean(EventBean.EVENTBUS_TAG_DELETE);
            bean.put(EVENTBUS_TAG_ENTITY, tag);
            EventBus.getDefault().post(bean);
        }
    };

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTagListComponent.builder()
                .appComponent(appComponent)
                .tagListModule(new TagListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.tags_fragment_tag_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<Tag> selectedList = null;
        if (getArguments() != null) {
            selectedList = getArguments().getParcelableArrayList(NAME_SELECTED_LIST);
            parentId = getArguments().getInt(NAME_PARENT_ID);
            mode = getArguments().getString(NAME_MODE);
            tagListType = getArguments().getInt(NAME_TAG_TYPE);
        }

        if (mode.equals(MODE_CONFIRM)) {
            // 确认模式
            mPresenter.setSelectedList(selectedList);

            resetTagListView();
        } else {
            // 没有传入选中列表，则需要加载数据
            mPresenter.loadTagList(parentId, selectedList);
        }
    }


    @Override
    public void resetTagListView() {
        tagAdapter = new TagAdapter(getContext(), tagDataProvider, tagClickListener);

        // RecyclerView 相关设置
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(
                ChipsLayoutManager.newBuilder(getContext())
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .build());
        recyclerView.addItemDecoration(
                new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        recyclerView.setAdapter(tagAdapter);
    }

    /**
     * 收到删除的通知，需要让界面执行取消选中的操作，因为可能是不用的界面
     */
//    @Subscriber(tag = EVENTBUS_TAG_DELETE)
//    public void tagDelete(Tag tag) {
//        mPresenter.unselectTag(tag);
//    }
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(EventBean event) {
//        Timber.i("EventBuss 事件通知");
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_DELETE:
                if (event.get(EVENTBUS_TAG_ENTITY) instanceof Tag) {
                    Tag tag = (Tag) event.get(EVENTBUS_TAG_ENTITY);
                    mPresenter.unselectTag(tag);
                } else {
                    throw new ClassCastException("EventBus 返回数据类型不符，需要 Tag");
                }
                break;
            case EventBean.EVENTBUS_TAG_SELECTED:
                break;
            case EventBean.EVENTBUS_TAG_UNSELECT:
                break;
        }
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
