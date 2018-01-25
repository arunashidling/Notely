package com.pe.notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotelySplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notely_splash_screen);
        showMainActivity();
    }

    private void showMainActivity() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent entry = new Intent(NotelySplashScreen.this, MainActivity.class);
                startActivity(entry);

                finish();
                overridePendingTransition(0, 0);
            }
        }, 1000);

    }
}
