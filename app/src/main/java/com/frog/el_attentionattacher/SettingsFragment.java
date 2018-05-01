package com.frog.el_attentionattacher;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;


import utils.PrefUtils;
import utils.ToastUtil;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //设置界面布局
        final CheckBoxPreference backgroundPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.save_background_mode));

        backgroundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中
                PrefUtils.setPreSaveBackgroundMode(checked);
                if (PrefUtils.isSaveBackgroundMode()) {
                    ToastUtil.showToast(getActivity(), "已切换。设置完成后请刷新主界面。", Toast.LENGTH_SHORT);
                    Intent setLocation = new Intent(getActivity(), ChooseWeatherActivity.class);
                    startActivityForResult(setLocation, 21);
                }
                //启动时确认
                return true;
            }
        });
        //切换背景图模式
        final CheckBoxPreference nightPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.save_night_mode));

        nightPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中
                PrefUtils.setNightMode(checked);
                if (PrefUtils.isNightMode()) {
                    ToastUtil.showToast(getActivity(), "Night", Toast.LENGTH_SHORT);
                }
                //启动时确认
                return true;
            }
        });
        //夜间模式（未实现）
        final Preference accountPref = (Preference) getPreferenceManager()
                .findPreference(getString(R.string.change_account));

        accountPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final int KILL_OTHER_PROGRESS = 171250662;
                Intent changeAccount = new Intent(getActivity(), Welcome.class);
                changeAccount.putExtra("command", KILL_OTHER_PROGRESS);
                startActivity(changeAccount);
                getActivity().finish();
                return true;
            }
        });
        //切换账号
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 21:
                if(resultCode==RESULT_OK){
                    String weather_id=data.getStringExtra("weather_id");
                    Intent back=new Intent();
                    back.putExtra("weather_id",weather_id);
                    getActivity().setResult(RESULT_OK,back);
                    getActivity().finish();
                }
                break;
            default:
                break;
        }
    }

}