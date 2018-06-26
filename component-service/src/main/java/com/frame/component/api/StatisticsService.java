package com.frame.component.api;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.GroupBean;
import com.frame.component.entities.PersonalInfo;
import com.frame.component.entities.Topic;
import com.frame.component.entities.UserWrap;
import com.frame.component.entities.dto.AccountBalanceDTO;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.frame.component.entities.dto.GroupBeanDTO;
import com.frame.component.entities.dto.GroupMemberInfoDTO;
import com.frame.component.entities.dto.IsShoppingRspDTO;
import com.frame.component.entities.dto.MyTalkBeanDTO;
import com.frame.component.entities.dto.NewMoneyTreeGameDTO;
import com.frame.component.entities.dto.PersonalInfoDTO;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.component.entities.dto.SearchUserInfoDTO;
import com.frame.component.entities.dto.SettingInfoDTO;
import com.frame.component.entities.dto.TalkBeanDTO;
import com.frame.component.entities.dto.TopicDTO;
import com.frame.component.entities.dto.VersionInfoDTO;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.funshow.FunshowGroup;
import com.frame.component.entities.funshow.FunshowMe;
import com.frame.component.entities.topic.TopicGroup;
import com.frame.component.entities.topic.TopicMe;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.ui.acticity.wwfriendsearch.entities.dto.SearchResultDTO;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-22 17:11
 * =========================================
 */

public interface StatisticsService {

    String HEADER_CONTENT_TYPE = "Content-Type:application/x-www-form-urlencoded; charset=utf-8";

    /**
     * 安装APP埋点记录接口
     */
    @FormUrlEncoded
    @POST("collectionAppInstall/appInstall?v=2.0.2")
    Observable<Object> appInstall(@FieldMap Map<String, Object> param);
}