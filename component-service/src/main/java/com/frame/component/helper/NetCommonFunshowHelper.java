package com.frame.component.helper;

import android.support.annotation.IntDef;

import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.funshow.FunshowGroup;
import com.frame.component.entities.funshow.FunshowMe;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 * 趣晒列表在工程中有多个接口，这里统一整合成一个，通过 type 标识接口
 * 忽略掉不同接口返回数据类型不相同的问题，转换为统一的模型 FunshowBean，在callback中回调
 */

public class NetCommonFunshowHelper {

    public static final int TYPE_ME = 0xffa101;
    public static final int TYPE_GROUP = 0xffa102;

    @IntDef({TYPE_ME,
            TYPE_GROUP,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private NetCommonFunshowHelper() {
    }

    public static NetCommonFunshowHelper newInstance() {
        return new NetCommonFunshowHelper();
    }


    public void netGetFunshowList(IView view, boolean needLoading, Integer groupId, int current, int size, @Type int type, OnFunshowBeanCallback callback) {
        switch (type) {
            case TYPE_ME:
                netGetFunshowByMe(view, needLoading, current, size, callback);
                break;
            case TYPE_GROUP:
                netGetFunshowByGroup(view, needLoading, groupId, current, size, callback);
                break;
        }
    }

    private void netGetFunshowByMe(IView view, boolean needLoading, int current, int size, OnFunshowBeanCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class).getFunshowList(current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowMe>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowMe>> basejson) {
                        BaseListWrap<FunshowMe> warp = basejson.getData();
                        List<FunshowBean> list = warp != null ? FunshowMe.tans2FunshowBeanList(warp.getList()) : null;
                        if (callback != null) callback.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) callback.onError(e);
                    }
                });
    }

    private void netGetFunshowByGroup(IView view, boolean needLoading, int groupId, int current, int size, OnFunshowBeanCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class).getGroupFunshowList(groupId, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowGroup>> basejson) {
                        BaseListWrap<FunshowGroup> warp = basejson.getData();
                        List<FunshowBean> list = warp != null ? FunshowGroup.tans2FunshowBeanList(warp.getList()) : null;
                        if (callback != null) callback.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) callback.onError(e);
                    }
                });
    }

    ////////////////////////////////

    public interface OnFunshowBeanCallback {
        void onSuccess(List<FunshowBean> list);

        void onError(Throwable e);
    }
}
