package edu.gatech.logitechs.movieselector.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by akhilesh on 2/23/16.
 */
public final class VolleySingleton {
    /**
     * singleton instance of itself
     */
    private static VolleySingleton mInstance;
    /**
     * queue of requests made
     */
    private RequestQueue mRequestQueue;
    /**
     * Image loader to request images
     */
    private ImageLoader mImageLoader;
    /**
     * context making the query
     */
    private static Context mCtx;

    /**
     * constructor for volley singleton
     * @param context context making the volley query
     */
    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> cache = new LruCache<>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
    }

    /**
     * gettier for volley singleton
     * @param context context used to contruct singleton if instnace doesnt exist
     * @return instance of VolleySingleton
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * getter for the request queue
     * @return the request que of the volley singleton
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * add a request to request queue
     * @param req request to make
     * @param <T> type of data in request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * getter for imageLoader
     * @return Volley's imageloader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}