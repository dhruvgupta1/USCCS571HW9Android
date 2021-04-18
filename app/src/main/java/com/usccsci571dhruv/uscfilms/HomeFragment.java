package com.usccsci571dhruv.uscfilms;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public ArrayList<ListMediaEntry> movies_new;
    public ArrayList<ListMediaEntry> movies_top;
    public ArrayList<ListMediaEntry> tv_trending;
    public ArrayList<ListMediaEntry> tv_top;
    public ArrayList<ListMediaEntry> movies_popular;
    public ArrayList<ListMediaEntry> tv_popular;

    private int current_tab;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(
            ArrayList<ListMediaEntry> im_new,
            ArrayList<ListMediaEntry> im_top,
            ArrayList<ListMediaEntry> im_pop,
            ArrayList<ListMediaEntry> it_trend,
            ArrayList<ListMediaEntry> it_top,
            ArrayList<ListMediaEntry> it_pop
    ) {
        
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        fragment.movies_new = im_new;
        fragment.movies_top = im_top;
        fragment.movies_popular = im_pop;
        fragment.tv_trending = it_trend;
        fragment.tv_top = it_top;
        fragment.tv_popular = it_pop;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_tab = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView movie_tab = view.findViewById(R.id.MovieTab);
        TextView tv_tab = view.findViewById(R.id.TVTab);
        View layout_movie = view.findViewById(R.id.layout_movie);
        View layout_tv = view.findViewById(R.id.layout_tv);
        movie_tab.setOnClickListener(v -> {
            if(current_tab == 0) return;
            current_tab = 0;
            movie_tab.setTextColor(Color.parseColor("#ffffffff"));
            tv_tab.setTextColor(Color.parseColor("#ff156eb4"));
            layout_tv.setVisibility(View.GONE);
            layout_movie.setVisibility(View.VISIBLE);
        });
        tv_tab.setOnClickListener(v -> {
            if(current_tab == 1) return;
            current_tab = 1;
            movie_tab.setTextColor(Color.parseColor("#ff156eb4"));
            tv_tab.setTextColor(Color.parseColor("#ffffffff"));
            layout_tv.setVisibility(View.VISIBLE);
            layout_movie.setVisibility(View.GONE);
        });
        current_tab = 0;
        movie_tab.setTextColor(Color.parseColor("#ffffffff"));
        tv_tab.setTextColor(Color.parseColor("#ff156eb4"));
        layout_tv.setVisibility(View.GONE);
        layout_movie.setVisibility(View.VISIBLE);

        SliderView moviesSliderView = view.findViewById(R.id.movieSlider);
        SliderAdapter movies_slider_adapter = new SliderAdapter(getActivity(), movies_new);
        moviesSliderView.setSliderAdapter(movies_slider_adapter);
        moviesSliderView.setScrollTimeInSec(3);
        moviesSliderView.setAutoCycle(true);
        moviesSliderView.startAutoCycle();

        HorizontalCardsAdapter top_rated_movies_adapter = new HorizontalCardsAdapter(movies_top);
        HorizontalCardsAdapter popular_movies_adapter = new HorizontalCardsAdapter(movies_popular);
        RecyclerView topRatedMoviesView = view.findViewById(R.id.topRatedMovies);
        RecyclerView popularMoviesView = view.findViewById(R.id.popularMovies);
        topRatedMoviesView.setNestedScrollingEnabled(false);
        topRatedMoviesView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        topRatedMoviesView.setAdapter(top_rated_movies_adapter);
        popularMoviesView.setNestedScrollingEnabled(false);
        popularMoviesView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularMoviesView.setAdapter(popular_movies_adapter);

        SliderView tvSliderView = view.findViewById(R.id.tvSlider);
        SliderAdapter tv_slider_adapter = new SliderAdapter(getActivity(), tv_trending);
        tvSliderView.setSliderAdapter(tv_slider_adapter);
        tvSliderView.setScrollTimeInSec(3);
        tvSliderView.setAutoCycle(true);
        tvSliderView.startAutoCycle();

        HorizontalCardsAdapter top_rated_tv_adapter = new HorizontalCardsAdapter(tv_top);
        HorizontalCardsAdapter popular_tv_adapter = new HorizontalCardsAdapter(tv_popular);
        RecyclerView topRatedTvView = view.findViewById(R.id.topRatedTV);
        RecyclerView popularTvView = view.findViewById(R.id.popularTV);
        topRatedTvView.setNestedScrollingEnabled(false);
        topRatedTvView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        topRatedTvView.setAdapter(top_rated_tv_adapter);
        popularTvView.setNestedScrollingEnabled(false);
        popularTvView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularTvView.setAdapter(popular_tv_adapter);

    }
}