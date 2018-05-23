package com.frame.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.utils.FunShowUtil;
import com.frame.component.utils.VideoCoverUtil;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import butterknife.BindView;

public class FunshowView extends FrameLayout implements View.OnClickListener {

    private ImageView imgHeader;
    private ImageView imgPic;
    private ImageView imgMore;
    private TextView textName;
    private TextView textTime;
    private TextView textTitle;
    private ImageView imgPlayer;
    private ImageView imgTagPay;
    private TextView textPicCount;
    private TextView textPosition;
    private TextView textZan;
    private TextView textComment;
    private TextView textShare;
    private Group groupHeader;

    private boolean isShowHeader;
    private FunshowBean funshowBean;

    public FunshowView(@NonNull Context context) {
        this(context, null);
    }

    public FunshowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunshowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FunshowView);
        isShowHeader = typedArray.getBoolean(R.styleable.FunshowView_show_header, false);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_funshow, this, true);
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        imgHeader = findViewById(R.id.img_header);
        imgPic = findViewById(R.id.img_pic);
        imgMore = findViewById(R.id.img_more);
        textName = findViewById(R.id.text_name);
        textTime = findViewById(R.id.text_time);
        textTitle = findViewById(R.id.text_title);
        imgTagPay = findViewById(R.id.img_tag_pay);
        imgPlayer = findViewById(R.id.img_player);
        textPicCount = findViewById(R.id.text_pic_count);
        textPosition = findViewById(R.id.text_position);
        textZan = findViewById(R.id.text_zan);
        textComment = findViewById(R.id.text_comment);
        textShare = findViewById(R.id.text_share);
        groupHeader = findViewById(R.id.group_header);
        textZan.setOnClickListener(this);
        groupHeader.setVisibility(isShowHeader ? VISIBLE : GONE);
    }

    public void setData(FunshowBean bean) {
        if (bean == null) return;
        funshowBean = bean;
        ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
        textName.setText(bean.getNickname());
        textTitle.setText(bean.getContent());
        textZan.setText(bean.getSupportTotal() + "");
        textComment.setText(bean.getCommentTotal() + "");
        textShare.setText(bean.getShareTotal() + "");
        textPicCount.setText("1/" + bean.getPicNum());
        textTime.setText(FunShowUtil.getFunshowTimeStr(bean.getCreateTime()));
        textZan.setSelected(bean.isSupport());
        imgTagPay.setVisibility(!bean.isFree() ? View.VISIBLE : View.GONE);
        imgPlayer.setVisibility(bean.isVideo() ? View.VISIBLE : View.GONE);
        textPosition.setText(bean.getPositionText());
        textPosition.setVisibility(!TextUtils.isDigitsOnly(bean.getPositionText()) ? VISIBLE : GONE);

//        不再解析视频第一帧，性能耗费太大
//        if (TextUtils.isEmpty(bean.getShowPic()) && !TextUtils.isEmpty(bean.getVideoUrl())) {
//            //如果没有封面，并且有视频链接，则自行解析视频第一帧作为封面
//            if (bean.hasAuth()) {
//                ImageLoaderHelper.loadImg(imgPic, bean.getVideoUrl());
//            } else {
//                ImageLoaderHelper.loadBlurImg(imgPic, bean.getVideoUrl());
//            }
//        }
        if (bean.hasAuth()) {
            ImageLoaderHelper.loadImg(imgPic, bean.getShowPic());
        } else {
            ImageLoaderHelper.loadBlurImg(imgPic, bean.getShowPic());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_zan) {
            IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
            NetZanHelper.newInstance().funshowZan(iView, textZan, funshowBean.getId(), !textZan.isSelected(), (isZan, zanCount) -> {
                funshowBean.setSupport(isZan);
                funshowBean.setSupportTotal(zanCount);
                if (onZanCallback != null) onZanCallback.onSuccess(isZan, zanCount);
            });
        }
    }

    ////////////////////////////// get & set //////////////////

    public ImageView getMoreBtn() {
        return imgMore;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    /////////////////////////////// interface /////////////////

    NetZanHelper.OnZanCallback onZanCallback;

    public void setZanCallback(NetZanHelper.OnZanCallback callback) {
        this.onZanCallback = callback;
    }
}
