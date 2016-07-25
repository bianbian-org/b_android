package com.techjumper.polyhome_b.adlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.techjumper.polyhome_b.adlib.manager.AdController;
import com.techjumper.polyhome_b.adlib.window.AdWindowManager;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/7/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class CommonReceiver extends BroadcastReceiver {

    public static final String ACTION_CLOSE_AD_WINDOW = "action_close_ad_window";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_CLOSE_AD_WINDOW:
                AdController.getInstance().cancelWindowAd();
                AdWindowManager.getInstance().closeWindow(true);
                break;
            default:
                break;
        }
    }
}
