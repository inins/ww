package com.wang.social.personal.data.db;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.frame.utils.StrUtil;
import com.frame.utils.Utils;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/4/2.
 */

public class AddressDataBaseManager {

    private static final String TAG = "AddressDataBaseManager";
    private static final String DB_NAME = "china_regions.db";

    private static AddressDataBaseManager instance;

    public static void init() {
        DataBaseUtil.packDataBase(Utils.getContext(), DB_NAME);
    }

    private AddressDataBaseManager() {
    }

    public static synchronized AddressDataBaseManager getInstance() {
        if (instance == null) instance = new AddressDataBaseManager();
        return instance;
    }

    public List<Province> queryProvince() {
        SQLiteDatabase db = Utils.getContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ArrayList<Province> provinces = new ArrayList<>();
        Cursor cursor = db.rawQuery("select province_id,name from province", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int province_id = cursor.getInt(cursor.getColumnIndex("province_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            provinces.add(new Province(province_id, name));
            cursor.moveToNext();
        }
        db.close();
        return provinces;
    }

    public Province queryProvinceById(int provinceId) {
        SQLiteDatabase db = Utils.getContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ArrayList<Province> provinces = new ArrayList<>();
        Cursor cursor = db.rawQuery("select province_id,name from province where province_id = ?", new String[]{String.valueOf(provinceId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int province_id = cursor.getInt(cursor.getColumnIndex("province_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            provinces.add(new Province(province_id, name));
            cursor.moveToNext();
        }
        db.close();
        if (!StrUtil.isEmpty(provinces)) return provinces.get(0);
        else return null;
    }

    public List<City> queryCityByProvinceId(int provinceId) {
        SQLiteDatabase db = Utils.getContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ArrayList<City> cities = new ArrayList<>();
        Cursor cursor = db.rawQuery("select city_id,province_id,name from city where province_id = ?", new String[]{String.valueOf(provinceId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
            int province_id = cursor.getInt(cursor.getColumnIndex("province_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            cities.add(new City(city_id, province_id, name));
            cursor.moveToNext();
        }
        db.close();
        return cities;
    }

    public City queryCityById(int cityId) {
        SQLiteDatabase db = Utils.getContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ArrayList<City> cities = new ArrayList<>();
        Cursor cursor = db.rawQuery("select city_id,province_id,name from city where city_id = ?", new String[]{String.valueOf(cityId)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
            int province_id = cursor.getInt(cursor.getColumnIndex("province_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            cities.add(new City(city_id, province_id, name));
            cursor.moveToNext();
        }
        db.close();
        if (!StrUtil.isEmpty(cities)) return cities.get(0);
        return null;
    }
}
