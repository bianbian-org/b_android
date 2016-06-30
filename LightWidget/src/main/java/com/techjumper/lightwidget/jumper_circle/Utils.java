package com.techjumper.lightwidget.jumper_circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class Utils {
//
//    public static int evaluateGradeColor(float fraction, Object startValue, Object endValue) {
//        int startInt = (Integer) startValue;
//        int startA = (startInt >> 24) & 0xff;
//        int startR = (startInt >> 16) & 0xff;
//        int startG = (startInt >> 8) & 0xff;
//        int startB = startInt & 0xff;
//
//        int endInt = (Integer) endValue;
//        int endA = (endInt >> 24) & 0xff;
//        int endR = (endInt >> 16) & 0xff;
//        int endG = (endInt >> 8) & 0xff;
//        int endB = endInt & 0xff;
//
//        return (startA + (int) (fraction * (endA  startA))) << 24 |
//                (startR + (int) (fraction * (endR  startR))) << 16 |
//                (startG + (int) (fraction * (endG  startG))) << 8 |
//                (startB + (int) (fraction * (endB  startB)));
//    }

    public static int dp2Px(Context ctx, float dp) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static boolean isInRegion(Path path, MotionEvent event) {
        return isInRegion(path, (int) event.getX(), (int) event.getY());
    }

    public static boolean isInRegion(Path path, int x, int y) {
        Region region = new Region();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return region.contains(x, y);
    }

    public static Quadrant quadrant(Rect rect, Point point) {
        Path path = new Path();
        path.addRect(rect.left, rect.top, rect.centerX(), rect.centerY(), Path.Direction.CW);
        if (isInRegion(path, point.x, point.y)) return Quadrant.SECOND;
        path.reset();
        path.addRect(rect.centerX(), rect.top, rect.right, rect.centerY(), Path.Direction.CW);
        if (isInRegion(path, point.x, point.y)) return Quadrant.FIRST;
        path.reset();
        path.addRect(rect.centerX(), rect.centerY(), rect.right, rect.bottom, Path.Direction.CW);
        if (isInRegion(path, point.x, point.y)) return Quadrant.FOURTH;
        return Quadrant.THIRD;
    }

    public static float calculateAngle(Point center, Point first, Point second) {
        float dx1, dx2, dy1, dy2;
        double angle;
        dx1 = first.x - center.x;
        dy1 = first.y - center.y;
        dx2 = second.x - center.x;
        dy2 = second.y - center.y;
        float c = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);
        if (c == 0) return 1;
        angle = Math.acos((dx1 * dx2 + dy1 * dy2) / c);
        return (float) (angle / Math.PI * 180);
    }

    public static float calculateSwipeAngle(Rect rect, Point point) {
        switch (quadrant(rect, point)) {
            case FIRST:
                return calculateAngle(new Point(rect.centerX(), rect.centerY())
                        , point, new Point(rect.centerX(), 0));
            case SECOND:
                return 360 - calculateAngle(new Point(rect.centerX(), rect.centerY())
                        , point, new Point(rect.centerX(), 0));
            case THIRD:
                return 180 + calculateAngle(new Point(rect.centerX(), rect.centerY())
                        , point, new Point(rect.centerX(), rect.bottom));
            default:
                return 90 + calculateAngle(new Point(rect.centerX(), rect.centerY())
                        , point, new Point(rect.right, rect.centerY()));
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        bitmap.recycle();
        return newBitmap;
    }

}
