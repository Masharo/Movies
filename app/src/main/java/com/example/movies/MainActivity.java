package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.adapters.MovieAdapter;
import com.example.movies.data.MainViewModel;
import com.example.movies.data.Movie;
import com.example.movies.utils.JSONUtils;
import com.example.movies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private TextView textPopularity,
                     textRating;
    private Switch switchSort;
    private RecyclerView recyclerView;

    private MovieAdapter adapter;

    private MainViewModel viewModel;

    public static final int LOADER_ID = 35;
    private LoaderManager loaderManager;

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

        loaderManager = LoaderManager.getInstance(this);

        recyclerView.setAdapter(adapter);

        switchSort.setChecked(true);

        switchSort.setOnCheckedChangeListener((buttonView, isChecked) -> setMethodSorting(isChecked));

        switchSort.setChecked(false);

        adapter.setOnPosterClickListener(position -> {
            Movie movie = adapter.getMovies().get(position);

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(Movie.ID_NAME, movie.getId());

            startActivity(intent);
        });
        adapter.setOnReachEndListener(() -> {
            //Toast.makeText(MainActivity.this, "Конец Страницы", Toast.LENGTH_LONG).show();
        });

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, movies -> adapter.setMovies(movies));
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
        URL url = NetworkUtils.buildURL(methodSorting, page);

        Bundle bundle = new Bundle();
        bundle.putString(NetworkUtils.KEY_URL, url.toString());

        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject jsonObject) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        if (Objects.nonNull(movies) && !movies.isEmpty()) {
            viewModel.deleteAllMovies();

            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
        }

        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}