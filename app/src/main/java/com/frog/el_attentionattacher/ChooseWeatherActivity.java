package com.frog.el_attentionattacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import utils.ActivityCollector;

public class ChooseWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_choose_weather);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
