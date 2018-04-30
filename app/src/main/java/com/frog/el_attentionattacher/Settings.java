package com.frog.el_attentionattacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button temp = (Button) findViewById(R.id.temp_button);
        temp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.temp_button:
                Intent intent = new Intent(this, ChooseWeatherActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
