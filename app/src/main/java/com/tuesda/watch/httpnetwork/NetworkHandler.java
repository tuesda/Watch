package com.tuesda.watch.httpnetwork;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zhanglei on 15/7/24.
 */
public class NetworkHandler {
    private static NetworkHandler mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private NetworkHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkHandler getInstance(Context context) {
        if (mInstance==null) {
            mInstance = new NetworkHandler(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue==null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
