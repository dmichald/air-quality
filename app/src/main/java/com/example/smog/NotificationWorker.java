package com.example.smog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class NotificationWorker extends Worker {
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        MainActivity.getInstance().getCurrentLocation();
        String latitude = Utils.getFromSharedPreferences(getApplicationContext(), "latitude", "");
        String longitude = Utils.getFromSharedPreferences(getApplicationContext(), "longitude", "");

        if (latitude.equals("")) {
            return Result.failure();
        }

        Utils.updateData(getApplicationContext(), latitude, longitude);


        String response = Utils.getFromSharedPreferences(getApplicationContext(), "response", "");
        String city = "";
        String aqi = "";

        String status;
        try {
            JSONObject res = new JSONObject(response);
            status = res.get("status").toString();
            if (status.equals("ok")) {
                city = res.getJSONObject("data").getJSONObject("city").get("name").toString();
                aqi = res.getJSONObject("data").get("aqi").toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Integer.parseInt(aqi) >= 100) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getApplicationContext()), "N")
                    .setSmallIcon(R.drawable.ic_warning_black_24dp)
                    .setContentTitle(getApplicationContext().getResources().getString(R.string.attention))
                    .setContentText(city.split(",")[0] + "  AQI: " + aqi)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


            notificationManager.notify(1, builder.build());
        }


        return Result.success();
    }

}
