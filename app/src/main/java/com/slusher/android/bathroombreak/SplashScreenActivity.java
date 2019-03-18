package com.slusher.android.bathroombreak;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    private ConstraintLayout splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        splashScreen = (ConstraintLayout) findViewById(R.id.layout_splash);

        scheduleNextScreen();
    }

    private void scheduleNextScreen() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }, 3000);
    }
}
