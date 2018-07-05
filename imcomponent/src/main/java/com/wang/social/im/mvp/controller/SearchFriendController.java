package com.wang.social.im.mvp.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.common.NetParam;
import com.frame.component.entities.PersonalInfo;
import com.frame.component.entities.dto.SearchUserInfoDTO;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.StringUtils;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.SearchActivityV2;
import com.wang.social.im.mvp.ui.SearchFriendActivity;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SearchFriendController extends BaseController {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_more)
    TextView btnMore;
    @BindView(R2.id.text_title)
    TextView textTitle;
    private RecycleAdapterSearchFriend adapter;
    private String key;

    public SearchFriendController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.im_lay_search_fram;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        textTitle.setText("好友");
        adapter = new RecycleAdapterSearchFriend();
        adapter.setOnItemClickListener((personalInfo, position) -> {
            CommonHelper.ImHelper.startPersonalCardForBrowse(getContext(), personalInfo.getUserId());
        });
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        btnMore.setOnClickListener(view -> {
            SearchFriendActivity.start(getContext(), key);
        });

        getRoot().setVisibility(View.GONE);
    }

    @Override
    protected void onInitData() {
    }

    public void search(String key) {
        this.key = key;
        netGetSearchList(key);
    }

    //////////////////////分页查询////////////////////

    private boolean isEmpty;

    public boolean isEmpty() {
        return isEmpty;
    }

    private void netGetSearchList(String key) {
        boolean isMobile = StringUtils.isMobileNO(key);
        Map<String, Object> param = new NetParam()
                .put("key", isMobile ? null : key)
                .put("phone", isMobile ? key : null)
                .put("current", 1)
                .put("size", 20)
                .put("v", "2.0.2")
                .build();
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(CommonService.class).chatListSearchUser(param),
                new ErrorHandleSubscriber<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>>() {
                    @Override
                    public void onNext(BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>> basejson) {
                        PageListDTO<SearchUserInfoDTO, PersonalInfo> warp = basejson.getData();
                        PageList<PersonalInfo> transform = warp != null ? warp.transform() : null;
                        List<PersonalInfo> list = transform.getList();
                        if (!StrUtil.isEmpty(list)) {
                            adapter.refreshData(list);
                            getRoot().setVisibility(View.VISIBLE);
                            btnMore.setVisibility(list.size() > 5 ? View.VISIBLE : View.GONE);
                            isEmpty = false;
                        } else {
                            getRoot().setVisibility(View.GONE);
                            isEmpty = true;
                        }
                        if (getContext() instanceof SearchActivityV2) {
                            ((SearchActivityV2) getContext()).notifyData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
