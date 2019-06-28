package com.example.flixter;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixter.models.Config;
import com.example.flixter.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    //constants
    //the basic URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for messages coming from this activity
    public final static String TAG = "Main";

    //Instance fields
    //Not sure about words used here???
    AsyncHttpClient client;

    //list of currently playing movies
    ArrayList<Movie> movies;
    //the recycled view
    RecyclerView rvMovies;
    // the adapter wored to the recycler view
    MovieAdapter adapter;
    //image config
    Config config;

    //Creates the display from the xml files we made
    // How do we get both xml files to appear together????
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializes client
        client = new AsyncHttpClient();
        //initialize list of movies
        movies = new ArrayList<>();
        //initialize adapter
        adapter = new MovieAdapter(movies);

        //resolve the recycler view and connect to layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        getConfiguration();

    }
    //Gets a list of movies from API including: title, overview, and poster images.
    private void getNowPlaying()  {
        //create the url
        // this is the url that has all of the info we need
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameters
        //what are the params for???
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM,getString(R.string.api_key)); //API key is always required
        client.get(url,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results from the movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterrate through movie list
                    for (int i = 0; i < results.length(); i++)  {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    logError("Failed parsing movies now playing",e,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data movies now playing",throwable,true);
            }
        });
    }

    //get the configuration from the API
    private void getConfiguration()  {
        //create the url
        String url = API_BASE_URL + "/configuration";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM,getString(R.string.api_key)); //API key is always required
        //execute a GET request expecting JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //get the image base url
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded image with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    //pas config to adapter
                    adapter.setConfig(config);
                    // get now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    e.printStackTrace();
                    logError("Failed parsing configuration",e,true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }
    //Handle errors, alert the user
    private void logError (String message, Throwable error, boolean alertUser)  {
        //always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser) {
            //show toast to user
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
        }
    }
}
