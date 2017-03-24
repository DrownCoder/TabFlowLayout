/*
package com.study.tabflowlayout.utils;

import android.app.Activity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

public class GsonRequest<T> extends Request<T> {
  
    private final Response.Listener<T> mListener;
  
    private Gson mGson;
  
    private Class<T> mClass;  
  
    public GsonRequest(int method, String url, Class<T> clazz, WeakListener<T> listener,
            WeakErrorListener errorListener) {
        super(method, url, errorListener);  
        mGson = new Gson();  
        mClass = clazz;  
        mListener = listener;  
    }  
  
    public GsonRequest(String url, Class<T> clazz, WeakListener<T> listener,
                       WeakErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);  
    }  
  
    @Override  
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {  
            String jsonString = new String(response.data,  
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClass),  
                    HttpHeaderParser.parseCacheHeaders(response));  
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }  
    }  
  
    @Override  
    protected void deliverResponse(T response) {  
        mListener.onResponse(response);  
    }

    private class WeakListener<T> implements Response.Listener<T> {
        private  final WeakReference<Activity> activityWeakReference;
        public WeakListener(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void onResponse(T response) {

        }
    }

    private class WeakErrorListener implements Response.ErrorListener {
        private  final WeakReference<Activity> activityWeakReference;
        public WeakErrorListener(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }
  
}*/
