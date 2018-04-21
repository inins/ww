package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.base.BaseListWrap;
import com.wang.social.personal.mvp.entities.privates.PrivateDetail;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyActivity extends BasicAppActivity implements IView {

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;
    @BindView(R2.id.text_friend)
    TextView textFriend;
    @BindView(R2.id.text_ql)
    TextView textQl;
    @BindView(R2.id.text_qs)
    TextView textQs;
    @BindView(R2.id.text_topic)
    TextView textTopic;
    @BindView(R2.id.switch_show)
    SwitchButton switchShow;

    public static void start(Context context) {
        Intent intent = new Intent(context, PrivacyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_PRIVATE_UPDATE:
                netGetPrivateList();
                break;
        }
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_privacy;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        netGetPrivateList();
        switchShow.setOnCheckedChangeListener((buttonView, isChecked) -> netShowAge());
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_friend) {
            PrivacyShowListActivity.start(this, 1);
        } else if (i == R.id.btn_ql) {
            PrivacyShowListActivity.start(this, 2);
        } else if (i == R.id.btn_qs) {
            PrivacyShowListActivity.start(this, 3);
        } else if (i == R.id.btn_topic) {
            PrivacyShowListActivity.start(this, 4);
        }
    }

    private void setPrivateData(List<PrivateDetail> privacyList) {
        textFriend.setText(R.string.personal_privacy_default);
        textQl.setText(R.string.personal_privacy_default);
        textQs.setText(R.string.personal_privacy_default);
        textTopic.setText(R.string.personal_privacy_default);
        if (!StrUtil.isEmpty(privacyList)) {
            for (PrivateDetail privacy : privacyList) {
                switch (privacy.getType()) {
                    case 1:
                        textFriend.setText(privacy.getShowText());
                        break;
                    case 2:
                        textQl.setText(privacy.getShowText());
                        break;
                    case 3:
                        textQs.setText(privacy.getShowText());
                        break;
                    case 4:
                        textTopic.setText(privacy.getShowText());
                        break;
                }
            }
        }
        switchShow.setCheckedNoEvent(PrivateDetail.getShowAgeBool(privacyList));
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void netGetPrivateList() {
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).privateList(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<PrivateDetail>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<PrivateDetail>> basejson) {
                        BaseListWrap<PrivateDetail> wrap = basejson.getData();
                        if (wrap != null) {
                            List<PrivateDetail> list = wrap.getList();
                            setPrivateData(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    private void netShowAge() {
        ApiHelperEx.execute(this, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).privateShowAge(),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
