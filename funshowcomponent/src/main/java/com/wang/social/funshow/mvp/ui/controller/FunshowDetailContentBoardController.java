package com.wang.social.funshow.mvp.ui.controller;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetIsShoppingHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.DialogPay;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiException;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.funshow.FunshowDetail;
import com.wang.social.funshow.mvp.entities.funshow.FunshowDetailVideoRsc;
import com.wang.social.funshow.mvp.entities.funshow.Pic;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.view.CtrlVideoView;
import com.wang.social.funshow.mvp.ui.view.MusicBubbleView;
import com.frame.component.utils.FunShowUtil;
import com.wang.social.pictureselector.ActivityPicturePreview;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

public class FunshowDetailContentBoardController extends FunshowDetailBaseController implements View.OnClickListener {

    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_position)
    TextView textPosition;
    @BindView(R2.id.text_time)
    TextView textTime;
    @BindView(R2.id.musicbubble)
    MusicBubbleView musicbubble;

    BundleImgView bundleview;
    JZVideoPlayerStandard videoview;

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
        imgHeader.setOnClickListener(this);
    }

    @Override
    public void onInitData() {
        netGetFunshowDetail();
    }

    private void setData(FunshowDetail funshowDetail) {
        if (funshowDetail != null) {
            if (!funshowDetail.isHideName()) {
                ImageLoaderHelper.loadCircleImg(imgHeader, funshowDetail.getAvatar());
                textName.setText(funshowDetail.getUserNickname());
            } else {
                imgHeader.setImageResource(R.drawable.ic_default_header);
                textName.setText(funshowDetail.getUserNickname());
            }
            textTitle.setText(funshowDetail.getTalkContent());
            textTime.setText(FunShowUtil.getFunshowTimeStr(funshowDetail.getCreateTime()));
            textPosition.setText(funshowDetail.getProvince() + funshowDetail.getCity());

            if (funshowDetail.hasVideo()) {
                setVideoData(funshowDetail);
            } else {
                setPicData(funshowDetail);
                setMusicData(funshowDetail.getMusicRsc());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_header) {
            if (funshowDetail != null && !funshowDetail.isHideName())
                CommonHelper.ImHelper.startPersonalCardForBrowse(getContext(), funshowDetail.getUserId());
        }
    }

    //初始化视频视图并设置视频
    private void setVideoData(FunshowDetail funshowDetail) {
        //加载视频视图
        ViewStub stub = getRoot().findViewById(R.id.stub_video);
        videoview = stub.inflate().findViewById(R.id.videoview);
        videoview.getLayoutParams().height = SizeUtils.dp2px(360);
        //获取视频资源
        FunshowDetailVideoRsc videoRsc = funshowDetail.getVideoRsc();
        //获取视频封面图片(如果没有封面，解析视频第一帧作为封面，这里只有一个视频所以不必太担心解析视频的性能问题)
        String picUrl = !StrUtil.isEmpty(funshowDetail.getPics()) ? funshowDetail.getPics().get(0).getUrl() : videoRsc.getUrl();
        //初始化视频，并设置封面
        videoview.setUp(videoRsc.getUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        videoview.startButton.performClick();
        ImageLoaderHelper.loadImg(videoview.thumbImageView, picUrl);
    }

    //设置音乐
    private void setMusicData(FunshowDetailVideoRsc musicRsc) {
        if (musicRsc != null) {
            musicbubble.resetMusic(musicRsc.trans2Music());
            musicbubble.setVisibility(View.VISIBLE);
        } else {
            musicbubble.setVisibility(View.GONE);
        }
    }

    //初始化图片视图并设置图片集
    private void setPicData(FunshowDetail funshowDetail) {
        ViewStub stub = getRoot().findViewById(R.id.stub_pic);
        bundleview = stub.inflate().findViewById(R.id.bundleview);
        bundleview.setOnBundleClickListener(new BundleImgView.OnBundleSimpleClickListener() {
            @Override
            public void onPhotoShowClick(String path, int position) {
                ActivityPicturePreview.startBrowse(getContext(), position, bundleview.getResultsStrArray().toArray(new String[]{}));
            }
        });
        if (funshowDetail.getPicCount() != 0) {
            if (funshowDetail.getPicCount() == 1) {
                bundleview.setColcountWihi(1, 1.76f);
            } else if (funshowDetail.getPicCount() == 2) {
                bundleview.setColcountWihi(2, 0.87f);
            } else if (funshowDetail.getPicCount() >= 3 && funshowDetail.getPicCount() <= 9) {
                bundleview.setColcountWihi(3, 0.87f);
            }
            bundleview.setPhotos(funshowDetail.getBundleImgEntities());
            bundleview.setVisibility(View.VISIBLE);
        } else {
            bundleview.setVisibility(View.GONE);
        }
    }

    public boolean onBackPressed() {
        return videoview != null ? videoview.backPress() : false;
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (videoview != null) videoview.releaseAllVideos();
    }

    private void checkAndshowPayDialog() {
        //如果是需要付费的内容，则弹出付费提示，并生产遮挡蒙层
        NetIsShoppingHelper.newInstance().isTalkShopping(getIView(), talkId, rsp -> {
            if (!rsp.isFree() && !rsp.isPay()) {
                //弹出付费弹出
                DialogPay.showPayFunshow(getIView(), getFragmentManager(), rsp.getPrice(), -1, () -> {
                    NetPayStoneHelper.newInstance().netPayFunshow(getIView(), talkId, rsp.getPrice(), () -> {
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_PAYED).put("talkId", talkId));
                    });
                }).setCancelCallback(() -> {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).finish();
                    }
                });
            }
        });
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
                        if (e instanceof ApiException) {
                            int errorCode = ((ApiException) e).getErrorCode();
                            if (errorCode == 102015) {
                                //未支付
                                checkAndshowPayDialog();
                            }
                        }
                    }
                });
    }

    ////////////////// get & set ///////////////////


    public FunshowDetail getFunshowDetail() {
        return funshowDetail;
    }
}
