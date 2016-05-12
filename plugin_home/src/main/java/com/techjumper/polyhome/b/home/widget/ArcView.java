package com.techjumper.polyhome.b.home.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.techjumper.polyhome.b.home.R;


/**
 * Created by kevin on 16/4/20.
 */
public class ArcView extends View {

    private int mRadius;
    private int mWidth;
    private int mHeight;
    private int mBgColor;
    private float mArcAngle;
    private double mLeftDistance; //弧形所在的圆向左平移的距离
    private double mTotalWidth; //整个view的宽度
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private RectF mRectF = new RectF();
    private int alpha;

    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidate();
    }


    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mRadius = (int) typedArray.getDimension(R.styleable.ArcView_arc_radius, 0f);
        mWidth = (int) typedArray.getDimension(R.styleable.ArcView_rect_width, 0f);
        mHeight = (int) typedArray.getDimension(R.styleable.ArcView_rect_height, 0f);
        mBgColor = typedArray.getColor(R.styleable.ArcView_av_bg_color, getResources().getColor(R.color.color_20c3f3));
        alpha = typedArray.getInt(R.styleable.ArcView_alpha, 255);

        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            //+1 是误差值
            width = getPaddingLeft() + Integer.parseInt(String.valueOf(mTotalWidth).substring(0, String.valueOf(mTotalWidth).indexOf("."))) + 1 + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getPaddingTop() + mHeight + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //// TODO: 16/4/21  记录一下
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mArcAngle = getArcAngle(mRadius);

        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);
        mPaint.setStrokeWidth((float) 3.0);
        mPaint.setAlpha(alpha);

        mPath.addRect(0, 0, mWidth, mHeight, Path.Direction.CW);

        float left = Float.parseFloat(String.valueOf(mLeftDistance));

        mRectF.left = -left;
        mRectF.top = mHeight / 2 - mRadius;
        mRectF.right = mRadius * 2 - left;
        mRectF.bottom = mRadius * 2 + mHeight / 2 - mRadius;

        mPath.addArc(mRectF, 360 - mArcAngle / 2, mArcAngle);
        canvas.drawPath(mPath, mPaint);

        mPath.close();
    }

    /**
     * 拿到圆弧的度
     *
     * @param radius
     * @return
     */
    private float getArcAngle(int radius) {
        double arcAngle = Math.acos((2 * Math.pow(radius, 2) - Math.pow(mHeight, 2)) / (2 * radius * radius)) * (180 / Math.PI);
        return Float.parseFloat(String.valueOf(arcAngle));
    }

    /**
     * 设置参数
     *
     * @param radius  半径
     * @param width   宽度
     * @param height  长度
     * @param bgColor 背景颜色
     */
    public void setParameter(int radius, int width, int height, int bgColor) {
        this.mRadius = radius;
        this.mWidth = width;
        this.mHeight = height;
        this.mBgColor = bgColor;

        initData();
    }

    private void initData() {
        if (mRadius == 0 || mWidth == 0 || mHeight == 0)
            throw new StringIndexOutOfBoundsException("parameter is not 0");

        mArcAngle = getArcAngle(mRadius);
        mLeftDistance = mRadius - (mWidth - Math.cos((mArcAngle / 2) * (Math.PI / 180)) * mRadius);
        mTotalWidth = mRadius - Math.cos((mArcAngle / 2) * (Math.PI / 180)) * mRadius + mWidth;
    }
}
