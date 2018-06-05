package com.frame.component.helper;


import android.support.v4.app.FragmentManager;

import com.frame.component.view.DialogPay;
import com.frame.mvp.IView;

public class CheckPayHelper {

    public static void checkAndPayTopic(IView view, FragmentManager fragmentManager, int topicId, OnPayCallback callback) {
        NetIsShoppingHelper.newInstance().isTopicShopping(view, topicId, rsp -> {
            if (!rsp.isFree() && !rsp.isPay()) {
                DialogPay.showPayTopic(view, fragmentManager, rsp.getPrice(), -1, () -> {
                    NetPayStoneHelper.newInstance().netPayTopic(view, topicId, rsp.getPrice(), () -> {
                        if (callback != null) callback.onSuccess();
                    });
                });
            } else {
                if (callback != null) callback.onSuccess();
            }
        });
    }


    public interface OnPayCallback {
        void onSuccess();
    }
}
