package com.example.pong;

import android.widget.ImageView;
import android.util.Pair;

import java.util.ArrayList;

public class Ball {
    float x0, y0;
    float vx0, vy0;
    float mass;

    int radius;
    ImageView imageView;
    ArrayList<Pair<Float, Float>> boardLines = new ArrayList<>();
    ArrayList<Pair<Float, Float>> coordinations = new ArrayList<>();

    public Ball(float x0, float y0, float vx0, float vy0, float mass, int radius, ImageView imageView) {
        this.x0 = x0;
        this.y0 = y0;
        this.vx0 = vx0;
        this.vy0 = vy0;
        this.mass = mass;
        this.imageView = imageView;
        this.radius = radius;
        this.imageView.getLayoutParams().width = 2 * this.radius;
        this.imageView.getLayoutParams().height = 2 * this.radius;
        this.updateImageLocation();
    }

    public void move(float deltaT) {
        this.checkCollision();
        this.x0 += this.vx0 * deltaT;
        this.y0 += this.vy0 * deltaT;
        this.updateImageLocation();
    }

    public void setBoardLines(ArrayList<Pair<Float, Float>> boardLines) {
        this.boardLines = boardLines;
    }

    public void setBoardCoordinations(ArrayList<Pair<Float, Float>> coordinations) {
        this.coordinations = coordinations;
    }

    private void checkCollision() {
        if (this.y0 + 2 * this.radius > this.x0 * (float)this.boardLines.get(0).first + (float)this.boardLines.get(0).second) {

            float Tan = (float) Math.atan(this.vx0 / this.vy0);
            float v = (float) Math.sqrt(Math.pow(this.vx0, 2) + Math.pow(this.vy0, 2));

            float degree = (float)(-Math.toDegrees(Tan) - 60);
            if (Math.abs(degree) < 0.01)
                degree = 0;
            this.vx0 = (float) (v * Math.sin(degree));
            this.vy0 = -(float) (v * Math.cos(degree));

            /// TODO: check this magic number.
            this.y0 -= MainActivity.WALL_MINIMUM_THRESHOLD;
        }

        if (this.x0 <= (float)this.coordinations.get(1).first) {
            this.vx0 *= -1;
        }

        if (this.y0 < this.x0 * (float)this.boardLines.get(2).first + (float)this.boardLines.get(2).second) {
            float Tan = (float) Math.atan(this.vx0 / this.vy0);
            float v = (float) Math.sqrt(Math.pow(this.vx0, 2) + Math.pow(this.vy0, 2));

            float degree = (float)(Math.toDegrees(Tan) - 60);
            if (Math.abs(degree) < 0.01)
                degree = 0;
            this.vx0 = (float) (v * Math.sin(degree));
            this.vy0 = (float) (v * Math.cos(degree));

            /// TODO: check this magic number.
            this.y0 += MainActivity.WALL_MINIMUM_THRESHOLD;
        }

        if (this.y0 < (this.x0 + 2 * radius) * (float)this.boardLines.get(3).first + (float)this.boardLines.get(3).second) {
            float Tan = (float) Math.atan(this.vx0 / this.vy0);
            float v = (float) Math.sqrt(Math.pow(this.vx0, 2) + Math.pow(this.vy0, 2));

            float degree = (float)(-Math.toDegrees(Tan) - 60);
            if (Math.abs(degree) < 0.01)
                degree = 0;
            this.vx0 = (float) (v * Math.sin(degree));
            this.vy0 = (float) (v * Math.cos(degree));

            /// TODO: check this magic number.
            this.y0 += MainActivity.WALL_MINIMUM_THRESHOLD;
        }


        if (this.x0 + 2 * this.radius >= (float)this.coordinations.get(4).first) {
            this.vx0 *= -1;
        }

        if (this.y0 + 2 * this.radius > (this.x0 + 2 * radius) * (float)this.boardLines.get(5).first + (float)this.boardLines.get(5).second) {

            float Tan = (float) Math.atan(this.vx0 / this.vy0);
            float v = (float) Math.sqrt(Math.pow(this.vx0, 2) + Math.pow(this.vy0, 2));
            System.out.println("b  vy " + vx0);
            System.out.println("b vx " + vy0);

            float degree = (float)(Math.toDegrees(Tan) - 60);
            if (Math.abs(degree) < 0.01)
                degree = 0;
            this.vx0 = (float) (v * Math.sin(degree));
            this.vy0 = -(float) (v * Math.cos(degree));
            System.out.println("degree " + degree);
            System.out.println("vy " + vx0);
            System.out.println("vx " + vy0);

            /// TODO: check this magic number.
            this.y0 -= MainActivity.WALL_MINIMUM_THRESHOLD;
        }


}

    public void updateImageLocation() {
        imageView.setX(this.x0);
        imageView.setY(this.y0);
    }

    public void setX0(float x0) {
        this.x0 = x0;
    }

    public void setY0(float y0) {
        this.y0 = y0;
    }

    public void resetV() {
        this.vy0 = 0;
        this.vx0 = 0;
    }

    public void setVx0(float vx0) {
        this.vx0 = vx0;
    }

    public void setVy0(float vy0) {
        this.vy0 = vy0;
    }
}
