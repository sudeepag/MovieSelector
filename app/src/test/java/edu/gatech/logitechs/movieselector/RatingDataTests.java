package edu.gatech.logitechs.movieselector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.logitechs.movieselector.Model.RatingData;

import static org.junit.Assert.*;

/**
 * Created by akhilesh on 4/10/16.
 */
public class RatingDataTests {
    private RatingData ratingData = new RatingData("This Movie", "1234");

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddRating() throws Exception {
        HashMap<String, Double> average = new HashMap<>();
        HashMap<String, Integer> numEntries = new HashMap<>();
        HashMap<String, Double> sum = new HashMap<>();
        assertEquals(average, ratingData.getAverage());
        assertEquals(numEntries, ratingData.getNumRating());
        assertEquals(sum, ratingData.getSum());
        ratingData.addRating("major", 4);
        average.put("major", 4.0);
        numEntries.put("major", 1);
        sum.put("major", 4.0);
        assertEquals(average, ratingData.getAverage());
        assertEquals(numEntries, ratingData.getNumRating());
        assertEquals(sum, ratingData.getSum());
        ratingData.addRating("major", 2.0);
        average.put("major", 3.0);
        numEntries.put("major", 2);
        sum.put("major", 6.0);
        assertEquals(average, ratingData.getAverage());
        assertEquals(numEntries, ratingData.getNumRating());
        assertEquals(sum, ratingData.getSum());
        ratingData.addRating("otherMajor", 3);
        average.put("otherMajor", 3.0);
        numEntries.put("otherMajor", 1);
        sum.put("otherMajor", 3.0);
        assertEquals(average, ratingData.getAverage());
        assertEquals(numEntries, ratingData.getNumRating());
        assertEquals(sum, ratingData.getSum());
    }

    @Test
    public void testCalculateRating() {
        Map<String, Double> average = new HashMap<>();
        RatingData data = new RatingData();
        assertEquals(0.0, data.calculateRating("CS"));
        data.addRating("CS", 5);
        assertEquals(5, data.calculateRating("CS"));
        data.addRating("CS", 0);
        assertEquals(2.5, data.calculateRating("CS"));
        data.addRating("CS", 1);
        assertEquals(2, data.calculateRating("CS"));
    }

}