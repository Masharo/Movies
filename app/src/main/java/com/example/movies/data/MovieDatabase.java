package com.example.movies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Objects;

@Database(entities = {Movie.class, FavouriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DB_NAME = "movies_db";
    private static MovieDatabase thisObject;

    public synchronized static MovieDatabase getInstance(Context context) {
        if (Objects.isNull(thisObject)) {
            thisObject = Room
                         .databaseBuilder(context, MovieDatabase.class, DB_NAME)
                         .fallbackToDestructiveMigration()
                         .build();
        }

        return thisObject;
    }

    public abstract MovieDao movieDao();
}
