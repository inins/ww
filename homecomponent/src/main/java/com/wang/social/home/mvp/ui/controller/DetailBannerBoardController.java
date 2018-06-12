package com.wang.social.home.mvp.ui.controller;

import android.support.constraint.Group;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.ConerTextView;
import com.frame.component.view.bannerview.BannerView;
import com.frame.component.view.bannerview.Image;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.ui.activity.CardDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class DetailBannerBoardController extends BaseController {

    @BindView(R2.id.banner)
    BannerView banner;
    @BindView(R2.id.connertext_lable)
    ConerTextView connertextLable;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_lable_gender)
    TextView textLableGender;
    @BindView(R2.id.text_lable_astro)
    TextView textLableAstro;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_position)
    TextView textPosition;
    @BindView(R2.id.text_sign)
    TextView textSign;
    @BindView(R2.id.group_tag)
    Group groupTag;
    @BindView(R2.id.group_sign)
    Group groupSign;

    private int userId;
    private CardUser cardUser;

    public DetailBannerBoardController(View root, int userId, CardUser cardUser) {
        super(root);
        this.userId = userId;
        this.cardUser = cardUser;
        int layout = R.layout.home_lay_carddetail_bannerboard;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
        //如果cardUser不是null，则在加载过程中先展示cardUser中的数据
        if (cardUser != null) setUserData(cardUser.tans2UserBoard());
        netGetNewFunshow(userId);
    }

    private void setUserData(UserBoard user) {
        if (user != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, user.getAvatar());
            textName.setText(user.getNickname());
            textLableGender.setSelected(!user.isMale());
            textLableGender.setText(TimeUtils.getAgeByBirth(user.getBirthday()) + "");
            textLableGender.setVisibility(user.isShowAge() ? View.VISIBLE : View.GONE);
            textLableAstro.setText(TimeUtils.getAstro(user.getBirthday()));
            textPosition.setText(user.getCityName());
            textPosition.setVisibility(TextUtils.isEmpty(user.getCityName()) ? View.GONE : View.VISIBLE);
            connertextLable.setTagText(user.getTagTextDot());
            groupTag.setVisibility(TextUtils.isEmpty(user.getTagTextDot()) ? View.GONE : View.VISIBLE);
            textSign.setText(user.getAutograph());
            groupSign.setVisibility(TextUtils.isEmpty(user.getAutograph()) ? View.GONE : View.VISIBLE);
            if (!StrUtil.isEmpty(user.getPicList())) {
                banner.setDatas(user.getBannerImageList());
            } else {
                //如果相册是空，则添加一张默认图
                banner.setDatas(new ArrayList<Image>() {{
                    add(new Image(String.valueOf(R.drawable.home_bg_carddetail_default)));
                }});
            }
            //设置添加按钮可见性
            if (getContext() instanceof CardDetailActivity) {
                ((CardDetailActivity) getContext()).setAddBtnVisible(!user.isFriend());
            }
        }
    }

    ///////////////////////////////////////

    public BannerView getBannerView() {
        return banner;
    }

    ///////////////////////////////////////

    public void netGetNewFunshow(int userId) {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(CommonService.class).getUserInfoAndPhotos(userId),
                new ErrorHandleSubscriber<BaseJson<UserBoard>>() {
                    @Override
                    public void onNext(BaseJson<UserBoard> basejson) {
                        UserBoard user = basejson.getData();
                        setUserData(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

}
