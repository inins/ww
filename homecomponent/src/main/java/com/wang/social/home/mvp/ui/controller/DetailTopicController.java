package com.wang.social.home.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.ConerTextView;
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

    public DetailTopicController(View root, int userId) {
        super(root);
        this.userId = userId;
        int layout = R.layout.home_item_topic;
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
                ToastUtil.showToastShort("topcId:" + bean.getTopicId());
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
