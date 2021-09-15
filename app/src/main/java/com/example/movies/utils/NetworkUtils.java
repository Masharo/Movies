package com.example.movies.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.movies.SortRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String API_KEY = "54dc0da927baae8ee51f4e0520471cfa";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%d/videos";
    public static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%d/reviews";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";

    private static final String LANGUAGE_VALUE = "ru-RU";

    private static URL buildURLToVideos(int id) {

        URL result = null;

        String url = String.format(BASE_URL_VIDEOS, id);
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static URL buildURLToReviews(int id) {

        URL result = null;

        String url = String.format(BASE_URL_REVIEWS, id);
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static URL buildURL(SortRequest sortBy, int page) {

        URL result = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, sortBy.toString())
                .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                .build();

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static JSONObject getJSONObjectForVideos(int id) {
        URL url = buildURLToVideos(id);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getJSONObjectForReviews(int id) {
        URL url = buildURLToReviews(id);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getJSONFromNetwork(SortRequest sortBy, int page) {
        URL url = buildURL(sortBy, page);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {

            if (urls == null || urls.length == 0) {
                return null;
            }

            HttpURLConnection httpURLConnection = null;
            JSONObject result = null;

            try {
                httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                StringBuilder stringBuilder = new StringBuilder();

                while (Objects.nonNull(line = bufferedReader.readLine())) {
                    stringBuilder.append(line).append("\n");
                }

                result = new JSONObject(stringBuilder.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (Objects.nonNull(httpURLConnection)) {
                    httpURLConnection.disconnect();
                }

                return result;
            }
        }
    }
}
