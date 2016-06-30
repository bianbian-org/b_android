package com.techjumper.lightwidget.jumper_circle;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.techjumper.lightwidget.R;
import com.techjumper.lightwidget.jumper_circle.interfaces.IJumperProgress;
import com.techjumper.lightwidget.jumper_circle.interfaces.IJumperProgressChangeDone;
import com.techjumper.lightwidget.jumper_circle.interfaces.IPainter;


/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/28
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JumperProgress extends View {

    private boolean mNotFirst;
    private Bitmap mBackgroundBitmap;
    private Canvas mBackgroundCanvas;
    private Rect mTouchRect;
    private Paint mUnselectLinePaint;
    private Paint mSelectLinePaint;
    private Path mInnerPath;
    private Path mOuterPath;
    private IPainter mPainter;
    private Animator mSwipeAnimator;
    private IJumperProgress iJumperProgress;
    private IJumperProgressChangeDone iJumperProgressChangeDone;
    private Bitmap mThumbBitmap;

    private int mLineUnSelectColor = 0x88979797;
    private int mLineSelectColor = 0xFF37A991;
    private int mLineFirstColor = 0xFFFF6C3A;
    private int mLineEndColor = 0xFF8CDBE7;
    private int mLineLength;
    private int mDegree = 3;
    private int mBreach = 82;
    private int mCurrDegree;
    private int mAnimDuration = 200;
    private int mTouchSlop;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mScale = 0.8F;
    private boolean mIsDown, mIsMove;
    private int mDownAngle;

    public JumperProgress(Context context) {
        super(context);
        init(null);
    }

    public JumperProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public JumperProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JumperProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (mNotFirst) return;
        mNotFirst = true;

        mLineLength = Utils.dp2Px(getContext(), 22.5F);

        mUnselectLinePaint = new Paint();
        mUnselectLinePaint.setStrokeWidth(Utils.dp2Px(getContext(), 1));
        mUnselectLinePaint.setAntiAlias(true);
        mUnselectLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mSelectLinePaint = new Paint();
        mSelectLinePaint.setStrokeWidth(Utils.dp2Px(getContext(), 1.5F));
        mSelectLinePaint.setAntiAlias(true);
        mSelectLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mTouchRect = new Rect();

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        mPainter = SelectLinePainter.getPainter()
                .setColor(mLineSelectColor)
                .setLineWidth(Utils.dp2Px(getContext(), 1.5F))
                .setStartDegree(getSelectStartDegree())
                .setEndDegree(getSelectEndDegree())
                .setDirection(IPainter.Direction.CCW)
                .setLineLength(mLineLength)
                .setPerDegree(mDegree);
        adjustDegree(getSelectEndDegree());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        recycle();
        mBackgroundBitmap = null;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }

    private void recycle() {
        if (mBackgroundBitmap != null && !mBackgroundBitmap.isRecycled()) {
            mBackgroundBitmap.recycle();
        }
        if (mThumbBitmap != null && !mThumbBitmap.isRecycled()) {
            mThumbBitmap.recycle();
            mThumbBitmap = null;
        }
        mPainter.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mInnerPath == null || mOuterPath == null) return super.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (!isValidRegion(event)) return super.onTouchEvent(event);
                int downAngle = (int) Utils.calculateSwipeAngle(mTouchRect
                        , new Point((int) event.getX(), (int) event.getY()));
                if (!isValidAngle(downAngle))
                    return super.onTouchEvent(event);
                mDownAngle = downAngle;
                mIsDown = true;
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (smallThanSlop(event)) return super.onTouchEvent(event);
//                Log.d("HIDETAG", "x:" + event.getX() + "; y:" + event.getY());
                int moveAngle = (int) Utils.calculateSwipeAngle(mTouchRect
                        , new Point((int) event.getX(), (int) event.getY()));
                if (!isValidAngle(moveAngle))
                    return super.onTouchEvent(event);
                if (mSwipeAnimator != null && mSwipeAnimator.isRunning()) mSwipeAnimator.cancel();
                mIsMove = true;
                setCurrentDegree(moveAngle);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDown && !mIsMove) {
                    updateDegree(mDownAngle);
                } else if(mIsDown) {
                    if (iJumperProgressChangeDone != null) {
                        iJumperProgressChangeDone.onProgressChanged();
                    }
                }
                mIsDown = mIsMove = false;
                mDownAngle = 0;
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mThumbBitmap == null) {
            mThumbBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_thumb);
            mThumbBitmap = Utils.scaleBitmap(mThumbBitmap, 1 / mScale, 1 / mScale);
        }
    }

    private boolean smallThanSlop(MotionEvent event) {
        return Math.abs(event.getX() - mLastTouchX) < mTouchSlop
                && Math.abs(event.getY() - mLastTouchY) < mTouchSlop;
    }

    private boolean isValidRegion(MotionEvent event) {
        return Utils.isInRegion(mOuterPath, event) && !Utils.isInRegion(mInnerPath, event);
    }

    private boolean isValidAngle(int angle) {
        return angle >= getSelectStartDegree() && angle <= getSelectEndDegree();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScale, mScale, getWidth() / 2, getHeight() / 2);
        if (mBackgroundBitmap != null && mBackgroundBitmap.isRecycled()) return;
        if (mBackgroundCanvas == null || mBackgroundBitmap == null) {
            mBackgroundBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mBackgroundCanvas = new Canvas(mBackgroundBitmap);
            mInnerPath = new Path();
            int dp10 = Utils.dp2Px(getContext(), 10);
            mInnerPath.addCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2 - mLineLength - dp10, Path.Direction.CW);
            Matrix matrix = new Matrix();
            matrix.postScale(mScale, mScale, getWidth() / 2, getHeight() / 2);
            mInnerPath.transform(matrix);
            updateTouchRect();
            drawUnselectLine();
        }

        canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
        mPainter.draw(canvas, mCurrDegree);
        drawIndicator(canvas);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas) {

        if (mOuterPath == null) {
            mOuterPath = new Path();
            int dp10 = Utils.dp2Px(getContext(), 10);
            mOuterPath.addCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2 + dp10 + mThumbBitmap.getHeight(), Path.Direction.CW);
            Matrix matrix = new Matrix();
            matrix.postScale(mScale, mScale, getWidth() / 2, getHeight() / 2);
            mOuterPath.transform(matrix);
        }
        int dp = (int) (Utils.dp2Px(getContext(), 2.5F) / mScale);
        int y = -mThumbBitmap.getHeight() - dp;
        canvas.save();
        rotateCanvasWithCenter(canvas, getCurrentDegree());
        mSelectLinePaint.setColor(mLineSelectColor);
        canvas.drawLine(getWidth() / 2, y + mThumbBitmap.getHeight() / 2, getWidth() / 2, 0, mSelectLinePaint);
        canvas.drawBitmap(mThumbBitmap, getWidth() / 2 - mThumbBitmap.getWidth() / 2
                , y, null);
        canvas.restore();
    }

    private void updateTouchRect() {
        mTouchRect.set(-10000, -10000, getWidth() + 10000, getHeight() + 10000);
    }

    private int getSelectEndDegree() {
        return 360 - mBreach / 2;
    }

    private int getSelectStartDegree() {
        return getUnselectStartDegree() + mDegree;
    }


    public int getUnselectStartDegree() {
        return mBreach / 2;
    }

    public int getUnselectEndDegree() {
        return 360 - mBreach;
    }

    private void drawUnselectLine() {
        mUnselectLinePaint.setColor(mLineUnSelectColor);
        int count = 1;
        mBackgroundCanvas.save();
        rotateCanvasWithCenter(mBackgroundCanvas, getUnselectStartDegree());
        boolean isFirst = true;
        while (count * mDegree < getUnselectEndDegree()) {
            drawLine(mBackgroundCanvas, mLineLength, mUnselectLinePaint);
            if (isFirst) {
                mSelectLinePaint.setColor(mLineFirstColor);
                drawLine(mBackgroundCanvas, mLineLength, mSelectLinePaint);
                isFirst = false;
            }
            rotateCanvasWithCenter(mBackgroundCanvas, mDegree);
            count++;
        }
        mSelectLinePaint.setColor(mLineEndColor);
        drawLine(mBackgroundCanvas, mLineLength, mSelectLinePaint);
        mBackgroundCanvas.restore();
    }

    private void drawLine(Canvas canvas, int length, Paint paint) {
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, length, paint);
    }

    private void rotateCanvasWithCenter(Canvas canvas, int degree) {
        canvas.rotate(degree, getWidth() / 2, getHeight() / 2);
    }

    public int getCurrentDegree() {
        return mCurrDegree;
    }

    public void setPercent(float percent) {
        percent = clamp(percent, 0.F, 1.F);
        int totalDegree = getSelectEndDegree() - getSelectStartDegree();
        float degree = totalDegree * 1.F * percent + getSelectStartDegree();
        updateDegree((int) (mPainter.getDirection() == IPainter.Direction.CW ? degree : 360 - degree));
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private void updateDegree(int degree) {
        startAnim(degree);
    }


    private void setCurrentDegree(int degree) {
        adjustDegree(degree);
        if (iJumperProgress != null) {
            iJumperProgress.onProgressUpdate(currentPercent());
        }
        invalidate();
    }

    private float currentPercent() {
        int totalDegree = getSelectEndDegree() - getSelectStartDegree();
        float percent = (mCurrDegree - getSelectStartDegree()) * 1.F / totalDegree;
        if (mPainter.getDirection() == IPainter.Direction.CCW) percent = 1 - percent;
        return percent;
    }

    public void setOnProgressChangeListener(IJumperProgress iJumperProgress) {
        this.iJumperProgress = iJumperProgress;
    }

    public void setOnProgressDoneListener(IJumperProgressChangeDone iJumperProgressChangeDone) {
        this.iJumperProgressChangeDone = iJumperProgressChangeDone;
    }

    private void adjustDegree(int degree) {
        if (mPainter.getDirection() == IPainter.Direction.CW) {
            mCurrDegree = degree;
            return;
        }
        int remainder = Math.abs(degree - getUnselectStartDegree()) % mDegree;
        mCurrDegree = degree - remainder;
    }

    private void startAnim(int degree) {
        if (mSwipeAnimator != null && mSwipeAnimator.isRunning()) mSwipeAnimator.cancel();

        mSwipeAnimator = ObjectAnimator.ofInt(this, "currentDegree", degree)
                .setDuration(mAnimDuration);
        mSwipeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSwipeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (iJumperProgressChangeDone != null) {
                    iJumperProgressChangeDone.onProgressChanged();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSwipeAnimator.start();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState ss = new SaveState(parcelable);
        ss.mCurrDegree = this.mCurrDegree;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SaveState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mCurrDegree = ss.mCurrDegree;
        postInvalidate();
    }

    private static class SaveState extends BaseSavedState {

        int mCurrDegree;

        public SaveState(Parcel source) {
            super(source);
            this.mCurrDegree = source.readInt();
        }

        public SaveState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mCurrDegree);
        }

        public static final Parcelable.Creator<SaveState> CREATOR =
                new Parcelable.Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };
    }

}
