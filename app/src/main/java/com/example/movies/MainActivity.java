package com.example.movies;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.data.MainViewModel;
import com.example.movies.data.Movie;
import com.example.movies.utils.JSONUtils;
import com.example.movies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView textPopularity,
                     textRating;
    private Switch switchSort;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(getApplication())
                    .create(MainViewModel.class);
        textPopularity = findViewById(R.id.textview_main_sortpopularity);
        textRating = findViewById(R.id.textview_main_sortrating);
        switchSort = findViewById(R.id.switch_main_switchsort);
        recyclerView = findViewById(R.id.recyclerview_main_films);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter();

        recyclerView.setAdapter(adapter);

        switchSort.setChecked(true);

        switchSort.setOnCheckedChangeListener((buttonView, isChecked) -> setMethodSorting(isChecked));

        switchSort.setChecked(false);

        adapter.setOnPosterClickListener(position -> {
            Toast.makeText(MainActivity.this, "Позиция " + position, Toast.LENGTH_LONG).show();
        });
        adapter.setOnReachEndListener(() -> {
            Toast.makeText(MainActivity.this, "Конец Страницы", Toast.LENGTH_LONG).show();
        });

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, movies -> adapter.setMovies(movies));
    }

    public void onClickSortRating(View view) {
        switchSort.setChecked(true);
    }

    public void onClickSortPopularity(View view) {
        switchSort.setChecked(false);
    }

    private void setMethodSorting(boolean isChecked) {
        SortRequest methodSorting;

        if (isChecked) {
            methodSorting = SortRequest.VOTE_AVERAGE_DESC;
            textRating.setTextColor(getColor(R.color.teal_200));
            textPopularity.setTextColor(getColor(R.color.white));
        } else {
            methodSorting = SortRequest.POPULARITY_DESC;
            textPopularity.setTextColor(getColor(R.color.teal_200));
            textRating.setTextColor(getColor(R.color.white));
        }

        downloadData(methodSorting, 1);
    }

    private void downloadData(SortRequest methodSorting, int page) {
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodSorting , page);
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        if (Objects.nonNull(movies) && !movies.isEmpty()) {
            viewModel.deleteAllMovies();

            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
        }
    }
}