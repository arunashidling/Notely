package com.pe.notes.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by aruna on 23/01/18.
 */

public class CommonUtils {
    private static final String FILTER_SHARED_PREF = "filter_shared_pref";
    private static final String IS_FILTER_APPLIED = "is_filter_applied";
    public static final String IS_FILTER_HEART_APPLIED = "is_filter_heart_applied";
    public static final String IS_FILTER_STAR_APPLIED = "is_filter_star_applied";


    public static boolean getIsFilterApplied(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        boolean isFilterApplied = sharedPref.getBoolean(IS_FILTER_APPLIED, false);
        return isFilterApplied;
    }

    public static void setIsFilterApplies(Context context,boolean isFilterApplied) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean(IS_FILTER_APPLIED, isFilterApplied);
        edit.apply();

    }

    public static void setFilters(Context context, boolean isheartApplied, boolean isStarApplied) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean(IS_FILTER_HEART_APPLIED, isheartApplied);
        edit.putBoolean(IS_FILTER_STAR_APPLIED, isStarApplied);
        if(isheartApplied || isStarApplied){
            edit.putBoolean(IS_FILTER_APPLIED, true);
        }else{
            edit.putBoolean(IS_FILTER_APPLIED, false);
        }

        edit.apply();
    }

   /* public static void setStarFilters(Context context, boolean isStarApplied) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean(IS_FILTER_STAR_APPLIED, isStarApplied);
        edit.apply();
    }*/

    public static HashMap<String, Boolean> getAppliedFilters(Context context) {
        HashMap<String, Boolean> filters = new HashMap<String, Boolean>();
        SharedPreferences sharedPref = context.getSharedPreferences(
                FILTER_SHARED_PREF, Context.MODE_PRIVATE);
        filters.put(IS_FILTER_HEART_APPLIED, sharedPref.getBoolean(IS_FILTER_HEART_APPLIED, false));
        filters.put(IS_FILTER_STAR_APPLIED, sharedPref.getBoolean(IS_FILTER_STAR_APPLIED, false));
        return filters;

    }




}
