package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/23/16.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    public static void getDVD(Context context, final Runnable runnable) {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
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
                                System.out.println(new Movie (title, year, critics_score,
                                        description, actor1, actor2));
                                movieList.add(new Movie(title, year, critics_score,
                                        description, actor1, actor2));
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

    public static void searchTitles(String searchTerm, Context context, final Runnable runnable) {
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
                                    movieList.add(new Movie(title, year, critics_score,
                                            description, actor1, actor2));
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


    public static void getRecent(Context context, final Runnable runnable) {
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
                                    movieList.add(new Movie(title, year, critics_score,
                                            description, actor1, actor2));
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

    public static List<Movie> getMovieList() {
        return movieList;
    }

}
