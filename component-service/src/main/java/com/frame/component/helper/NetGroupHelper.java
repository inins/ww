package com.frame.component.helper;

import android.content.Context;

import com.frame.component.api.CommonService;
import com.frame.component.app.Constant;
import com.frame.component.entities.AddGroupApplyRsp;
import com.frame.component.entities.AddGroupRsp;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.ui.dialog.PayDialog;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetGroupHelper {

    private NetGroupHelper() {
    }

    public static NetGroupHelper newInstance() {
        return new NetGroupHelper();
    }

    public void addGroup(Context context, IView view , int groupId, AddGroupHelperCallback callback) {
        netAddGroupMemberApply(view, groupId, addGroupApplyRsp -> {
            //applyId:11,	//订单申请ID
            //payState:0,    //0：不需要支付,1:需要调用支付接口
            //gemstone:100,  //所需宝石数
            //needValidation:true   //是否需要群主同意  true/false

            // 需要群主验证，弹出对话框
            if (addGroupApplyRsp.isNeedValidation()) {
                DialogValiRequest.showDialog(
                        context,
                        content -> {
                            netAddGroupMember(context, view,
                                    content,
                                    addGroupApplyRsp,
                                    addGroupRsp -> {
                                        successToast(addGroupApplyRsp.isNeedValidation());
                                        if (null != callback) {
                                            callback.success(addGroupApplyRsp.isNeedValidation());
                                        }
                                    });
                        });
            } else {
                netAddGroupMember(context, view,
                        Integer.toString(addGroupApplyRsp.getApplyId()),
                        addGroupApplyRsp,
                        addGroupRsp -> {
                            successToast(addGroupApplyRsp.isNeedValidation());
                            if (null != callback) {
                                callback.success(addGroupApplyRsp.isNeedValidation());
                            }
                        });
            }
        });
    }

    private void successToast(boolean isNeedValidation) {
        if (isNeedValidation) {
            ToastUtil.showToastLong("申请成功，请等待群主验证");
        } else {
            // 不需要要群主验证，已经成功加入群
            ToastUtil.showToastLong("成功加入该群");
        }
    }

    private void netAddGroupMember(Context context, IView view, String applyDesc,
                                   AddGroupApplyRsp addGroupApplyRsp, AddGroupMemberCallback callback) {
        if (addGroupApplyRsp.getPayState() == 1) {
            // 需要支付
            String message = String.format("加入此趣聊需要支付%1d宝石", addGroupApplyRsp.getGemstone());
            PayDialog payDialog = new PayDialog(context,
                    () -> pay(
                            view,
                            addGroupApplyRsp.getApplyId(),
                            applyDesc,
                            addGroupApplyRsp.getGemstone(),
                            callback),
                    message,
                    String.valueOf(addGroupApplyRsp.getGemstone()));
            payDialog.show();
        } else {
            // 加入群
            netAddGroupMember(
                    view ,
                    addGroupApplyRsp.getApplyId(),
                    applyDesc,
                    callback);
        }
    }

    /**
     * 加群支付
     * @param applyId 申请id
     * @param price 支付价格
     */
    private void pay(IView view , int applyId, String applyDesc, int price, AddGroupMemberCallback callback) {
        NetPayStoneHelper.newInstance().stonePay(
                view,
                price,
                Constant.PAY_OBJECT_TYPE_ADD_GROUP,
                Integer.toString(applyId),
                () -> {
                    netAddGroupMember(view ,
                            applyId,
                            applyDesc,
                            callback);
                });
    }

    /**
     * 创建加入趣聊、觅聊申请
     */
    private void netAddGroupMemberApply(IView view, int groupId, AddGroupMemberApplyCallback callback) {
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

    /**
     * 尝试加入趣聊、觅聊
     */
    private void netAddGroupMember(IView view, int applyId, String applyDesc,
                                   AddGroupMemberCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class)
                        .addGroupMember("2.0.0", applyId, applyDesc),
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

    public interface AddGroupHelperCallback {
        void success(boolean isNeedValidation);
    }

    private interface AddGroupMemberApplyCallback {
        void success(AddGroupApplyRsp addGroupApplyRsp);
    }

    private interface AddGroupMemberCallback {
        void success(AddGroupRsp addGroupRsp);
    }
}
