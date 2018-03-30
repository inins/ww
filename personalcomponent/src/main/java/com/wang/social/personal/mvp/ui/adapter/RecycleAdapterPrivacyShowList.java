package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.SelectHelper;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.ShowListCate;
import com.wang.social.personal.mvp.entities.ShowListGroup;
import com.wang.social.personal.mvp.ui.view.ListViewLinearLayout;

import org.w3c.dom.Text;

import butterknife.BindView;

public class RecycleAdapterPrivacyShowList extends BaseAdapter<ShowListCate> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new HolderHeader(context, parent, R.layout.personal_item_privacy_showlist);
    }

    public class HolderHeader extends BaseViewHolder<ShowListCate> {
        @BindView(R.id.text_title)
        TextView text_title;
        @BindView(R.id.text_count)
        TextView text_count;
        @BindView(R.id.listlayout)
        ListViewLinearLayout listlayout;
        ListAdapterPrivacyShowList adapter;

        public HolderHeader(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
            adapter = new ListAdapterPrivacyShowList(context);
            listlayout.setAdapter(adapter, false);
        }

        @Override
        protected void bindData(ShowListCate bean, int position, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.setShow(!bean.isShow());
                    notifyItemChanged(position);
//                    notifyDataSetChanged();
                }
            });
            text_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.setSelect(!bean.isSelect());
                    notifyItemChanged(position);
                }
            });
            text_title.setText(bean.getTitle());
            text_title.setSelected(bean.isSelect());
            text_count.setText("已选" + bean.getCount());
            text_count.setVisibility(bean.getCount() != 0 ? View.VISIBLE : View.GONE);
            listlayout.setVisibility(bean.isShow() ? View.VISIBLE : View.GONE);

            adapter.setCheckView(text_title);
            adapter.getResults().clear();
            adapter.getResults().addAll(bean.getGroupList());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRelease() {
        }
    }
}
