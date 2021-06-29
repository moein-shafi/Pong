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
    MediaPlayer playBackMediaPlayer;
    MediaPlayer goalMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_device);
//        playBackMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.playback);
//        goalMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.goal);
//        playBackMediaPlayer.start();
//        playBackMediaPlayer.setLooping(true);

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

        bluetoothService.getChannel().setOnMessageReceivedListener(new BluetoothService.OnMessageReceivedListener() {
            @Override
            public void process(byte[] buffer) {
                if (buffer != null){
                    String data = new String(buffer, StandardCharsets.UTF_8);
                    Log.d("bluetooth-debug", data);
                    List <String> parsedData = parseData(data);
                    if (parsedData.size() == 8) {
                        ball.setX0ByPercentage(Float.parseFloat(parsedData.get(0)));
                        ball.setY0ByPercentage(Float.parseFloat(parsedData.get(1)));

                        ball.getRacket1().setY(Float.parseFloat(parsedData.get(2))); //TODO: percent -> pixel
//                        ball.getRacket1().setX(Float.parseFloat(parsedData.get(2)));

                        ball.getRacket2().setY(Float.parseFloat(parsedData.get(3))); //TODO: percent -> pixel
                        ball.getRacket2().setX((Float.parseFloat(parsedData.get(3)) - ball.getBoardLines().get(3).second)/ball.getBoardLines().get(3).first); // x = (y-b)/a

                        ball.getRacket3().setY(Float.parseFloat(parsedData.get(4)));
                        ball.getRacket3().setX((Float.parseFloat(parsedData.get(4)) - ball.getBoardLines().get(5).second)/ball.getBoardLines().get(5).first); // x = (y-b)/a

                        player1.setScore(Integer.parseInt(parsedData.get(5)));
                        player2.setScore(Integer.parseInt(parsedData.get(6)));
                        player3.setScore(Integer.parseInt(parsedData.get(7)));

//                        ball.getRacket1().setStartX(Float.parseFloat(parsedData.get(2)));
//                        ball.getRacket1().setStopX(Float.parseFloat(parsedData.get(3)));
//                        ball.getRacket1().setStartY(Float.parseFloat(parsedData.get(4)));
//                        ball.getRacket1().setStopY(Float.parseFloat(parsedData.get(5)));
//
//                        ball.getRacket2().setStartX(Float.parseFloat(parsedData.get(6)));
//                        ball.getRacket2().setStopX(Float.parseFloat(parsedData.get(7)));
//                        ball.getRacket2().setStartY(Float.parseFloat(parsedData.get(8)));
//                        ball.getRacket2().setStopY(Float.parseFloat(parsedData.get(9)));
//
//                        ball.getRacket3().setStartX(Float.parseFloat(parsedData.get(10)));
//                        ball.getRacket3().setStopX(Float.parseFloat(parsedData.get(11)));
//                        ball.getRacket3().setStartY(Float.parseFloat(parsedData.get(12)));
//                        ball.getRacket3().setStopY(Float.parseFloat(parsedData.get(13)));

//                        player1.setScore(Integer.parseInt(parsedData.get(14)));
//                        player2.setScore(Integer.parseInt(parsedData.get(15)));
//                        player3.setScore(Integer.parseInt(parsedData.get(16)));

                    }
                }else {
                    Log.d("bluetooth-debug","buffer is empty");
                }
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
//        if (bluetoothService.getChannel() == null){
//            Log.d("bluetooth-debug","service nulllll");
//        }else {
//            Log.d("bluetooth-debug","service ok");
//        }

//        if (!isServer) {
//            return;
//        }

//        String salaam = "salaam doost";
//        bluetoothService.getChannel().send(salaam.getBytes(StandardCharsets.UTF_8));
//        Log.d("bluetooth-debug","after send");

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
                if(isServer) {
                    ball.move(deltaT);
                    sendCoordsToClient();
                }
                ball.showBall();
                showScores();
            }
        }, 0, 17);
    }

    private void sendCoordsToClient() {

//        ball.getX0();
//        ball.getY0();
//        String data = Float.toString(ball.getXPercentage()) + ',' + Float.toString(ball.getYPercentage());

//        Log.d("bluetooth-debug", data);
        String data = "" + ball.pixelToPercentageX(ball.getX0()) + ',' + ball.pixelToPercentageY(ball.getY0()) + ',' +

                      ball.pixelToPercentageRacket(1) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket1().getStopX(),1) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket1().getStartY(),1) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket1().getStopY(),1) + ',' +


                      ball.pixelToPercentageRacket(2) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket2().getStopX(),2) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket2().getStartY(),2) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket2().getStopY(),2) + ',' +


                      ball.pixelToPercentageRacket(3) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket3().getStopX(),3) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket3().getStartY(),3) + ',' +
//                      ball.pixelToPercentageRacket(ball.getRacket3().getStopY(),3) + ',' +

                      player1.getScore() + ',' + player2.getScore() + ',' + player3.getScore();


//                      ball.getRacket1().getStartY() + ',' + ball.getRacket1().getStopY() + ',' +
//                      ball.getRacket2().getStartX() + ',' + ball.getRacket2().getStopX() + ',' +
//                      ball.getRacket2().getStartY() + ',' + ball.getRacket2().getStopY() + ',' +

//                ball.getRacket3().getStartX() + ',' + ball.getRacket3().getStopX() + ',' +
//                      ball.getRacket3().getStartY() + ',' + ball.getRacket3().getStopY() + ',' +


//        data += Integer.toString(lastScore1) + ',' + Integer.toString(lastScore2) + ',' + Integer.toString(lastScore3);
        //TODO: add scores
        //TODO: check if we should also send V
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
//                playBackMediaPlayer.seekTo(0);
//                playBackMediaPlayer.start();
//                goalMediaPlayer.pause();
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
//                    playBackMediaPlayer.pause();
//                    goalMediaPlayer.seekTo(0);
//                    goalMediaPlayer.start();
                    stopGoal();
                }
            }
        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (playBackMediaPlayer.isPlaying()) {
//            playBackMediaPlayer.stop();
//            playBackMediaPlayer.release();
//        }
//        if (goalMediaPlayer.isPlaying()) {
//            goalMediaPlayer.stop();
//            goalMediaPlayer.release();
//        }
//    }

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
