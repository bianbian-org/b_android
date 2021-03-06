package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techjumper.corelib.utils.common.ResourceUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.lightwidget.ratio.RatioFrameLayout;
import com.techjumper.polyhomeb.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PolyTabView extends RatioFrameLayout {

    @Bind(R.id.iv_icon)
    ImageView mIvIcon;

    @Bind(R.id.tv_text)
    TextView mTvText;

    private View mInflateView;
    private Drawable mDrawable;
    private String mText;
    private int mTextColor;
    private float mIconMargin;
    private float mTextMargin;
    private float mTextSize;
    boolean notFirst;


    public PolyTabView(Context context) {
        super(context);
    }

    public PolyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolyTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PolyTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean init(AttributeSet attrs) {
        super.init(attrs);
        if (notFirst) return false;
        notFirst = true;
        mInflateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_poly_tab_view, null);
        addView(mInflateView);
        ButterKnife.bind(this, mInflateView);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PolyMode);
        mDrawable = ta.getDrawable(R.styleable.PolyMode_src);
        mText = ta.getString(R.styleable.PolyMode_text);
        mTextColor = ta.getColor(R.styleable.PolyMode_textColor, ResourceUtils.getColorResource(R.color.color_37a991));
        mIconMargin = -ta.getDimension(R.styleable.PolyMode_iconMarginTop, 0.F);
        mTextMargin = ta.getDimension(R.styleable.PolyMode_textMarginTop, 0.F);
        mTextSize = ta.getDimension(R.styleable.PolyMode_textSize, RuleUtils.dp2Px(14));
        ta.recycle();

        mIvIcon.setImageDrawable(mDrawable);
        mTvText.setText(mText);
        mTvText.setTextColor(mTextColor);
        mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        setMarginBottom(mIvIcon, mIconMargin);
        setMarginTop(mTvText, mTextMargin);
        return true;
    }

    private void setMarginTop(View view, float margin) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.topMargin += margin;
    }

    private void setMarginBottom(View view, float margin) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.bottomMargin += margin;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
        notFirst = false;
    }

    public void setIcon(int resId) {
        if (resId > 0)
//            PicassoHelper.loadWithNoFade(resId).into(mIvIcon);
            mIvIcon.setImageResource(resId);
        else mIvIcon.setImageBitmap(null);
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            mIvIcon.setImageDrawable(drawable);
        }
    }

    public ImageView getIconView() {
        return mIvIcon;
    }

    public void setText(String text) {
        mTvText.setText(text);
    }

    public void setTextColor(int color) {
        mTvText.setTextColor(color);
    }
}