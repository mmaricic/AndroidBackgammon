package com.example.backgammon.GUIComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Marija on 14.2.2018..
 */

public class Border {
    protected  Paint color;
    protected RectF position;


    public Border(float l, float r, float t, float b) {
        position = new RectF(l, t, r, b);

       setDefaultColor();
    }

    public  Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public void setDefaultColor(){
        color = new Paint();
        color.setColor(Color.parseColor("#602D10"));
    }
    public RectF getPosition() {
        return position;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(position, color);
    }
}
