package com.techjumper.polyhomeb.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.techjumper.corelib.utils.bitmap.BitmapUtils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;

import butterknife.ButterKnife;

public class SwitchButton extends ToggleButton
        implements CompoundButton.OnCheckedChangeListener {

    public static final long MOVEMENT_ANIMATION_DURATION_MS = 200;

    private Bitmap mTracker;
    private Bitmap mThumb;

    private RectF mTrackerRectF;
    private RectF mThumbRectF = new RectF();

    private ObjectAnimator mAnimator;


    private float mMinRange;
    private float mMaxRange;
    private float mCurrRange;

    private float mUncheckAlpha = 0.4F;
    private float mCheckAlpha = 1.F;
    private float mPaddingHorizontal = RuleUtils.dp2Px(2.2F);
    private float mPaddingVertical = RuleUtils.dp2Px(0.6F);
    private float mThumbWidth;
    private float mThumbHeight;

    private boolean isInited;
    private OnCheckedChangeListener mListener;
    private ISwitchUpdate iSwitchUpdate;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchButton(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (isInited) return;
        ButterKnife.bind(this);
        isInited = true;
        setBackgroundColor(Color.argb(0, 0, 0, 0));
        setText("");
        setTextOn("");
        setTextOff("");

        mThumb = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_thumb);
        mTracker = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_track);

        super.setOnCheckedChangeListener(this);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isInited = false;
        ButterKnife.unbind(this);
        BitmapUtils.recycleBitmap(mThumb);
        BitmapUtils.recycleBitmap(mTracker);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fixWidth = mTracker.getWidth();
        int fixHeight = mTracker.getHeight();
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(fixWidth, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(fixHeight, MeasureSpec.EXACTLY));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (mListener != null) mListener.onCheckedChanged(SwitchButton.this, isChecked);
        post(() -> startAnim(isChecked));
    }

    public void setSlideProgress(float progress) {
        mCurrRange = clamp(progress);
        updateAlpha();
        updateThumbRect();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void updateAlpha() {
        float percent = mCurrRange / mMaxRange;
        float max = mCheckAlpha - mUncheckAlpha;
        setAlpha(mUncheckAlpha + max * percent);
    }

    public float getSlideProgress() {
        return mCurrRange;
    }

    public float clamp(float progress) {
        return Math.max(mMinRange, Math.min(mMaxRange, progress));
    }

    public void updateThumbRect() {
        mThumbRectF.set(mPaddingHorizontal + mCurrRange + RuleUtils.dp2Px(0.4F)
                , mPaddingVertical
                , mPaddingHorizontal + mThumbWidth + mCurrRange
                , mPaddingVertical + mThumbHeight);
    }

    public void startAnim(boolean checked) {
        if (mMaxRange <= 0.F) calcuMaxRange();
        float end = checked ? mMaxRange : mMinRange;
        if (mAnimator != null && mAnimator.isRunning()) mAnimator.cancel();
        mAnimator = ObjectAnimator.ofFloat(this, "slideProgress", end);
        mAnimator.setDuration(MOVEMENT_ANIMATION_DURATION_MS);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(animation -> {
            if (iSwitchUpdate == null) return;
            float percent = animation.getAnimatedFraction();
            iSwitchUpdate.onSwitchProgress(isChecked() ? percent : (1 - percent));
        });
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTrackerRectF == null) {
            int fixWidth = mTracker.getWidth();
            int fixHeight = mTracker.getHeight();
            calcuTumbWith();
            mThumbHeight = mThumb.getHeight() * 1.F / mThumb.getWidth() * mThumbWidth;

            calcuMaxRange();
            mTrackerRectF = new RectF();
            mTrackerRectF.set(0, 0, fixWidth, fixHeight);
            updateAlpha();
            updateThumbRect();
        }

        canvas.drawBitmap(mTracker, null, mTrackerRectF, null);
        canvas.drawBitmap(mThumb, null, mThumbRectF, null);
    }

    private void calcuTumbWith() {
        mThumbWidth = mTracker.getWidth() / 2 + mPaddingHorizontal;
    }

    private void calcuMaxRange() {
        if (mTracker == null) return;
        if (mThumbWidth <= 0) calcuTumbWith();
        mMaxRange = mTracker.getWidth() - mPaddingHorizontal * 2 - mThumbWidth - RuleUtils.dp2Px(1);
    }

    public void setOnSwitchUpdateListener(ISwitchUpdate switchUpdate) {
        this.iSwitchUpdate = switchUpdate;
    }

    public interface ISwitchUpdate {
        void onSwitchProgress(float percent);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState ss = new SaveState(parcelable);
        ss.mTrackerRectF = this.mTrackerRectF;
        ss.mThumbRectF = this.mThumbRectF;
        ss.mMinRange = this.mMinRange;
        ss.mMaxRange = this.mMaxRange;
        ss.mCurrRange = this.mCurrRange;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SaveState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mTrackerRectF = ss.mTrackerRectF;
        this.mThumbRectF = ss.mThumbRectF;
        this.mMinRange = ss.mMinRange;
        this.mMaxRange = ss.mMaxRange;
        this.mCurrRange = ss.mCurrRange;
        postInvalidate();
    }


    private static class SaveState extends BaseSavedState {

        RectF mTrackerRectF;
        RectF mThumbRectF;
        float mMinRange;
        float mMaxRange;
        float mCurrRange;


        public SaveState(Parcel source) {
            super(source);
            this.mTrackerRectF = source.readParcelable(RectF.class.getClassLoader());
            this.mThumbRectF = source.readParcelable(RectF.class.getClassLoader());
            this.mMinRange = source.readFloat();
            this.mMaxRange = source.readFloat();
            this.mCurrRange = source.readFloat();

        }

        public SaveState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(mTrackerRectF, flags);
            dest.writeParcelable(mThumbRectF, flags);
            dest.writeFloat(this.mMinRange);
            dest.writeFloat(this.mMaxRange);
            dest.writeFloat(this.mCurrRange);
        }

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };
    }
}