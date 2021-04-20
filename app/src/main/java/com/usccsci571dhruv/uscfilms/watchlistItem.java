package com.usccsci571dhruv.uscfilms;

import org.json.JSONException;
import org.json.JSONObject;

public class watchlistItem {
    public String media_type;
    public String id;
    public String img;
    public String name;

    private watchlistItem() {}

    public watchlistItem(String media_type, String id) {
        this.media_type = media_type;
        this.id = id;
    }

    public watchlistItem(String media_type, String id, String img, String name) {
        this.media_type = media_type;
        this.id = id;
        this.img = img;
        this.name = name;
    }

    public watchlistItem(JSONObject obj) {
        try {
            media_type = obj.getString("media_type");
            id = obj.getString("id");
            img = obj.getString("img");
            name = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("media_type", media_type);
            obj.put("id", id);
            obj.put("img", img);
            obj.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
