package com.frog.el_attentionattacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 老黄历Beta
 * Framed by Wen Sun
 */

public class Almanac extends AppCompatActivity {

    private ImageView almanic_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almanac);
        almanic_view=(ImageView)findViewById(R.id.pic_almanac);
        Glide.with(this).load(R.drawable.almanac).into(almanic_view);
    }
}
