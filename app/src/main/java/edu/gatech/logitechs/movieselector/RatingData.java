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
    public int getNumRating(String major) {
        return numRating.get(major) == null ? 0 : numRating.get(major);
    }

    public double getSum(String major) {
        return sum.get(major) == null ? 0 : sum.get(major);
    }

    public void setSum(String major, double sum) {
        this.sum.put(major, sum);
    }

    public void setNumRating(String major, int numRating) {
        this.numRating.put(major, numRating);
    }

    public void addRating(String major, double rating) {
        int majorRating = numRating.get(major) == null ? 0 : numRating.get(major);;
        double majorSum = sum.get(major) == null ? 0 : sum.get(major);
        numRating.put(major, majorRating++);
        sum.put(major, majorSum++);
    }

    public double calculateRating(String major) {
        return sum.get(major)/(numRating.get(major) == null ? 0 : numRating.get(major));
    }

}
