package com.bishe.hjh.ros.Thread;

import android.util.Log;

import com.bishe.hjh.ros.Restaurant;
import com.bishe.hjh.ros.util.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJH on 2016/4/18.
 */
public class updateRestaurant extends Thread {
    private List<Restaurant> restaurants=new ArrayList<Restaurant>();

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @Override
    public void run() {
        try {
            URL u = new URL("http://119.29.190.80:8080/web/servlet/ReadRestaurant");
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setReadTimeout(5000);
            con.setConnectTimeout(5000);
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            if (code == 200) {
                String result = StreamUtils.parserStream(con.getInputStream());
                Log.d("111111", "onRefreshBegin: " + result);
                JSONArray ja = new JSONArray(result);
                Log.d("111111", "onRefreshBegin: " + ja.length());
                for (int i = 0; i < ja.length(); i++) {
                    Log.d("111111", "onRefreshBegin: " + i);
                    JSONObject j = (JSONObject) ja.get(i);
                    Restaurant r = new Restaurant(j);
                    Log.d("111111", "onRefreshBegin: " + r.getId() + r.getSell() + r.getName() + r.getResPhone() + r.getImageFile() + r.getResDes() + r.getResAddress() + r.getRatingStar());
                    Log.d("111111", "onRefreshBegin: " + i);

                    restaurants.add(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
