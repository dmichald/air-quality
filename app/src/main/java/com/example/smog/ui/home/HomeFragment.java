package com.example.smog.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.smog.MainActivity;
import com.example.smog.Measurement;
import com.example.smog.R;
import com.example.smog.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private static HomeFragment homeFragment;


    private TextView cityText;
    private TextView dateText;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView pm10Text;
    private TextView tempText;
    private TextView o3Text;
    private TextView no2Text;
    private TextView so2Text;
    private TextView coText;
    private TextView level;
    private TextView pollutions;
    private BottomNavigationItemView homeMenuItem;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initializeTextField(root);
        homeFragment = this;
        homeMenuItem = MainActivity.getInstance().findViewById(R.id.navigation_home);

        return root;

    }

    public void onResume() {
        super.onResume();
        MainActivity.getInstance().getCurrentLocation();
        new DataDownloader().execute();
    }

    void initializeTextField(View view) {
        this.cityText = view.findViewById(R.id.cityTW);
        this.dateText = view.findViewById(R.id.dateTW);
        this.aqiText = view.findViewById(R.id.aqiTw);
        this.pm25Text = view.findViewById(R.id.pm25Tw);
        this.pm10Text = view.findViewById(R.id.pM10Tw);
        this.tempText = view.findViewById(R.id.tempTw);
        this.o3Text = view.findViewById(R.id.o3TW);
        this.no2Text = view.findViewById(R.id.noTw);
        this.so2Text = view.findViewById(R.id.soTW);
        this.coText = view.findViewById(R.id.coTw);
        this.level = view.findViewById(R.id.levelTW);
        this.pollutions = view.findViewById(R.id.pollutionsTw);

    }


    public void setData(Measurement measurement) {
        cityText.setText(measurement.getCity());
        dateText.setText(String.format(getResources().getString(R.string.date), measurement.getDate()));
        aqiText.setText(measurement.getAqi());
        pollutions.setText(getResources().getString(R.string.pollutions));
        pm25Text.setText(String.format(getResources().getString(R.string.pm25), measurement.getPm25()));
        pm10Text.setText(String.format(getResources().getString(R.string.pm10), measurement.getPm10()));
        tempText.setText(String.format(getResources().getString(R.string.temp), measurement.getTemperature(), getResources().getString(R.string.celsius)));
        o3Text.setText(String.format(getResources().getString(R.string.o3), measurement.getO3()));
        no2Text.setText(String.format(getResources().getString(R.string.no2), measurement.getNo2()));
        so2Text.setText(String.format(getResources().getString(R.string.so2), measurement.getSo2()));
        coText.setText(String.format(getResources().getString(R.string.co), measurement.getCo()));


        int aqi = Integer.parseInt(measurement.getAqi());

        if (aqi <= 50) {
            aqiText.setBackgroundResource(R.drawable.good_rounded_corner);
            level.setText(getResources().getString(R.string.good));
            level.setTextColor(Color.parseColor("#1eff92"));
        } else if (aqi <= 100) {
            aqiText.setBackgroundResource(R.drawable.moderate_rounded_corner);
            level.setText(getResources().getString(R.string.moderate));
            level.setTextColor(Color.parseColor("#ffe11e"));
        } else if (aqi <= 150) {
            aqiText.setBackgroundResource(R.drawable.unhealthy_for_somebody_rounded_corner);
            level.setText(getResources().getString(R.string.unhealthy_for));
            level.setTextColor(Color.parseColor("#ffa51e"));
        } else if (aqi <= 200) {
            aqiText.setBackgroundResource(R.drawable.rounded_corner);
            level.setText(getResources().getString(R.string.unhealthy));
            level.setTextColor(Color.parseColor("#ff2d1e"));
        } else if (aqi <= 300) {
            aqiText.setBackgroundResource(R.drawable.very_unhealthy_somebody_rounded_corner);
            level.setText(getResources().getString(R.string.very_unhealthy));
            level.setTextColor(Color.parseColor("#801eff"));
        } else {
            aqiText.setBackgroundResource(R.drawable.hazardous);
            level.setText(getResources().getString(R.string.hazardous));
            level.setTextColor(Color.parseColor("#6e3131"));
        }
    }

    public static HomeFragment getHomeFragment() {
        return homeFragment;
    }

    private  class DataDownloader extends AsyncTask<String, String, JSONObject> {

        private Context ctx;
        private BottomNavigationItemView homeItem;
        private Toast toast;


        public DataDownloader( ) {
            toast = Toast.makeText(ctx, ctx.getResources().getString(R.string.check_connection), Toast.LENGTH_LONG);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            homeItem.setEnabled(false);
            MainActivity.getInstance().getCurrentLocation();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String lat = "1";
            String lon = "1";

            LocationManager locManager = (LocationManager) MainActivity.getInstance().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(MainActivity.getInstance(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());

                }
            }


            SharedPreferences sharedPreferences = Objects.requireNonNull(ctx).getSharedPreferences("settings", MODE_PRIVATE);
            String latitude = sharedPreferences.getString("latitude", "1");
            String longitude = sharedPreferences.getString("longitude", "1");


            JSONObject response;

            if (sharedPreferences.getBoolean("firstRun", true)) {
                response = Utils.getData(ctx, lat, lon);
            } else {
                response = Utils.getData(ctx, latitude, longitude);
            }


            if (response != null) {
                SharedPreferences sh = ctx.getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sh.edit();
                editor.putString("response", response.toString());
                editor.apply();
            } else {


                try {
                    response = new JSONObject(sharedPreferences.getString("response", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                toast.show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            homeItem.setEnabled(true);
            if (result != null) {
                getHomeFragment().setData(Utils.getMeasurementFromJson(result));
            }

        }
    }

}