package com.usccsci571dhruv.uscfilms;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultEntry {
    public String id;
    public String name;
    public String img;
    public String media_type;
    public String year;
    public String stars;

    private SearchResultEntry() {}

    public SearchResultEntry(JSONObject obj) {
        try {
            id = obj.getString("id");
            name = obj.getString("name");
            img = obj.getString("backdrop_path");
            media_type = obj.getString("media_type");
            year = obj.getString("year");
            stars = obj.getString("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
