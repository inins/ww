package com.frame.component.helper;

import android.os.Build;

import com.frame.component.BuildConfig;
import com.frame.component.api.CommonService;
import com.frame.component.api.StatisticsService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.location.LocationInfo;
import com.frame.component.utils.ChannelUtils;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.PhoneUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/4.
 * 统计接口
 */

public class NetStatisticsHelper {


    private NetStatisticsHelper() {
    }

    public static NetStatisticsHelper newInstance() {
        return new NetStatisticsHelper();
    }

    /**
     * 安装APP埋点记录接口
     */
    public void netAppInstall() {
        //版本号
        int versionCode = AppUtils.getAppVersionCode();
        //渠道好
        int channelCode = ChannelUtils.getChannelCode();
        //获取经纬度，没有则为null
        LocationInfo locationInfo = AppDataHelper.getLocationInfo();
        Double longitude = null;
        Double latitude = null;
        if (locationInfo != null) {
            longitude = locationInfo.getLongitude();
            latitude = locationInfo.getLatitude();
        }

        Map<String, Object> param = NetParam.newInstance()
                .put("devicesKey", PhoneUtils.getIMEI())
                .put("channelId", channelCode + "")
                .put("appVersion", versionCode + "")
                .put("devicesModel", Build.MODEL)
                .put("devicesSystem", "android" + Build.VERSION.RELEASE)
                .put("longitude", longitude + "")
                .put("latitude", latitude + "")
                .build();
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(StatisticsService.class).appInstall(param),
                new ErrorHandleSubscriber<Object>() {
                    @Override
                    public void onNext(Object basejson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
