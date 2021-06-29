package com.example.pong;

import android.widget.TextView;

public class Player {


    private int score = 0;
    private int id;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player(int id) {
        this.id = id;
    }

    public void increaseScore() {
        this.score += 1;
    }

    public void decreaseScore() {
        this.score -= 1;
    }

    public int getId() {
        return id;
    }

}
