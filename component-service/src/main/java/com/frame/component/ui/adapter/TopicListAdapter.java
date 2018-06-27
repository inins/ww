package com.frame.component.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.IsShoppingRsp;
import com.frame.component.entities.Topic;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetIsShoppingHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.utils.SearchUtil;
import com.frame.component.view.ConerTextView;
import com.frame.component.view.DialogPay;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private IView mIView;
    private Context mContext;
    private Activity mActivity;
    private FragmentManager mFragmentManager;
    private List<Topic> mList;
    // 是否是搜索的结果，是的话关键字高亮
    private String mSearchKeyword;
    private String mSearchTags;

    public TopicListAdapter(IView bindView, RecyclerView recyclerView, List<Topic> list) {
        mIView = bindView;
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public TopicListAdapter(IView iView, Activity activity, FragmentManager fragmentManager, List<Topic> list) {
        mIView = (activity instanceof IView) ? (IView) activity : iView;
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mFragmentManager = fragmentManager;
        mList = list;
    }

    public void setSearchKeyword(String searchKeyword) {
        mSearchKeyword = searchKeyword;
    }

    public void setSearchTags(String searchTags) {
        mSearchTags = searchTags;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_topic_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        Topic topic = mList.get(position);

        if (null == topic) return;

        // 是否置顶
        holder.setTopTV.setVisibility(topic.isTop() ? View.VISIBLE : View.GONE);

        // 是否付费
        if (topic.getRelateState() == 0) {
            holder.payFlagIV.setVisibility(View.GONE);
            holder.blankView.setVisibility(View.GONE);
        } else {
            holder.payFlagIV.setVisibility(View.VISIBLE);
            holder.blankView.setVisibility(View.INVISIBLE);
        }
        // 创建时间
//        holder.createDateTV.setText(formatCreateDate(mContext, topic.getCreateTime()));
        holder.createDateTV.setText(TimeUtils.getFriendlyTimeSpanByNow(topic.getCreateTime()));
        // 话题标题
        holder.titleTV.setText(SearchUtil.getHotText(mSearchTags, mSearchKeyword, topic.getTitle()));
        // 简要
        holder.contentTV.setText(SearchUtil.getHotText(mSearchTags, mSearchKeyword, topic.getFirstStrff()));
        // 图片
        if (!TextUtils.isEmpty(topic.getBackgroundImage())) {
            holder.imageCV.setVisibility(View.VISIBLE);
//            FrameUtils.obtainAppComponentFromContext(mContext)
//                    .imageLoader()
//                    .loadImage(mContext,
//                            ImageConfigImpl.builder()
//                                    .imageView(holder.imageView)
//                                    .url(topic.getBackgroundImage())
//                                    .placeholder(R.drawable.default_rect)
//                                    .transformation(new RoundedCorners(SizeUtils.dp2px(8)))
//                                    .build());
            ImageLoaderHelper.loadImg(holder.imageView, topic.getBackgroundImage());
        } else {
            holder.imageCV.setVisibility(View.GONE);
        }
        // 用户头像
        ImageLoaderHelper.loadCircleImg(holder.avatarIV, topic.getAvatar());
        // 用户昵称
        holder.userNameTV.setText(topic.getNickname());
        // 是否官方号
        if (topic.isOfficial()) {
            Drawable officialIcon = mContext.getResources().getDrawable(R.drawable.common_ic_official);
            officialIcon.setBounds(0, 0, officialIcon.getMinimumWidth(), officialIcon.getMinimumHeight());

            holder.userNameTV.setCompoundDrawables(
                    null,
                    null,
                    officialIcon,
                    null);
        } else {
            holder.userNameTV.setCompoundDrawables(
                    null,
                    null,
                    null,
                    null);
        }

        // 点击头像和昵称跳转到用户名片
        holder.userInfoLayout.setTag(topic);
        holder.userInfoLayout.setOnClickListener(v -> {
            if (v.getTag() instanceof  Topic) {
                Topic t = (Topic) v.getTag();
                CommonHelper.ImHelper.startPersonalCardForBrowse(mActivity, t.getCreatorId());
            }
        });

        // 是否点赞
        holder.praiseTV.setChecked(topic.isSupport());
        holder.praiseTV.setTag(topic);
        holder.praiseTV.setOnClickListener((View v) -> {
            if (v.getTag() instanceof Topic) {
                Topic t = (Topic) v.getTag();

                NetZanHelper.newInstance()
                        .topicZan(mIView, holder.praiseTV, t.getTopicId(), !t.isSupport(),
                                (boolean isZan, int zanCount) -> {
                                    t.setSupportTotal(zanCount);
                                    t.setSupport(isZan);

                                    notifyDataSetChanged();
                                });
            }
        });
        // 点赞次数
        holder.praiseTV.setText(String.format("%d", topic.getSupportTotal()));
        // 评论次数
        holder.commentTV.setText(String.format("%d", topic.getCommentTotal()));
        // 阅读次数
        holder.readTV.setText(String.format("%d", topic.getReadTotal()));
        // 标签
        holder.tagTV.setTagText(topic.getTagStr());

        // 点击
        holder.rootView.setTag(topic);
        holder.rootView.setOnClickListener(v -> {
            if (v.getTag() instanceof Topic) {
                Topic t = (Topic) v.getTag();
                // 是否需要支付
                if (t.getRelateState() == 1) {
                    // 话题需要支付，先请求是否已支付数据
                    NetIsShoppingHelper.newInstance().isTopicShopping(mIView,
                            t.getTopicId(),
                            (IsShoppingRsp rsp) -> {
                                /**
                                 "isFree": "是否收费（0免费 1收费）",
                                 "price": "收费价格,宝石数",
                                 "isShopping": "是否需要购买（0 无需购买 1 购买）"
                                 */
                                if (rsp.getIsFree() >= 1 && rsp.getIsShopping() >= 1) {
                                    // 可能之前的数据里面没有价格，所以这里要先设置价格
                                    t.setRelateMoney(rsp.getPrice());
                                    // 处理支付
                                    if (null != mFragmentManager) {
                                        // 弹出支付对话框
                                        DialogPay.showPayTopic(mIView, mFragmentManager,
                                                t,
                                                -1,
                                                () -> payTopic(t));
                                    } else {
                                        Timber.i("fragmentmanager is null");
                                    }
                                } else {
                                    // 不需要支付，直接打开详情
                                    openTopicDetail(t);
                                }
                            });
                } else {
                    // 直接打开话题详情
                    if (null != mActivity) {
                        // 不需要支付，直接打开详情
                        openTopicDetail(t);
                    } else {
                        Timber.e("activity is null");
                    }
                }
            }
        });
    }

    private void openTopicDetail(Topic topic) {
        CommonHelper.TopicHelper.startTopicDetail(mActivity, topic.getTopicId());

        topic.setReadTotal(topic.getReadTotal() + 1);
        notifyDataSetChanged();
    }

    private void payTopic(Topic topic) {
        NetPayStoneHelper.newInstance()
                .netPayTopic(mIView,
                        topic.getTopicId(),
                        topic.getRelateMoney(),
                        () -> {
                            Timber.i("支付成功，打开详情");
                            topic.setShopping(true);

                            notifyDataSetChanged();

                            // 打开话题详情
                            if (null != mActivity) {
                                openTopicDetail(topic);
                            } else {
                                Timber.e("activity is null");
                            }
                        });
    }

    public String formatCreateDate(Context context, long mills) {
        String dateString;
        Date date = new Date();
//        long mills = TimeUtils.string2Millis(topic.getCreateTime());
        date.setTime(mills);
        if (TimeUtils.isToday(date)) {
            dateString = "今天" +
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        } else {
            dateString = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(date);
        }

        return dateString;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView payFlagIV;
        View blankView;
        TextView createDateTV;
        TextView titleTV;
        TextView contentTV;
        ImageView avatarIV;
        TextView userNameTV;
        CardView imageCV;
        ImageView imageView;
        CheckedTextView praiseTV;
        TextView commentTV;
        TextView readTV;
        ConerTextView tagTV;
        View userInfoLayout;
        TextView setTopTV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            payFlagIV = itemView.findViewById(R.id.pay_flag_image_view);
            blankView = itemView.findViewById(R.id.blank_view);
            createDateTV = itemView.findViewById(R.id.create_date_text_view);
            titleTV = itemView.findViewById(R.id.title_text_view);
            contentTV = itemView.findViewById(R.id.content_text_view);
            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            userNameTV = itemView.findViewById(R.id.user_name_text_view);
            imageCV = itemView.findViewById(R.id.card_pic);
            imageView = itemView.findViewById(R.id.image_view);
            praiseTV = itemView.findViewById(R.id.praise_text_view);
            commentTV = itemView.findViewById(R.id.comment_text_view);
            readTV = itemView.findViewById(R.id.read_text_view);
            tagTV = itemView.findViewById(R.id.conertext_tag);
            userInfoLayout = itemView.findViewById(R.id.user_info_layout);
            setTopTV = itemView.findViewById(R.id.set_top_text_view);
        }
    }


    /**
     * 获取数据集
     */
    public List<Topic> getResults() {
        return mList;
    }

    /**
     * 刷新数据
     */
    public void refreshData(List<Topic> valueList) {
        mList.clear();
        mList.addAll(valueList);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    public void addItem(List<Topic> items) {
        mList.addAll(items);
        notifyItemInserted(this.mList.size() - items.size());
        notifyItemRangeChanged(this.mList.size() - items.size(), this.mList.size() - 1);
    }

    ////////////////////////

    public void reFreshZanCountById(int topicId, boolean isSupport) {
        if (StrUtil.isEmpty(getResults())) return;
        for (int i = 0; i < getResults().size(); i++) {
            Topic topic = getResults().get(i);
            if (topic.getTopicId() == topicId) {
                topic.setSupport(isSupport);
                topic.setSupportTotal(Math.max(0, topic.getSupportTotal() + (isSupport ? 1 : -1)));
                notifyItemChanged(i);
            }
        }
    }

    public void reFreshCommentCountById(int topicId) {
        if (StrUtil.isEmpty(getResults())) return;
        for (int i = 0; i < getResults().size(); i++) {
            Topic topic = getResults().get(i);
            if (topic.getTopicId() == topicId) {
                topic.setCommentTotal(topic.getCommentTotal() + 1);
                notifyItemChanged(i);
            }
        }
    }
}
