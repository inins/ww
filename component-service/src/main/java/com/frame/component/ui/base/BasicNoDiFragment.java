package com.frame.component.ui.base;

import android.support.annotation.NonNull;

import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;

public abstract class BasicNoDiFragment extends BasicFragment implements IView{

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public void showLoading() {
        if (getActivity() instanceof BasicAppActivity){
            ((BasicAppActivity) getActivity()).showLoadingDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BasicAppActivity){
            ((BasicAppActivity) getActivity()).hideLoadingDialog();
        }
    }
}
