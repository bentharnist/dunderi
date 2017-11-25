package com.junction.exep.dunderi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by exep on 11/25/17.
 */

public class ApiManager {
    private Context con;
    private RequestQueue queue;
    private static final String TAG = "API";

    private ArrayList<String> hakuTermit = new ArrayList<>();

    public ApiManager(Context con) {
        this.con = con;
        queue = Volley.newRequestQueue(con);
    }

    public void addHakutermi(String add) {
        hakuTermit.add(add);
    }

    public void haeDuunit(final MatchActivity.DuuniListener listener){
        Collections.shuffle(hakuTermit);
        final ArrayList<Duuni> duunit = new ArrayList<>();
        try{
            for(String s : hakuTermit.subList(0, Math.min(hakuTermit.size(), 5))){
                Log.d("LOL", "haetaan " + s);
                final String tmp = s;
                haeDuunitori(s, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("LOL", "Saatiin " + response.toString());
                            JSONArray arr = response.getJSONArray("results");
                            for (int i = 0; i < arr.length(); ++i) {
                                duunit.add(new Duuni(arr.getString(0)));
                            }
                            Log.d("LOL", tmp + " vs " + hakuTermit.get(Math.min(4, hakuTermit.size() - 1)));
                            if (tmp.equals(hakuTermit.get(Math.min(5, hakuTermit.size() - 1)))) {
                                listener.done(duunit);
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (tmp.equals(hakuTermit.get(Math.min(5, hakuTermit.size() - 1)))) {
                            listener.done(duunit);
                        }
                    }
                });
            }

        } catch(Exception e){
        }
    }

    public void haeDuunitori(String query, Response.Listener<JSONObject> listener, Response.ErrorListener err) {
        String url = String.format("https://duunitori.fi/api/v1/jobentries?search=%s&sho=1&format=json", query);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, listener, err);

        queue.add(jsObjRequest);

    }

    public void haeDuunitoriSecret(String query, Response.Listener<JSONObject> listener) {
        String url = String.format("https://duunitori.fi/api/v1/5d3fc27dec93f5e5105e3443edfc421bb57c3603/jobentries?search=&area=helsinki&tag=harjoittelija&source=&company=&publish_level=&apply=&days_left__gte=&days_left__lte=10&pageviews_sum__gte=&pageviews_sum__lte=&format=json", query);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, listener, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                });

        queue.add(jsObjRequest);
    }

}
