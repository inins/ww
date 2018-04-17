package com.wang.social.funshow.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.frame.base.BasicFragment;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleFragmentComponent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 */

public class FunShowFragment extends BasicFragment {

    @BindView(R2.id.barview)
    BarView barview;

    @Inject
    ImageLoaderHelper imageLoaderHelper;

    public static FunShowFragment newInstance() {
        Bundle args = new Bundle();
        FunShowFragment fragment = new FunShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funshow_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        barview.setOnLoadImageCallBack((imageView, imgUrl) -> imageLoaderHelper.loadCircleImg(imageView, imgUrl));
        barview.refreshData(new ArrayList<BarUser>() {{
            add(new BarUser("http://i-7.vcimg.com/trim/48b866104e7efc1ffd7367e7423296c11060910/trim.jpg"));
            add(new BarUser("https://tse3-mm.cn.bing.net/th?id=OIP.XzZcrXAIrxTtUH97rMlNGQHaEo&p=0&o=5&pid=1.1"));
            add(new BarUser("http://photos.tuchong.com/23552/f/624083.jpg"));
        }});
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSingleFragmentComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }
}
