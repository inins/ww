package com.wang.social.location.mvp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.frame.base.BaseAdapter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.location.R;
import com.wang.social.location.R2;
import com.wang.social.location.mvp.model.entities.LocationInfo;
import com.wang.social.location.mvp.ui.adapters.LocationAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 位置选择
 */
public class LocationActivity extends BasicAppActivity implements LocationSource, AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, DialogInterface.OnCancelListener {

    private final String TAG = this.getClass().getCanonicalName();
    private static final float GUIDELINE_BASE_PERCENT = 0.60f;
    private static final float GUIDELINE_CHANGE_MAX_PERCENT = 0.2f;

    public static final String RESULT_EXTRA_KEY = "location";

    @BindView(R2.id.loc_guideline)
    Guideline locGuideline;
    @BindView(R2.id.loc_toolbar)
    SocialToolbar locToolbar;
    @BindView(R2.id.loc_locations)
    RecyclerView locLocations;
    @BindView(R2.id.loc_load)
    SpringView locLoad;
    @BindView(R2.id.loc_map)
    MapView locMap;
    @BindView(R2.id.loc_marker)
    ImageView locIvMarker;

    private BottomSheetBehavior<SpringView> mBehavior;

    private AMap mMap;
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mLocationChangedListener;

    private boolean isLocation;
    private GeocodeSearch mGeocodeSearch;
    private String mCityCode;

    private LocationInfo mMapLocation;
    private LocationAdapter mAdapter;
    private int mPageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListener();

        locMap.onCreate(savedInstanceState);

        initMap();
    }

    @Override
    protected void onResume() {
        locMap.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        locMap.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        locMap.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        mBehavior.setBottomSheetCallback(null);
        mBehavior = null;
        locMap.onDestroy();
        mMap.setOnCameraChangeListener(null);
        mMap = null;
        mGeocodeSearch.setOnGeocodeSearchListener(null);
        mGeocodeSearch = null;
        mAdapter = null;
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.loc_ac_location;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        locLoad.setEnableHeader(false);
        locLoad.setFooter(new AliFooter(this, false));
        locLocations.setLayoutManager(new LinearLayoutManager(this));
        locLocations.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new LocationAdapter();
        locLocations.setAdapter(mAdapter);
    }

    private void setListener() {
        mBehavior = BottomSheetBehavior.from(locLoad);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float percent = GUIDELINE_BASE_PERCENT - GUIDELINE_CHANGE_MAX_PERCENT * slideOffset;
                locGuideline.setGuidelinePercent(percent);
            }
        });

        locToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                    case RIGHT_TEXT:
                        LocationInfo location = mAdapter.getSelectLocation();
                        //同时以2种方式返回位置信息，ActivityResults的方法返回参数的同时post位置参数
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_LOCATION_SELECT).put("location", location));
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_EXTRA_KEY, location);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });

        locLoad.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadmore() {
                searchLocation(new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude()), mCityCode, false);
            }
        });

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<LocationInfo>() {
            @Override
            public void onItemClick(LocationInfo locationInfo, int position) {
                mAdapter.setSelectedPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangedListener = onLocationChangedListener;

        initLocation();
    }

    @Override
    public void deactivate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mLocationChangedListener = null;
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
        dialogLoading.get().setOnCancelListener(this);
    }

    private void initMap() {
        mMap = locMap.getMap();

        //设置定位小蓝点样式
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.loc_location_marker));
        int color = Color.argb(50, 0, 0, 0);
        locationStyle.radiusFillColor(color);// 设置圆形的填充颜色
        locationStyle.strokeColor(color);
        locationStyle.strokeWidth(0.5f);

        mMap.setMyLocationStyle(locationStyle);
        mMap.setLocationSource(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mMap.setOnCameraChangeListener(this);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = getDefaultOption();
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(this);

        showLoadingDialog();
        mLocationClient.startLocation();
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

    /**
     * 获取到位置信息
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            isLocation = true;
            if (mLocationChangedListener != null) {
                if (mMapLocation == null) {
                    mMapLocation = new LocationInfo();
                }
                mMapLocation.setLatitude(aMapLocation.getLatitude());
                mMapLocation.setLongitude(aMapLocation.getLongitude());
                mMapLocation.setPlace(aMapLocation.getPoiName());
                mMapLocation.setAddress(aMapLocation.getAddress());
                mMapLocation.setProvince(aMapLocation.getProvince());
                mMapLocation.setCity(aMapLocation.getCity());


                Location location = new Location("AMap");
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());
                location.setTime(aMapLocation.getTime());
                location.setAccuracy(aMapLocation.getAccuracy());

                mLocationChangedListener.onLocationChanged(location);
            }
        } else {//定位失败

        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (isLocation) {
            LatLonPoint point = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
            RegeocodeQuery query = new RegeocodeQuery(point, 200f, GeocodeSearch.AMAP);
            mGeocodeSearch.getFromLocationAsyn(query);
            animMarker();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        dismissLoadingDialog();
        if (regeocodeResult != null) {
            if (mMapLocation == null) {
                mMapLocation = new LocationInfo();
            }
            RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
            mCityCode = address.getCityCode();
            mMapLocation.setProvince(address.getProvince());
            mMapLocation.setCity(address.getCity());
            mMapLocation.setLatitude(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude());
            mMapLocation.setLongitude(regeocodeResult.getRegeocodeQuery().getPoint().getLongitude());
            mMapLocation.setAddress(address.getFormatAddress()
                    .replace(address.getProvince(), "")
                    .replace(address.getCity(), ""));
            mMapLocation.setPlace(mMapLocation.getAddress()
                    .replace(address.getDistrict(), "")
                    .replace(address.getNeighborhood(), ""));

            searchLocation(new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude()), address.getCityCode(), true);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 移动完成后执行一个跳动动画
     */
    private void animMarker() {
        int translateY = SizeUtils.dp2px(12);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -translateY);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(150L);
        animation.setInterpolator(new DecelerateInterpolator());
        locIvMarker.startAnimation(animation);
    }

    /**
     * 查找附近位置
     *
     * @param latLng
     * @param cityCode
     */
    private void searchLocation(LatLng latLng, String cityCode, final boolean refresh) {
        PoiSearch.Query query = new PoiSearch.Query("", "", cityCode);
        query.setPageSize(15);
        if (refresh) {
            mPageNum = 1;
        } else {
            mPageNum += 1;
        }
        query.setPageNum(mPageNum);

        PoiSearch search = new PoiSearch(this, query);
        search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 1000));
        search.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult != null && poiResult.getPois() != null) {
                    List<LocationInfo> locations = new ArrayList<>();
                    for (PoiItem item : poiResult.getPois()) {
                        LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setCity(item.getCityName());
                        locationInfo.setProvince(item.getProvinceName());
                        locationInfo.setPlace(item.getTitle());
                        locationInfo.setAddress(item.getSnippet());
                        locationInfo.setAdCode(item.getAdCode());
                        locationInfo.setLatitude(item.getLatLonPoint().getLatitude());
                        locationInfo.setLongitude(item.getLatLonPoint().getLongitude());
                        locations.add(locationInfo);
                    }
                    if (refresh) {
                        locations.add(0, mMapLocation);
                        mAdapter.setSelectedPosition(0);
                        mAdapter.refreshData(locations);
                    } else {
                        locLoad.onFinishFreshAndLoadDelay();

                        mAdapter.getData().addAll(locations);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        search.searchPOIAsyn();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finish();
    }
}
