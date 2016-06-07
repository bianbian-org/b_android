package com.techjumper.polyhome.b.home.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by kevin on 16/6/7.
 */
public class AndroidForJs {

    private Context mContext;

    public AndroidForJs(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public String getId() {
        String id = "伍老师没我帅";
        return id;
    }
}
