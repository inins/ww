package com.wang.social.funshow.mvp.ui.controller;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.NetShareHelper;
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
import com.frame.component.helper.NetZanHelper;
import com.frame.component.utils.FunShowUtil;
import com.wang.social.socialize.SocializeUtil;

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
    private int userId;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA:
                int zanCount = (int) event.get("zanCount");
                int commonCount = (int) event.get("commonCount");
                int shareCount = (int) event.get("shareCount");
                boolean isSupport = (boolean) event.get("isSupport");
                userId = (int) event.get("userId");
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
        popupWindow.setOnDislikeClickListener(v -> netDislike(userId));
        adapterZan = new RecycleAdapterZan();
        adapterZan.setOnMoreClickListener(v -> ZanUserListActivity.start(getContext(), talkId));
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
            share();
        } else if (id == R.id.img_more) {
            popupWindow.showPopupWindow(v);
        }
    }

    private void share() {
        if (getContext() instanceof AppCompatActivity) {
            SocializeUtil.shareWeb(((AppCompatActivity) getContext()).getSupportFragmentManager(),
                    new SocializeUtil.SimpleShareListener() {
                        @Override
                        public void onResult(int platform) {
                            FunShowUtil.addSubTextViewCount(textShare, true);
                            NetShareHelper.newInstance().netShareFunshow(null, null, talkId, null);
                            //同时通知列表刷新数量
                            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_DETAIL_ADD_SHARE));
                        }
                    },
                    "http://www.wangsocial.com/",
                    "往往",
                    "有点2的社交软件",
                    "http://resouce.dongdongwedding.com/activity_cashcow_moneyTree.png");
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

    private void netDislike(int userId) {
        if (userId == 0) return;
        if (userId == AppDataHelper.getUser().getUserId()) {
            ToastUtil.showToastShort("不能屏蔽自己");
            return;
        }
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(CommonService.class).shatDownUser(userId + "", 1),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_LIST_FRESH));
                        if (getContext() instanceof Activity) ((Activity) getContext()).finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
