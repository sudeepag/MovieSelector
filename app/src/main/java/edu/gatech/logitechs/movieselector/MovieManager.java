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

import java.util.ArrayList;
import java.util.List;

public class MovieManager {
    private static List<Movie> movieList;
    public static void getDVD(Context context, final Runnable runnable) {
        String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json?apikey=yedukp76ffytfuy24zsqk7f5";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("movies");
                        movieList= new ArrayList<Movie>(array.length());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String title = object.getString("title");
                            int year = object.getInt("year");
                            JSONObject rating = object.getJSONObject("ratings");
                            int critics_score = rating.getInt("critics_score");
                            movieList.add(new Movie(title, year, critics_score));
                            runnable.run();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
