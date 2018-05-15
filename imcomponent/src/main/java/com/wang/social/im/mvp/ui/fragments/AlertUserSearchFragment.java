package com.wang.social.im.mvp.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.utils.KeyboardUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.ui.AlertUserListActivity;
import com.wang.social.im.mvp.ui.adapters.AlertUserSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 9:14
 * ============================================
 */
public class AlertUserSearchFragment extends BasicFragment implements BaseAdapter.OnItemClickListener<IndexMemberInfo> {

    @BindView(R2.id.aus_et_search)
    EditText ausEtSearch;
    @BindView(R2.id.aus_tvb_search)
    TextView ausTvbSearch;
    @BindView(R2.id.aus_search_result)
    RecyclerView ausSearchResult;

    private AlertUserSearchAdapter mAdapter;

    private List<IndexMemberInfo> mMembers;

    private Disposable mDisposable;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_alert_user_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ausSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        HVItemDecoration itemDecoration = new HVItemDecoration(getContext(), HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        itemDecoration.setLeftMargin(UIUtil.getDimen(R.dimen.common_border_margin));
        ausSearchResult.addItemDecoration(itemDecoration);
        mAdapter = new AlertUserSearchAdapter();
        mAdapter.setOnItemClickListener(this);
        ausSearchResult.setAdapter(mAdapter);

        ausEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = ausEtSearch.getText().toString();
                    if (!TextUtils.isEmpty(key)) {
                        //执行搜索
                        doSearch(ausEtSearch.getText().toString());
                    }
                    KeyboardUtils.hideSoftInput(ausEtSearch);
                }
                return false;
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {
        mMembers = (List<IndexMemberInfo>) data;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = null;
        mAdapter = null;
        mMembers = null;
    }

    @SuppressLint("CheckResult")
    private void doSearch(String key) {
        if (mMembers == null || mMembers.isEmpty()) {
            return;
        }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        Observable.just(key)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
                .map(new Function<String, List<IndexMemberInfo>>() {
                    @Override
                    public List<IndexMemberInfo> apply(String s) throws Exception {
                        List<IndexMemberInfo> result = new ArrayList<>();
                        for (IndexMemberInfo mMember : mMembers) {
                            if (mMember.getNickname().contains(s)) {
                                result.add(mMember);
                            }
                        }
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<IndexMemberInfo>>() {
                    @Override
                    public void accept(List<IndexMemberInfo> indexMemberInfos) throws Exception {
                        mAdapter.refreshData(indexMemberInfos);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            ausEtSearch.setText("");
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
        }else {
            ausEtSearch.requestFocus();
            KeyboardUtils.showSoftInput(ausEtSearch);
        }
    }

    @OnClick(R2.id.aus_tvb_search)
    public void onViewClicked() {
        getActivity().onBackPressed();
        KeyboardUtils.hideSoftInput(ausEtSearch);
    }

    @Override
    public void onItemClick(IndexMemberInfo indexMemberInfo, int position) {
        Intent intent = new Intent();
        intent.putExtra(AlertUserListActivity.EXTRA_MEMBER, indexMemberInfo);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}