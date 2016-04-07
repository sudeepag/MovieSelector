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
    private static List<Movie> movieList;
    private static Firebase ref = new Firebase("https://muvee.firebaseio.com/");
    private static RatingData currentMovie;
    private static Movie lastMovie;
    private static Runnable runnable;
    private static Context context;
    private static List<String> orderedIds;
    private static final String MOVIE_LABEL = "movies";

    private static final Response.Listener<JSONObject> LISTENER = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray array = response.getJSONArray(MOVIE_LABEL);
                movieList = new ArrayList<>(array.length());
                if (array.length() == 0) {
                    Movie movie = new Movie("Sorry", 0, 0, "No movies found, please try again", "", "");
                    movie.setThumbnail(null);
                    movieList.add(movie);
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    final Movie movie = new Movie(runnable, context, object);
                    movieList.add(movie);
                }
            }  catch (JSONException e){
                Logger logger = Logger.getAnonymousLogger();
                logger.fine(e.getMessage());}
        }
    };

    private MovieManager () {

    }
    /*
    * A getter for movies recently released on DVD
    *
    * @param context     Context of the call to get; used to return to flow
    * @param runnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void getDVD(final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        final String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /*
    * A getter for titles searched for
    *
    * @param searchTerm  The term being searched for
    * @param context     Context of the call to get; used to return to flow
    * @param runnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void searchTitles(String searchTerm, final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        String query = null;
        try {
            query = URLEncoder.encode(searchTerm, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.fine(e.getMessage());
        }
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5&q="+query;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void getMoviesFromIds(final Context aContext, final Runnable aRunnable, String id) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;

        String url = String.format("http://api.rottentomatoes.com/api/public/v1.0/movies/%s.json?apikey=%s",id, "yedukp76ffytfuy24zsqk7f5");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Movie movie = new Movie(runnable, context, response);
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


    /*
    * A getter for recent movies
    *
    * @param context     Context of the call to get; used to return to flow
    * @param runnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void getRecent(final Context aContext, final Runnable aRunnable) {
        MovieManager.context = aContext;
        MovieManager.runnable = aRunnable;
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=yedukp76ffytfuy24zsqk7f5";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, LISTENER, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        VolleySingleton.getInstance(aContext).addToRequestQueue(jsObjRequest);
    }

    /*
    * A getter for the populated movie list
    *
    * @return The a paramatrized list of movies
     */
    public static List<Movie> getMovieList() {
        return movieList;
    }

    public static void updateMovie(RatingData movie) {
        Map<String, Object> map = new HashMap<>();
        String key = null;
        try {
            key = URLEncoder.encode(movie.getTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.fine(e.getMessage());
        }
        map.put("data", movie);
        assert key != null;
        Firebase movieRef = ref.child(MOVIE_LABEL).child(key);
        movieRef.setValue(map);
    }

    public static void queryMovieRating(Movie movie, final Runnable aRunnable) {
        currentMovie = null;
        String key = null;
        try {
            key = URLEncoder.encode(movie.getTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.fine(e.getMessage());
        }
        lastMovie = movie;
        assert key != null;
        ref.child(MOVIE_LABEL).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                currentMovie = dataSnapshot.getValue(RatingData.class);
                aRunnable.run();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                currentMovie = dataSnapshot.getValue(RatingData.class);
                aRunnable.run();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    public static RatingData getCurrentMovie() {
        if (currentMovie == null) {
            return new RatingData(lastMovie.getTitle(), lastMovie.getId());
        }
       return currentMovie;
    }

    public static void getRankedMovies(final Context aContext, final Runnable aRunnable, final String major) {
        Firebase movieRef = ref.child(MOVIE_LABEL);
        movieList = new ArrayList<>();
        orderedIds = new ArrayList<>();

        Query query = movieRef.orderByChild("data/average/" + major);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    RatingData data = child.getValue(RatingData.class);
                    if (data.getAverage().containsKey(major)) {
                        getMoviesFromIds(aContext, aRunnable, data.getUid());
                        orderedIds.add(0, data.getUid());
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
}
