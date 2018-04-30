package com.frog.el_attentionattacher;

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
        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.save_net_mode));

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中
                PrefUtils.setSaveNetMode(checked);
                if(PrefUtils.isSaveNetMode()){
                    ToastUtil.showToast(getActivity(),"Open", Toast.LENGTH_SHORT);
                }
                //启动时确认
                return true;
            }
        });
    }

}
