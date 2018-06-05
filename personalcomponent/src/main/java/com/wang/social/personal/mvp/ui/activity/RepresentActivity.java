package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.user.UserRepresent;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterRepresent;
import com.wang.social.socialize.SocializeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@RouteNode(path = "/profit", desc = "代言收益")
public class RepresentActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.img_pic)
    ImageView imgPic;
    @BindView(R2.id.text_code)
    TextView textCode;
    @BindView(R2.id.text_diamond)
    TextView textDiamond;
    @BindView(R2.id.text_user_count)
    TextView textUserCount;
    private RecycleAdapterRepresent adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, RepresentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_represont;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterRepresent();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetRepresentUserList(true);
            }

            @Override
            public void onLoadmore() {
                netGetRepresentUserList(false);
            }
        });
        springView.callFreshDelay();

        //FIXME:这张图片被移除了
        //ImageLoaderHelper.loadImgTest(imgPic);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            SocializeUtil.shareWeb(getSupportFragmentManager(),
                    null,
                    String.format(AppConstant.Share.SHARE_PROFIT_URL, CommonHelper.LoginHelper.getUserId()),
                    AppConstant.Share.SHARE_PROFIT_TITLE,
                    String.format(AppConstant.Share.SHARE_PROFIT_CONTENT, CommonHelper.LoginHelper.getUserName()),
                    AppConstant.Share.SHARE_PROFIT_IMAGE);
        } else if (i == R.id.btn_question) {
            WebActivity.start(this, AppConstant.Url.ruleDescription);
        }
    }

    private void setUserRepresentData(BaseListWrap wrap) {
        if (wrap != null) {
            textCode.setText(wrap.getUserCode());
            textDiamond.setText(wrap.getUserTotalMoney() + "");
            textUserCount.setText(wrap.getTotal() + "人");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetRepresentUserList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).getRepresentUserList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<UserRepresent>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<UserRepresent>> basejson) {
                        BaseListWrap<UserRepresent> warp = basejson.getData();
                        setUserRepresentData(warp);
                        List<UserRepresent> list = warp != null ? warp.getList() : null;
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }
}
