package com.example.flixter;

import android.content.Context;
import android.content.res.Configuration;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.models.Config;
import com.example.flixter.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //list of movies
    ArrayList<Movie> movies;
    //config needed for image urls
    Config config;
    //context for glide
    Context context;



    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get context from parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie,parent,false);
        //return new ViewHolder

        return new ViewHolder(movieView);
    }

    // binds an inflated view to a new item
    //what does this mean
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //get the movie data at a specified position
        //what is meant by position here
        Movie movie = movies.get(position);

        //populate the view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        holder.tvOverview.setMovementMethod(new ScrollingMovementMethod());

        // determines the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //builds url for poster image
        String imageUrl = null;

        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
                //load url for landscape image
                imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
            }
        //get correct place holder and imageView based on orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        //load image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context,25,0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);


    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }
    // create viewholder as a static inner class

    public static class ViewHolder extends RecyclerView.ViewHolder {


        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView)itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            ivBackdropImage = (ImageView)itemView.findViewById(R.id.ivBackdropImage);
        }


    }
}
