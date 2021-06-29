package com.example.pong;

import android.graphics.Color;
import android.graphics.Paint;

public class Racket {
    private float x;
    private float y;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private final Paint paint;

    public Racket(int color, int width) {
        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setStrokeWidth(width);
    }

//    public Racket(float startX, float stopX, float startY, float stopY, float playerNo){
//        this.startX = startX;
//        this.stopX = stopX;
//
//        this.startY = startY;
//        this.stopY = stopY;
//
//        if
//    }

    public Paint getPaint() {
        return paint;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getStopX() {
        return stopX;
    }

    public void setStopX(float stopX) {
        this.stopX = stopX;
    }

    public float getStopY() {
        return stopY;
    }

    public void setStopY(float stopY) {
        this.stopY = stopY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
