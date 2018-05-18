package com.wang.social.im.helper;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.component.helper.NetReportHelper;
import com.frame.component.ui.dialog.DialogActionSheet;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;

public class GroupPersonReportHelper {
    public static final String[] reports = {
            "语言谩骂/骚扰信息",
            "存在欺诈骗钱行为",
            "发布不适当内容"
    };

    public static void doReport(FragmentManager fragmentManager, DialogActionSheet.ActionSheetListener listener) {
        DialogActionSheet.show(fragmentManager, reports)
                .setActionSheetListener(listener);
    }

    public static void confirmReportPerson(Context context, IView view, String hint, int objectId, String comment, String[] picUrls) {
        // 提示确认是否删除
        DialogSure.showDialog(context,
                hint,
                () -> NetReportHelper.newInstance()
                        .netReportPersonWithUpload(
                                view,
                                objectId,
                                comment,
                                picUrls,
                                () -> ToastUtil.showToastShort("举报成功")));
    }

    public static void confirmReportGroup(Context context, IView view, String hint, int objectId, String comment, String[] picUrls) {
        // 提示确认是否删除
        DialogSure.showDialog(context,
                hint,
                () -> NetReportHelper.newInstance()
                        .netReportGroupWithUpload(
                                view,
                                objectId,
                                comment,
                                picUrls,
                                () -> ToastUtil.showToastShort("举报成功")));
    }
}
