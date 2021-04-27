package com.usccsci571dhruv.uscfilms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WatchlistFragment extends Fragment {

    private View view;

    public WatchlistFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        createView();
    }

    public void createView() {
        watchlistHandler handler = new watchlistHandler(getActivity());
        ArrayList<watchlistItem> dataSet = handler.get();

        TextView no_results = view.findViewById(R.id.watchlist_nothing_saved);
        if(dataSet.size() == 0) no_results.setVisibility(View.VISIBLE);
        else no_results.setVisibility(View.GONE);
        WatchlistCardAdapter adapter = new WatchlistCardAdapter(dataSet, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.watchlist_rv);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3,
                RecyclerView.VERTICAL,
                false));

        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }
}