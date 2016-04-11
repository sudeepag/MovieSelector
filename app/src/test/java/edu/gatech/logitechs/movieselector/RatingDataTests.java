package edu.gatech.logitechs.movieselector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

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
        numEntries.put("major", 1);
        sum.put("major", 6.0);
        assertEquals(average, ratingData.getAverage());
        assertEquals(numEntries, ratingData.getNumRating());
        assertEquals(sum, ratingData.getSum());
        assertEquals(5,4);
    }

    @Test
    public void testCalculateRating() throws Exception {

    }
}