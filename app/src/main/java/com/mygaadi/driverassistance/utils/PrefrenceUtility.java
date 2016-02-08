package com.mygaadi.driverassistance.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.mygaadi.driverassistance.constants.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Vaibhav on 12/10/2015.
 */
public class PrefrenceUtility {
    Context mContext;
    private static PrefrenceUtility prefrenceUtility;
    private static SharedPreferences mSharedPrefrence;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences mInsurerSharedPreferences;
    private static SharedPreferences mCitySharedPreferences;
    private static SharedPreferences mRSACitySharedPreferences;

    private PrefrenceUtility(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
        mInsurerSharedPreferences = context.getSharedPreferences(Constants.APP_INSURER, Context.MODE_PRIVATE);
    }

    public static PrefrenceUtility getInstance(Context context) {
        if (prefrenceUtility == null) {
            prefrenceUtility = new PrefrenceUtility(context);
        }
        return prefrenceUtility;
    }


    public static ArrayList<String> getInsurersFromSharedPrefrence() {
        ArrayList<String> insurers = new ArrayList<>();
        insurers = fetchListFromPreference(mInsurerSharedPreferences.getAll());
        return insurers;
    }

    public static Map<String, ?> getInsurersMapFromSharedPrefrence() {

        return mInsurerSharedPreferences.getAll();
    }

//    public static void savePrevInsurerInSharedPrefrence(ArrayList<PrevInsurer> list) {
//        mSharedPrefrence = mPrevInsurerSharedPreferences;
//        ApplicationController.getDbManager().insertInInsurerTable(list);
//        /*for (PrevInsurer item : list) {
//            saveStringInSharedPref(item.getPreInsurerId(), item.getPreInsurerName());
//        }*/
//    }

//    public static ArrayList<String> getPrevInsurersFromSharedPrefrence() {
//        ArrayList<String> prevInsurer = new ArrayList<>();
//        prevInsurer = fetchListFromPreference(mPrevInsurerSharedPreferences.getAll());
//        return prevInsurer;
//    }

    public ArrayList<String> getRSACityFromSharedPrefrence() {
        ArrayList<String> cities = new ArrayList<>();
        cities.addAll(mRSACitySharedPreferences.getAll().keySet());
        return cities;
    }




    private static void saveStringInSharedPref(String key, String value) {
        SharedPreferences.Editor editor = mSharedPrefrence.edit();
        editor.putString(key, value);
        editor.commit();
    }


    private static void saveIntInSharedPref(String key, Integer value) {
        SharedPreferences.Editor editor = mSharedPrefrence.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getStringFromSharedPref(String key, String defaultValue, SharedPreferences sharedPreferences) {
        mSharedPrefrence = sharedPreferences;
        return mSharedPrefrence.getString(key, defaultValue);
    }

    private int getIntFromSharedPrefrence(String key, int defaultValue) {
        return mSharedPrefrence.getInt(key, defaultValue);

    }

    public void saveStringInDefaultSharedPrefrence(String key, String value) {
        mSharedPrefrence = sharedPreferences;
        saveStringInSharedPref(key, value);
    }


    public static void saveIntInDefaultSharedPrefrence(String key, int value) {
        mSharedPrefrence = sharedPreferences;
        saveIntInSharedPref(key, value);
    }

    public String getStringFromDefaultSharedPrefrence(String key, String defaultvalue) {
        return sharedPreferences.getString(key, defaultvalue);
    }


    public static int getIntFromDefaultSharedPrefrence(String key, int defaultvalue) {
        return sharedPreferences.getInt(key, defaultvalue);
    }

    public static String getUserIdFromSharedPreference() {
        return String.valueOf(sharedPreferences.getInt(Constants.USERID, 0));
    }

    public static SharedPreferences getInsurerSharedPreferences() {

        return mInsurerSharedPreferences;
    }

    @NonNull
    private static ArrayList<String> fetchListFromPreference(Map<String, ?> prefValues) {
        Collection<String> arr;
        arr = (Collection<String>) prefValues.values();
        ArrayList<String> list;
        list = new ArrayList(arr);
        return list;
    }

    public static void clearSharedPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor = mCitySharedPreferences.edit();
        editor.clear();
        editor.commit();
        editor = mInsurerSharedPreferences.edit();
        editor.clear();
        editor.commit();

    }
}
