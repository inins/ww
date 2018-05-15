package com.wang.social.im.mvp.contract;

import android.graphics.PointF;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.ShareInfo;
import com.wang.social.im.mvp.model.entities.ShareModel;
import com.wang.social.im.mvp.model.entities.dto.ShareDTO;
import com.wang.social.im.mvp.model.entities.dto.ShareInfoDTO;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:33
 * ============================================
 */
public interface HappyWoodContract {

    interface View extends IView {

        void showShareInfo(ShareInfo shareInfo);

        void showTree(List<ShareModel> data, Map<Integer, Integer> levmap, int maxWidth, int maxle, PointF startCenter);
    }

    interface Model extends IModel {

        Observable<BaseJson<ShareDTO>> getTreeData(String type, String id);

        Observable<BaseJson<ShareInfoDTO>> getShareInfo(String type, String id);
    }
}