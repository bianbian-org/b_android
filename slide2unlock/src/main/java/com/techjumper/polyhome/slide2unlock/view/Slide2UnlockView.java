package com.techjumper.polyhome.slide2unlock.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.techjumper.polyhome.slide2unlock.R;
import com.techjumper.polyhome.slide2unlock.interfaces.IUnLockViewState;
import com.techjumper.polyhome.slide2unlock.interpolator.Easing;
import com.techjumper.polyhome.slide2unlock.interpolator.RPInterpolator;
import com.techjumper.polyhome.slide2unlock.view.enums.LockViewResult;

import java.util.Timer;
import java.util.TimerTask;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/21
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Slide2UnlockView extends View {

    private Context mContext;
    private Paint mTextPaint, mInsideRightPaint, mOutSidePaint, mLockPaint, mOutSideStrokePaint, mInsideLeftPaint;
    private float mWidth;
    private float mHeight;
    private float mRadius;
    private RectF mBodyRect = new RectF();
    private RectF mStrokeRect = new RectF();
    private RectF mInsideLeftRect = new RectF();
    private RectF mInsideRightRect = new RectF();
    private Rect mTextBounds = new Rect();
    //锁圆圈图片
    private int mLockPicDefault, mLockPicSuccess, mLockPicFailed, mLockPicUnusable;
    //通过mLockPic得到的bitmap
    private Bitmap mLockBitmap;
    //内圈右边背景色,默认白色
    private int mInsideRightDefaultBgColor, mInsideRightSuccessBgColor, mInsideRightFailedBgColor;
    //内圈左边背景色,默认白色
    private int mInsideLeftDefaultBgColor, mInsideLeftSuccessBgColor, mInsideLeftFailedBgColor;
    //内外圈间距,默认0dp
    private int mInsidePadding;
    //外圈背景色,默认白色
    private int mOutsideDefaultBgColor, mOutsideSuccessBgColor, mOutsideFailedBgColor;
    //外圈描边宽度,默认2dp
    private int mOutsideStrokeWidth;
    //外圈描边宽度的绘制专用,是mOutsideStrokeWidth的一半
    private int mOutsideStrokeWidthReal;
    //外圈描边颜色,默认黑色
    private int mOutsideStrokeDefaultColor, mOutsideStrokeSuccessColor, mOutsideStrokeFailedColor;
    //文字
    private String mTextDefault, mTextSuccess, mTextFailed, mTextUnusable, mText;
    //文字颜色,默认黑色
    private int mTextDefaultColor, mTextSuccessColor, mTextFailedColor;
    //文字大小,默认12sp
    private int mTextSize;
    //是否可用,默认可用
    private boolean mIsUsable;
    //从开始解锁算起,等待多久才自动调用解锁成功or失败方法.或者可以理解为超时时间.超过mWaitTime之后还没主动调用解锁成功or失败的这段时间,默认5000毫秒
    private long mWaitTime;
    //解锁成功or失败之后,停留几秒重置回到左边,默认1500毫秒
    private long mDelayTime;
    //滑动的距离
    private float mSlidingDistance = 0f;
    //当前状态是否能够进行滑动
    private boolean mCanSlide;
    private Matrix mMatrix;
    //回调外部,通知当前这个View的状态(开始解锁,正在解锁)
    private IUnLockViewState mIUnLockViewState;
    //重置解锁图片时的动画差值器(默认CUBIC_IN)
    private Easing mEasing = Easing.CUBIC_IN;
    //动画时间
    private int mDuration = 300;
    //滑动时判断是不是在图片上,主要是针对滑动时
    private boolean mIsFingerOnPic;
    //当前是不是正在解锁中
    private boolean mIsUnLocking;
    //开始解锁以及解锁过成功,是否已经调用过解锁成功或者失败的方法
    private boolean mIsCalledResultMethod;
    //标志位,用于重置mIsCalledResultMethod(如果在解锁失败之后 的 N秒之后,成功或者失败的方法再次被调用了,那么就依靠这个标志位去重置用于重置mIsCalledResultMethod)
    private boolean mFlag;
    //开始解锁的时间戳
    private long mStartTime;
    private TimerTask mTask;
    private Timer mTimer = new Timer();
    //主要作用是处理mWaitTime以及调用解锁成功或者失败方法的逻辑
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                boolean b = (System.currentTimeMillis() - mStartTime <= mWaitTime) ? true : false;
                if (b) {  //true说明还在设定的mWaitTime时间范围内,坐等调用者随时调用成功或者失败的方法,
                    if (mIsCalledResultMethod) {
                        //调用过成功或者失败了.需要终止计时,并且将isCallingResultMethod置为false.
                        resetHandlerTask();
                    }
                } else {  //已经超过了设定的mWaitTime时间,需要View自己调用失败的方法
                    if (mIsCalledResultMethod) {
                        //调用过成功或者失败的方法了.需要终止计时,并将isCallingResultMethod置为false
                        resetHandlerTask();
                    } else {
                        //还没调用过,需要自己调用啦
                        unLockResult(LockViewResult.FAILED);
                        resetHandlerTask();
                    }
                }
            }
            return false;
        }
    });

    public Slide2UnlockView(Context context) {
        this(context, null);
    }

    public Slide2UnlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Slide2UnlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initTypedArray(context, attrs, defStyleAttr);
        init();
    }

    private void initTypedArray(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.Slide2UnlockView, defStyleAttr, 0);

        mLockPicDefault = tp.getResourceId(R.styleable.Slide2UnlockView_lock_pic_default, -1);
        mLockPicSuccess = tp.getResourceId(R.styleable.Slide2UnlockView_lock_pic_success, -1);
        mLockPicFailed = tp.getResourceId(R.styleable.Slide2UnlockView_lock_pic_failed, -1);
        mLockPicUnusable = tp.getResourceId(R.styleable.Slide2UnlockView_lock_pic_unusable, -1);

        mInsideRightDefaultBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_right_bg_color_default, Color.WHITE);
        mInsideRightSuccessBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_right_bg_color_success, Color.WHITE);
        mInsideRightFailedBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_right_bg_color_failed, Color.WHITE);

        mInsideLeftDefaultBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_left_bg_color_default, Color.WHITE);
        mInsideLeftSuccessBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_left_bg_color_success, Color.WHITE);
        mInsideLeftFailedBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_inside_left_bg_color_failed, Color.WHITE);

        mInsidePadding = tp.getDimensionPixelOffset(R.styleable.Slide2UnlockView_lock_inside_padding, 0);

        mOutsideDefaultBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_bg_color_default, Color.WHITE);
        mOutsideSuccessBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_bg_color_success, Color.WHITE);
        mOutsideFailedBgColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_bg_color_failed, Color.WHITE);

        mOutsideStrokeWidth = tp.getDimensionPixelOffset(R.styleable.Slide2UnlockView_lock_outside_stroke_width, 1);

        mOutsideStrokeDefaultColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_stroke_color_default, Color.BLACK);
        mOutsideStrokeSuccessColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_stroke_color_success, Color.BLACK);
        mOutsideStrokeFailedColor = tp.getColor(R.styleable.Slide2UnlockView_lock_outside_stroke_color_failed, Color.BLACK);

        mTextDefault = tp.getString(R.styleable.Slide2UnlockView_lock_text);

        mTextDefaultColor = tp.getColor(R.styleable.Slide2UnlockView_lock_text_color_default, Color.BLACK);
        mTextSuccessColor = tp.getColor(R.styleable.Slide2UnlockView_lock_text_color_success, Color.BLACK);
        mTextFailedColor = tp.getColor(R.styleable.Slide2UnlockView_lock_text_color_failed, Color.BLACK);

        mTextSize = tp.getDimensionPixelOffset(R.styleable.Slide2UnlockView_lock_text_size, 12);
        mIsUsable = tp.getBoolean(R.styleable.Slide2UnlockView_lock_is_usable, true);
        mWaitTime = tp.getInteger(R.styleable.Slide2UnlockView_lock_wait_time, 5000);
        mDelayTime = tp.getInteger(R.styleable.Slide2UnlockView_lock_delay_time, 1500);

        tp.recycle();
    }

    private void init() {
        mMatrix = new Matrix();
        //部分需要用这个值来计算
        mOutsideStrokeWidthReal = mOutsideStrokeWidth / 2;

        mTextPaint = new Paint();
        mInsideRightPaint = new Paint();
        mOutSidePaint = new Paint();
        mLockPaint = new Paint();
        mOutSideStrokePaint = new Paint();
        mInsideLeftPaint = new Paint();

        //解锁图片画笔
        mLockPaint.setAntiAlias(true);

        //外层描边画笔
        mOutSideStrokePaint.setAntiAlias(true);
        mOutSideStrokePaint.setStrokeWidth(mOutsideStrokeWidth);
        mOutSideStrokePaint.setColor(mOutsideStrokeDefaultColor);
        mOutSideStrokePaint.setStyle(Paint.Style.STROKE);

        //外层背景画笔
        mOutSidePaint.setAntiAlias(true);
        mOutSidePaint.setColor(mOutsideDefaultBgColor);
        mOutSidePaint.setStyle(Paint.Style.FILL);

        //内层右边背景画笔,滑动时不需要改变长度的那部分
        mInsideRightPaint.setAntiAlias(true);
        mInsideRightPaint.setColor(mInsideRightDefaultBgColor);
        mInsideRightPaint.setStyle(Paint.Style.FILL);

        //内层左边背景画笔,滑动时改变长度的那部分
        mInsideLeftPaint.setAntiAlias(true);
        mInsideLeftPaint.setColor(mInsideLeftDefaultBgColor);
        mInsideLeftPaint.setStyle(Paint.Style.FILL);

        //文字画笔
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextDefaultColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRadius = mHeight / 2;

        //外圈圆角矩形描边
        if (mOutsideStrokeWidth > 0) {
            mStrokeRect.left = mOutsideStrokeWidthReal;
            mStrokeRect.top = mOutsideStrokeWidthReal;
            mStrokeRect.right = mStrokeRect.left + mWidth - mOutsideStrokeWidth;
            mStrokeRect.bottom = mStrokeRect.top + mHeight - mOutsideStrokeWidth;
        }

        //外圈圆角矩形
        mBodyRect.left = mOutsideStrokeWidth;
        mBodyRect.top = mOutsideStrokeWidth;
        mBodyRect.right = mWidth - mOutsideStrokeWidth;
        mBodyRect.bottom = mHeight - mOutsideStrokeWidth;

        //内圈右边的圆角矩形(随着移动不需要变化的,在左边这个的下方,被它遮挡)
        mInsideRightRect.left = mOutsideStrokeWidth + mInsidePadding;
        mInsideRightRect.top = mOutsideStrokeWidth + mInsidePadding;
        mInsideRightRect.right = mBodyRect.right - mInsidePadding;
        mInsideRightRect.bottom = mBodyRect.bottom - mInsidePadding;

        if (mLockPicDefault == -1) {
            throw new RuntimeException("并没有设置解锁圆圈默认状态下的图片");
        }

        if (isFirst) {
            getBitmap(mLockPicDefault);
            isFirst = false;
        }
    }

    private boolean isFirst = true;

    private void getBitmap(int pic) {
        //将锁圆圈图片转换成bitmap
        mLockBitmap = BitmapFactory.decodeResource(mContext.getResources(), pic);
        int oldSize = mLockBitmap.getHeight();
        float newSize = 2 * mRadius - 2 * mOutsideStrokeWidth - 2 * mInsidePadding;
        float scale = newSize * 1.0f / oldSize;
        mMatrix.setScale(scale, scale);
        mLockBitmap = Bitmap.createBitmap(mLockBitmap, 0, 0, oldSize, oldSize, mMatrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutside(canvas);
        drawInsideRight(canvas);
        drawInsideLeft(canvas);
        drawText(canvas);
        drawLockBitmap(canvas);
    }

    private void drawInsideLeft(Canvas canvas) {
        //内圈左边的圆角矩形(随着移动需要变化的)
        mInsideLeftRect.left = mOutsideStrokeWidth + mInsidePadding;
        mInsideLeftRect.top = mOutsideStrokeWidth + mInsidePadding;
        mInsideLeftRect.right = mInsideLeftRect.left + mLockBitmap.getHeight() + mSlidingDistance;
        mInsideLeftRect.bottom = mInsideLeftRect.top + mLockBitmap.getHeight();
        //内层背景,左边部分
        canvas.drawRoundRect(mInsideLeftRect, mRadius - mOutsideStrokeWidth - mInsidePadding, mRadius - mOutsideStrokeWidth - mInsidePadding, mInsideLeftPaint);
    }

    private void drawInsideRight(Canvas canvas) {
        //内层背景,右边部分
        canvas.drawRoundRect(mInsideRightRect, mRadius - mOutsideStrokeWidth - mInsidePadding, mRadius - mOutsideStrokeWidth - mInsidePadding, mInsideRightPaint);
    }

    private void drawOutside(Canvas canvas) {
        //外层描边
        if (mOutsideStrokeWidth > 0) {
            canvas.drawRoundRect(mStrokeRect, mRadius, mRadius, mOutSideStrokePaint);
        }
        //外层背景
        canvas.drawRoundRect(mBodyRect, mRadius, mRadius, mOutSidePaint);
    }

    private void drawLockBitmap(Canvas canvas) {
        //画锁圆圈图片.初始情况下解锁圆圈图片的位置和内圈背景的位置的起点是一致的
        canvas.drawBitmap(mLockBitmap, mInsideRightRect.left + mSlidingDistance, mInsideRightRect.top, mLockPaint);
    }

    private void drawText(Canvas canvas) {
        //绘制文字
        if (!TextUtils.isEmpty(mTextDefault)) {
            mTextPaint.getTextBounds(mTextDefault, 0, mTextDefault.length(), mTextBounds);
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            float baseline = (mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(mTextDefault, getMeasuredWidth() / 2 - mTextBounds.width() / 2, baseline, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsUsable) return true;
        if (mIsUnLocking) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //如果解锁过程中触摸解锁图标才会触发回调，触碰其他地方则不会
                    if (isFingerOnLockPic(event.getX(), event.getY())) {
                        if (mIUnLockViewState != null) {
                            mIUnLockViewState.unLocking(this);
                        }
                    }
                    return true;
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isFingerOnLockPic(event.getX(), event.getY())) {
                        mCanSlide = true;
                    } else {
                        mCanSlide = false;
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (!mCanSlide) return true;
                    if (!isFingerOnLockPic_(event.getX(), event.getY())) return true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    controlMaxDistance(event.getX());
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    mIsFingerOnPic = false;
                    if (!mCanSlide) return true;
                    resetState();
                    break;
            }
            return super.onTouchEvent(event);
        }
    }

    //是否点击到图片
    private boolean isFingerOnLockPic(float x, float y) {
        //图片半径大小
        float r = mLockBitmap.getHeight() / 2;
        //图片圆心位置X坐标Y坐标
        float centerX = mInsidePadding + mOutsideStrokeWidth + r + mSlidingDistance;
        float centerY = mHeight / 2;
        return (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) < r * r;
    }

    //滑动时判断是不是在图片上
    private boolean isFingerOnLockPic_(float x, float y) {
        boolean fingerOnLockPic = isFingerOnLockPic(x, y);
        if (mIsFingerOnPic) return true;
        if (fingerOnLockPic) {
            mIsFingerOnPic = true;
            return mIsFingerOnPic;
        } else {
            mIsFingerOnPic = false;
            return mIsFingerOnPic;
        }
    }

    //限制滑动范围
    private void controlMaxDistance(float x) {
        float maxDistance = mInsideRightRect.right - mLockBitmap.getHeight() - mOutsideStrokeWidth - mInsidePadding;
        if (x < 0) {
            x = 0;
        }
        x += mInsidePadding + mOutsideStrokeWidth;
        mSlidingDistance = x - mOutsideStrokeWidth - mInsidePadding - mRadius;
        startUnlock(maxDistance);
    }

    private void startUnlock(float maxDistance) {
        if (mSlidingDistance < 0) {
            mSlidingDistance = 0;
        } else if (mSlidingDistance >= maxDistance) {
            mSlidingDistance = maxDistance;
            mCanSlide = false;
            mIsUnLocking = true;
            if (mIUnLockViewState != null) {
                //接口不为null,意味着调用者需要在解锁触发之后,等待其操作的回调结果,然后按照回调结果来判断解锁成功与否
                //使用情景类似于 需要判断返回内容的网络接口请求等
                //此时的mWaitTime参数按照默认值或者调用者自己手动设置的时间来控制.此处的mWaitTime可理解为超时
                //如果在mWaitTime时间之后,调用者依然没有调用成功或者失败的方法,那么就认为此次解锁失败.
                //如果在mWaitTime时间范围内,调用者调用了成功或者失败的方法,那么就由调用的方法来判断成功还是失败
                mIUnLockViewState.startUnLock(this);
                //判断isCallingResultMethod,如果在时间范围内为true了
                // ,那就说明调用者已经掉用过成功或者失败的方法,计时中断.
                //如果时间范围内还是false,那就说明调用者依然没有调用成功或者失败的方法,可以理解为解锁失败,那么这里主动调用失败方法,计时中断
                //开始计时的节点时刻:
                mStartTime = System.currentTimeMillis();
                mTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                };
                //延迟100毫秒启动任务,每隔500毫秒发送消息给Handler,检查一次是否超时和是否调用了成功或者失败的方法;
                mTimer.schedule(mTask, 100, 500);
            } else {
                //接口为null,意味着调用者自己不想手动调用解锁成功或者失败.
                //使用情景类似于,滑动之后立即成功或者滑动之后等待mWaitTime毫秒之后自动成功,可用于设置的开启关闭,
                //不需要判断返回内容的网络接口的请求等
                //此时的mWaitTime参数就按照默认值或者调用者自己设置的时间来控制解锁成功的延时等待时间
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unLockResult(LockViewResult.SUCCESS);
                    }
                }, mWaitTime);
            }
        }
    }

    //重置状态,图片动画过度回到最左边初始状态
    private void resetState() {
        final ObjectAnimator resetAnim = ObjectAnimator.ofFloat(mLockBitmap, "translationX", mSlidingDistance, 0f);
        resetAnim.setInterpolator(new RPInterpolator(mEasing));
        resetAnim.setDuration(mDuration);
        resetAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSlidingDistance = (Float) resetAnim.getAnimatedValue();
                Slide2UnlockView.this.invalidate();
            }
        });
        resetAnim.start();
        mIsUnLocking = false;
    }

    public void setOnUnLockViewStateListener(IUnLockViewState iUnLockViewState) {
        mIUnLockViewState = iUnLockViewState;
    }

    public void setLockPic(int mLockPic) {
        this.mLockPicDefault = mLockPic;
        invalidate();
    }

    public void setInsideRightBgColor(int mInsideRightBgColor) {
        this.mInsideRightDefaultBgColor = mInsideRightBgColor;
        invalidate();
    }

    public void setInsideLeftBgColor(int mInsideLeftBgColor) {
        this.mInsideLeftDefaultBgColor = mInsideLeftBgColor;
        invalidate();
    }

    public void setInsidePadding(int mInsidePadding) {
        this.mInsidePadding = mInsidePadding;
        invalidate();
    }

    public void setOutsideBgColor(int mOutsideBgColor) {
        this.mOutsideDefaultBgColor = mOutsideBgColor;
        invalidate();
    }

    public void setOutsideStrokeWidth(int mOutsideStrokeWidth) {
        this.mOutsideStrokeWidth = mOutsideStrokeWidth;
        invalidate();
    }

    public void setOutsideStrokeColor(int mOutsideStrokeColor) {
        this.mOutsideStrokeDefaultColor = mOutsideStrokeColor;
        invalidate();
    }

    public void setTextDefault(String mText) {
        this.mTextDefault = mText;
        invalidate();
    }

    public void setTextColor(int mTextColor) {
        this.mTextDefaultColor = mTextColor;
        invalidate();
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        invalidate();
    }

    public void setIsUsable(boolean mIsUsable) {
        this.mIsUsable = mIsUsable;
    }

    public void setWaitTime(long mWaitTime) {
        this.mWaitTime = mWaitTime;
    }

    public void setAnimationInfo(Easing easing, int duration) {
        this.mEasing = easing;
        this.mDuration = duration;
    }

    public void setUsable(boolean isUsable) {
        this.mIsUsable = isUsable;
        if (isUsable) {
            //以下属性恢复
            mOutSideStrokePaint.setColor(mOutsideStrokeDefaultColor);
            mInsideRightPaint.setColor(mInsideRightDefaultBgColor);
            mOutSidePaint.setColor(mOutsideDefaultBgColor);
            mInsideLeftPaint.setColor(mInsideLeftDefaultBgColor);
            getBitmap(mLockPicDefault);  //需要重新得到bitmap.
            mTextDefault = mText;
            mTextPaint.setColor(mTextDefaultColor);
        } else {
            //颜色  151,151,151    979797
            //描边设置为灰色
            //padding部分是外圈的背景色,如果不可用则设置为白色
            //内圈颜色设置为灰色
            //圆圈图片设置为灰色
            //文字设置为灰色,且没有特效
            mOutSideStrokePaint.setColor(0xFF979797);
            mInsideRightPaint.setColor(Color.WHITE);
            mOutSidePaint.setColor(Color.WHITE);
            mInsideLeftPaint.setColor(Color.WHITE);
            if (mLockPicUnusable == -1) {
                throw new RuntimeException("并没有设置解锁结果不可用的圆圈图片");
            }
            getBitmap(mLockPicUnusable);  //需要重新得到bitmap.
            mTextDefault = mTextUnusable;
            mTextPaint.setColor(0xFF979797);
        }
        this.setEnabled(mIsUsable);
        invalidate();
    }

    /*******
     * 以下属性均是解锁成功or失败之后的视觉表现,建议初始化view时就配置好
     *****/
    public void setInsideRightFailedBgColor(int mInsideRightFailedBgColor) {
        this.mInsideRightFailedBgColor = mInsideRightFailedBgColor;
    }

    public void setInsideRightSuccessBgColor(int mInsideRightSuccessBgColor) {
        this.mInsideRightSuccessBgColor = mInsideRightSuccessBgColor;
    }

    public void setInsideLeftSuccessBgColor(int mInsideLeftSuccessBgColor) {
        this.mInsideLeftSuccessBgColor = mInsideLeftSuccessBgColor;
    }

    public void setInsideLeftFailedBgColor(int mInsideLeftFailedBgColor) {
        this.mInsideLeftFailedBgColor = mInsideLeftFailedBgColor;
    }

    public void setOutsideSuccessBgColor(int mOutsideSuccessBgColor) {
        this.mOutsideSuccessBgColor = mOutsideSuccessBgColor;
    }

    public void setOutsideFailedBgColor(int mOutsideFailedBgColor) {
        this.mOutsideFailedBgColor = mOutsideFailedBgColor;
    }

    public void setOutsideStrokeSuccessColor(int mOutsideStrokeSuccessColor) {
        this.mOutsideStrokeSuccessColor = mOutsideStrokeSuccessColor;
    }

    public void setOutsideStrokeFailedColor(int mOutsideStrokeFailedColor) {
        this.mOutsideStrokeFailedColor = mOutsideStrokeFailedColor;
    }

    public void setTextSuccessColor(int mTextSuccessColor) {
        this.mTextSuccessColor = mTextSuccessColor;
    }

    public void setTextFailedColor(int mTextFailedColor) {
        this.mTextFailedColor = mTextFailedColor;
    }

    public void setLockPicSuccess(int mLockPicSuccess) {
        this.mLockPicSuccess = mLockPicSuccess;
    }

    public void setLockPicFailed(int mLockPicFailed) {
        this.mLockPicFailed = mLockPicFailed;
    }

    public void setLockPicUnusable(int mLockPicUnusable) {
        this.mLockPicUnusable = mLockPicUnusable;
    }

    public void setTextUnusable(String unusable) {
        this.mTextUnusable = unusable;
    }

    public void setTextSuccess(String success) {
        this.mTextSuccess = success;
    }

    public void setTextFailed(String failed) {
        this.mTextFailed = failed;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public boolean isUsable() {
        return mIsUsable;
    }

    public void autoUnlock(long time,Easing easing) {
        if (mLockPicSuccess == -1)
            throw new RuntimeException("并没有设置解锁成功后的圆圈图片");
        if (mLockPicFailed == -1)
            throw new RuntimeException("并没有设置解锁失败后的圆圈图片");

        final float maxDistance = mInsideRightRect.right - mLockBitmap.getHeight() - mOutsideStrokeWidth - mInsidePadding;

        final ObjectAnimator resetAnim = ObjectAnimator.ofFloat(mLockBitmap, "translationXauto", 0f, maxDistance);
        if (easing != null) {
            resetAnim.setInterpolator(new RPInterpolator(easing));
        }
        resetAnim.setDuration(time);
        resetAnim.addUpdateListener(animation -> {
            mSlidingDistance = (Float) resetAnim.getAnimatedValue();
            if (mSlidingDistance >= maxDistance) {
                mSlidingDistance = maxDistance;
            }
            invalidate();
        });
        resetAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startUnlock(maxDistance);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        resetAnim.start();
    }

    public void setDelayTime(long delayTime) {
        this.mDelayTime = delayTime;
    }

    private void resetHandlerTask() {
        if (mHandler != null) {
            mHandler.removeMessages(1);
        }
        mIsCalledResultMethod = true;
//        mFlag = true;
        mFlag = !mIsCalledResultMethod;
        mTask.cancel();
    }

    public void unLockResult(LockViewResult result) {
        mHandler.postDelayed(() -> {
            resetState();
            processDefault();
        }, mDelayTime);
        mIsCalledResultMethod = !mFlag;
        switch (result) {
            case SUCCESS:
                processSuccess();
                break;
            case FAILED:
                processFailed();
                break;
        }
        invalidate();
    }

    private void processSuccess() {
        mTextPaint.setColor(mTextSuccessColor);
        mInsideRightPaint.setColor(mInsideRightSuccessBgColor);
        mInsideLeftPaint.setColor(mInsideLeftSuccessBgColor);
        mOutSidePaint.setColor(mOutsideSuccessBgColor);
        mOutSideStrokePaint.setColor(mOutsideStrokeSuccessColor);
        mTextDefault = mTextSuccess;
        //还差解锁成功的图片样式
        if (mLockPicSuccess == -1)
            throw new RuntimeException("并没有设置解锁成功后的圆圈图片");
        getBitmap(mLockPicSuccess);
    }

    private void processFailed() {
        mTextPaint.setColor(mTextFailedColor);
        mInsideRightPaint.setColor(mInsideRightFailedBgColor);
        mInsideLeftPaint.setColor(mInsideLeftFailedBgColor);
        mOutSidePaint.setColor(mOutsideFailedBgColor);
        mOutSideStrokePaint.setColor(mOutsideStrokeFailedColor);
        mTextDefault = mTextFailed;
        //还差解锁失败的图片样式
        if (mLockPicFailed == -1)
            throw new RuntimeException("并没有设置解锁失败后的圆圈图片");
        getBitmap(mLockPicFailed);
    }

    private void processDefault() {
        mTextPaint.setColor(mTextDefaultColor);
        mInsideRightPaint.setColor(mInsideRightDefaultBgColor);
        mInsideLeftPaint.setColor(mInsideLeftDefaultBgColor);
        mOutSidePaint.setColor(mOutsideDefaultBgColor);
        mOutSideStrokePaint.setColor(mOutsideStrokeDefaultColor);
        mTextDefault = mText;
        getBitmap(mLockPicDefault);
    }
}