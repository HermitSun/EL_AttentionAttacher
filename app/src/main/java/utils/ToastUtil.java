package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by codekxx
 * Modified by WenSun, on 2018/4/8
 * 用于处理多次点击按钮导致的Toast重叠
 * 加入了可选参数，默认LENGTH_SHORT
 */

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String content, int...lastTime) {
        if (lastTime.length > 0) {
            if (toast == null) {
                toast = Toast.makeText(context, content, lastTime[0]);
            } else {
                toast.setText(content);
            }
            toast.show();
        } else {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            } else {
                toast.setText(content);
            }
            toast.show();
        }
    }
}
