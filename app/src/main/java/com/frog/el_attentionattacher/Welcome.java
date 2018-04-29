package com.frog.el_attentionattacher;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import utils.ActivityCollector;

/**
 * 进入界面
 * Framed by Wen Sun
 */

public class Welcome extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_welcome);
        //初始化
        Button enterNewWorld = (Button) findViewById(R.id.enter_new_world);
        enterNewWorld.setOnClickListener(this);
        //进入界面按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_new_world:
                Intent intent = new Intent(this, AttentionAttacherActivity.class);
                startActivity(intent);
                this.finish();
                //进入界面不需要出现两次，直接finish
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
