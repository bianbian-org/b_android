package com.techjumper.polyhomeb.manager;

import android.app.Activity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ActivityStack {

    private static ActivityStack instance = new ActivityStack();
    private Activity resumeActivity;

    private ActivityStack() {

    }

    public static ActivityStack getInstance() {
        return instance;
    }

    public Activity getResumeActivity() {
        return resumeActivity;
    }

    public void setResumeActivity(Activity resumeActivity) {
        this.resumeActivity = resumeActivity;
    }
}
