package com.frame.component.helper;

import android.support.annotation.IntDef;

import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Topic;
import com.frame.component.entities.topic.TopicGroup;
import com.frame.component.entities.topic.TopicMe;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 * 话题列表在工程中有多个接口，这里统一整合成一个，通过 type 标识接口
 * 忽略掉不同接口返回数据类型不相同的问题，转换为统一的模型 Topic，在callback中回调
 */

public class NetCommonTopicHelper {

    public static final int TYPE_ME = 0xffa101;
    public static final int TYPE_GROUP = 0xffa102;

    @IntDef({TYPE_ME,
            TYPE_GROUP,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private NetCommonTopicHelper() {
    }

    public static NetCommonTopicHelper newInstance() {
        return new NetCommonTopicHelper();
    }


    public void netGetTopicList(IView view, boolean needLoading, Integer groupId, int current, int size, @Type int type, OnTopicCallback callback) {
        switch (type) {
            case TYPE_ME:
                netGetTopicByMe(view, needLoading, current, size, callback);
                break;
            case TYPE_GROUP:
                netGetTopicByGroup(view, needLoading, groupId, current, size, callback);
                break;
        }
    }

    private void netGetTopicByMe(IView view, boolean needLoading, int current, int size, OnTopicCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class).getTopicList(current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopicMe>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<TopicMe>> basejson) {
                        BaseListWrap<TopicMe> warp = basejson.getData();
                        List<Topic> list = TopicMe.tans2TopicList(warp.getList());
                        if (callback != null) callback.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) onError(e);
                    }
                });
    }

    private void netGetTopicByGroup(IView view, boolean needLoading, int groupId, int current, int size, OnTopicCallback callback) {
        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class).getGroupTopicList(groupId, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopicGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<TopicGroup>> basejson) {
                        BaseListWrap<TopicGroup> warp = basejson.getData();
                        List<Topic> list = TopicGroup.tans2TopicList(warp.getList());
                        if (callback != null) callback.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) onError(e);
                    }
                });
    }

    ////////////////////////////////

    public interface OnTopicCallback {
        void onSuccess(List<Topic> list);

        void onError(Throwable e);
    }
}
