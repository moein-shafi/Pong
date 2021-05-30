package com.example.pong;

import android.content.res.Resources;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    public static final float KINETIC_FRICTION = 0.07f;
    public static final float STATIC_FRICTION = 0.15f;
    public static final int G = 10;
    private ArrayList<Pair<Float, Float>> coordinations;
    private ArrayList<Pair<Float, Float>> lines = new ArrayList<>();


    public static int width;
    public static int height;

    public Board(ArrayList<Pair<Float, Float>> coordinations) {
        this.coordinations = coordinations;
        this.calculateLines();
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void calculateLines() {
        float m0 = ((float)this.coordinations.get(1).second - this.coordinations.get(0).second) /
                ((float)this.coordinations.get(1).first - this.coordinations.get(0).first);
        float b0 = (float)this.coordinations.get(0).second - m0 * (float)this.coordinations.get(0).first;

        float m1 = ((float)this.coordinations.get(2).second - this.coordinations.get(1).second) /
                ((float)this.coordinations.get(2).first - this.coordinations.get(1).first);
        float b1 = (float)this.coordinations.get(1).second - m1 * (float)this.coordinations.get(1).first;

        float m2 = ((float)this.coordinations.get(3).second - this.coordinations.get(2).second) /
                ((float)this.coordinations.get(3).first - this.coordinations.get(2).first);
        float b2 = (float)this.coordinations.get(2).second - m2 * (float)this.coordinations.get(2).first;

        float m3 = ((float)this.coordinations.get(4).second - this.coordinations.get(3).second) /
                ((float)this.coordinations.get(4).first - this.coordinations.get(3).first);
        float b3 = (float)this.coordinations.get(3).second - m3 * (float)this.coordinations.get(3).first;

        float m4 = ((float)this.coordinations.get(5).second - this.coordinations.get(4).second) /
                ((float)this.coordinations.get(5).first - this.coordinations.get(4).first);
        float b4 = (float)this.coordinations.get(4).second - m4 * (float)this.coordinations.get(4).first;

        float m5 = ((float)this.coordinations.get(0).second - this.coordinations.get(5).second) /
                ((float)this.coordinations.get(0).first - this.coordinations.get(5).first);
        float b5 = (float)this.coordinations.get(5).second - m5 * (float)this.coordinations.get(5).first;

        this.lines.add(new Pair(m0, b0));
        this.lines.add(new Pair(m1, b1));
        this.lines.add(new Pair(m2, b2));
        this.lines.add(new Pair(m3, b3));
        this.lines.add(new Pair(m4, b4));
        this.lines.add(new Pair(m5, b5));
    }

    public Pair<Integer, Integer> getRandomPoint() {
        return new Pair<>(randInt(width, 0), randInt(height, 0));
    }

    public int randInt(int max, int min) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public ArrayList<Pair<Float, Float>> getLines() {
        return lines;
    }

    public ArrayList<Pair<Float, Float>> getCoordinations() {
        return coordinations;
    }

}
