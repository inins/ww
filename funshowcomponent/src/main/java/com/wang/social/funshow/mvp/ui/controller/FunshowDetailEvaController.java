package com.wang.social.funshow.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.LoadingLayout;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.entities.event.CommentEvent;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterEva;
import com.frame.component.utils.FunShowUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class FunshowDetailEvaController extends FunshowDetailBaseController implements BaseAdapter.OnItemClickListener<Comment> {

    int layout = R.layout.funshow_lay_detail_eva;

    @BindView(R2.id.recycler_eva)
    RecyclerView recyclerEva;
    SpringView springView;
    @BindView(R2.id.text_eva_count)
    TextView textEvaCount;
    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;

    private RecycleAdapterEva adapterEva;
    private EditText editEva;
    private int talkId;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA:
                netLoadEvaList(true);
                FunShowUtil.addSubTextViewCountForEnd(textEvaCount, true, getContext().getResources().getString(R.string.funshow_home_funshow_detail_eva));
                break;
            case EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA:
                int commonCount = (int) event.get("commonCount");
                textEvaCount.setText(commonCount + getContext().getResources().getString(R.string.funshow_home_funshow_detail_eva));
                break;
        }
    }

    public FunshowDetailEvaController(View root, SpringView springView, EditText editEva, int talkId) {
        super(root);
        this.springView = springView;
        this.editEva = editEva;
        this.talkId = talkId;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        adapterEva = new RecycleAdapterEva();
        adapterEva.setTalkId(talkId);
        adapterEva.setEditEva(editEva);
        adapterEva.setOnItemClickListener(this);
        recyclerEva.setNestedScrollingEnabled(false);
        recyclerEva.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerEva.setAdapter(adapterEva);
        recyclerEva.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netLoadEvaList(true);
            }

            @Override
            public void onLoadmore() {
                netLoadEvaList(false);
            }
        });
    }

    @Override
    public void onInitData() {
        springView.callFreshDelay();
    }


    @Override
    public void onItemClick(Comment comment, int position) {
        EventBus.getDefault().post(new CommentEvent(comment));
        KeyboardUtils.showSoftInput(editEva);
    }

    //////////////////////分页查询////////////////////
    private int current = 0;

    private int size = 20;

    public void netLoadEvaList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(FunshowService.class).funshowEvaList(talkId, ++current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Comment>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Comment>> basejson) {
                        BaseListWrap<Comment> warp = basejson.getData();
                        List<Comment> list = warp.getList();
                        Comment.convertList(list);
                        if (!StrUtil.isEmpty(list)) {
                            loadingview.showOut();
                            current = warp.getCurrent();
                            if (isFresh) adapterEva.refreshItem(list);
                            else adapterEva.addItem(list);
                        } else {
                            if (!isFresh) {
                                ToastUtil.showToastLong("没有更多数据了");
                            } else {
                                loadingview.showLackView();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        loadingview.showFailView();
                    }
                }, null, () -> {
                    springView.onFinishFreshAndLoadDelay();
                });
    }
}
