package com.wang.social.im.mvp.model;

import android.text.TextUtils;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.frame.utils.AppUtils;
import com.wang.social.im.mvp.contract.CreateEnvelopContract;
import com.wang.social.im.mvp.model.api.EnvelopService;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.dto.CreateEnvelopResultDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 10:04
 * ============================================
 */
@ActivityScope
public class CreateEnvelopModel extends BaseModel implements CreateEnvelopContract.Model {

    @Inject
    public CreateEnvelopModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<CreateEnvelopResultDTO>> createEnvelop(EnvelopInfo.EnvelopType envelopType, int diamond, int count, String message, String groupId) {

        int versionCode = AppUtils.getAppVersionCode();
        // FIXME: 2018-04-27 修改为ChannelUtils.getChannelCode()
        int channelCode = 1;
        int type = 0;
        switch (envelopType) {
            case PRIVATE:
                type = 0;
                break;
            case EQUAL:
                type = 1;
                break;
            case SPELL:
                type = 2;
                break;
        }

        NetParam param = new NetParam()
                .put("v", "2.0.0")
                .put("diamond", diamond)
                .put("packType", type)
                .put("packCount", count)
                .put("remark", message)
                .put("versionCode", versionCode)
                .put("channelCode", channelCode);
        if (!TextUtils.isEmpty(groupId)) {
            param = param.put("groupId", groupId);
        }
        return mRepositoryManager
                .obtainRetrofitService(EnvelopService.class)
                .createEnvelop(param.putSignature().build());
    }
}
