package com.frame.component.helper;

import android.support.v4.app.FragmentManager;
import android.content.Context;

import com.frame.component.api.CommonService;
import com.frame.component.app.Constant;
import com.frame.component.common.NetParam;
import com.frame.component.entities.AddGroupApplyRsp;
import com.frame.component.entities.AddGroupRsp;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.component.service.R;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.ui.dialog.PayDialog;
import com.frame.component.utils.EntitiesUtil;
import com.frame.component.view.DialogPay;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;

import java.util.Map;

import retrofit2.http.Field;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetGroupHelper {

    private boolean mIsMi;

    private NetGroupHelper() {
    }

    public static NetGroupHelper newInstance() {
        return new NetGroupHelper();
    }

    public void addMiGroup(Context context, IView view, FragmentManager fragmentManager, int groupId, AddGroupHelperCallback callback) {
        mIsMi = true;
        addGroup(context, view, fragmentManager, groupId, callback);
    }

    //同意或拒绝加入趣聊
    public void applyAddGroup(Context context, IView view, FragmentManager fragmentManager, int groupId, int msgId, boolean isAgree, AddGroupHelperCallback callback) {
        netAgreeGroupApply(view, groupId, msgId, isAgree, addGroupApplyRsp -> {
            valiApply(context, view, fragmentManager, addGroupApplyRsp, callback);
        });
    }

    public void addGroup(Context context, IView view, FragmentManager fragmentManager, int groupId, AddGroupHelperCallback callback) {
        netAddGroupMemberApply(view, groupId, addGroupApplyRsp -> {
            //applyId:11,	//订单申请ID
            //payState:0,    //0：不需要支付,1:需要调用支付接口
            //gemstone:100,  //所需宝石数
            //needValidation:true   //是否需要群主同意  true/false
            valiApply(context, view, fragmentManager, addGroupApplyRsp, callback);
        });
    }

    private void valiApply(Context context, IView view, FragmentManager fragmentManager, AddGroupApplyRsp addGroupApplyRsp, AddGroupHelperCallback callback) {
        //applyId:11,	//订单申请ID
        //payState:0,    //0：不需要支付,1:需要调用支付接口
        //gemstone:100,  //所需宝石数
        //needValidation:true   //是否需要群主同意  true/false
        // 需要群主验证，弹出对话框
        if (addGroupApplyRsp.isNeedValidation()) {
            // 弹出输入验证信息的对话框
            DialogValiRequest.showDialog(
                    context,
                    content -> {
                        addGroupMember(view, fragmentManager,
                                content,
                                addGroupApplyRsp,
                                addGroupRsp -> {
                                    successToast(context, addGroupApplyRsp.isNeedValidation());
                                    if (null != callback) {
                                        callback.success(addGroupApplyRsp.isNeedValidation());
                                    }

                                    ToastUtil.showToastShort("请等待群主验证");
                                });
                    });
        } else {
            // 不需要群主验证，直接尝试加群
            addGroupMember(view, fragmentManager,
                    Integer.toString(addGroupApplyRsp.getApplyId()),
                    addGroupApplyRsp,
                    addGroupRsp -> {
                        successToast(context, addGroupApplyRsp.isNeedValidation());
                        if (null != callback) {
                            callback.success(addGroupApplyRsp.isNeedValidation());
                        }
                    });
        }
    }

    private void successToast(Context context, boolean isNeedValidation) {
        if (isNeedValidation) {
            ToastUtil.showToastLong(context.getResources()
                    .getString(R.string.group_add_success_need_validate));
        } else {
            // 不需要要群主验证，已经成功加入群
            ToastUtil.showToastLong(context.getResources()
                    .getString(R.string.group_add_success));
        }
    }

    private void addGroupMember(IView view, FragmentManager fragmentManager, String applyDesc,
                                AddGroupApplyRsp addGroupApplyRsp, AddGroupMemberCallback callback) {
        if (addGroupApplyRsp.getPayState() == 1) {
            // 需要支付,弹出支付对话框
            DialogPay.showPayAddGroup(view, fragmentManager, mIsMi,
                    addGroupApplyRsp.getGemstone(),
                    -1,
                    () -> {
                        // 支付
                        pay(view,
                                addGroupApplyRsp.getApplyId(),
                                "",
                                addGroupApplyRsp.getGemstone(),
                                callback);
                    });
        } else {
            // 加入群
            netAddGroupMember(
                    view,
                    addGroupApplyRsp.getApplyId(),
                    applyDesc,
                    callback);
        }
    }

    /**
     * 加群支付
     *
     * @param applyId 申请id
     * @param price   支付价格
     */
    private void pay(IView view, int applyId, String applyDesc, int price, AddGroupMemberCallback callback) {
        NetPayStoneHelper.newInstance().stonePay(
                view,
                price,
                Constant.PAY_OBJECT_TYPE_ADD_GROUP,
                Integer.toString(applyId),
                () -> {
                    netAddGroupMember(view,
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
     * 同意、拒绝邀请加入趣聊、觅聊（别人邀请我的）
     */
    public void netAgreeGroupApply(IView view, int groupId, int msgId, boolean isAgree, AddGroupMemberApplyCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).agreeGroupApply(groupId, msgId, isAgree ? 0 : 1),
                new ErrorHandleSubscriber<BaseJson<AddGroupApplyRspDTO>>() {
                    @Override
                    public void onNext(BaseJson<AddGroupApplyRspDTO> basejson) {
                        AddGroupApplyRspDTO dto = basejson.getData();
                        if (callback != null) callback.success(dto.transform());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    /**
     * 尝试加入趣聊、觅聊
     */
    private void netAddGroupMember(IView view, int applyId, String applyDesc,
                                   AddGroupMemberCallback callback) {
        Map<String, Object> param = new NetParam()
                .put("applyId", applyId)
                .put("applyDesc", applyDesc)
                .put("v", "2.0.0")
                .build();
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class)
                        .addGroupMember(param),
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
