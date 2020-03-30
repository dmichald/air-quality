package com.example.smog.ui.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smog.Measurement;
import com.example.smog.R;
import com.example.smog.Utils;

public class SearchFragment extends Fragment {
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
    private EditText editText;
    private Button searchButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        initializeTextField(root);

        searchButton.setOnClickListener(v -> {

            Utils.updateData(getContext(), editText.getText().toString(), result -> {
                Measurement measurement = Utils.getMeasurementFromJson(result);
                if (measurement.getStatus().equals("error")) {
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.unknown_city), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    setData(measurement);
                }
            });
        });

        return root;
    }

    void initializeTextField(View view) {
        this.cityText = view.findViewById(R.id.sCityTW);
        this.dateText = view.findViewById(R.id.sDateTW);
        this.aqiText = view.findViewById(R.id.sAqiTw);
        this.pm25Text = view.findViewById(R.id.sPm25Tw);
        this.pm10Text = view.findViewById(R.id.sPM10Tw);
        this.tempText = view.findViewById(R.id.sTempTw);
        this.o3Text = view.findViewById(R.id.sO3TW);
        this.no2Text = view.findViewById(R.id.sNoTw);
        this.so2Text = view.findViewById(R.id.sSoTW);
        this.coText = view.findViewById(R.id.sCoTw);
        this.editText = view.findViewById(R.id.cityTWS);
        this.searchButton = view.findViewById(R.id.searchBTN);
        this.level = view.findViewById(R.id.sLevelTW);
        this.pollutions = view.findViewById(R.id.sPollutionTW);
    }

    void setData(Measurement measurement) {
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


}
