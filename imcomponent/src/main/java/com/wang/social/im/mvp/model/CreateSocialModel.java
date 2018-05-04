package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CreateSocialContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.model.entities.dto.CreateGroupResultDTO;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 20:25
 * ============================================
 */
@ActivityScope
public class CreateSocialModel extends BaseModel implements CreateSocialContract.Model {

    @Inject
    public CreateSocialModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<PayCheckInfoDTO>> checkCreateSocialStatus() {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .checkCreateSocialStatus("2.0.0");
    }

    @Override
    public Observable<BaseJson<CreateGroupResultDTO>> createSocial(String orderId, String name, String desc, String cover, String head, SocialAttribute attr, boolean canCreateTeam, String tags) {
        Integer gender = null;
        switch (attr.getGenderLimit()) {
            case MALE:
                gender = 0;
                break;
            case FEMALE:
                gender = 1;
                break;
        }
        StringBuffer ageLimit = new StringBuffer();
        for (SocialAttribute.AgeLimit limit : attr.getAgeLimit()) {
            if (limit == SocialAttribute.AgeLimit.UNLIMITED) {
                ageLimit = null;
                break;
            } else {
                switch (limit) {
                    case AFTER_90:
                        ageLimit.append("90,");
                        break;
                    case AFTER_95:
                        ageLimit.append("95,");
                        break;
                    case AFTER_00:
                        ageLimit.append("00,");
                        break;
                    case OTHER:
                        ageLimit.append("其他,");
                        break;
                }
            }
        }
        if (ageLimit != null){
            ageLimit.deleteCharAt(ageLimit.length() - 1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("v", "2.0.0");
        map.put("applyId", orderId);
        map.put("groupName", name);
        map.put("groupDesc", desc);
        map.put("groupCoverPlan", cover);
        map.put("headUrl", head);
        map.put("isOpen", attr.isOpen() ? 1 : 0);
        map.put("isFree", attr.isCharge() ? 0 : 1);
        map.put("gemstone", attr.getGem());
        if (gender != null) {
            map.put("gender", gender);
        }
        if (ageLimit != null) {
            map.put("ageRange", ageLimit.toString());
        }
        map.put("isCreateMi", canCreateTeam ? 1 : 2);
        map.put("tagIds", tags);
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .createSocial(map);
    }
}