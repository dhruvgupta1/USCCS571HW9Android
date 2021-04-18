package com.usccsci571dhruv.uscfilms;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private boolean mLoaded;
    private int mCurrentFrag;
    Lock _mLock = new ReentrantLock(true);
    private int data_points_loaded;

    ArrayList<ListMediaEntry> movies_new;
    ArrayList<ListMediaEntry> movies_top;
    ArrayList<ListMediaEntry> movies_popular;
    ArrayList<ListMediaEntry> tv_trending;
    ArrayList<ListMediaEntry> tv_top;
    ArrayList<ListMediaEntry> tv_popular;

    private HomeFragment frag_home;
    private SearchFragment frag_search;
    private WatchlistFragment frag_watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mLoaded = false;
        mCurrentFrag = 1;
        // Set Fragment to home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoadingScreenFragment()).commit();
        fetchData();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        if (item.getItemId() == R.id.nav_home) {
            mCurrentFrag = 1;
        } else if (item.getItemId() == R.id.nav_search) {
            mCurrentFrag = 2;
        } else if (item.getItemId() == R.id.nav_watchlist) {
            mCurrentFrag = 3;
        }
        if (mLoaded) {
            LoadCurrentFragment();
        }
        return true;
    };

    private void LoadCurrentFragment() {
        if (mCurrentFrag == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag_home).commit();
        } else if (mCurrentFrag == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag_search).commit();
        } else if (mCurrentFrag == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag_watchlist).commit();
        }
    }

    private void fetchData() {
        _mLock.lock();
        data_points_loaded = 0;
        _mLock.unlock();

        movies_new = new ArrayList<>();
        movies_popular = new ArrayList<>();
        movies_top = new ArrayList<>();
        tv_trending = new ArrayList<>();
        tv_popular = new ArrayList<>();
        tv_top = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url_movie_now = getString(R.string.api_base_url) + "list/media_type=movie&filter=current";
        String url_movie_top = getString(R.string.api_base_url) + "list/media_type=movie&filter=top";
        String url_movie_popular = getString(R.string.api_base_url) + "list/media_type=movie&filter=popular";
        String url_tv_trending = getString(R.string.api_base_url) + "list/media_type=tv&filter=trending";
        String url_tv_top = getString(R.string.api_base_url) + "list/media_type=tv&filter=top";
        String url_tv_popular = getString(R.string.api_base_url) + "list/media_type=tv&filter=popular";

        StringRequest req_movie_now = new StringRequest(Request.Method.GET,
                url_movie_now,
                response -> {
                    movies_new = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });
        StringRequest req_movie_top = new StringRequest(Request.Method.GET,
                url_movie_top,
                response -> {
                    movies_top = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });
        StringRequest req_movie_popular = new StringRequest(Request.Method.GET,
                url_movie_popular,
                response -> {
                    movies_popular = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });

        StringRequest req_tv_trending = new StringRequest(Request.Method.GET,
                url_tv_trending,
                response -> {
                    tv_trending = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });
        StringRequest req_tv_top = new StringRequest(Request.Method.GET,
                url_tv_top,
                response -> {
                    tv_top = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });
        StringRequest req_tv_popular = new StringRequest(Request.Method.GET,
                url_tv_popular,
                response -> {
                    tv_popular = stringToArray(response);
                    markLoaded();
                },
                error -> {
                    markLoaded();
                });

        queue.add(req_movie_now);
        queue.add(req_movie_popular);
        queue.add(req_movie_top);
        queue.add(req_tv_trending);
        queue.add(req_tv_popular);
        queue.add(req_tv_top);
    }

    private ArrayList<ListMediaEntry> stringToArray(String json_str) {
        ArrayList<ListMediaEntry> res = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json_str);
            JSONArray ar = obj.getJSONArray("results");
            for (int i = 0; i < ar.length(); i++) {
                res.add(new ListMediaEntry(ar.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void markLoaded() {
        _mLock.lock();
        data_points_loaded++;
        _mLock.unlock();
        if (data_points_loaded == 6) {
            frag_home = HomeFragment.newInstance(movies_new,
                    movies_top,
                    movies_popular,
                    tv_trending,
                    tv_top,
                    tv_popular);
            frag_search = new SearchFragment();
            frag_watchlist = new WatchlistFragment();
            mLoaded = true;
        }
        if (mLoaded) {
            LoadCurrentFragment();
        }
    }
}
