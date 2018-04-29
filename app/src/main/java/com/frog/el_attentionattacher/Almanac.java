package com.frog.el_attentionattacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import utils.ActivityCollector;

/**
 * 老黄历Beta
 * Framed by Wen Sun
 */

public class Almanac extends AppCompatActivity {

    private ImageView almanic_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_almanac);
        almanic_view=(ImageView)findViewById(R.id.pic_almanac);
        Glide.with(this).load(R.drawable.almanac).into(almanic_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
