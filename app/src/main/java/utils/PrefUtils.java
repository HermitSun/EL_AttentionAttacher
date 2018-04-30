package utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.frog.el_attentionattacher.app.BasicApp;

/**
 * SharedPreferences工具类
 * Created by tomchen on 2/3/16.
 * Modified by Wen Sun
 */
public class PrefUtils {

    //日间或者夜间模式
    private static final String PRE_THEME_MODE = "night_mode";

    //背景图模式 和R.string.save_net_mode相同
    private static final String PRE_SAVE_BACKGROUND_MODE = "save_background_mode";

    private static final String PRE_NAME = "com.frog.el_attentionattacher";

    private static SharedPreferences getSharedPreferences() {
        return BasicApp.getContext()
                .getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isNightMode() {
        return getSharedPreferences().getBoolean(PRE_THEME_MODE, false);
    }

    /**
     * 夜间模式
     * true为夜间模式
     */
    public static void setNightMode(boolean isDarkMode) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PRE_THEME_MODE, isDarkMode);
        editor.commit();
    }

    /**
     * 根据天气切换背景
     */
    public static boolean isSaveBackgroundMode() {
        return getSharedPreferences().getBoolean(PRE_SAVE_BACKGROUND_MODE, false);
    }

    /**
     * true为根据天气切换
     */
    public static void setPreSaveBackgroundMode(boolean isSaveBackgroundMode) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(PRE_SAVE_BACKGROUND_MODE, isSaveBackgroundMode);
        editor.commit();
    }

    /**
     * 删除 SharedPreferences 的某个 key
     *
     * @param key
     */
    public static void removeFromPrefs(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }
}