package com.example.names;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonFromUrl extends Thread{

    //private ArrayList<String> names = new ArrayList<>();
    private String urli;
    private String jsonStr;

    public JsonFromUrl(String urli){
        this.urli = urli;
    }

    public void run() {
        try {
            URL url = new URL(urli);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(1000);
            InputStream is =  urlConnection.getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            is.close();
            Log.d("Result:", result.toString("UTF-8"));
            jsonStr = result.toString("UTF-8");

        } catch (Exception ex) {
            //return null;
            Log.e("Error:", ex.toString());
        }
    }

    public JSONObject getJsonObject() throws JSONException {
        return new JSONObject(jsonStr);
    }

    public JSONArray getJsonArray() throws JSONException {
        JSONObject jObject = new JSONObject(jsonStr);
        return jObject.getJSONArray("names");
    }
}
