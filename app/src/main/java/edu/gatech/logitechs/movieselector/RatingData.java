package edu.gatech.logitechs.movieselector;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by akhilesh on 3/3/16.
 */
public class RatingData {
    private HashMap<String, Integer> numRating;
    private HashMap<String, Double> sum;
    private String title;

    public RatingData() {
        super();
        sum = new HashMap<String, Double>();
        numRating = new HashMap<String, Integer>();
    }
    public RatingData(String title) {
        this();
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
    public HashMap<String, Integer> getNumRating() {
        return numRating;
    }

    public void setNumRating(HashMap<String, Integer> numRating) {
        this.numRating = numRating;
    }


    public HashMap<String, Double>  getSum() {
        return sum;
    }

    public void setSum(HashMap<String, Double> sum) {
        this.sum = sum;
    }

    public int getMajorNumRating(String major) {
        return numRating.get(major) == null ? 0 : numRating.get(major);
    }

    public double getMajorSum(String major) {
        return sum.get(major) == null ? 0 : sum.get(major);
    }

    public void setMajorSum(String major, double sum) {
        this.sum.put(major, sum);
    }

    public void setMajorNumRating(String major, int numRating) {
        this.numRating.put(major, numRating);
    }

    public void addRating(String major, double rating) {
        int majorRating = numRating.get(major) == null ? 0 : numRating.get(major);;
        double majorSum = sum.get(major) == null ? 0 : sum.get(major);
        numRating.put(major, majorRating + 1);
        sum.put(major, majorSum + rating);
    }

    public double calculateRating(String major) {
        return sum.get(major) == null ? 0 : sum.get(major)/(numRating.get(major) == null ? 1 : numRating.get(major));
    }

}
