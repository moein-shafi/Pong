package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer playBackMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        playBackMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main);
//        playBackMediaPlayer.start();
//        playBackMediaPlayer.setLooping(true);

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        if (playBackMediaPlayer.isPlaying()) {
////            playBackMediaPlayer.pause();
////        }
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        playBackMediaPlayer.seekTo(0);
////        playBackMediaPlayer.start();
//    }

    public void singleDevice(View view) {
        Intent intent = new Intent(this, SingleDeviceActivity.class);
        startActivity(intent);
    }
    public void multiDevice(View view) {
        Intent intent = new Intent(this, MultiDeviceActivity.class);
        startActivity(intent);
    }
}
