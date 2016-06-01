package com.bishe.hjh.ros.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HJH on 2016/6/1.
 */
public class OkHttpUtils {
    private static OkHttpUtils mInstance;
    private OkHttpClient client;

    private OkHttpUtils() {

        int cacheSize=10*1024*1024;
        client = new OkHttpClient();
    }

    public static OkHttpUtils Instance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    public void get(String url, final ReturnResult result) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        new AsyncTask<Request, String, String>() {

            @Override
            protected String doInBackground(Request... params) {
                String res = null;
                try {
                    Response rp=client.newCall(params[0]).execute();
                    res = rp.body().string();
                    System.out.println(rp.cacheResponse());
                    Log.d("88888", "cache: "+rp.cacheResponse());
                    Log.d("88888", "doInBackground: "+rp.networkResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    result.onSuccess(s);
                } else {
                    result.onFail(s);
                }
            }
        }.execute(request);

    }

    public Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    public void enqueue(Request request, final ReturnResult res) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (res != null) {
                    res.onFail(e.toString());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (res != null) {
                    String r = response.body().string();
                    res.onSuccess(r);
                }
            }
        });
    }

    public String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("code" + response);
        }
    }


    public interface ReturnResult {
        void onSuccess(String s);

        void onFail(String s);
    }

}
