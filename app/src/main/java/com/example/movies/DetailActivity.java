package com.example.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.adapters.ReviewAdapter;
import com.example.movies.adapters.TrailerAdapter;
import com.example.movies.data.FavouriteMovie;
import com.example.movies.data.MainViewModel;
import com.example.movies.data.Movie;
import com.example.movies.data.Review;
import com.example.movies.data.Trailer;
import com.example.movies.utils.JSONUtils;
import com.example.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageFilmBigPoster,
                      imageStar;
    private TextView title,
                     originalTitle,
                     rating,
                     releaseDate,
                     description;

    private int id;

    private Movie movie;
    private FavouriteMovie favouriteMovie;

    private RecyclerView recyclerViewTrailers,
                         recyclerViewReviews;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    private MainViewModel viewModel;

    private static String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lang = Locale.getDefault().getLanguage();

        imageFilmBigPoster = findViewById(R.id.imageview_detail_poster);
        imageStar = findViewById(R.id.imageview_detail_star);
        title = findViewById(R.id.textview_detail_texttitle);
        originalTitle = findViewById(R.id.textview_detail_textoriginaltitle);
        rating = findViewById(R.id.textview_detail_textrating);
        releaseDate = findViewById(R.id.textview_detail_textdate);
        description = findViewById(R.id.textview_detail_textdescription);
        recyclerViewReviews = findViewById(R.id.recyclerview_detail_reviews);
        recyclerViewTrailers = findViewById(R.id.recyclerview_detail_trailers);

        Intent intent = getIntent();

        if (Objects.nonNull(intent) && intent.hasExtra(Movie.ID_NAME)) {
            id = intent.getIntExtra(Movie.ID_NAME, -1);
        } else {
            finish();
        }

        viewModel = ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(getApplication())
                    .create(MainViewModel.class);

        movie = viewModel.getMovieById(id);

        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.poster).into(imageFilmBigPoster);
        title.setText(movie.getTitle());
        originalTitle.setText(movie.getOriginalTitle());
        rating.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());
        description.setText(movie.getOverview());
        setFavouriteMovie();

        JSONObject jsonObjectReviews = NetworkUtils.getJSONObjectForReviews(id, lang);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReviews);
        reviewAdapter = new ReviewAdapter(reviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);

        JSONObject jsonObjectTrailers = NetworkUtils.getJSONObjectForVideos(id, lang);
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers);
        trailerAdapter = new TrailerAdapter(trailers);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setAdapter(trailerAdapter);

        trailerAdapter.setOnTrailerClickListener(url -> {
            Intent intentYouTube = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intentYouTube);
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

    public void onClickChangeStar(View view) {
        if (Objects.isNull(favouriteMovie)) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.toast_detail_addfavourite, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.toast_detail_deletefavourite, Toast.LENGTH_SHORT).show();
        }

        setFavouriteMovie();
    }

    private void setFavouriteMovie() {
        favouriteMovie = viewModel.getFavouriteMovieById(id);

        if (Objects.isNull(favouriteMovie)) {
            imageStar.setImageResource(R.drawable.favourite_add_to);
        } else {
            imageStar.setImageResource(R.drawable.favourite_remove);
        }
    }
}