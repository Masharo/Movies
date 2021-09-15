package com.example.movies;

import android.content.Intent;
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

import com.example.movies.data.FavouriteMovie;
import com.example.movies.data.MainViewModel;
import com.example.movies.data.Movie;
import com.squareup.picasso.Picasso;

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

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageFilmBigPoster = findViewById(R.id.imageview_detail_poster);
        imageStar = findViewById(R.id.imageview_detail_star);
        title = findViewById(R.id.textview_detail_texttitle);
        originalTitle = findViewById(R.id.textview_detail_textoriginaltitle);
        rating = findViewById(R.id.textview_detail_textrating);
        releaseDate = findViewById(R.id.textview_detail_textdate);
        description = findViewById(R.id.textview_detail_textdescription);

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

        Picasso.get().load(movie.getBigPosterPath()).into(imageFilmBigPoster);
        title.setText(movie.getTitle());
        originalTitle.setText(movie.getOriginalTitle());
        rating.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());
        description.setText(movie.getOverview());
        setFavouriteMovie();
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