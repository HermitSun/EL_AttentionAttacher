package utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activities);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public static void finishOthers(Activity thisActivity) {
        for (Activity activity : activities) {
            if(activity!=thisActivity){
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activities.clear();
        activities.add(thisActivity);
    }
}