package edu.gatech.logitechs.movieselector.Model;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Created by akhilesh on 2/23/16.
 */
public class Movie {
    private String url;
    /** movie's title */
    private String title;
    /** movie's production year */
    private int year;

    /** movie's critic score in rotten tomatoes */
    private int cScore;
    /** movie's id in firebase */
    private String id;
    /** movie's description from rotten tomatoes */
    private String description;
    /** movie's lead actorr according rotten tomatoes */
    private String actor1;
    /** movie's second actor acording to rotten tomatoes */
    private String actor2;
    /** movie's thumbnail image from rotten tomatoes */
    private Bitmap thumbnail;

    /** title key in Rotten Tomatoes  */
    private static final String TITLE_LABEL = "title";
    /** year key in Rotten Tomatoes  */
    private static final String YEAR_LABEL = "year";
    /** name key in Rotten Tomatoes  */
    private static final String NAME_STRING = "name";
    /** id key in Rotten Tomatoes  */
    private static final String ID_STRING = "id";
    /** synopsis key in Rotten Tomatoes  */
    private static final String SYNOPSIS_STRING = "synopsis";
    /** message if no description is available  */
    private static final String DESCRIPTION_UNAVAILABLE = "Desciprion unavailable";
    /** critics score key in Rotten Tomatoes  */
    private static final String CRITICS_SCORE = "critics_score";
    /** rating key in Rotten Tomatoes  */
    private static final String RATING_STRING = "ratings";
    /** abridged cast key in Rotten Tomatoes  */
    private static final String ABRIDGED_CAST = "abridged_cast";
    /** posters key in Rotten Tomatoes  */
    private static final String POSTERS = "posters";
    /** thumbnail key in Rotten Tomatoes  */
    private static final String THUMBNAIL = "thumbnail";

    /**
     * No args constuctor for movie; needed for firebase
     */
    public Movie() {
        super();
    }

    /**
     * alternate constructor for defining a movie from a JSONobject
     * @param runnable what is called when finished
     * @param context the context for volley to run in
     * @param object thhe JSON object to extract data from
     */
    public Movie (final Runnable runnable, Context context, JSONObject object) {
        try {
            title = object.getString(TITLE_LABEL);
            year = object.getInt(YEAR_LABEL);
            id = object.getString(ID_STRING);
            description = object.getString(SYNOPSIS_STRING);
            if (description.length() == 0) {
                description = DESCRIPTION_UNAVAILABLE;
            }

            final JSONObject rating = object.getJSONObject(RATING_STRING);
            cScore = rating.getInt(CRITICS_SCORE);


            final JSONArray cast = object.getJSONArray(ABRIDGED_CAST);
            actor1 = null;
            actor2 = null;
            if (cast.length() >= 2) {
                actor1 = cast.getJSONObject(0).getString(NAME_STRING);
                actor2 = cast.getJSONObject(1).getString(NAME_STRING);
            } else if (cast.length() >= 1) {
                actor1 = cast.getJSONObject(0).getString(NAME_STRING);
            }
            final JSONObject links = object.getJSONObject("links");
            this.url = links.getString("alternate");
            final JSONObject posters = object.getJSONObject(POSTERS);
            final String url = posters.getString(THUMBNAIL);
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
            final Logger logger = Logger.getAnonymousLogger();
            logger.fine(e.getMessage());
        }
    }

    /**
     * Partial constructor for movie
     * @param aTitle  aTitle of the movie
     * @param aYear  the year the movie was released
     * @param criticScore   rating score of the movie
     */
    public Movie(String aTitle, int aYear, int criticScore, String url) {
        this(aTitle, aYear, criticScore, url, "", "", "");
    }

    /**
     * Partial constructor for movie
     * @param aTitle  aTitle of the movie
     * @param aYear  the year the movie was released
     * @param criticsScore   rating score of the movie
     * @param aDescription  the string description of the movie
     * @param anActor1  the first actor listed in the movie's cast
     * @param anActor2 the second actor listed in the movie's cast
     */
    public Movie(String aTitle, int aYear, int criticsScore, String url, String aDescription,
                 String anActor1, String anActor2) {
        this.title = aTitle;
        this.year = aYear;
        this.cScore = criticsScore;
        this.url = url;
        this.description = aDescription;
        this.actor1 = anActor1;
        this.actor2 = anActor2;
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
     * @param aThumbnail  the thumnail assigned to the movie
     */
    public void setThumbnail(Bitmap aThumbnail) {
        this.thumbnail = aThumbnail;
    }

    /**
     * set the id for firebase retrieval purposes
     * @param anID the new id of the movie
     */
    public void setId(String anID)  {
        this.id = anID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
