package com.frog.el_attentionattacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import utils.ActivityCollector;

/**
 * 进入界面
 * Framed by Wen Sun
 */

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        preferences = getSharedPreferences("guideActivity", MODE_PRIVATE);
        //初始化
        //判断是否首次进入
        if (preferences.getBoolean("firstStart", true)) {
            editor = preferences.edit();
            //设置为false，不再显示引导页
            editor.putBoolean("firstStart", false);
            editor.apply();
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            //不是首次登录
            Button enterNewWorld = (Button) findViewById(R.id.enter_new_world);
            enterNewWorld.setOnClickListener(this);
            //进入界面按钮
        }
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
