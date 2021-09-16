package com.example.movies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {

    @Ignore
    public static final String ID_NAME = "id";

    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id,
                voteCount;

    private String  title,
                    originalTitle,
                    overview,
                    posterPath,
                    bigPosterPath,
                    backdropPath,
                    releaseDate;

    private double voteAverage;

    public Movie(int uniqueId, int id, int voteCount, String title, String originalTitle, String overview,
                 String posterPath, String bigPosterPath, String backdropPath, String releaseDate, double voteAverage) {

        this(id, voteCount, title, originalTitle, overview,
             posterPath, bigPosterPath, backdropPath, releaseDate, voteAverage);

        this.uniqueId = uniqueId;
    }

    @Ignore
    public Movie(int id, int voteCount, String title, String originalTitle, String overview,
                 String posterPath, String bigPosterPath, String backdropPath, String releaseDate, double voteAverage) {

        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
