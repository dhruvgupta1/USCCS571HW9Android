package com.usccsci571dhruv.uscfilms;


import org.json.JSONException;
import org.json.JSONObject;

public class ListMediaEntry {
    public String id;
    public String name;
    public String poster_path;
    public String media_type;

    private ListMediaEntry() {
    }

    public ListMediaEntry(JSONObject obj) {
        try {
            id = obj.get("id").toString();
            name = obj.get("name").toString();
            poster_path = obj.get("poster_path").toString();
            media_type = obj.get("media_type").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
