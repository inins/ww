package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.entities.AddGroupApplyRsp;
import com.frame.component.entities.AddGroupRsp;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetGroupHelper {

    private NetGroupHelper() {
    }

    public static NetGroupHelper newInstance() {
        return new NetGroupHelper();
    }

    /**
     * 创建加入趣聊、觅聊申请
     */
    public void addGroupMemberApply(IView view, int groupId, AddGroupMemberApplyCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class)
                        .addGroupMemberApply("2.0.0", groupId),
                new ErrorHandleSubscriber<BaseJson<AddGroupApplyRspDTO>>() {
                    @Override
                    public void onNext(BaseJson<AddGroupApplyRspDTO> bean) {
                        if (null != callback) {
                            callback.success(bean.getData().transform());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    public void addGroupMember(IView view, int applyId, AddGroupMemberCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class)
                        .addGroupMember("2.0.0", applyId),
                new ErrorHandleSubscriber<BaseJson<AddGroupRspDTO>>() {
                    @Override
                    public void onNext(BaseJson<AddGroupRspDTO> bean) {
                        if (null != callback) {
                            callback.success(bean.getData().transform());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    public interface AddGroupMemberApplyCallback {
        void success(AddGroupApplyRsp rsp);
    }

    public interface AddGroupMemberCallback {
        void success(AddGroupRsp rsp);
    }
}
