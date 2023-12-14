package com.innovapp.luk7;


import static com.innovapp.luk7.Utiles.aplicarTema;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarTema(SplashActivity.this);

        setContentView(R.layout.activity_splash);



        Thread splashThread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(1000);



                    String action = getIntent().getDataString();

                    if (action == null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("action",action));
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        splashThread.start();



    }


}