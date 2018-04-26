package com.frog.el_attentionattacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.HttpUtil;
import utils.ToastUtil;

public class AttentionAttacherActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout mDrawerLayout;
    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_attention_attacher);
        //将任务栏加入布局

        Button startAttachAttention = (Button) findViewById(R.id.start_attach_attention);
        startAttachAttention.setOnClickListener(AttentionAttacherActivity.this);
        //开始专注按钮

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                AttentionAttacherActivity.this);
        //缓存数据

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(AttentionAttacherActivity.this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        //加载必应每日一图（可替换为本地服务器数据）

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //滑动侧边栏

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_change_userdata:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_schedule:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //侧边栏按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_attach_attention:
                ToastUtil.showToast(AttentionAttacherActivity.this,
                        "Started.", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }
    //按钮的具体实现

    private void loadBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";//Thanks!
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager.
                        getDefaultSharedPreferences(AttentionAttacherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(AttentionAttacherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
    //必应每日一图的具体实现
}
