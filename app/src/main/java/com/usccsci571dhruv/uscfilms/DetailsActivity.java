package com.usccsci571dhruv.uscfilms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DetailsActivity extends AppCompatActivity {

    private String media_type;
    private String media_id;

    private DetailsClasses.Details details;
    private String youtube_video;
    private ArrayList<DetailsClasses.Cast> castsArrayList;
    private ArrayList<DetailsClasses.Review> reviewArrayList;
    private ArrayList<ListMediaEntry> recommendedMoviesArrayList;

    private boolean mLoaded;
    private Lock _mLock = new ReentrantLock(true);
    private int data_points_loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        this.media_type = intent.getStringExtra("media_type");
        this.media_id = intent.getStringExtra("media_id");

        View loading_screen = findViewById(R.id.details_loading_screen);
        View content_screen = findViewById(R.id.details_content);
        content_screen.setVisibility(View.GONE);
        loading_screen.setVisibility(View.VISIBLE);

        mLoaded = false;
        data_points_loaded = 0;
        fetch_data();
    }

    private void fetch_data() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String api_base_url = getString(R.string.api_base_url);
        String api_parameters = "media_type=" + media_type + "&id=" + media_id;
        String str_req_details = api_base_url + "get_details/" + api_parameters;
        String str_req_youtube_video = api_base_url + "get_video/" + api_parameters;
        String str_req_cast = api_base_url + "get_cast/" + api_parameters;
        String str_req_reviews = api_base_url + "get_reviews/" + api_parameters;
        String str_req_recommended_movies = api_base_url + "list_intent/" + "media_type="
                + media_type + "&intent=recommended&reference=" + media_id;

        castsArrayList = new ArrayList<>();
        reviewArrayList = new ArrayList<>();
        recommendedMoviesArrayList = new ArrayList<>();

        StringRequest req_details = new StringRequest(Request.Method.GET,
                str_req_details,
                response -> {
                    try {
                        details = new DetailsClasses.Details(new JSONObject(response));
                        mark_loaded();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {mark_loaded();});
        StringRequest req_youtube_video = new StringRequest(Request.Method.GET,
                str_req_youtube_video,
                response -> {
                    try {
                        DetailsClasses.YoutubeVideo yt = new DetailsClasses.YoutubeVideo(new JSONObject(response));
                        if(yt.isNone()) youtube_video = "None";
                        else youtube_video = yt.link;
                        mark_loaded();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {mark_loaded();});
        StringRequest req_cast = new StringRequest(Request.Method.GET,
                str_req_cast,
                response -> {
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("results");
                        for(int i = 0; i < array.length(); i++) {
                            castsArrayList.add(new DetailsClasses.Cast(array.getJSONObject(i)));
                        }
                        mark_loaded();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {mark_loaded();});
        StringRequest req_review = new StringRequest(Request.Method.GET,
                str_req_reviews,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("results");
                        for(int i = 0; i < array.length(); i++) {
                            reviewArrayList.add(new DetailsClasses.Review(array.getJSONObject(i)));
                        }
                        mark_loaded();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {mark_loaded();});
        StringRequest req_recommended_movies = new StringRequest(Request.Method.GET,
                str_req_recommended_movies,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("results");
                        for(int i = 0; i < array.length(); i++) {
                            recommendedMoviesArrayList.add(new ListMediaEntry(array.getJSONObject(i)));
                        }
                        mark_loaded();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {mark_loaded();});

        queue.add(req_youtube_video);
        queue.add(req_details);
        queue.add(req_cast);
        queue.add(req_review);
        queue.add(req_recommended_movies);
    }

    private void mark_loaded() {
        _mLock.lock();
        data_points_loaded++;
        if(data_points_loaded == 5) {
            mLoaded = true;
        }
        _mLock.unlock();
        if(mLoaded) {
            create_view();
            View loading_screen = findViewById(R.id.details_loading_screen);
            View content_screen = findViewById(R.id.details_content);
            loading_screen.setVisibility(View.GONE);
            content_screen.setVisibility(View.VISIBLE);
        }
    }
    private void create_view() {
        ImageView detailsBackdropImg = findViewById(R.id.details_backdrop_img);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.details_video);
        if(youtube_video.equals("None")) {
            Glide.with(this)
                    .load(details.backdrop_path)
                    .fitCenter()
                    .into(detailsBackdropImg);
            youTubePlayerView.setVisibility(View.GONE);
        }
        else {
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                youTubePlayer.cueVideo(youtube_video, 0);
            });
            detailsBackdropImg.setVisibility(View.GONE);
        }
        //Title
        TextView details_title = findViewById(R.id.details_title);
        details_title.setText(details.name);
        //Overview
        ReadMoreTextView details_overview = findViewById(R.id.details_section_overview);
        details_overview.setText(details.overview, TextView.BufferType.NORMAL);
        //Genres
        TextView details_genres = findViewById(R.id.details_section_genres);
        details_genres.setText(details.genres);
        //Year
        TextView details_year = findViewById(R.id.details_section_year);
        details_year.setText(details.release_year);
        //Watchlist
        ImageView details_btn_add_to_watchlist = findViewById(R.id.details_btn_add_watchlist);
        ImageView details_btn_remove_from_watchlist = findViewById(R.id.details_btn_remove_watchlist);
        watchlistHandler watchlist = new watchlistHandler(this);
        watchlistItem watchlistItem = new watchlistItem(media_type, media_id, details.poster_path, details.name);
        if(watchlist.isMediaInWatchlist(watchlistItem)) {
            details_btn_add_to_watchlist.setVisibility(View.GONE);
        }
        else {
            details_btn_remove_from_watchlist.setVisibility(View.GONE);
        }
        details_btn_add_to_watchlist.setOnClickListener(v -> {
            watchlist.add(watchlistItem);
            Toast.makeText(getApplicationContext(), watchlistItem.name + " was added to Watchlist", Toast.LENGTH_SHORT).show();
            details_btn_add_to_watchlist.setVisibility(View.GONE);
            details_btn_remove_from_watchlist.setVisibility(View.VISIBLE);
        });
        details_btn_remove_from_watchlist.setOnClickListener(v-> {
            watchlist.remove(watchlistItem);
            Toast.makeText(getApplicationContext(), watchlistItem.name + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
            details_btn_remove_from_watchlist.setVisibility(View.GONE);
            details_btn_add_to_watchlist.setVisibility(View.VISIBLE);
        });
        //Social
        ImageView details_btn_facebook = findViewById(R.id.details_btn_facebook);
        details_btn_facebook.setOnClickListener(v -> {
            String link = "https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + media_type +"/" + media_id;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(link));
            startActivity(browserIntent);
        });
        ImageView details_btn_twitter = findViewById(R.id.details_btn_twitter);
        details_btn_twitter.setOnClickListener(v -> {
            String link = "https://twitter.com/intent/tweet?text=Check this out! https://www.themoviedb.org/" + media_type +"/" + media_id;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(link));
            startActivity(browserIntent);
        });
        //Cast
        TextView cast_header = findViewById(R.id.details_header_cast);
        RecyclerView cast_gridview = findViewById(R.id.details_section_cast);
        if(castsArrayList.size() == 0) {
            cast_header.setVisibility(View.GONE);
            cast_gridview.setVisibility(View.GONE);
        }
        else{
            DetailsCastAdapter castAdapter = new DetailsCastAdapter(getApplicationContext(), castsArrayList);
            cast_gridview.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
            cast_gridview.setAdapter(castAdapter);
        }

        //Reviews
        TextView review_header = findViewById(R.id.details_header_reviews);
        RecyclerView reviewRecylcerView = findViewById(R.id.details_review_recycler_view);
        if(reviewArrayList.size() == 0) {
            review_header.setVisibility(View.GONE);
            reviewRecylcerView.setVisibility(View.GONE);
        }
        else {
            DetailsReviewAdapter reviewAdapter = new DetailsReviewAdapter(this, reviewArrayList);
            reviewRecylcerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            reviewRecylcerView.setAdapter(reviewAdapter);
        }

        //Recommended
        TextView recommended_header = findViewById(R.id.details_header_recommended);
        RecyclerView recommendedRecyclerView = findViewById(R.id.details_recommended_recycler_view);
        if(recommendedMoviesArrayList.size() == 0) {
            recommended_header.setVisibility(View.GONE);
            recommendedRecyclerView.setVisibility(View.GONE);
        }
        else {
            HorizontalCardsAdapter recommendedAdapter = new HorizontalCardsAdapter(this, recommendedMoviesArrayList);
            recommendedAdapter.setForRecommended(true);
            recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
            recommendedRecyclerView.setAdapter(recommendedAdapter);
        }
    }
}