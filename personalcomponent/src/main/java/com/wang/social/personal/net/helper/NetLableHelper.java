package com.wang.social.personal.net.helper;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.lable.MiChat;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetLableHelper {


    private NetLableHelper() {
    }

    public static NetLableHelper newInstance() {
        return new NetLableHelper();
    }

    /**
     * 获取某标签下的觅聊列表
     */
    public void netGetMiListByLable(IView view, boolean needLoading, int tagId, String tagName, OnMiChatListCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(UserService.class).getTagMiList(tagId, tagName, 1, 1000),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<MiChat>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<MiChat>> baseJson) {
                        BaseListWrap<MiChat> wrap = baseJson.getData();
                        List<MiChat> list = wrap != null ? wrap.getList() : null;
                        if (callback != null) callback.success(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) callback.onError(e);
                    }
                });
    }

    /**
     * 获取某标签下的觅聊数量
     */
    public void netGetMiCountByLible(IView view, boolean needLoading, Lable lable, OnMiChatCountCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(UserService.class).getTagMiList(lable.getId(), lable.getName(), 1, 1000),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<MiChat>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<MiChat>> baseJson) {
                        BaseListWrap<MiChat> wrap = baseJson.getData();
                        List<MiChat> list = wrap != null ? wrap.getList() : null;
                        int count = list != null ? list.size() : 0;
                        if (callback != null) callback.success(count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnMiChatListCallback {
        void success(List<MiChat> miChats);

        void onError(Throwable e);
    }

    public interface OnMiChatCountCallback {
        void success(int count);
    }
}
