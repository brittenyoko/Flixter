//package com.example.flixter.models;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.parceler.Parcel;
//
//public class MovieDetailsActivity {
//
//    @Parcel // annotation indicates class is Parcelable
//    public class Movie {
//
//        // fields must be public for parceler
//        String title;
//        String overview;
//        String posterPath;
//        String backdropPath;
//
//        // no-arg, empty constructor required for Parceler
//        public Movie() {}
//
//        public Movie(JSONObject movie) throws JSONException {
//            title = movie.getString("title");
//            overview = movie.getString("overview");
//            posterPath = movie.getString("poster_path");
//            backdropPath = movie.getString("backdrop_path");
//        }
//
//        // ... additional code
//}
