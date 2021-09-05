package com.example.movies.utils;

import com.example.movies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class JSONUtils {


    public static final String  BASE_POSTER_URL = "https://image.tmdb.org/t/p/",
                                SMALL_POSTER_SIZE = "w185",
                                BIG_POSTER_SIZE = "w780";

    public static final String  KEY_RESULTS = "results",
                                KEY_ID = "id",
                                KEY_VOTE_COUNT = "vote_count",
                                KEY_TITLE = "title",
                                KEY_ORIGINAL_TITLE = "original_title",
                                KEY_OVERVIEW = "overview",
                                KEY_POSTER_PATH = "poster_path",
                                KEY_BACKGROUND_PATH = "backdrop_path",
                                KEY_RELEASE_DATA = "release_date",
                                KEY_VOTE_AVERAGE = "vote_average";

    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) {

        if (Objects.isNull(jsonObject)) {
            return null;
        }

        ArrayList<Movie> result = new ArrayList<>();

        try {
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {

                JSONObject objectMovie = results.getJSONObject(i);

                result.add(new Movie(
                        objectMovie.getInt(KEY_ID),
                        objectMovie.getInt(KEY_VOTE_COUNT),
                        objectMovie.getString(KEY_TITLE),
                        objectMovie.getString(KEY_ORIGINAL_TITLE),
                        objectMovie.getString(KEY_OVERVIEW),
                        BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH),
                        BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH),
                        objectMovie.getString(KEY_BACKGROUND_PATH),
                        objectMovie.getString(KEY_RELEASE_DATA),
                        objectMovie.getDouble(KEY_VOTE_AVERAGE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
