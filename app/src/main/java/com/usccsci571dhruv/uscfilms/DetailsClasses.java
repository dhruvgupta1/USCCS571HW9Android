package com.usccsci571dhruv.uscfilms;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsClasses {

    public static class Details {
        public String media_type;
        public String name;
        public String genres;
        public String spoken_languages;
        public String release_date;
        public String release_year;
        public String overview;
        public String vote_average;
        public String tagline;
        public String poster_path;
        public String backdrop_path;

        public Details(JSONObject obj) {
            try {
                media_type = obj.getString("media_type");
                name = obj.getString("name");
                genres = obj.getString("genres");
                spoken_languages = obj.getString("languages");
                release_date = obj.getString("release_date");
                release_year = obj.getString("release_year");
                overview = obj.getString("overview");
                vote_average = obj.getString("vote_average");
                tagline = obj.getString("tagline");
                poster_path = obj.getString("poster_path");
                backdrop_path = obj.getString("backdrop_path");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class YoutubeVideo {
        public String link;

        public YoutubeVideo(JSONObject obj) {
            try {
                link = obj.getString("link");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean isNone() {
            if(link.equals("NONE")) return true;
            return false;
        }
    }

    public static class Cast {
        public String id;
        public String name;
        public String character;
        public String profile_path;

        public Cast(JSONObject obj) {
            try {
                id = obj.getString("id");
                name = obj.getString("name");
                character = obj.getString("character");
                profile_path = obj.getString("profile_path");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Review {
        public String author;
        public String content;
        public String created_at;
        public String url;
        public String rating;
        public String avatar_path;

        public Review(JSONObject obj) {
            try {
                author = obj.getString("author");
                content = obj.getString("content");
                created_at = obj.getString("created_at");
                url = obj.getString("url");
                rating = obj.getString("rating");
                avatar_path = obj.getString("avatar_path");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
