package com.techjumper.lightwidget.jumper_circle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;

import com.techjumper.lightwidget.jumper_circle.interfaces.IPainter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class SelectLinePainter implements IPainter {

    private int mStartDegree;
    private int mEndDegree;
    private int mPerDegree;
    private int mLineLength;
    private Direction mDirection = Direction.CW;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private SelectLinePainter() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public static SelectLinePainter getPainter() {
        return new SelectLinePainter();
    }

    @Override
    public IPainter setStartDegree(int degree) {
        mStartDegree = degree;
        return this;
    }

    @Override
    public IPainter setEndDegree(int degree) {
        mEndDegree = degree;
        return this;
    }

    @Override
    public IPainter setDirection(Direction direction) {
        mDirection = direction;
        return this;
    }

    @Override
    public Direction getDirection() {
        return mDirection;
    }

    @Override
    public IPainter setPerDegree(int degree) {
        mPerDegree = degree;
        return this;
    }

    @Override
    public IPainter setLineLength(int length) {
        mLineLength = length;
        return this;
    }

    @Override
    public IPainter setColor(@ColorInt int color) {
        mPaint.setColor(color);
        return this;
    }

    @Override
    public IPainter setLineWidth(int width) {
        mPaint.setStrokeWidth(width);
        return this;
    }

    @Override
    public void draw(Canvas canvas, int endDegree) {
        if (mBitmap != null && mBitmap.isRecycled()) return;
        if (mCanvas == null || mBitmap == null) {
            mBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        int startDegree = mDirection == Direction.CW ? mStartDegree : mEndDegree;

        if (startDegree > endDegree) {
            startDegree = startDegree ^ endDegree;
            endDegree = startDegree ^ endDegree;
            startDegree = startDegree ^ endDegree;
        }
        endDegree = clamp(endDegree, mStartDegree, mEndDegree);
        mCanvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
        mCanvas.save();
        int count = 1;
        rotateCanvasWithCenter(startDegree);
        while (count * mPerDegree + startDegree < endDegree) {
            drawLine();
            rotateCanvasWithCenter(mPerDegree);
            count++;
        }
        mCanvas.restore();

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private int clamp(int value, int min, int max) {
        return Math.min(max, Math.max(value, min));
    }

    @Override
    public void recycle() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    private void drawLine() {
        if (mCanvas == null || mBitmap == null) return;
        mCanvas.drawLine(mBitmap.getWidth() / 2, 0, mBitmap.getWidth() / 2, mLineLength, mPaint);
    }

    private void rotateCanvasWithCenter(int degree) {
        if (mCanvas == null) return;
        mCanvas.rotate(degree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
    }
}
