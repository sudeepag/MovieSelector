package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/20/16.
 */
public interface Consumer<T> {
    public abstract <T> void consume(T input);

}
