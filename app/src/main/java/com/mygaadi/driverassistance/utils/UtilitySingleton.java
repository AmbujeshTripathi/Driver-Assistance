package com.mygaadi.driverassistance.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mygaadi.driverassistance.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.client.Response;

public final class UtilitySingleton {

    private static final String TAG = UtilitySingleton.class.getName();
    private static volatile UtilitySingleton instance = null;
    private final SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<String> mStatusTypes = new ArrayList<>();
    private String currentDateStr;

    private UtilitySingleton(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
    }

    public static UtilitySingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (UtilitySingleton.class) {
                instance = new UtilitySingleton(context);
            }
        }
        return instance;
    }


    // --------- click Listener in whole application-----------
    public void SetClickListener(int[] clickIDs, Context context) {
        if (context instanceof OnClickListener) {
            for (int id : clickIDs) {
                try {
                    ((Activity) context).findViewById(id).setOnClickListener((OnClickListener) context);
                } catch (Exception e) {
                    Log.e(TAG, "Wrong view ID sent.");
                }
            }
        }
    }

    public void hideSoftKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void ShowToast(String msg, Context context) {
        if (msg != null && !msg.trim().equalsIgnoreCase("")) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    public String getResponse(Response response) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(convertStreamToString(response.getBody().in()));
            return gson.toJson(je);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            try {
                return convertStreamToString(response.getBody().in());
            } catch (IOException e1) {
                e1.printStackTrace();
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void saveStringInSharedPref(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringFromSharedPref(String key) {
        return getStringFromSharedPref(key, null);
    }

    public String getStringFromSharedPref(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveIntInSharedPref(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntFromSharedPref(String key) {
        return getIntFromSharedPref(key, 0);
    }

    public int getIntFromSharedPref(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBooleanInSharedPref(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanFromSharedPref(String key) {
        return getBooleanFromSharedPref(key, false);
    }

    public boolean getBooleanFromSharedPref(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public ArrayList<String> getStatusTypes() {
        return mStatusTypes;
    }

    public void setStatusTypes(ArrayList<String> mStatusTypes) {
        this.mStatusTypes = mStatusTypes;
    }

    public void resetStatusTypes() {
        mStatusTypes.clear();
        currentDateStr = "";
    }

    public void setDateSelected(String currentDateStr) {
        this.currentDateStr = currentDateStr;
    }

    public String getCurrentDateStr() {
        return this.currentDateStr;
    }


    public void clearSharedPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
