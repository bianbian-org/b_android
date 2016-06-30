package com.techjumper.lightwidget.jumper_circle.interfaces;

import android.graphics.Canvas;
import android.support.annotation.ColorInt;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public interface IPainter {

    IPainter setStartDegree(int degree);

    IPainter setEndDegree(int degree);

    IPainter setDirection(Direction direction);

    Direction getDirection();

    IPainter setColor(@ColorInt int color);

    IPainter setLineWidth(int width);

    IPainter setPerDegree(int degree);

    IPainter setLineLength(int length);

    void draw(Canvas canvas, int endDegree);

    void recycle();

    enum Direction {
        CW,
        CCW
    }
}
