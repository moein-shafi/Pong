package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SingleDeviceActivity extends AppCompatActivity {
    public static final int ACCELERATION_SCALE = 100;
    public static final int V_MINIMUM_THRESHOLD = 200;
    public static final int WALL_MINIMUM_THRESHOLD = 10;
    public static final int Rocket_Movement = 30;
    public static final int MIN_BALL_V = 40;
    public static final int MAX_BALL_V = 60;
    public static final float BALL_INCREASE_V_RATIO = 1.05f;
    public static final int RACKET_SIZE = 8;

    Ball ball;
    Board board;
    int ballRadius = 30;
    float deltaT = 0.1f;
    Player player1;
    Player player2;
    Player player3;
    static TextView player1Score;
    static TextView player2Score;
    static TextView player3Score;
    Handler mHandler;
    int lastScore1 = 0, lastScore2 = 0, lastScore3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_device);


        ImageView ballImage = findViewById(R.id.ball);

        HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);
        player1Score = (TextView)findViewById(R.id.score_ply1);
        player2Score = (TextView)findViewById(R.id.score_ply2);
        player3Score = (TextView)findViewById(R.id.score_ply3);

        player1 = new Player(1);
        player2 = new Player(2);
        player3 = new Player(3);
        mHandler = new Handler();

        this.ball = new Ball(
                hexaView.getCenterX(),    // not correct at this point.
                hexaView.getCenterY(),    // not correct at this point.
                getRandomV(),
                getRandomV(),
                this.ballRadius,
                ballImage
        );
    }

    private int getRandomV() {
        Random randomDirection = new Random();
        Random randomValue = new Random();
        int direction = 1;

        int rand_int1 = randomDirection.nextInt(10);
        if (rand_int1 < 5)
            direction = -1;
        return (int)(((Math.random() * ((MAX_BALL_V - MIN_BALL_V) + 1)) + MIN_BALL_V) * direction);
    }

    public void startGame(View view) {
        HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);
        this.board = new Board(hexaView.getCoordinations());
        this.ball.setBoardLines(this.board.getLines());
        this.ball.setBoardCoordinations(this.board.getCoordinations());
        this.ball.setRacket1(hexaView.getRacket1());
        this.ball.setRacket2(hexaView.getRacket2());
        this.ball.setRacket3(hexaView.getRacket3());
        this.ball.setPlayer1(this.player1);
        this.ball.setPlayer2(this.player2);
        this.ball.setPlayer3(this.player3);

        findViewById(R.id.start_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.right_btn_ply1).setVisibility(View.VISIBLE);
        findViewById(R.id.right_btn_ply2).setVisibility(View.VISIBLE);
        findViewById(R.id.right_btn_ply3).setVisibility(View.VISIBLE);
        findViewById(R.id.left_btn_ply1).setVisibility(View.VISIBLE);
        findViewById(R.id.left_btn_ply2).setVisibility(View.VISIBLE);
        findViewById(R.id.left_btn_ply3).setVisibility(View.VISIBLE);
        findViewById(R.id.score_ply1).setVisibility(View.VISIBLE);
        findViewById(R.id.score_ply2).setVisibility(View.VISIBLE);
        findViewById(R.id.score_ply3).setVisibility(View.VISIBLE);

        this.ball.setX0(hexaView.getCenterX());
        this.ball.setY0(hexaView.getCenterY());


        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                ball.move(deltaT);
                show();
            }
        }, 0, 20);
    }

    public void stopGoal() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.goal).setVisibility(View.INVISIBLE);
                ball.setVx0(getRandomV());
                ball.setVy0(getRandomV());
            }
        },3000);
    }

    public void show() {
        mHandler.post(new Runnable() {
            public void run() {
                if (lastScore1 != player1.getScore() || lastScore2 != player2.getScore() ||
                        lastScore3 != player3.getScore()) {

                    TextView score1View = findViewById(R.id.score_ply1);
                    score1View.setText(String.format("score: %d", player1.getScore()));

                    TextView score2View = findViewById(R.id.score_ply2);
                    score2View.setText(String.format("score: %d", player2.getScore()));

                    TextView score3View = findViewById(R.id.score_ply3);
                    score3View.setText(String.format("score: %d", player3.getScore()));


                    lastScore1 = player1.getScore();
                    lastScore2 = player2.getScore();
                    lastScore3 = player3.getScore();

                    HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);
                    ball.setX0(hexaView.getCenterX());
                    ball.setY0(hexaView.getCenterY());

                    findViewById(R.id.goal).setVisibility(View.VISIBLE);
                    stopGoal();
                }
            }
        });
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

    public void showScores(View view) {
        TextView score1 = findViewById(R.id.score_ply1);
        score1.setText(String.format("score: %d", player1.getScore()));

        TextView score2 = findViewById(R.id.score_ply2);
        score2.setText(String.format("score: %d", player2.getScore()));

        TextView score3 = findViewById(R.id.score_ply3);
        score3.setText(String.format("score: %d", player3.getScore()));
    }
}
