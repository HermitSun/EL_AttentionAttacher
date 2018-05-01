package com.frog.el_attentionattacher;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.frog.el_attentionattacher.db.PersonalInfoData;
import com.frog.el_attentionattacher.gson.Weather;
import com.frog.el_attentionattacher.service.AutoUpdateService;

import org.litepal.crud.DataSupport;

import utils.ActivityCollector;
import utils.AnalyzeWeatherUtil;
import utils.HttpUtil;
import utils.PrefUtils;
import utils.ToastUtil;

/**
 * 程序主体
 * Framed by Wen Sun
 */

public class AttentionAttacherActivity extends AppCompatActivity implements View.OnClickListener {

    private int id;
    private String mWeatherId;
    private DrawerLayout mDrawerLayout;
    private ScrollView mainBody;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private TextView userName;
    private ImageView userIcon;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = PreferenceManager.getDefaultSharedPreferences(AttentionAttacherActivity.this).edit();
        //缓存数据
        //初始化

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_attention_attacher);
        //将任务栏加入布局

        mainBody = (ScrollView) findViewById(R.id.main_body_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //下拉刷新

        Button startAttachAttention = (Button) findViewById(R.id.start_attach_attention);
        startAttachAttention.setOnClickListener(AttentionAttacherActivity.this);
        //开始专注按钮

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        Intent update = new Intent(this, AutoUpdateService.class);
        startService(update);
        //启动后台更新服务

        if (PrefUtils.isSaveBackgroundMode()) {
            changeBackgroundByWeather();
        } else {
            String bingPic = prefs.getString("bing_pic", null);
            if (bingPic != null) {
                Glide.with(AttentionAttacherActivity.this).load(bingPic).into(bingPicImg);
            } else {
                loadBingPic();
            }
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (PrefUtils.isSaveBackgroundMode()) {
                    requestWeather(mWeatherId);
                } else {
                    loadBingPic();
                }
            }
        });
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);

        Intent rawIntent = getIntent();
        Bundle bundle = rawIntent.getExtras();
        id = bundle.getInt("user_id");
        userName = (TextView) headView.findViewById(R.id.user_name);
        List<PersonalInfoData> list = DataSupport.findAll(PersonalInfoData.class);
        userName.setText(list.get(id - 1).getUsername());
        userIcon = (ImageView) headView.findViewById(R.id.nav_icon_image);
        userIcon.setImageResource(R.drawable.ic_launcher_background);
        //初始化个人信息

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_change_userdata:
                        mDrawerLayout.closeDrawers();
                        Intent intent = new Intent(AttentionAttacherActivity.this, PersonalInfo.class);
                        intent.putExtra("user_id", id);
                        startActivity(intent);
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
                        Intent settings = new Intent(AttentionAttacherActivity.this, Settings.class);
                        startActivityForResult(settings, 11);
                        break;
                    case R.id.nav_delete:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AttentionAttacherActivity.this);
                        dialog.setTitle("彻底退出");
                        dialog.setMessage("不再考虑考虑？");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.showToast(AttentionAttacherActivity.this,
                                        "很惭愧，就做了一点微小的工作", Toast.LENGTH_SHORT);
                                ActivityCollector.finishAll();
                            }
                        });
                        dialog.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.showToast(AttentionAttacherActivity.this,
                                        "你们哪，不要整天想着搞一个大新闻", Toast.LENGTH_SHORT);
                                mDrawerLayout.closeDrawers();
                            }
                        });
                        dialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK) {
                    mWeatherId = data.getStringExtra("weather_id");
                }
                break;
            default:
                break;
        }
    }

    private void changeBackgroundByWeather() {
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = AnalyzeWeatherUtil.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            mainBody.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=41407a0da87447a6abcd65bb8f0c1794";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = AnalyzeWeatherUtil.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    AttentionAttacherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            ToastUtil.showToast(AttentionAttacherActivity.this,
                                    "获取天气信息失败", Toast.LENGTH_SHORT);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(AttentionAttacherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather中的数据
     */
    String cityName;
    String updateTime;
    String degree;
    String weatherInfo;

    private void showWeatherInfo(final Weather weather) {
        cityName = weather.basic.cityName;
        updateTime = weather.basic.update.updateTime.split(" ")[1];
        degree = weather.now.temperature + "℃";
        weatherInfo = weather.now.more.info;
        mainBody.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weatherInfo.equals("多云")) {
                    Log.d("ELA", weatherInfo);
                    Glide.with(AttentionAttacherActivity.this).load(R.drawable.cloudy).into(bingPicImg);
                } else if (weatherInfo.equals("阵雨")) {
                    Glide.with(AttentionAttacherActivity.this).load(R.drawable.rainy).into(bingPicImg);
                } else {
                    Glide.with(AttentionAttacherActivity.this).load(R.drawable.sunny).into(bingPicImg);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    //根据天气切换背景的具体实现

    private long mExitTime = 0;

    //计时器，虽然放在这里很丑，但放在实例区明显不合适，就凑合一下（可理解）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1000) {
                ToastUtil.showToast(this, "再按一次退出程序", Toast.LENGTH_SHORT);
                mExitTime = System.currentTimeMillis();
            } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}