package com.casc.pgkg.activity;

import android.app.Activity;

import com.casc.pgkg.MyParams;
import com.casc.pgkg.MyVars;
import com.casc.pgkg.activity.LoginActivity;
import com.casc.pgkg.helper.SpHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<>();

    private ActivityCollector(){}

    public static <T> boolean topNotOf(Class<T> c) {
        return c == null || !c.equals(getTopActivity().getClass());
    }

    public static Activity getTopActivity() {
        return activities.get(activities.size() - 1);
    }

    public static void addActivity(Activity activity) {
        if (activities == null) {
            return;
        }
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        activities.remove(activity);
    }

    public static void removeActivityAndFinish(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activities.remove(activity) && !activity.isFinishing()) {
            activity.finish();
        }
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void backToLogin() {
        for (int i = activities.size() - 1; i > 0; i--) {
            if (!activities.get(i).isFinishing()) {
                activities.get(i).finish();
            }
        }
        SpHelper.clear(MyParams.S_USERNAME);
        SpHelper.clear(MyParams.S_PASSWORD);
        activities.get(0).finish();
        LoginActivity.actionStart(activities.get(0));
    }
}
