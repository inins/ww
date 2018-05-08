package com.wang.social.funpoint.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.ConerEditText;
import com.frame.utils.StrUtil;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.frame.component.entities.funpoint.Funpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class RecycleAdapterSearch extends BaseAdapter<Funpoint> {

    private String tags;
    private String key;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funpoint_item_search);
    }

    public class Holder extends BaseViewHolder<Funpoint> {
        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_note)
        TextView textNote;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.card_img)
        CardView cardImg;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funpoint bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImg(imgPic, bean.getImgUrl());
            FontUtils.boldText(textTitle);
            cardImg.setVisibility(!TextUtils.isEmpty(bean.getImgUrl()) ? View.VISIBLE : View.GONE);
            textNote.setText(bean.getNoteStr());

            //设置搜索关键字高亮
            //遍历搜索tags和关键字，匹配成功则设置为高亮
            //获取关键字集合
            List<String> keys = new ArrayList<>();
            if (!StrUtil.isEmpty(tags)) keys.addAll(Arrays.asList(tags.split(",")));
            if (!TextUtils.isEmpty(key)) keys.add(key);
            if (StrUtil.isEmpty(keys)) return;
            //遍历匹配关键字
            String newsTitle = bean.getNewsTitle();
            int color = ContextCompat.getColor(getContext(), R.color.common_blue_deep);
            SpannableString spannableString = new SpannableString(newsTitle);
            for (String str : keys) {
                //匹配成功：设置高亮
                if (newsTitle.contains(str)) {
                    int start = newsTitle.indexOf(str);
                    int end = start + str.length();
                    spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            textTitle.setText(spannableString);
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    /////////////////////

    public void reFreshReadCountById(int newsId) {
        if (StrUtil.isEmpty(getData())) return;
        for (int i = 0; i < getData().size(); i++) {
            Funpoint funpoint = getData().get(i);
            if (funpoint.getNewsId() == newsId) {
                funpoint.setReadTotal(funpoint.getReadTotal() + 1);
                notifyItemChanged(i);
            }
        }
    }

    /////////////////////

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
