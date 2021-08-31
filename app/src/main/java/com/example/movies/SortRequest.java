package com.example.movies;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public enum SortRequest {
    POPULARITY_DESC {

        @Override
        int getId() {
            return 0;
        }

        @NonNull
        @Override
        public String toString() {
            return "popularity.desc";
        }
    },
    VOTE_AVERAGE_DESC {

        @Override
        int getId() {
            return 1;
        }

        @NonNull
        @Override
        public String toString() {
            return "vote_average.desc";
        }
    };

    abstract int getId();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static SortRequest getFromId(int id) {
        return Arrays.stream(values())
                .filter(value -> value.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

