package com.wang.social.location.mvp.helper;

import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.location.mvp.model.entities.LocationInfo;

/**
 * 定位帮助类
 * 简化高德地图定位
 * {@link #startLocation()}    开始定位
 * {@link #stopLocation()}      结束定位
 * {@link #onDestroy()}         释放资源
 * {@link #setOnLocationListener(OnLocationListener)}   回调监听
 */
public class LocationHelper {

    private AMapLocationClient mLocationClient;

    private LocationHelper() {
        initLocation();
    }

    public static LocationHelper newInstance() {
        return new LocationHelper();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(Utils.getContext());
        mLocationClient.setLocationOption(getDefaultOption());
        mLocationClient.setLocationListener(onAmapLocationListener);
    }

    /**
     * 定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private AMapLocationListener onAmapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                LocationInfo locationInfo = new LocationInfo();
                locationInfo.setLatitude(aMapLocation.getLatitude());
                locationInfo.setLongitude(aMapLocation.getLongitude());
                locationInfo.setPlace(aMapLocation.getPoiName());
                locationInfo.setAddress(aMapLocation.getAddress());
                locationInfo.setProvince(aMapLocation.getProvince());
                locationInfo.setCity(aMapLocation.getCity());
                locationInfo.setAdCode(aMapLocation.getAdCode());
                if (onLocationListener != null) onLocationListener.onLocation(locationInfo);
            } else {
                ToastUtil.showToastShort("定位失败");
                if (onLocationListener != null) onLocationListener.onLocation(null);
            }
        }
    };

    public LocationHelper startLocation() {
        if (mLocationClient != null) mLocationClient.startLocation();
        return this;
    }

    public LocationHelper stopLocation() {
        if (mLocationClient != null) mLocationClient.stopLocation();
        return this;
    }

    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    ////////////////////////

    private OnLocationListener onLocationListener;

    public void setOnLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }

    public interface OnLocationListener {
        void onLocation(LocationInfo locationInfo);
    }
}
