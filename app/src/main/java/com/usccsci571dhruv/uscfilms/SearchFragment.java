package com.usccsci571dhruv.uscfilms;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView search_bar = view.findViewById(R.id.search_view);
        int text_id = search_bar.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView search_hint_text = search_bar.findViewById(text_id);
        search_hint_text.setTextColor(Color.WHITE);
        search_hint_text.setHintTextColor(Color.WHITE);

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeRequest(view, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                makeRequest(view, newText);
                return false;
            }
        });

        search_bar.setIconifiedByDefault(false);
        search_bar.requestFocus();
    }

    private ArrayList<SearchResultEntry> responseToArray(String response) {
        ArrayList<SearchResultEntry> res = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray ar = obj.getJSONArray("results");
            for (int i = 0; i < ar.length(); i++) {
                res.add(new SearchResultEntry(ar.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void makeRequest(View view, String query) {

        query = query.trim();
        if(query.length() == 0) {
            SearchResultAdapter adapter = new SearchResultAdapter(new ArrayList<>());
            RecyclerView recyclerView = view.findViewById(R.id.search_results);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL,
                    false));
            recyclerView.setAdapter(adapter);
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

        StringRequest req = new StringRequest(Request.Method.GET,
                view.getResources()
                        .getString(R.string.api_base_url) + "multi_search/query=" + query,
                response -> {
                    ArrayList<SearchResultEntry> resultEntries = responseToArray(response);
                    if(resultEntries.size() == 0) {
                        View toRemove = view.findViewById(R.id.search_result_holder);
                        View toShow = view.findViewById(R.id.search_no_result);
                        if(toRemove.getVisibility() == View.VISIBLE) {
                            toRemove.setVisibility(View.GONE);
                        }
                        if(toShow.getVisibility() != View.VISIBLE) {
                            toShow.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    View toRemove = view.findViewById(R.id.search_no_result);
                    View toShow = view.findViewById(R.id.search_result_holder);
                    if(toRemove.getVisibility() == View.VISIBLE) {
                        toRemove.setVisibility(View.GONE);
                    }
                    if(toShow.getVisibility() != View.VISIBLE) {
                        toShow.setVisibility(View.VISIBLE);
                    }
                    SearchResultAdapter adapter = new SearchResultAdapter(resultEntries);
                    RecyclerView recyclerView = view.findViewById(R.id.search_results);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                            RecyclerView.VERTICAL,
                            false));
                    recyclerView.setAdapter(adapter);
                }, error -> {
            error.printStackTrace();
        });
        requestQueue.add(req);
    }
}