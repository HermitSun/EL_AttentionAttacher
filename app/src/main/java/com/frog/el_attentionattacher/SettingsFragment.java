package com.frog.el_attentionattacher;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;


import utils.PrefUtils;
import utils.ToastUtil;

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
                    ToastUtil.showToast(getActivity(), "Weather", Toast.LENGTH_SHORT);
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

}