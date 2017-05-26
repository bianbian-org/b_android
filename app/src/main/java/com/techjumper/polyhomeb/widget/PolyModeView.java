package com.techjumper.polyhomeb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class PolyModeView extends RatioFrameLayout {

//    @Bind(R.id.iv_icon)
    ImageView mIvIcon;

//    @Bind(R.id.iv_rightTop_icon)
    ImageView mIvRightTopIcon;

//    @Bind(R.id.tv_text)
    TextView mTvText;

    private View mInflateView;
    private Drawable mDrawable;
    private Drawable mTopRightDrawable;
    private String mText;
    private int mTextColor;
    private float mIconMargin;
    private float mTextMargin;
    private float mTextSize;
    boolean notFirst;
    private int mIvWidth, mIvHeight;


    public PolyModeView(Context context) {
        super(context);
    }

    public PolyModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolyModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PolyModeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean init(AttributeSet attrs) {
        super.init(attrs);
//        if (notFirst) return false;
        notFirst = true;
        mInflateView = LayoutInflater.from(getContext()).inflate(R.layout.layout_poly_mode, null);
        addView(mInflateView);
//        ButterKnife.bind(this, mInflateView);

        mIvIcon = (ImageView) mInflateView.findViewById(R.id.iv_icon);
        mTvText = (TextView) mInflateView.findViewById(R.id.tv_text);
        mIvRightTopIcon = (ImageView) mInflateView.findViewById(R.id.iv_rightTop_icon);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PolyMode);
        mDrawable = ta.getDrawable(R.styleable.PolyMode_src);
        mTopRightDrawable = ta.getDrawable(R.styleable.PolyMode_topRightIcon);
        mText = ta.getString(R.styleable.PolyMode_text);
        mTextColor = ta.getColor(R.styleable.PolyMode_textColor, ResourceUtils.getColorResource(R.color.color_37a991));
        mIconMargin = -ta.getDimension(R.styleable.PolyMode_iconMarginTop, 0.F);
        mTextMargin = ta.getDimension(R.styleable.PolyMode_textMarginTop, 0.F);
        mTextSize = ta.getDimension(R.styleable.PolyMode_textSize, RuleUtils.dp2Px(14));
        mIvHeight = ta.getInteger(R.styleable.PolyMode_centerIconHeight, 40);  //layout_poly_mode中iv的大小就是40
        mIvWidth = ta.getInteger(R.styleable.PolyMode_centerIconWidth, 40);
        ta.recycle();

        mIvIcon.setImageDrawable(mDrawable);
        mIvRightTopIcon.setImageDrawable(mTopRightDrawable);
        mTvText.setText(mText);
        mTvText.setTextColor(mTextColor);
        mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        setMarginBottom(mIvIcon, mIconMargin);
        setMarginTop(mTvText, mTextMargin);
        setIconSize(mIvIcon, mIvWidth, mIvHeight);
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

    private void setIconSize(ImageView view, int width, int height) {
        ViewGroup.LayoutParams ivParams = view.getLayoutParams();
        ivParams.height = RuleUtils.dp2Px(height);
        ivParams.width = RuleUtils.dp2Px(width);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        ButterKnife.unbind(this);
        notFirst = false;
    }

    public void setIcon(int resId) {
        if (resId > 0)
            PicassoHelper.load(resId).into(mIvIcon);
        else mIvIcon.setImageBitmap(null);
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            mIvIcon.setImageDrawable(drawable);
        }
    }

    public void setIcon(String url) {
        if (!TextUtils.isEmpty(url)) {
            PicassoHelper.load(url).into(mIvIcon);
        }
    }

    public ImageView getIconView() {
        return mIvIcon;
    }

    public void setRightTopIcon(int resId) {
        if (resId > 0)
            PicassoHelper.load(resId).into(mIvRightTopIcon);
        else mIvRightTopIcon.setImageBitmap(null);
    }

    public void setText(String text) {
        mTvText.setText(text);
    }

    public void appendText(String text) {
        mTvText.append("\n" + text);
    }
}
