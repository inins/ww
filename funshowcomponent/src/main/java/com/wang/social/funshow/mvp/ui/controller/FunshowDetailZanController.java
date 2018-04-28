package com.wang.social.funshow.mvp.ui.controller;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BaseController;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.event.CommentEvent;
import com.wang.social.funshow.mvp.entities.user.ZanUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.activity.ZanUserListActivity;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterZan;
import com.wang.social.funshow.mvp.ui.dialog.MorePopupWindow;
import com.wang.social.funshow.net.helper.NetZanHelper;
import com.wang.social.funshow.utils.FunShowUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class FunshowDetailZanController extends BaseController implements View.OnClickListener {

    @BindView(R2.id.recycler_zan)
    RecyclerView recyclerZan;
    @BindView(R2.id.img_more)
    ImageView imgMore;
    @BindView(R2.id.text_zan)
    TextView textZan;
    @BindView(R2.id.text_comment)
    TextView textComment;
    @BindView(R2.id.text_share)
    TextView textShare;
    @BindView(R2.id.text_zan_count)
    TextView textZanCount;

    private AppBarLayout appBarLayout;
    private EditText editEva;

    private RecycleAdapterZan adapterZan;
    private MorePopupWindow popupWindow;

    private int talkId;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA:
                int zanCount = (int) event.get("zanCount");
                int commonCount = (int) event.get("commonCount");
                int shareCount = (int) event.get("shareCount");
                boolean isSupport = (boolean) event.get("isSupport");
                textZan.setSelected(isSupport);
                textZan.setText(String.valueOf(zanCount));
                textComment.setText(String.valueOf(commonCount));
                textShare.setText(String.valueOf(shareCount));
                textZanCount.setText(zanCount + getContext().getResources().getString(R.string.funshow_home_funshow_detail_zan));
                break;
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA:
                FunShowUtil.addSubTextViewCount(textComment, true);
                break;
        }
    }

    public FunshowDetailZanController(View root, AppBarLayout appBarLayout, EditText editEva, int talkId) {
        super(root);
        this.talkId = talkId;
        this.appBarLayout = appBarLayout;
        this.editEva = editEva;
        int layout = R.layout.funshow_lay_detail_zan;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        popupWindow = new MorePopupWindow(getContext());
        popupWindow.setOnDislikeClickListener(v -> {
            ToastUtil.showToastShort("开发中。。");
        });

        adapterZan = new RecycleAdapterZan();
        adapterZan.setOnMoreClickListener(v -> {
            ZanUserListActivity.start(getContext(), talkId);
        });
        recyclerZan.setNestedScrollingEnabled(false);
        recyclerZan.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerZan.setAdapter(adapterZan);
        recyclerZan.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));

        imgMore.setOnClickListener(this);
        textZan.setOnClickListener(this);
        textComment.setOnClickListener(this);
        textShare.setOnClickListener(this);
    }

    @Override
    protected void onInitData() {
        netGetFunshowZanList();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_zan) {
            NetZanHelper.newInstance().funshowZan(getIView(), textZan, talkId, !textZan.isSelected(), (isZan, zanCount) -> {
                //刷新点赞列表
                FunShowUtil.addSubTextViewCountForEnd(textZanCount, isZan, getContext().getResources().getString(R.string.funshow_home_funshow_detail_zan));
                netGetFunshowZanList();
                //通知其他组件点赞状态
                EventBean eventBean = new EventBean(EventBean.EVENT_FUNSHOW_UPDATE_ZAN);
                eventBean.put("talkId", talkId);
                eventBean.put("isZan", isZan);
                eventBean.put("zanCount", zanCount);
                EventBus.getDefault().post(eventBean);
            });
        } else if (id == R.id.text_comment) {
            appBarLayout.setExpanded(false);
            KeyboardUtils.showSoftInput(editEva);
            EventBus.getDefault().post(new CommentEvent());
        } else if (id == R.id.text_share) {

        } else if (id == R.id.img_more) {
            popupWindow.showPopupWindow(v);
        }
    }

    ////////////////////////////////////////////

    private void netGetFunshowZanList() {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(FunshowService.class).funshowZanList(talkId, 1, 10),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<ZanUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<ZanUser>> basejson) {
                        BaseListWrap<ZanUser> wrap = basejson.getData();
                        List<ZanUser> zanUsers = wrap.getList();
                        if (!StrUtil.isEmpty(zanUsers)) {
                            adapterZan.refreshData(zanUsers);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
