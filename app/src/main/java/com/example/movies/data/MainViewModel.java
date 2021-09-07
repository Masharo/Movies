package com.example.movies.data;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movies.R;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MovieDatabase database;
    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        database = MovieDatabase.getInstance(application);
        movies = database.movieDao().getAllMovies();
    }

    public Movie getMovieById(int id) {

        Movie resultMovie = null;

        try {
            resultMovie = new GetMovieTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            String messageError = getApplication().getString(R.string.errormessage_mainviewmodel_getmoviebyid);
            Toast.makeText(getApplication(), messageError, Toast.LENGTH_LONG).show();
        }

        return resultMovie;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void deleteAllMovies() {
        new DeleteAllMoviesTask().execute();
    }

    public void insertMovie(Movie movie) {
        new InsertMovieTask().execute(movie);
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask().execute(movie);
    }

    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {

            if (Objects.nonNull(movies) && movies.length > 0) {
                database.movieDao().deleteMovie(movies[0]);
            }

            return null;
        }
    }

    private static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {

            if (Objects.nonNull(movies) && movies.length > 0) {
                database.movieDao().insertMovie(movies[0]);
            }

            return null;
        }
    }

    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            database.movieDao().deleteAllMovies();

            return null;
        }
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {

            if (Objects.nonNull(integers) && integers.length > 0) {
                return database.movieDao().getMovieById(integers[0]);
            }

            return null;
        }
    }
}
