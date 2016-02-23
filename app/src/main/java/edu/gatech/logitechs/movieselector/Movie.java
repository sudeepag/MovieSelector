package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/23/16.
 */
public class Movie {
    private String title;
    private int year;
    private int cScore;

    public Movie(String title, int year, int cScore) {
        this.title = title;
        this.year = year;
        this.cScore = cScore;
    }

    @Override
    public String toString() {
        return "Title: " + title + "year: " + year + "score: " + cScore;
    }


}
