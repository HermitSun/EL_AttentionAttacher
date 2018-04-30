package com.frog.el_attentionattacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmActivity extends AppCompatActivity {
    // 这是一个透明的activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final SimpleDialog dialog = new SimpleDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle("闹钟提醒");
        dialog.setMessage("时间到");
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

    }
}
