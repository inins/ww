package com.wang.social.im.mvp.model.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 17:03
 * ============================================
 */
public class PayCheckInfo {

    /**
     * 不需要支付
     */
    public static final int STATUS_WITHOUT = 0x001;
    /**
     * 需要支付
     */
    public static final int STATUS_NEED = 0x002;

    @Getter
    @Setter
    private String orderId;
    @Getter
    @Setter
    private int checkState;
}
