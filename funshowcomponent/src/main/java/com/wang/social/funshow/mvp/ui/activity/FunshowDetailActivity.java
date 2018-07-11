package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetReportHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.widget.SpringView;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailContentBoardController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEditController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailEvaController;
import com.wang.social.funshow.mvp.ui.controller.FunshowDetailZanController;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import timber.log.Timber;

@RouteNode(path = "/detail", desc = "趣晒详情")
public class FunshowDetailActivity extends BasicAppNoDiActivity implements IView {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R2.id.edit_eva)
    EditText editEva;
    @BindView(R2.id.btn_right)
    TextView btnRight;

    private FunshowDetailContentBoardController contentBoardController;
    private FunshowDetailZanController zanController;
    private FunshowDetailEvaController evaController;
    private FunshowDetailEditController editController;

    private int talkId;

    public static void start(Context context, int talkId) {
        Intent intent = new Intent(context, FunshowDetailActivity.class);
        intent.putExtra("talkId", talkId);
        context.startActivity(intent);
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA:
                int userId = (int) event.get("userId");
                if (userId == CommonHelper.LoginHelper.getUserId()) {
                    btnRight.setText(R.string.funshow_home_funshow_detail_btn_right_del);
                } else {
                    btnRight.setText(R.string.funshow_home_funshow_detail_btn_right);
                }
                break;
            case EventBean.EVENT_FUNSHOW_PAYED: {
                //趣晒支付了
                int talkId = (int) event.get("talkId");
                if (talkId == this.talkId) {
                    contentBoardController.onInitData();
                    zanController.onInitData();
                    evaController.onInitData();
                }
                break;
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_funshow_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        Timber.i("启动趣晒详情");
        FocusUtil.focusToTop(toolbar);
        talkId = getIntent().getIntExtra("talkId", 0);

        //初始化各部分控制器
        contentBoardController = new FunshowDetailContentBoardController(findViewById(R.id.include_contentboard), talkId);
        zanController = new FunshowDetailZanController(findViewById(R.id.include_zan), appbarlayout, editEva, talkId);
        evaController = new FunshowDetailEvaController(findViewById(R.id.include_eva), springView, editEva, talkId);
        editController = new FunshowDetailEditController(findViewById(R.id.include_edit), talkId);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            if (btnRight.getText().equals(getString(R.string.funshow_home_funshow_detail_btn_right))) {
                DialogSure.showDialog(this, "确定要举报该趣晒？", () -> {
                    NetReportHelper.newInstance().netReportFunshow(this, talkId, () -> finish());
                });
            } else if (btnRight.getText().equals(getString(R.string.funshow_home_funshow_detail_btn_right_del))) {
                DialogSure.showDialog(this, "确定要删除该趣晒？", () -> {
                    netDelFunshow();
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (contentBoardController != null && contentBoardController.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contentBoardController != null) contentBoardController.onDestory();
        if (zanController != null) zanController.onDestory();
        if (evaController != null) evaController.onDestory();
        if (editController != null) editController.onDestory();
    }

    ///////////////////////////

    private void netDelFunshow() {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(FunshowService.class).delFunshow(talkId),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                        ToastUtil.showToastShort("删除成功");
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_DEL).put("talkId", talkId));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public FunshowDetailContentBoardController getContentBoardController() {
        return contentBoardController;
    }

    public FunshowDetailZanController getZanController() {
        return zanController;
    }

    public FunshowDetailEvaController getEvaController() {
        return evaController;
    }

    public FunshowDetailEditController getEditController() {
        return editController;
    }
}
