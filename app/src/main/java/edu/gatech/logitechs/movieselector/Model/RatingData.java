package edu.gatech.logitechs.movieselector.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akhilesh on 3/3/16.
 */


public class RatingData {
    /** numRating is a Map that holds the number of ratings for each major */
    private Map<String, Integer> numRating;
    /** sum is a Map that holds the total sum for the ratings for each major */
    private Map<String, Double> sum;
    /**average is a Map that holds the averages for each major */
    private Map<String, Double> average;
    /** the movies title */
    private String title;
    /** the movies uid in firebase */
    private String uid;

    /**
     * constructor for rating data
     */
    public RatingData() {
        super();
        sum = new HashMap<>();
        numRating = new HashMap<>();
        average = new HashMap<>();
    }

    /**
     * constructor for rating data
     * @param title title of the movie
     * @param uid id of the movie
     */
    public RatingData(String title, String uid) {
        this();
        this.title = title;
        this.uid = uid;
    }

    /**\
     * getter for a ratingData's id
     * @return rating data's id
     */
    public String getUid() {
        return uid;
    }

    /**
     * setter for rating data's id
     * @param uid the new id to be taken
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * getter for the title of the rating data
     * @return rating data's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for the title of the movie rating data
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * setter for the average rating
     * @param average dictionary holding the average rating for each major
     */
    public void setAverage(Map<String, Double> average) {
        this.average = average;
    }

    /**
     * getter for average
     * @return a dictionary containing average rating data for eachmajor
     */
    public Map<String, Double> getAverage() {
        return average;
    }

    /**
     * getter fpr num rating
     * @return numrating
     */
    public Map<String, Integer> getNumRating() {
        return numRating;
    }

    /**\
     * setter for numRating
     * @param numRating a dictionary holding all of the number of votes per major
     */
    public void setNumRating(Map<String, Integer> numRating) {
        this.numRating = numRating;
    }

    /**
     * sum of the rating data for each major
     * @return the dictionary
     */
    public Map<String, Double>  getSum() {
        return sum;
    }

    /**
     * setter for the sum dictionary
     * @param sum a dictionary that holds each majors total
     */
    public void setSum(Map<String, Double> sum) {
        this.sum = sum;
    }


    /**
     * Add and recalculate the average for a given major
     *
     * @param major major to add rating to
     * @param rating the numerical value for the rating
     */
    public void addRating(String major, double rating) {
        final int majorRating = numRating.get(major) == null ? 0 : numRating.get(major);
        final double majorSum = sum.get(major) == null ? 0 : sum.get(major);
        numRating.put(major, majorRating + 1);
        sum.put(major, majorSum + rating);
        final double average = sum.get(major) / numRating.get(major);
        this.average.put(major, average);
    }

    /**
     * calculate Rating
     * @param major major for which to produce data for
     * @return the average movie rating for the given major
     */
    public double calculateRating(String major) {
        final double average = sum.get(major) == null ? 0 : sum.get(major)/(numRating.get(major) == null ? 1 : numRating.get(major));
        this.average.put(major, average);
        return average;
    }

}
