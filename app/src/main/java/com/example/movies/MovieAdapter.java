package com.example.movies;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.data.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter() {
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        View view = View.inflate(holder.image.getContext(), R.layout.movie_item, )
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageview_movieitem_smallposter);
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        this.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMovies(Movie movie) {
        this.movies.add(movie);
        this.notifyDataSetChanged();
    }
}
