package com.wang.social.im.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.component.helper.NetReportHelper;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.PersonalCard.ui.widget.DialogActionSheet;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.pictureselector.helper.PhotoHelperEx;

public class GroupPersonReportHelper {
    public final static String[] reports = {
            "语言谩骂/骚扰信息",
            "存在欺诈骗钱行为",
            "发布不适当内容"
    };

    public static void report(Activity activity, IView iView, FragmentManager fragmentManager,
                              int objectId) {

        DialogActionSheet.show(fragmentManager, reports)
                .setActionSheetListener(
                        (position, text) -> {
                            // 弹出图片选择
                            PhotoHelperEx.newInstance(activity, path -> {

                            })
                                    .setMaxSelectCount(5);
                        }
                );
    }

    private static void confirmReport(Context context, IView iView, FragmentManager fragmentManager,
                                      int objectId, String comment) {
        // 提示确认是否删除
        DialogSure.showDialog(context,
                "确定要举报该用户？",
                () -> NetReportHelper.newInstance()
                        .netReportPerson(
                                iView,
                                objectId,
                                comment,
                                () -> ToastUtil.showToastShort("举报成功")));
    }
}
