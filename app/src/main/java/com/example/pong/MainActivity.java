package com.example.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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