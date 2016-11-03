package com.techjumper.polyhome.b.home.js;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.techjumper.polyhome.b.home.UserInfoManager;

/**
 * Created by kevin on 16/6/7.
 */
public class AndroidForJs {

    private Context mContext;

    public AndroidForJs(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public String getTicket() {
        String ticket = UserInfoManager.getTicket();
        return TextUtils.isEmpty(ticket) ? "" : ticket;
    }

    @JavascriptInterface
    public String getUserId() {
        String userId = UserInfoManager.getUserId();
        return TextUtils.isEmpty(userId) ? "" : userId;
    }

    @JavascriptInterface
    public String getFamilyId() {
        String familyId = UserInfoManager.getFamilyId();
        return TextUtils.isEmpty(familyId) ? "" : familyId;
    }
}
