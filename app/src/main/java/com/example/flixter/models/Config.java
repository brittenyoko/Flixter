package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //the base url for loading images
    String imageBaseUrl;
    // the poster size for images
    String posterSize;
    // the backdrop size to use when fetching images, part of the url
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        //get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at or index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3,"w342");
        //the backdrop size the option at index 1 or w78= 0 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1,"w780");
    }
    //helper method for creating urls
    public String getImageUrl (String size, String path) {
        return String.format("%s%s%s", imageBaseUrl,size,path);
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public  String getBackdropSize() { return backdropSize;}
}
