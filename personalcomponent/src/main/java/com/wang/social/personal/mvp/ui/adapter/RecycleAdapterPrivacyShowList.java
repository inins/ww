package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.SelectHelper;
import com.frame.component.utils.ListUtil;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.ShowListCate;
import com.frame.component.view.ListViewLinearLayout;
import com.wang.social.personal.mvp.entities.ShowListGroup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RecycleAdapterPrivacyShowList extends BaseAdapter<ShowListCate> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new HolderHeader(context, parent, R.layout.personal_item_privacy_showlist);
    }

    public class HolderHeader extends BaseViewHolder<ShowListCate> {
        @BindView(R2.id.text_title)
        TextView text_title;
        @BindView(R2.id.text_count)
        TextView text_count;
        @BindView(R2.id.listlayout)
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
                    for (ShowListCate cate : getData()) {
                        if (bean != cate) {
                            cate.setSelect(false);
                        } else {
                            bean.setSelect(!bean.isSelect());
                        }
                    }
                    notifyItemRangeChanged(0, getItemCount());
                }
            });
            text_title.setText(bean.getTitle());
            text_title.setSelected(bean.isSelect());
            text_count.setText("已选" + bean.getSelectChildCount());
            text_count.setVisibility(bean.getCount() != 0 ? View.VISIBLE : View.GONE);
            listlayout.setVisibility(bean.isShow() ? View.VISIBLE : View.GONE);

            adapter.setOnCheckCallback(hasCheck -> {
                for (ShowListCate cate : getData()) {
                    if (bean != cate) {
                        cate.setSelectOnly(false);
                    } else {
                        bean.setSelectOnly(hasCheck);
                    }
                }
                notifyItemRangeChanged(0, getItemCount());
            });
            adapter.getResults().clear();
            adapter.getResults().addAll(bean.getGroupList());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRelease() {
        }
    }

    ///////////////////////////////////

    public String getSelectGroupIds() {
        ShowListCate showListCate = getData().get(3);
        List<ShowListGroup> selectBeans = SelectHelper.getSelectBeans(showListCate.getGroupList());
        String ids = "";
        for (ShowListGroup group : selectBeans) {
            ids += group.getId() + ",";
        }
        return StrUtil.subLastChart(ids, ",");
    }

    public boolean isSelectByPosition(int position) {
        ShowListCate showListCate = ListUtil.get(getData(), position);
        if (showListCate != null) {
            return showListCate.isSelect();
        }
        return false;
    }

    public String getSelectParamName() {
        ShowListCate selectBean = SelectHelper.getSelectBean(getData());
        if (selectBean == null) return "";
        return selectBean.getParaName();
    }

    public Map<String, Object> getParamMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        for (ShowListCate cate : getData()) {
            map.put(cate.getParaName(), cate.isSelect());
        }
        return map;
    }
}
