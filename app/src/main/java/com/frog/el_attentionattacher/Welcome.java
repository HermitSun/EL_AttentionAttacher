package com.frog.el_attentionattacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frog.el_attentionattacher.db.PersonalInfoData;

import org.litepal.crud.DataSupport;

import java.util.List;

import utils.ActivityCollector;

/**
 * login界面
 * Framed by Wen Sun
 */

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;
    private Editor editor;
    private ImageView background;
    private EditText account;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int command = bundle.getInt("command");
            if (command == 171250662) {
                ActivityCollector.finishOthers(this);
            }
        }
        //启动时解析intent，清除其他activity，如果不是通过切换账号前来则跳过
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //将任务栏加入布局
        setContentView(R.layout.activity_welcome);
        preferences = getSharedPreferences("welcomeActivity", MODE_PRIVATE);
        //初始化
        //判断是否首次进入
        if (preferences.getBoolean("welcome_firstStart", true)) {
            editor = preferences.edit();
            //设置为false，不再显示引导页
            editor.putBoolean("welcome_firstStart", false);
            editor.apply();
            Intent toGuide = new Intent(this, GuideActivity.class);
            startActivity(toGuide);
            this.finish();
        } else {
            //不是首次登录
            background = (ImageView) findViewById(R.id.welcome_background);
            Glide.with(Welcome.this).load(R.drawable.guide_background).into(background);
            //加载背景
            account = (EditText) findViewById(R.id.account);
            password = (EditText) findViewById(R.id.password);
            Button enterNewWorld = (Button) findViewById(R.id.enter_new_world);
            Button registerAgain = (Button) findViewById(R.id.register_again);
            enterNewWorld.setOnClickListener(this);
            registerAgain.setOnClickListener(this);
            //进入界面按钮
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_new_world:
                String accountGot = account.getText().toString();
                String passwordGot = password.getText().toString();
                int id = 0;
                if (TextUtils.isEmpty(accountGot) || TextUtils.isEmpty(passwordGot)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Welcome.this);
                    dialog.setTitle("登录失败");
                    dialog.setMessage("账号密码不能为空！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            account.setText("");
                            password.setText("");
                        }
                    });
                    dialog.show();
                    break;
                } else {
                    List<PersonalInfoData> list = DataSupport.findAll(PersonalInfoData.class);
                    boolean flag = false;
                    for (PersonalInfoData data : list) {
                        String accountData = data.getAccount();
                        String passwordData = data.getPassword();
                        if (accountData.equals(accountGot) && passwordData.equals(passwordGot)) {
                            flag = true;
                            id = data.getId();
                            break;
                        }
                    }
                    if (!flag) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Welcome.this);
                        dialog.setTitle("登录失败");
                        dialog.setMessage("账号或密码错误!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                account.setText("");
                                password.setText("");
                            }
                        });
                        dialog.show();
                        break;
                    }
                    Intent enter = new Intent(this, AttentionAttacherActivity.class);
                    enter.putExtra("user_id", id);
                    startActivity(enter);
                    this.finish();
                    //进入界面不需要出现两次，直接finish
                    break;
                }
            case R.id.register_again:
                Intent regist = new Intent(this, GuideActivity.class);
                startActivity(regist);
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