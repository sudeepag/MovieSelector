package edu.gatech.logitechs.movieselector.Model;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String MOVIE_LABEL = "movies";
    private static final String TITLE_LABEL = "title";
    private static final String YEAR_LABEL = "year";
    private static final String NAME_STRING = "name";
    private static final String ID_STRING = "id";
    private static final String SYNOPSIS_STRING = "synopsis";
    private static final String DESCRIPTION_UNAVAILABLE = "Desciprion unavailable";
    private static final String CRITICS_SCORE = "critics_score";
    private static final String RATING_STRING = "ratings";
    private static final String ABRIDGED_CAST = "abridged_cast";
    private static final String POSTERS = "posters";
    private static final String THUMBNAIL = "thumbnail";

    /**
     * No args constuctor for movie; needed for firebase
     */
    public Movie() {
        super();
    }

    public Movie (final Runnable runnable, Context context, JSONObject object) {
        try {
            title = object.getString(TITLE_LABEL);
            year = object.getInt(YEAR_LABEL);
            id = object.getString(ID_STRING);
            description = object.getString(SYNOPSIS_STRING);
            if (description.length() == 0) {
                description = DESCRIPTION_UNAVAILABLE;
            }

            JSONObject rating = object.getJSONObject(RATING_STRING);
            cScore = rating.getInt(CRITICS_SCORE);


            JSONArray cast = object.getJSONArray(ABRIDGED_CAST);
            actor1 = null;
            actor2 = null;
            if (cast.length() >= 2) {
                actor1 = cast.getJSONObject(0).getString(NAME_STRING);
                actor2 = cast.getJSONObject(1).getString(NAME_STRING);
            } else if (cast.length() >= 1) {
                actor1 = cast.getJSONObject(0).getString(NAME_STRING);
            }
            JSONObject posters = object.getJSONObject(POSTERS);
            String url = posters.getString(THUMBNAIL);
            VolleySingleton.getInstance(context).getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    thumbnail = response.getBitmap();
                    runnable.run();
                }
            });
        } catch (JSONException e) {
        }
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
