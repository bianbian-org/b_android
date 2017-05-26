package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lib2.utils.PicassoHelper;
import com.techjumper.lightwidget.ratio.RatioFrameLayout;
import com.techjumper.polyhomeb.R;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class IconTextSwitchView extends RatioFrameLayout implements View.OnClickListener {

    public static final int STATE_ICON = 0;
    public static final int STATE_TEXT = 1;

    private ImageView mIvIcon;
    private TextView mTvText;
    private int mCurrState = STATE_ICON;
    private IIconText iIconText;

    public IconTextSwitchView(Context context) {
        super(context);
    }

    public IconTextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconTextSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IconTextSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public IIconText getIIconText() {
        return iIconText;
    }

    public void setOnIconAndTextClickListener(IIconText iIconText) {
        this.iIconText = iIconText;
    }

    @Override
    protected boolean init(AttributeSet attrs) {
        if (!super.init(attrs)) return false;

        LayoutParams params = new LayoutParams(RuleUtils.dp2Px(18), RuleUtils.dp2Px(18));
        params.gravity = Gravity.CENTER;

        mIvIcon = new ImageView(getContext());
        mIvIcon.setFocusable(false);
        mIvIcon.setFocusableInTouchMode(false);
        setIcon(R.mipmap.icon_pencil);
        addView(mIvIcon, params);

        mTvText = new TextView(getContext());
        mTvText.setFocusable(false);
        mTvText.setFocusableInTouchMode(false);
        setTextSize(16);
        setTextColor(ResourceUtils.getColorResource(R.color.color_37a991));
        setText(getResources().getString(R.string.done));
        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER;
        addView(mTvText, params2);

        showIcon();

        setOnClickListener(this);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = RuleUtils.dp2Px(46);
        int height = RuleUtils.dp2Px(46);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public void setIcon(int resId) {
        if (resId != 0) {
            PicassoHelper.load(resId).into(mIvIcon);
        } else {
            mIvIcon.setImageBitmap(null);
        }
    }

    public void setTextSize(int sp) {
        mTvText.setTextSize(sp);
    }

    public void setTextColor(@ColorInt int color) {
        mTvText.setTextColor(color);
    }

    public void setText(String text) {
        mTvText.setText(text);
    }

    public void showText() {
        setVisible(true);
    }

    public void showIcon() {
        setVisible(false);
    }

    private void setVisible(boolean showText) {
        mIvIcon.setVisibility(showText ? View.GONE : View.VISIBLE);
        mTvText.setVisibility(showText ? View.VISIBLE : View.GONE);
        mCurrState = showText ? STATE_TEXT : STATE_ICON;
    }

    @Override
    public void onClick(View v) {
        if (iIconText == null) return;
        if (mCurrState == STATE_ICON) {
            showText();
            iIconText.onIconClick();
        } else {
            showIcon();
            iIconText.onTextClick();
        }


    }

    public interface IIconText {
        void onIconClick();

        void onTextClick();
    }
}

