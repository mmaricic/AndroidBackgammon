package com.example.backgammon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Marija on 14.2.2018..
 */

public class Blot extends Border {
    ArrayList<Checker> white = new ArrayList<>();
    ArrayList<Checker> black = new ArrayList<>();
    float space;

    public Blot(float l, float r, float t, float b) {
        super(l, r, t, b);
        space = (position.top - position.bottom) / 20;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (Checker c : white)
            c.draw(canvas);

        for (Checker c : black)
            c.draw(canvas);
    }

    public void addChecker(int color) {
        if (color == Color.WHITE) {
            if (white.isEmpty())
                white.add(new Checker(color, new PointF((position.left + position.right) / 2, position.top - Checker.getSize())));
            else {
                PointF newPos = new PointF((position.left + position.right) / 2, white.get(white.size() - 1).getPosition().y - space);
                white.add(new Checker(color, newPos));
            }
        } else if (black.isEmpty())
            black.add(new Checker(color, new PointF((position.left + position.right) / 2, position.bottom + Checker.getSize())));
        else {
            PointF newPos = new PointF((position.left + position.right) / 2, black.get(black.size() - 1).getPosition().y + space);
            black.add(new Checker(color, newPos));
        }
    }


    public void removeChecker(int color) {
        if (color == Color.WHITE)
            white.remove(white.size() - 1);
        else
            black.remove(black.size() - 1);
    }
}
