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

import com.bumptech.glide.Glide;
import com.frog.el_attentionattacher.db.PersonalInfoData;

import org.litepal.crud.DataSupport;

import java.util.List;

import utils.ActivityCollector;

/**
 * 进入界面
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //将任务栏加入布局
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
            background=(ImageView)findViewById(R.id.welcome_background);
            Glide.with(Welcome.this).load(R.drawable.guide_background).into(background);
            //加载背景
            account = (EditText) findViewById(R.id.account);
            password = (EditText) findViewById(R.id.password);
            Button enterNewWorld = (Button) findViewById(R.id.enter_new_world);
            enterNewWorld.setOnClickListener(this);
            //进入界面按钮
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_new_world:
                String accountGot = account.getText().toString();
                String passwordGot = password.getText().toString();
                if (TextUtils.isEmpty(accountGot) || TextUtils.isEmpty(passwordGot)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Welcome.this);
                    dialog.setTitle("登录失败");
                    dialog.setMessage("账号和密码不能为空！");
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
                    Intent intent = new Intent(this, AttentionAttacherActivity.class);
                    startActivity(intent);
                    this.finish();
                    //进入界面不需要出现两次，直接finish
                    break;
                }
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