package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.acticity.PhotoActivity;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.funshow.FunshowDetail;
import com.wang.social.funshow.mvp.entities.funshow.FunshowDetailVideoRsc;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.view.MusicBubbleView;
import com.wang.social.funshow.utils.FunShowUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class FunshowDetailContentBoardController extends BaseController {

    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.bundleview)
    BundleImgView bundleview;
    @BindView(R2.id.text_position)
    TextView textPosition;
    @BindView(R2.id.text_time)
    TextView textTime;
    @BindView(R2.id.musicbubble)
    MusicBubbleView musicbubble;

    private int talkId;
    private FunshowDetail funshowDetail;


    public FunshowDetailContentBoardController(View root, int talkId) {
        super(root);
        this.talkId = talkId;
        int layout = R.layout.funshow_lay_detail_contentboard;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textName);
        FontUtils.boldText(textTitle);
        bundleview.setOnBundleClickListener(new BundleImgView.OnBundleSimpleClickListener() {
            @Override
            public void onPhotoShowClick(String path, int position) {
                PhotoActivity.start(getContext(), bundleview.getResultsStrArray(), position);
            }
        });
    }

    @Override
    protected void onInitData() {
        netGetFunshowDetail();
    }

    private void setData(FunshowDetail funshowDetail) {
        if (funshowDetail != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, funshowDetail.getAvatar());
            textName.setText(funshowDetail.getUserNickname());
            textTitle.setText(funshowDetail.getTalkContent());
            textTime.setText(FunShowUtil.getFunshowTimeStr(funshowDetail.getCreateTime()));
            textPosition.setText(funshowDetail.getProvince() + funshowDetail.getCity());
            //设置图片集
            if (funshowDetail.getPicCount() == 1) {
                bundleview.setColcountWihi(1, 1.76f);
            } else if (funshowDetail.getPicCount() == 2) {
                bundleview.setColcountWihi(2, 0.87f);
            } else if (funshowDetail.getPicCount() >= 3 && funshowDetail.getPicCount() <= 9) {
                bundleview.setColcountWihi(3, 0.87f);
            }
            bundleview.setPhotos(funshowDetail.getBundleImgEntities());
            //设置音乐
            FunshowDetailVideoRsc musicRsc = funshowDetail.getMusicRsc();
            if (musicRsc != null) {
                musicbubble.resetMusic(musicRsc.trans2Music());
                musicbubble.setVisibility(View.VISIBLE);
            }else {
                musicbubble.setVisibility(View.GONE);
            }
        }
    }

    /////////////////////////////////////////

    public void changeBundleView(int colcount, float wihi) {
        bundleview.setColcountWihi(colcount, wihi);
    }

    public int getBundleViewColCount() {
        return bundleview.getColcount();
    }

    /////////////////////////////////////////

    private void netGetFunshowDetail() {
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(FunshowService.class).getFunshowDetail(talkId),
                new ErrorHandleSubscriber<BaseJson<FunshowDetail>>() {
                    @Override
                    public void onNext(BaseJson<FunshowDetail> basejson) {
                        funshowDetail = basejson.getData();
                        setData(funshowDetail);
                        if (funshowDetail != null) {
                            //通知其他控制器，接收数据
                            EventBean eventBean = new EventBean(EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA);
                            eventBean.put("zanCount", funshowDetail.getSupportNum());
                            eventBean.put("commonCount", funshowDetail.getCommentNum());
                            eventBean.put("shareCount", funshowDetail.getShareNum());
                            eventBean.put("isSupport", funshowDetail.isSupport());
                            eventBean.put("userId", funshowDetail.getUserId());
                            EventBus.getDefault().post(eventBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
