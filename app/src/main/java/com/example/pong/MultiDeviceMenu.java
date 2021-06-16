package com.example.pong;

import android.content.Intent;
import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MultiDeviceMenu extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_device_menu);

    }

    public void openServerPage(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }

    public void openClientPage(View view) {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }
}