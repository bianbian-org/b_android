package com.techjumper.lightwidget.touch;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.techjumper.lightwidget.touch.interfaces.ITouch;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/13
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HoldTouchView extends ImageView {

    public ITouch iTouch;

    public HoldTouchView(Context context) {
        super(context);
    }

    public HoldTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HoldTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HoldTouchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnHoldListener(ITouch iTouch) {
        this.iTouch = iTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (iTouch != null) {
                    iTouch.onHoldTouch(this);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (iTouch != null) {
                    iTouch.onHoldRelease(this);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
