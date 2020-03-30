package com.example.smog;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class Utils {


    public static void updateData(Context context, String latitude, String longitude) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = String.format("https://api.waqi.info/feed/geo:%s;%s/?token=930944592bc3aef92bc18782f134b2a9328b52ad", latitude, longitude);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {

                    SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("response", response.toString());
                    editor.apply();

                }, error -> Toast.makeText(context, context.getResources().getString(R.string.check_connection), Toast.LENGTH_LONG).show());

        queue.add(jsonObjectRequest);
    }


    public static void updateData(Context context, String keyword, final VolleyCallback callback) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = String.format("https://api.waqi.info/feed/%s/?token=930944592bc3aef92bc18782f134b2a9328b52ad", keyword);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    callback.onSuccess(response);
                }, error -> Toast.makeText(context, context.getResources().getString(R.string.check_connection), Toast.LENGTH_LONG).show());
        queue.add(jsonObjectRequest);


    }

    public static Measurement getMeasurementFromJson(JSONObject response) {


        Measurement measurement = null;
        try {
            if (response.get("status").equals("error")) {
                measurement = Measurement.builder()
                        .status(response.get("status").toString())
                        .build();
            } else {
                measurement = Measurement.builder()
                        .status(response.get("data").toString())
                        .city(response.getJSONObject("data").getJSONObject("city").get("name").toString())
                        .aqi(response.getJSONObject("data").get("aqi").toString())
                        .date(response.getJSONObject("data").getJSONObject("time").get("s").toString())
                        .pm25(response.getJSONObject("data").getJSONObject("iaqi").has("pm25") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm25").get("v").toString() : "-")
                        .co(response.getJSONObject("data").getJSONObject("iaqi").has("co") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("co").get("v").toString() : "-")
                        .o3(response.getJSONObject("data").getJSONObject("iaqi").has("o3") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("o3").get("v").toString() : "-")
                        .no2(response.getJSONObject("data").getJSONObject("iaqi").has("no2") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("no2").get("v").toString() : "-")
                        .pm10(response.getJSONObject("data").getJSONObject("iaqi").has("pm10") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm10").get("v").toString() : "-")
                        .temperature(response.getJSONObject("data").getJSONObject("iaqi").has("t") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("t").get("v").toString() : "-")
                        .so2(response.getJSONObject("data").getJSONObject("iaqi").has("so2") ? response.getJSONObject("data").getJSONObject("iaqi").getJSONObject("so2").get("v").toString() : "-")
                        .build();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return measurement;

    }

    public static JSONObject getData(Context ctx, String latitude, String longitude) {


        JSONObject response = null;
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "https://api.waqi.info/feed/geo:%s;%s/?token=930944592bc3aef92bc18782f134b2a9328b52ad";
        JsonObjectRequest request = new JsonObjectRequest(String.format(url, latitude, longitude), null, future, future);
        requestQueue.add(request);


        try {
            response = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        return response;
    }

    public  static String getFromSharedPreferences(Context context, String key, String defVal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        return sharedPreferences.getString(key, defVal);
    }
}
