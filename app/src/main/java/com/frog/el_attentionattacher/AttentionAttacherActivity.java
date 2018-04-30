package com.frog.el_attentionattacher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.frog.el_attentionattacher.service.AutoUpdateService;
import utils.HttpUtil;
import utils.ToastUtil;

/**
 * 程序主体
 * Framed by Wen Sun
 */

public class AttentionAttacherActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //下拉刷新
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBingPic();
            }
        });
        Intent loadPicIntent = new Intent(this, AutoUpdateService.class);
        startService(loadPicIntent);
        //加载必应每日一图（可替换为本地服务器数据）

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Button openDrawer = (Button) findViewById(R.id.nav_open_drawer);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
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
                    case R.id.nav_almanac:
                        Intent toAlmanac = new Intent(
                                AttentionAttacherActivity.this, Almanac.class);
                        startActivity(toAlmanac);
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
                Calendar currentTime = Calendar.getInstance();
                // 创建一个时间选择器
                new TimePickerDialog(AttentionAttacherActivity.this, 0, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 创建一个PendingIntent
                        Intent intent = new Intent(AttentionAttacherActivity.this, AlarmActivity.class);
                        PendingIntent sender = PendingIntent.getActivity(AttentionAttacherActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        long systemTime = System.currentTimeMillis();
                        // 设置当前时间
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Calendar c = Calendar.getInstance();
                        // 先设置为默认时间
                        c.setTimeInMillis(System.currentTimeMillis());
                        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        // 根据用户选择的时间来设置Calendar对象
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        long selectTime = c.getTimeInMillis();
                        if (systemTime > selectTime){
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            selectTime = c.getTimeInMillis();
                            ToastUtil.showToast(AttentionAttacherActivity.this, "你也想拥有永恒的青春？");
                        }
                        // 设置AlarmManager在Calendar对应的时间启动Activity
                        else {
                            am.setExact(AlarmManager.RTC_WAKEUP, selectTime, sender);
                            ToastUtil.showToast(AttentionAttacherActivity.this, "闹钟设置成功");
                        }
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();
                break;
            default:
                break;
        }
    }
    //开始专注按钮的具体实现

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";//Thanks!
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AttentionAttacherActivity.this,
                                "图片加载失败", Toast.LENGTH_SHORT);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AttentionAttacherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(AttentionAttacherActivity.this).load(bingPic).into(bingPicImg);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    //必应每日一图的具体实现
    private long mExitTime = 0;

    //计时器，虽然放在这里很丑，但放在实例区明显不合适，就凑合一下（可理解）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1000) {
                ToastUtil.showToast(this, "再按一次退出程序", Toast.LENGTH_SHORT);
                mExitTime = System.currentTimeMillis();
            } else {
                ToastUtil.showToast(this, "很惭愧，就做了一点微小的工作", Toast.LENGTH_SHORT);
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //实现再按一次退出，退出时说骚话并以home形式存储
}
