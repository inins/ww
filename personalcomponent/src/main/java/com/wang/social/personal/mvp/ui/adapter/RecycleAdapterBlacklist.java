package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.ShatDownUser;
import com.frame.component.entities.User;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.ui.view.SlidingMenu;

import butterknife.BindView;

public class RecycleAdapterBlacklist extends BaseAdapter<ShatDownUser> {

    private boolean isBlankList;

    public RecycleAdapterBlacklist(boolean isBlankList) {
        this.isBlankList = isBlankList;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_blacklist);
    }

    public class Holder extends BaseViewHolder<ShatDownUser> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_lable_gender)
        TextView textLableGender;
        @BindView(R2.id.text_lable_astro)
        TextView textLableAstro;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_tags)
        TextView textTags;
        @BindView(R2.id.btn_free)
        TextView btnFree;
        @BindView(R2.id.include_coutent)
        View include_coutent;
        @BindView(R2.id.slidmenu)
        SlidingMenu slidmenu;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(ShatDownUser bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTags.setText(bean.getTagText());
            btnFree.setText(isBlankList ? R.string.personal_blacklist_btn_free : R.string.personal_shutdown_btn_free);
            slidmenu.close();

            include_coutent.setOnClickListener(v -> {
                if (onBlankListUserClickListener != null)
                    onBlankListUserClickListener.onUserClick(bean, position);
            });
            btnFree.setOnClickListener(v -> {
                if (onBlankListUserClickListener != null)
                    onBlankListUserClickListener.onFreeBtnClick(bean, position);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    ////////////////////////////////

    public String getAllItemIds() {
        if (StrUtil.isEmpty(getData())) return "";
        String ids = "";
        for (ShatDownUser user : getData()) {
            ids += user.getShieldUserId() + ",";
        }
        return StrUtil.subLastChart(ids, ",");
    }

    ////////////////////////////////

    private OnBlankListUserClickListener onBlankListUserClickListener;

    public void setOnBlankListUserClickListener(OnBlankListUserClickListener onBlankListUserClickListener) {
        this.onBlankListUserClickListener = onBlankListUserClickListener;
    }

    public interface OnBlankListUserClickListener {
        void onUserClick(ShatDownUser user, int position);

        void onFreeBtnClick(ShatDownUser user, int position);
    }
}
