package com.wang.social.location.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.location.R;
import com.wang.social.location.R2;

import butterknife.BindView;

/**
 * 显示坐标位置
 */
public class LocationShowActivity extends BasicAppActivity {

    private static final String EXTRA_LATITUDE = "latitude";
    private static final String EXTRA_LONGITUDE = "longitude";

    @BindView(R2.id.ls_map)
    MapView lsMap;
    @BindView(R2.id.ls_toolbar)
    SocialToolbar lsToolbar;

    private AMap mMap;

    public static void start(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context, LocationShowActivity.class);
        intent.putExtra(EXTRA_LATITUDE, latitude);
        intent.putExtra(EXTRA_LONGITUDE, longitude);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsMap.onCreate(savedInstanceState);

        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lsMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lsMap.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        lsMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        lsMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.loc_ac_location_show;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initMap();

        double latitude = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0f);
        double longitude = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0f);

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.addMarker(new MarkerOptions().position(latLng));
    }

    private void setListener() {
        lsToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    private void initMap() {
        mMap = lsMap.getMap();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }
}
