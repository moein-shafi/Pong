package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int ACCELERATION_SCALE = 100;
    public static final int V_MINIMUM_THRESHOLD = 200;
    public static final int WALL_MINIMUM_THRESHOLD = 10;
    Ball ball;
    Board board;
    int ballRadius = 40;
    float deltaT = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ballImage = findViewById(R.id.ball);
        HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);


        /// TODO: set all buttons (except start_game button) INVISIBLE.
        /// TODO: set vx and vy by random numbers (+ random direction).
        /// TODO: check 'mass'.
        this.ball = new Ball(
                hexaView.getCenterX(),    // not correct at this point.
                hexaView.getCenterY(),    // not correct at this point.
                +50,
                    -50,
//                -(float) (50 * Math.sqrt(3) / 3),
                0.01f,
                this.ballRadius,
                ballImage
        );
    }

    public void startGame(View view) {
        HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);
        this.board = new Board(hexaView.getCoordinations());
        this.ball.setBoardLines(this.board.getLines());
        this.ball.setBoardCoordinations(this.board.getCoordinations());

        findViewById(R.id.start_button).setVisibility(View.INVISIBLE);
        this.ball.setX0(hexaView.getCenterX());
        this.ball.setY0(hexaView.getCenterY() - 150);


        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                ball.move(deltaT);
            }
        }, 0, 20);
    }


    public void racketButton1Left(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket1Left();
    }

    public void racketButton1Right(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket1Right();
    }

    public void racketButton2Left(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket2Left();
    }

    public void racketButton2Right(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket2Right();
    }

    public void racketButton3Left(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket3Left();
    }

    public void racketButton3Right(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket3Right();
    }



}