package edu.gatech.logitechs.movieselector.Controller;

/**
 * Created by akhilesh on 2/23/16.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.gatech.logitechs.movieselector.Model.Movie;
import edu.gatech.logitechs.movieselector.Model.RatingData;
import edu.gatech.logitechs.movieselector.Model.VolleySingleton;

public final class MovieManager {


    /**
     * constructor for movie manage
     */
    private MovieManager() {

    }

    /**
     * movies to be displayed
     */
    private static List<Movie> movieList;
    /**
     * firebase link
     */
    private static Firebase ref = new Firebase("https://muvee.firebaseio.com/");
    /**
     * rating data for the current Move
     */
    private static RatingData currentMovie;
    /**
     * last movie to be queried
     */
    private static Movie lastMovie;
    /**
     * runnable to run on complete
     */
    private static Runnable runnable;
    /**
     * context for volley queries
     */
    private static Context context;
    /**
     * list of ids to maintain order
     */
    private static List<String> orderedIds;
    /**
     * movies key for firebase
     */
    private static final String MOVIE_LABEL = "movies";

    /** listener for firebase queries */
    private static final Response.Listener<JSONObject> LISTENER = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                final JSONArray array = response.getJSONArray(MOVIE_LABEL);
                movieList = new ArrayList<>(array.length());
                if (array.length() == 0) {
                    final Movie movie = new Movie("Sorry", 0, 0, "No movies found, please try again", "", "");
                    movieList.add(movie);
                }
                for (int i = 0; i < array.length(); i++) {
                    final Movie movie = new Movie(runnable, context, array.getJSONObject(i));
                    movieList.add(movie);
                }
            } catch (JSONException e) {
                final Logger logger = Logger.getAnonymousLogger();
                logger.fine(e.getMessage());
            }
        }
    };


    /**
    * A getter for movies recently released on DVD
    *
    * @param aContext     Context of the call to get; used to return to flow
    * @param aRunnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void getDVD(final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
    * A getter for titles searched for
    *
    * @param searchTerm  The term being searched for
    * @param aContext     Context of the call to get; used to return to flow
    * @param aRunnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void searchTitles(String searchTerm, final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        String query = null;
        try {
            query = URLEncoder.encode(searchTerm, "utf-8");
        } catch (UnsupportedEncodingException e) {
            final Logger logger = Logger.getAnonymousLogger();
            logger.fine(e.getMessage());
        }
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5&q=" + query;

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
     * add a movie to the movie list based on the movies id
     * @param aContext context for volley query
     * @param aRunnable runnable on completion
     * @param id id of the movie on rotten tomatoes
     */
    public static void getMoviesFromIds(final Context aContext, final Runnable aRunnable, String id) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;

        final String url = String.format("http://api.rottentomatoes.com/api/public/v1.0/movies/%s.json?apikey=%s", id, "yedukp76ffytfuy24zsqk7f5");
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final Movie movie = new Movie(runnable, context, response);
                movieList.add(movie);
                Collections.sort(movieList, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie lhs, Movie rhs) {
                        return orderedIds.indexOf(lhs.getId()) - orderedIds.indexOf(rhs.getId());
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    /**
    * A getter for recent movies
    *
    * @param aContext     Context of the call to get; used to return to flow
    * @param aRunnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void getRecent(final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=yedukp76ffytfuy24zsqk7f5";
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance(aContext).addToRequestQueue(jsObjRequest);
    }

    /**
    * A getter for the populated movie list
    *
    * @return The a paramatrized list of movies
     */
    public static List<Movie> getMovieList() {
        return movieList;
    }

    /**
     * update the firebase representation of a given movie
     * @param movie the movie's rating data to query
     */
    public static void updateMovie(RatingData movie) {
        final Map<String, Object> map = new HashMap<>();
        String key = movie.getUid();
        map.put("data", movie);
        assert key != null;
        final Firebase movieRef = ref.child(MOVIE_LABEL).child(key);
        movieRef.setValue(map);
    }

    /**
     * get the rating of a movie from firebase
     * @param movie the movie whose rating you want
     * @param aRunnable the completion handler
     */
    public static void queryMovieRating(Movie movie, final Runnable aRunnable) {
        currentMovie = null;
        String key = movie.getId();
        lastMovie = movie;
        assert key != null;
        ref.child(MOVIE_LABEL).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                currentMovie = dataSnapshot.getValue(RatingData.class);
                aRunnable.run();
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                currentMovie = dataSnapshot.getValue(RatingData.class);
                aRunnable.run();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    /**
     * return the rating data for the most recently querried data or a new rating data object
     * @return the rating data for most recently queued content
     */
    public static RatingData getCurrentMovie() {
        if (currentMovie == null) {
            return new RatingData(lastMovie.getTitle(), lastMovie.getId());
        }
        return currentMovie;
    }

    /**
     * populates ordered ids to rank all the movies
     * @param aContext context for the volley queries
     * @param aRunnable completion handler
     * @param major major for the firebase
     */
    public static void getRankedMovies(final Context aContext, final Runnable aRunnable, final String major) {
        final Firebase movieRef = ref.child(MOVIE_LABEL);
        movieList = new ArrayList<>();
        orderedIds = new ArrayList<>();

        final Query query = movieRef.orderByChild("data/average/" + major);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    final RatingData data = child.getValue(RatingData.class);
                    if (data.getAverage().containsKey(major)) {
                        getMoviesFromIds(aContext, aRunnable, data.getUid());
                        orderedIds.add(0, data.getUid());
                    }
                }
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
