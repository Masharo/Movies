package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.adapters.MovieAdapter;
import com.example.movies.data.FavouriteMovie;
import com.example.movies.data.MainViewModel;
import com.example.movies.data.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView movies;
    private MovieAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        movies = findViewById(R.id.recyclerview_favourite_films);

        movies.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter();
        movies.setAdapter(adapter);

        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getApplication())
                .create(MainViewModel.class);

        LiveData<List<FavouriteMovie>> favouriteMoviesLive = viewModel.getFavouriteMovies();
        favouriteMoviesLive.observe(this, favouriteMovies -> {
            if (Objects.nonNull(favouriteMovies)) {
                List<Movie> movies = new ArrayList<Movie>() {{
                    addAll(favouriteMovies);
                }};

                adapter.setMovies(movies);
            }
        });

        adapter.setOnPosterClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);

            Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
            intent.putExtra(Movie.ID_NAME, movie.getId());

            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_menu_root:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;

            case R.id.item_menu_favourite:
                Intent intentFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentFavourite);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}