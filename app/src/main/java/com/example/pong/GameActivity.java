package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    public static final int ACCELERATION_SCALE = 100;
    public static final int V_MINIMUM_THRESHOLD = 200;
    public static final int WALL_MINIMUM_THRESHOLD = 10;
    public static final int Rocket_Movement = 30;
    public static final int MIN_BALL_V = 40;
    public static final int MAX_BALL_V = 60;
    public static final float BALL_INCREASE_V_RATIO = 1.05f;
    public static final int RACKET_SIZE = 8;
    private static boolean isServer;
    private HexagonMaskView hexaView;
    public static void setIsServer(boolean isServer) {
        GameActivity.isServer = isServer;
    }

    private static BluetoothService bluetoothService;
    private static final GameActivity gameActivity = new GameActivity();

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    public static void setBluetoothService(BluetoothService bluetoothService) {
        GameActivity.bluetoothService = bluetoothService;
    }

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
                hexaView.getCenterX(),
                hexaView.getCenterY(),
                getRandomV(),
                getRandomV(),
                this.ballRadius,
                ballImage
        );

        bluetoothService.getChannel().setOnMessageReceivedListener(new BluetoothService.OnMessageReceivedListener() {
            @Override
            public void process(byte[] buffer) {
                if (buffer != null && !isServer){
                    String data = new String(buffer, StandardCharsets.UTF_8);
//                    Log.d("bluetooth-debug-data", data);
                    List <String> parsedData = parseData(data);
                    if (parsedData.size() == 9) {

                        ball.setX0ByPercentage(Float.parseFloat(parsedData.get(0)));
                        ball.setY0ByPercentage(Float.parseFloat(parsedData.get(1)));

                        hexaView.setRacketCoordsByPercentage(1,Float.parseFloat(parsedData.get(2)));
                        hexaView.setRacketCoordsByPercentage(2,Float.parseFloat(parsedData.get(3)));
                        hexaView.setRacketCoordsByPercentage(3,Float.parseFloat(parsedData.get(4)));

                        player1.setScore(Integer.parseInt(parsedData.get(5)));
                        player2.setScore(Integer.parseInt(parsedData.get(6)));
                        player3.setScore(Integer.parseInt(parsedData.get(7)));
                        Log.d("bluetooth-debug-ping", String.valueOf(Long.parseLong(parsedData.get(8)) - System.currentTimeMillis()));
                    }

                }else if( buffer != null && isServer){
                    String data = new String(buffer, StandardCharsets.UTF_8);
                    Log.d("bluetooth-debug", "else in server");
                    List <String> parsedData = parseData(data);
                    Log.d("bluetooth-debug-data", data);
                    if (parsedData.size() == 2)
                        hexaView.setRacketCoordsByPercentage((int) Float.parseFloat(parsedData.get(0)),Float.parseFloat(parsedData.get(1)));
                } else
                    Log.d("bluetooth-debug","buffer is empty");
            }
        });
    }

    private List<String> parseData(String data) {
        List<String> parsedData;
        parsedData = Arrays.asList(data.split(","));
        System.out.println(parsedData.toString());
        return parsedData;
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
        Log.d("bluetooth-debug","before send");

        this.hexaView = findViewById(R.id.hexagonBoard);
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
                if(isServer) {
                    ball.move(deltaT);
                    sendCoordsToClient();
                }
                ball.showBall();
                showScores();
            }
        }, 0, 17);
    }

    private float compactFloat(float f){
        return Float.parseFloat(new DecimalFormat("##.####").format(f));
    }

    private void sendCoordsToClient() {
        String data = "" + compactFloat(ball.getXPercentage()) + ',' +
                      compactFloat(ball.getYPercentage()) + ',' +
                      compactFloat(hexaView.pixelToPercentageRacket(1)) + ',' +
                      compactFloat(hexaView.pixelToPercentageRacket(2)) + ',' +
                      compactFloat(hexaView.pixelToPercentageRacket(3)) + ',' +
                      player1.getScore() + ',' + player2.getScore() + ',' + player3.getScore()+ ',' +
                      System.currentTimeMillis();

        bluetoothService.getChannel().sendString(data);
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

    public void showScores() {
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
        if (!isServer){
            sendNewRacketPosition(2);
        }
    }

    public void racketButton2Right(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket2Right();
        if (!isServer){
            sendNewRacketPosition(2);
        }
    }

    public void racketButton3Left(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket3Left();
        if (!isServer){
            sendNewRacketPosition(3);
        }
    }

    public void racketButton3Right(View view) {
        HexagonMaskView view2 = findViewById(R.id.hexagonBoard);
        view2.racket3Right();
        if (!isServer){
            sendNewRacketPosition(3);
        }
    }

    public void sendNewRacketPosition(int racketNumber){
        String data = "" + racketNumber + "," + hexaView.pixelToPercentageRacket(racketNumber);
        Log.d("bluetooth-debug-cl-r", "client is sending racket position");
        bluetoothService.getChannel().sendString(data);
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
