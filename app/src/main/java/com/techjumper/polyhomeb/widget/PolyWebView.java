package com.techjumper.polyhomeb.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.techjumper.polyhomeb.interfaces.IWebView;
import com.techjumper.polyhomeb.other.JavascriptObject;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyWebView extends AdvancedWebView {

    private IWebView iWebView;

    public PolyWebView(Context context) {
        super(context);
    }

    public PolyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addJsInterface(Activity activity, String clazzName) {
        addJavascriptInterface(new JavascriptObject(activity), clazzName);
    }

    public void setPolyWebViewListener(IWebView iWebView) {
        this.iWebView = iWebView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (iWebView != null) {
            iWebView.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
