package com.example.pong;

import android.widget.TextView;

public class Player {
    private int score = 0;
    private int id;

    public int getScore() {
        return score;
    }

    public Player(int id) {
        this.id = id;
    }

    public void increaseScore() {
        this.score += 1;
//        MainActivity.setText(id, score);
    }

    public int getId() {
        return id;
    }

}
