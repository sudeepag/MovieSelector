package edu.gatech.logitechs.movieselector.Model;

/**
 * Created by akhilesh on 2/20/16.
 */
public interface Consumer {

    /**
     * method of functional interface
     * @param input data to consume
     */
    void consume(String input);
}
