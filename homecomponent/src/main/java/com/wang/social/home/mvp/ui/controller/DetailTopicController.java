package com.wang.social.home.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.ConerTextView;
import com.frame.component.view.DialogPay;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.topic.TopicHome;
import com.wang.social.home.mvp.entities.topic.TopicHomeDetail;
import com.wang.social.home.mvp.model.api.HomeService;

import butterknife.BindView;

public class DetailTopicController extends BaseController {

    @BindView(R2.id.img_flag)
    ImageView imgFlag;
    @BindView(R2.id.conertext_tag)
    ConerTextView conertextTag;
    @BindView(R2.id.text_time)
    TextView textTime;
    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.text_content)
    TextView textContent;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.img_pic)
    ImageView imgPic;
    @BindView(R2.id.text_zan)
    TextView textZan;
    @BindView(R2.id.text_eva)
    TextView textEva;
    @BindView(R2.id.text_watch)
    TextView textWatch;

    private int userId;
    private TopicHome topicHome;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TOPIC_SUPPORT: {
                //在详情页点赞，收到通知刷新点赞状态及其点赞数量
                int topicId = (int) event.get("topicId");
                boolean isSupport = (boolean) event.get("isSupport");
                if (topicHome != null && topicHome.getTopicId() == topicId) {
                    topicHome.setIsSupportBool(isSupport);
                    topicHome.setTopicSupportNum(topicHome.getTopicSupportNum() + 1);
                    textZan.setSelected(topicHome.isSupport());
                    textZan.setText(String.valueOf(topicHome.getTopicSupportNum()));
                }
                break;
            }
            case EventBean.EVENTBUS_ADD_TOPIC_COMMENT: {
                //在详情页评论，收到通知刷新评论数量
                int topicId = (int) event.get("topicId");
                if (topicHome != null && topicHome.getTopicId() == topicId) {
                    topicHome.setTopicCommentNum(topicHome.getTopicCommentNum() + 1);
                    textEva.setText(String.valueOf(topicHome.getTopicCommentNum()));
                }
                break;
            }
        }
    }

    public DetailTopicController(View root, int userId) {
        super(root);
        this.userId = userId;
        int layout = R.layout.lay_item_topic;
        addLoadingLayout();
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
        netGetNewTopicByUser(userId);
    }

    private void setTopicData(TopicHome bean) {
        if (bean != null) {
            topicHome = bean;
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getUserCover());
            ImageLoaderHelper.loadImgTest(imgPic);
            ImageLoaderHelper.loadImg(imgPic, bean.getTopicImage());
            textTitle.setText(bean.getTitle());
            textContent.setText(bean.getFirstStrff());
            textName.setText(bean.getUserName());
            textZan.setSelected(bean.isSupport());
            textZan.setText(String.valueOf(bean.getTopicSupportNum()));
            textEva.setText(String.valueOf(bean.getTopicCommentNum()));
            textWatch.setText(String.valueOf(bean.getTopicReadNum()));
            textTime.setText(TimeUtils.date2String(bean.getCreateTime(), "MM-dd"));
            conertextTag.setTagText(bean.getTagStr());
            imgFlag.setVisibility(bean.isFree() ? View.GONE : View.VISIBLE);

            textZan.setOnClickListener(v -> {
                IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
                NetZanHelper.newInstance().topicZan(iView, textZan, bean.getTopicId(), !bean.isSupport(), (isZan, zanCount) -> {
                    bean.setIsSupportBool(isZan);
                    bean.setTopicSupportNum(zanCount);
                });
            });
            getRoot().setOnClickListener(v -> {
                // 是否需要支付
                if (!bean.isFree() && !bean.isPay()) {
                    // 处理支付
                    DialogPay.showPayTopic(getIView(), getFragmentManager(), bean.getPrice(), -1, () -> {
                        NetPayStoneHelper.newInstance().netPayTopic(getIView(), bean.getTopicId(), bean.getPrice(), () -> {
                            bean.setIsPay(true);
                            CommonHelper.TopicHelper.startTopicDetail(getContext(), bean.getTopicId());
                            //阅读数+1
                            bean.setTopicReadNum(bean.getTopicReadNum() + 1);
                            textWatch.setText(bean.getTopicReadNum());
                        });
                    });
                } else {
                    CommonHelper.TopicHelper.startTopicDetail(getContext(), bean.getTopicId());
                    //阅读数+1
                    bean.setTopicReadNum(bean.getTopicReadNum() + 1);
                    textWatch.setText(bean.getTopicReadNum());
                }
            });
        }
    }

    ///////////////////////////////////////

    public void netGetNewTopicByUser(int userId) {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getNewTopicByUser(userId),
                new ErrorHandleSubscriber<BaseJson<TopicHomeDetail>>() {
                    @Override
                    public void onNext(BaseJson<TopicHomeDetail> basejson) {
                        TopicHomeDetail topicHomeDetail = basejson.getData();
                        if (topicHomeDetail != null) {
                            setTopicData(topicHomeDetail.tans2TopicHome());
                            getLoadingLayout().showOut();
                        } else {
                            getLoadingLayout().showLackView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        getLoadingLayout().showFailView();
                    }
                }, () -> {
                    getLoadingLayout().showLoadingView();
                }, null);
    }
}
