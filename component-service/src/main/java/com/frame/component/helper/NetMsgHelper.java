package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetMsgHelper {

    private NetMsgHelper() {
    }

    public static NetMsgHelper newInstance() {
        return new NetMsgHelper();
    }

    public void readSysMsg(){
        netReadSysMsg(0);
    }

    public void readFriendMsg(){
        netReadSysMsg(1);
    }

    public void readGroupMsg(){
        netReadSysMsg(2);
    }

    public void readGroupJoinMsg(){
        netReadSysMsg(3);
    }

    public void readZanMsg(){
        netReadDynMsg(1);
    }

    public void readEvaMsg(){
        netReadDynMsg(2);
    }

    public void readAiteMsg(){
        netReadDynMsg(3);
    }

    /**
     * 系统消息已读
     * 0：系统消息；1：好友申请；2：加群申请；3：群邀请
     */
    private void netReadSysMsg(int type) {
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(CommonService.class).readSysMsg(type),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    /**
     * 动态消息已读
     * 1：动态点赞；2：动态评论 3：@我的
     */
    private void netReadDynMsg(int type) {
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(CommonService.class).readDynMsg(type),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

}
