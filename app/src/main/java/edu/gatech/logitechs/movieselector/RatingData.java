package edu.gatech.logitechs.movieselector;

import java.net.URLEncoder;

/**
 * Created by akhilesh on 3/3/16.
 */
public class RatingData {
    private int numRating;
    private double sum;
    private String title;

    public RatingData() {
        super();
    }
    public RatingData(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }
    /**
     * getter fpr num rating
     * @return numrating
     */
    public int getNumRating() {
        return numRating;
    }

    public double getSum() {
        return sum;
    }

    public void setSum( double sum) {
        this.sum = sum;
    }

    public void setNumRating(int numRating) {
        this.numRating = numRating;
    }

    public void addRating(double rating) {
        numRating ++;
        sum += rating;
    }

    public double calculateRating() {
        return sum/numRating;
    }

}
