package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void singleDevice(View view) {
        Intent intent = new Intent(this, SingleDeviceActivity.class);
        HexagonMaskView hexaView = findViewById(R.id.hexagonBoard);
        startActivity(intent);
    }
    public void multiDevice(View view) {
//        Intent intent = new Intent(this, SingleDeviceActivity.class);
//        startActivity(intent);
    }


}
