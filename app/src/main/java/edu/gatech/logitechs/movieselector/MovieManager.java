package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/23/16.
 */
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieManager {
    private static List<Movie> movieList;

    /*
    * A getter for movies recently released on DVD
    *
    * @param context     Context of the call to get; used to return to flow
    * @param runnable    Runnable to execute upon completion of synchronous call to Rotten Tomatoes
     */
    public static void getDVD(final Context context, final Runnable runnable) {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/" +
                "dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    System.out.print("-------DVDs---------");
                    try {
                        JSONArray array = response.getJSONArray("movies");
                        movieList = new ArrayList<Movie>(array.length());
                        for (int i = 0; i < array.length();i++) {
                            try {
                                JSONObject object = array.getJSONObject(i);
                                String title = object.getString("title");
                                int year = object.getInt("year");
                                String description = object.getString("synopsis");
                                if (description.equals("")) {
                                    description = "Description Unavailable";
                                }

                                JSONObject rating = object.getJSONObject("ratings");
                                int critics_score = rating.getInt("critics_score");


                                JSONArray cast = object.getJSONArray("abridged_cast");
                                String actor1 = "";
                                String actor2 = "";
                                if (cast.length() >= 2) {
                                    actor1 = cast.getJSONObject(0).getString("name");
                                    actor2 = cast.getJSONObject(1).getString("name");
                                } else if (cast.length() >= 1) {
                                    actor1 = cast.getJSONObject(0).getString("name");
                                }
                                System.out.println(new Movie(title, year, critics_score,
                                        description, actor1, actor2));
                                final Movie movie = new Movie(title, year, critics_score,
                                        description, actor1, actor2);
                                movieList.add(movie);
                                JSONObject posters = object.getJSONObject("posters");
                                String url = posters.getString("thumbnail");
                                VolleySingleton.getInstance(context).getImageLoader().get(url, new ImageLoader.ImageListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }

                                    @Override
                                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                        movie.setThumbnail(response.getBitmap());
                                    }
                                });
                            } catch (JSONException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }  catch (JSONException e) {
                        System.out.println(response);
                        e.printStackTrace();
                    }
                    runnable.run();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                    // TODO Auto-generated method stub
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
    public static void searchTitles(String searchTerm, final Context context, final Runnable runnable) {
        JSONObject params = new JSONObject();
        String query = "";
        try {
            query = URLEncoder.encode(searchTerm, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5&q="+query;

        ;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.print("-------Searches---------");
                        try {
                            JSONArray array = response.getJSONArray("movies");
                            movieList = new ArrayList<Movie>(array.length());
                            for (int i = 0; i < array.length();i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    String title = object.getString("title");
                                    int year = object.getInt("year");
                                    String description = object.getString("synopsis");
                                    if (description.equals("")) {
                                        description = "Description Unavailable";
                                    }

                                    JSONObject rating = object.getJSONObject("ratings");
                                    int critics_score = rating.getInt("critics_score");


                                    JSONArray cast = object.getJSONArray("abridged_cast");
                                    String actor1 = "";
                                    String actor2 = "";
                                    if (cast.length() >= 2) {
                                        actor1 = cast.getJSONObject(0).getString("name");
                                        actor2 = cast.getJSONObject(1).getString("name");
                                    } else if (cast.length() >= 1) {
                                        actor1 = cast.getJSONObject(0).getString("name");
                                    }
                                    System.out.println(new Movie(title, year, critics_score,
                                            description, actor1, actor2));
                                    final Movie movie = new Movie(title, year, critics_score,
                                            description, actor1, actor2);
                                    movieList.add(movie);
                                    JSONObject posters = object.getJSONObject("posters");
                                    String url = posters.getString("thumbnail");
                                    VolleySingleton.getInstance(context).getImageLoader().get(url, new ImageLoader.ImageListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }

                                        @Override
                                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                            movie.setThumbnail(response.getBitmap());
                                        }
                                    });
                                } catch (JSONException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }  catch (JSONException e) {
                            System.out.println(response);
                            e.printStackTrace();
                        }
                        runnable.run();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        // TODO Auto-generated method stub
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
    public static void getRecent(final Context context, final Runnable runnable) {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=yedukp76ffytfuy24zsqk7f5";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("movies");
                            movieList = new ArrayList<Movie>(array.length());
                            System.out.println("-------Recent---------");
                            for (int i = 0; i < array.length();i++) {
                                try {

                                    JSONObject object = array.getJSONObject(i);
                                    String title = object.getString("title");
                                    int year = object.getInt("year");
                                    String description = object.getString("synopsis");
                                    if (description.equals("")) {
                                        description = "Description Unavailable";
                                    }

                                    JSONObject rating = object.getJSONObject("ratings");
                                    int critics_score = rating.getInt("critics_score");


                                    JSONArray cast = object.getJSONArray("abridged_cast");
                                    String actor1 = "";
                                    String actor2 = "";
                                    if (cast.length() >= 2) {
                                        actor1 = cast.getJSONObject(0).getString("name");
                                        actor2 = cast.getJSONObject(1).getString("name");
                                    } else if (cast.length() >= 1) {
                                        actor1 = cast.getJSONObject(0).getString("name");
                                    }
                                    System.out.println(new Movie(title, year, critics_score,
                                            description, actor1, actor2));
                                    final Movie movie = new Movie(title, year, critics_score,
                                            description, actor1, actor2);
                                    movieList.add(movie);
                                    JSONObject posters = object.getJSONObject("posters");
                                    String url = posters.getString("thumbnail");
                                    VolleySingleton.getInstance(context).getImageLoader().get(url, new ImageLoader.ImageListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }

                                        @Override
                                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                            movie.setThumbnail(response.getBitmap());
                                        }
                                    });
                                } catch (JSONException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }  catch (JSONException e) {
                            System.out.println(response);
                            e.printStackTrace();
                        }
                        runnable.run();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        // TODO Auto-generated method stub
                    }
                });
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /*
    * A getter for the populated movie list
    *
    * @return The a paramatrized list of movies
     */
    public static List<Movie> getMovieList() {
        if (movieList.size() == 0) {
            movieList.add(new Movie("Sorry", 0, 0, "No movies found, please try again", "", ""));
        }
        return movieList;
    }

}
