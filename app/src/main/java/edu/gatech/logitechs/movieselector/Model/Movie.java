package edu.gatech.logitechs.movieselector.Model;

import android.graphics.Bitmap;

/**
 * Created by akhilesh on 2/23/16.
 */
public class Movie {
    private String title;
    private int year;
    private int cScore;
    private String id;
    private String description;
    private String actor1;
    private String actor2;
    private Bitmap thumbnail;

    /**
     * No args constuctor for movie; needed for firebase
     */
    public Movie() {
        super();
    }

    /**
     * Partial constructor for movie
     * @param title  title of the movie
     * @param year  the year the movie was released
     * @param cScore   rating score of the movie
     */
    public Movie(String title, int year, int cScore) {
        this(title, year, cScore, "", "", "");
    }

    /**
     * Partial constructor for movie
     * @param title  title of the movie
     * @param year  the year the movie was released
     * @param cScore   rating score of the movie
     * @param description  the string description of the movie
     * @param actor1  the first actor listed in the movie's cast
     * @param actor2 the second actor listed in the movie's cast
     */
    public Movie(String title, int year, int cScore, String description,
                 String actor1, String actor2) {
        this.title = title;
        this.year = year;
        this.cScore = cScore;
        this.description = description;
        this.actor1 = actor1;
        this.actor2 = actor2;
    }

    /**
     * A getter for the movie title
     *
     * @return  the title o the movie
     */
    public String getTitle() {
        return title;
    }


    /**
     * a getter for the movie's firebase id
     * @return the movie's id
     */
    public String getId() {
        return id;
    }

    /**
     * A getter for the movie description
     *
     * @return the descritpion of the movie
     */
    public String getDescription() {
        return description;
    }

    /**
     * a getter for the cScore
     *
     * @return  the cScore for the movie
     */
    public int getcScore() { return cScore; }


    @Override
    public String toString() {
        return "Title: " + title + "year: " + year + "score: " + cScore + " with " + actor1 + " and " + actor2;
    }

    /**
     * A getter for the thumbnail
     * @return  the thumbnail for the movie
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    /**
     * The setter for the thumbnail
     *
     * @param thumbnail  the thumnail assigned to the movie
     */
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * set the id for firebase retrieval purposes
     * @param id the new id of the movie
     */
    public void setId(String id)  {
        this.id = id;
    }

}
