package com.example.backgammon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.backgammon.Border;
import com.example.backgammon.Triangle;
import com.example.backgammon.gameLogic.GameLogic;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Marija on 13.2.2018..
 */

public class ImageData {
    private int w, h;
    private float wunit, hunit;
    private Border leftBar, rightBar, topBar, bottomBar;
    private Blot blot;
    private Triangle[] board = new Triangle[24];
    private GameLogic controller;
    private ArrayList<Checker> highlightedCheckers;
    Paint triangleColor[];

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
        wunit = w/15f;
        hunit = h/16f;
       createShapes();
       Checker.setSize(hunit/1.6f);
       controller.sizeSet();
    }

    private void createShapes(){
        topBar = new Border(0, w, 0, hunit/2);
        bottomBar = new Border(0, w, h-hunit/2, h);
        blot = new Blot(7*wunit, 8*wunit, 0, h);
        leftBar = new Border(0, wunit, 0, h);
        rightBar = new Border(w - wunit, w, 0, h);

        Paint brightColor = createTrianglePaint(Color.parseColor("#E3854E"));
        Paint darkColor = createTrianglePaint(Color.parseColor("#9B4C1E"));
        triangleColor = new Paint[2];
        triangleColor[0] = brightColor;
        triangleColor[1] = darkColor;

        for(int i = 0; i < 6; i++)
            board[i] = new Triangle((13-i)*wunit, h-hunit/2, (14-i)*wunit, h-hunit/2, (13-i+0.5f)*wunit, h-7.5f*hunit, triangleColor[i%2], -1);

        for(int i = 6; i < 12; i++)
            board[i] = new Triangle((12-i)*wunit, h-hunit/2, (13-i)*wunit, h-hunit/2, (12-i+0.5f)*wunit, h-7.5f*hunit, triangleColor[i%2], -1);

        for(int i = 12; i < 18; i++)
            board[i] = new Triangle((i-11)*wunit, hunit/2, (i-10)*wunit, hunit/2, (i-11+0.5f)*wunit, 7.5f*hunit, triangleColor[i%2], 1);

        for(int i = 18; i < 24; i++)
            board[i] = new Triangle((i-10)*wunit, hunit/2, (i-9)*wunit, hunit/2, (i-10+0.5f)*wunit, 7.5f*hunit, triangleColor[i%2], 1);

    }

    private Paint createTrianglePaint(int color){
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

    public void draw(Canvas canvas){
        topBar.draw(canvas);
        bottomBar.draw(canvas);
        leftBar.draw(canvas);
        rightBar.draw(canvas);
        blot.draw(canvas);
        Paint lineColor = new Paint();
        lineColor.setStrokeWidth(3);
        canvas.drawLine(w/2, 0, w/2, h, lineColor);
        for(Triangle t: board)
            t.draw(canvas);
    }

    public void setCheckers(int row, int checkerNum, int color) {
        for(int i = 0; i < checkerNum; i++){
            board[row].addChecker(color);
        }
    }

    public void setController(GameLogic controller) {
        this.controller = controller;
    }

    public void highlightCheckers(Set<Integer> rows) {
        highlightedCheckers = new ArrayList<>();
        Paint newColor = new Paint();
        newColor.setColor(Color.GREEN);
        newColor.setStrokeWidth(4);
        for(Integer r: rows){
            highlightedCheckers.add(board[r].getTopChecker());
            highlightedCheckers.get(highlightedCheckers.size()-1).setBorderColor(newColor);
        }

    }


    public void resetColorChecker(){
        Paint newColor = new Paint();
        newColor.setColor(Color.BLACK);
        newColor.setStrokeWidth(2);
        for(Checker c: highlightedCheckers)
            c.setBorderColor(newColor);
        highlightedCheckers = null;

    }


    public void highlighTriangles(ArrayList<Integer> rows){
        Paint temp = createTrianglePaint(Color.GREEN);
        for(Integer i: rows)
            board[i].setColor(temp);
    }

    public void resetColorTriangles(ArrayList<Integer> rows){
        for(Integer i: rows)
            board[i].setColor(triangleColor[i%2]);
    }
}
