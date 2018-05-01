package com.javaman.olcudefteri.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

public class SharedPreferenceHelper {

    private static final String SHARED_PREFERENCE_FILE = "shared_data";
    private static SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceHelper(Context context){
        this.context=context;
    }

    public void setStringPreference(String key, String value) {

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

    }

    public String getStringPreference(String key,String defaultValue) {
        String value=null;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            value = sharedPreferences.getString(key, defaultValue);
        }
        return value;
    }


    public void setIntegerPreference(String key, int value) {

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }

    }

    public int getIntegerPreference(String key,int defaultValue) {
        int value = -1;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            value = sharedPreferences.getInt(key, defaultValue);
        }
        return value;
    }

    public boolean containKey(String key) {

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            return sharedPreferences.contains(key);
        }
        return false;
    }

    public void removeKey(String key){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            if(sharedPreferences.contains(key)){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.commit();
            }
        }

    }


    public void removeAll(){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

}
