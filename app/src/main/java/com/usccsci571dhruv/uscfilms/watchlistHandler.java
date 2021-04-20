package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class watchlistHandler {

    private Activity appActivity;

    public watchlistHandler(Activity activity) {
        appActivity = activity;
    }

    public boolean isMediaInWatchlist(String media_type, String id) {
        SharedPreferences pref = appActivity.getSharedPreferences(
                appActivity.getString(R.string.watchlist_sp),0);
        String data_str = pref.getString("data", "None");
        if(data_str.equals("None")) return false;

        try {
            JSONArray data_list = new JSONArray(data_str);
            for(int i = 0; i < data_list.length(); i++) {
                JSONObject obj = data_list.getJSONObject(i);
                watchlistItem item = new watchlistItem(obj);
                if(item.media_type.equals(media_type) && item.id.equals(id)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMediaInWatchlist(watchlistItem item) {
        return this.isMediaInWatchlist(item.media_type, item.id);
    }

    public void add(watchlistItem item) {
        if(this.isMediaInWatchlist(item)) return;

        SharedPreferences pref = appActivity.getSharedPreferences(
                appActivity.getString(R.string.watchlist_sp),0);
        Editor editor = pref.edit();
        String data_str = pref.getString("data", "None");
        if(data_str.equals("None")) {
            data_str = "[]";
            editor.putString("data", data_str);
            editor.commit();
        }

        try {
            JSONArray data_list = new JSONArray(data_str);
            data_list.put(item.toJSONObject());
            data_str = data_list.toString();
            editor.putString("data", data_str);
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove(watchlistItem item) {
        if(!this.isMediaInWatchlist(item)) return;

        SharedPreferences pref = appActivity.getSharedPreferences(
                appActivity.getString(R.string.watchlist_sp),0);
        Editor editor = pref.edit();
        String data_str = pref.getString("data", "None");
        if(data_str.equals("None")) return;

        try {
            JSONArray data_list = new JSONArray(data_str);
            for(int i = 0; i < data_list.length(); i++) {
                watchlistItem tmp = new watchlistItem(data_list.getJSONObject(i));
                if(tmp.media_type.equals(item.media_type) && tmp.id.equals(item.id)) {
                    data_list.remove(i);
                    break;
                }
            }
            data_str = data_list.toString();
            editor.putString("data", data_str);
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<watchlistItem> get() {
        SharedPreferences pref = appActivity.getSharedPreferences(
                appActivity.getString(R.string.watchlist_sp),0);
        String data_str = pref.getString("data", "None");
        if(data_str.equals("None")) return new ArrayList<>();

        try {
            JSONArray data_list = new JSONArray(data_str);
            ArrayList<watchlistItem> res = new ArrayList<>();
            for(int i = 0; i < data_list.length(); i++) {
                res.add(new watchlistItem(data_list.getJSONObject(i)));
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void set(ArrayList<watchlistItem> watchlist) {
        SharedPreferences pref = appActivity.getSharedPreferences(
                appActivity.getString(R.string.watchlist_sp),0);
        Editor editor = pref.edit();
        JSONArray data_list = new JSONArray();
        for(int i = 0; i < watchlist.size(); i++) {
            data_list.put(watchlist.get(i).toJSONObject());
        }
        editor.putString("data", data_list.toString());
        editor.commit();
    }
}
