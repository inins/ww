package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.search.SearchGroup;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import butterknife.BindView;

public class RecycleAdapterSearchGroup extends BaseAdapter<SearchGroup> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_search_group);
    }

    public class Holder extends BaseViewHolder<SearchGroup> {

        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.img_tag)
        ImageView imgTag;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_note)
        TextView textNote;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(SearchGroup bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getGroupCoverPlan());
            textName.setText(bean.getGroupName());
            imgTag.setSelected(!bean.isMi());
            String note;
            if (bean.isMi()) {
                note = "觅聊 来自：" + bean.getParentGroupName();
            } else {
                note = "趣聊 " + bean.getMemberNum() + "人";
            }
            textNote.setText(note);
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

}
