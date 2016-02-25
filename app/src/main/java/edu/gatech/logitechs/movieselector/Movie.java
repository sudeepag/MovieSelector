package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/23/16.
 */
public class Movie {
    private String title;
    private int year;
    private int cScore;
    private String description;
    private String actor1;
    private String actor2;

    public Movie() {}


    public Movie(String title, int year, int cScore) {
        this (title, year, cScore, "", "", "");
    }

    public Movie(String title, int year, int cScore, String description,
                 String actor1, String actor2) {
        this.title = title;
        this.year = year;
        this.cScore = cScore;
        this.description = description;
        this.actor1 = actor1;
        this.actor2 = actor2;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return "Title: " + title + "year: " + year + "score: " + cScore + " with " + actor1 + " and " + actor2;
    }


}
