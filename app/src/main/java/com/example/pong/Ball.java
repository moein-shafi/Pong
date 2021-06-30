package com.example.pong;

import android.widget.ImageView;
import android.util.Pair;

import java.util.ArrayList;

public class Ball {
    float x0, y0;
    float vx0, vy0;

    int radius;
    ImageView imageView;
    
    ArrayList<Pair<Float, Float>> boardLines = new ArrayList<>();
    ArrayList<Pair<Float, Float>> coordinations = new ArrayList<>();
    private Racket racket1;

    public Racket getRacket1() {
        return racket1;
    }

    public Racket getRacket2() {
        return racket2;
    }

    public Racket getRacket3() {
        return racket3;
    }

    public ArrayList<Pair<Float, Float>> getBoardLines() {
        return boardLines;
    }

    private Racket racket2;
    private Racket racket3;
    private int lastHit = 1;
    private Player player1;
    private Player player2;
    private Player player3;
    private float defaultX0;
    private float defaultY0;
    private final float defaultVx0;
    private final float defaultVy0;

    public Ball(float x0, float y0, float vx0, float vy0, int radius, ImageView imageView) {
        this.x0 = x0;
        this.y0 = y0;
        this.vx0 = vx0;
        this.defaultVx0 = vx0;
        this.vy0 = vy0;
        this.defaultVy0 = vy0;
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
    }

    public void showBall(){
        this.updateImageLocation();
    }

    public void setBoardLines(ArrayList<Pair<Float, Float>> boardLines) {
        this.boardLines = boardLines;
    }

    public void setBoardCoordinations(ArrayList<Pair<Float, Float>> coordinations) {
        this.coordinations = coordinations;
    }

    public void setRacket1(Racket racket1) {
        this.racket1 = racket1;
    }

    public void setRacket2(Racket racket2) {
        this.racket2 = racket2;
    }

    public void setRacket3(Racket racket3) {
        this.racket3 = racket3;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setPlayer3(Player player3) {
        this.player3 = player3;
    }


    private void increasePlayerScore() {
        switch (lastHit) {
            case 1:
                this.player1.increaseScore();
                break;
            case 2:
                this.player2.increaseScore();
                break;
            case 3:
                this.player3.increaseScore();
                break;
        }
        this.vx0 = 0;
        this.vy0 = 0;
    }

    private void decreasePlayerScore() {
        switch (lastHit) {
            case 1:
                this.player1.decreaseScore();
                break;
            case 2:
                this.player2.decreaseScore();
                break;
            case 3:
                this.player3.decreaseScore();
                break;
        }
        this.vx0 = 0;
        this.vy0 = 0;
    }

    private void increaseVelocity() {
        this.vx0 *= SingleDeviceActivity.BALL_INCREASE_V_RATIO;
        this.vy0 *= SingleDeviceActivity.BALL_INCREASE_V_RATIO;
    }

    private void collideBallFromWall(int WallId) {
        float tangential = (float) Math.atan(this.boardLines.get(WallId).first);
        float tangentialX = (float) (1 * Math.cos(tangential));
        float tangentialY = (float) (1 * Math.sin(tangential));

        float normX = -tangentialY;
        float normY = tangentialX;
        float vNorm = vx0 * normX + vy0 * normY;
        float vTangential = vx0 * tangentialX + vy0 * tangentialY;
        vx0 = -vNorm * normX + vTangential * tangentialX;
        vy0 = -vNorm * normY + vTangential * tangentialY;
    }

    private void checkCollision() {
        if (this.y0 + 2 * this.radius > (this.x0 * (float)this.boardLines.get(0).first + (float)this.boardLines.get(0).second)) {
            collideBallFromWall(0);
            this.y0 -= SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;
        }
        else if (this.x0 <= (float)this.coordinations.get(1).first) {
            if (this.y0 > racket1.getStartY() && this.y0 < racket1.getStopY() ||
                    this.y0 + 2 * radius > racket1.getStartY() && this.y0  + 2 * radius < racket1.getStopY()) {
                this.vx0 *= -1;
                lastHit = 1;
                this.increaseVelocity();
            }

            else {
                if (lastHit == 1)
                    this.decreasePlayerScore();
                else
                    this.increasePlayerScore();
            }
            this.x0 += SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;
        }
        else if (this.y0 < (this.x0 * (float)this.boardLines.get(2).first + (float)this.boardLines.get(2).second)) {
            collideBallFromWall(2);
            this.y0 += SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;

        }
        else if (this.y0 < (this.x0 + 2 * radius) * (float)this.boardLines.get(3).first + (float)this.boardLines.get(3).second) {
            if (this.x0 > racket2.getStartX() && this.x0 < racket2.getStopX() ||
                    this.x0 + 2 * radius > racket2.getStartX() && this.x0 + 2 * radius < racket2.getStopX()) {
                lastHit = 2;
                collideBallFromWall(3);
                this.y0 += SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;
                this.increaseVelocity();
            }
            else {
                if (lastHit == 2)
                    this.decreasePlayerScore();
                else
                    this.increasePlayerScore();
            }
        }
        else if (this.x0 + 2 * this.radius >= (float)this.coordinations.get(4).first) {
            this.vx0 *= -1;
            this.x0 -= SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;
        }
        else if (this.y0 + 2 * this.radius > (this.x0 + 2 * radius) * (float)this.boardLines.get(5).first + (float)this.boardLines.get(5).second) {
            if (this.x0 > racket3.getStopX() && this.x0 < racket3.getStartX() ||
                    this.x0 + 2 * radius > racket3.getStopX() && this.x0 + 2 * radius < racket3.getStartX()) {
                lastHit = 3;
                collideBallFromWall(5);
                this.y0 -= SingleDeviceActivity.WALL_MINIMUM_THRESHOLD;
                this.increaseVelocity();
            }
            else {
                if (lastHit == 3)
                    this.decreasePlayerScore();
                else
                    this.increasePlayerScore();
            }
        }
    }

    public void updateImageLocation() {
        imageView.setX(this.x0);
        imageView.setY(this.y0);
    }

    public void setX0(float x0) {
        this.defaultX0 = x0;
        this.x0 = x0;
    }

    public void setY0(float y0) {
        this.y0 = y0;
        this.defaultY0 = y0;
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

    public float getX0() {
        return x0;
    }

    public float getY0() {
        return y0;
    }

    public float getXPercentage() {
        if (coordinations.size() < 6) {
            return 0.5f;
        }
        return pixelToPercentageX(x0);

    }

    public float getYPercentage() {
        if (coordinations.size() < 6) {
            return 0.5f;
        }
        return pixelToPercentageY(y0);
    }

    public float pixelToPercentageX(float pixelInput) {
        return pixelInput / (coordinations.get(4).first - coordinations.get(2).first);
    }


    public float pixelToPercentageY(float pixelInput) {
        return pixelInput / (coordinations.get(0).second - coordinations.get(3).second);
    }


    public void setX0ByPercentage(float xPercentage) {
        x0 = xPercentage * (coordinations.get(4).first - coordinations.get(2).first);
    }

    public void setY0ByPercentage(float yPercentage) {
        y0 = yPercentage * (coordinations.get(0).second - coordinations.get(3).second);
    }
}
