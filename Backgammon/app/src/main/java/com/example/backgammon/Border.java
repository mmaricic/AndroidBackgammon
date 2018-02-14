package com.example.backgammon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Marija on 14.2.2018..
 */

public class Border {
    protected static Paint color;
    protected RectF position;

    static {

        color = new Paint();
        color.setColor(Color.parseColor("#602D10"));
    }

    public Border(float l, float r, float t, float b) {
        position = new RectF(l, t, r, b);
    }


    public void draw(Canvas canvas) {
        canvas.drawRect(position, color);
    }
}
