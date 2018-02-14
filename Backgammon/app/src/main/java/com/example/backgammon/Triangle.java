package com.example.backgammon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Marija on 14.2.2018..
 */

public class Triangle {
    Path path;
    Paint paint;
    PointF centralPoint;
    ArrayList<Checker> checkers = new ArrayList<>();
    float height;
    int orientation;

    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3, Paint paint, int o) {
        orientation = o;
        this.paint = paint;
        centralPoint = new PointF(x3, y1);
        height = y3;
        path = new Path();
       // path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x1,y1);
        path.lineTo(x2,y2);
        path.lineTo(x3,y3);
        path.lineTo(x1,y1);
        path.close();
    }

    public void draw(Canvas canvas){

        canvas.drawPath(path, paint);
        for(Checker c: checkers)
            c.draw(canvas);
    }

    public void addChecker(int color) {
        Checker newCheker;
        PointF position;
        if(checkers.isEmpty())
            position = new PointF(centralPoint.x, centralPoint.y+orientation*Checker.getSize());
        else {
            PointF lastChecker = checkers.get(checkers.size() - 1).getPosition();
            position = new PointF(lastChecker.x,  lastChecker.y+ orientation * Checker.getSize()*2);
        }
        newCheker = new Checker(color, position);
        checkers.add(newCheker);
        if(checkers.size() > 5){
            moveCheckers();
        }
    }

    private void moveCheckers() {
        float space = (height - centralPoint.y)/(checkers.size());
        for(int i = 1; i < checkers.size(); i++){
            PointF newPos = new PointF(centralPoint.x, checkers.get(i-1).getPosition().y + space);
            checkers.get(i).setPosition(newPos);
        }
    }

    public Checker getTopChecker() {
        return checkers.get(checkers.size()-1);
    }

    public void setColor(Paint color) {
        this.paint = color;
    }
}
