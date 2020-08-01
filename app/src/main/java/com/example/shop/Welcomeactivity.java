package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class Welcomeactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(Welcomeactivity.this,Welcome.class);
////                startActivity(intent);
////                finish();//把不需要的activity关闭
                Intent intent = new Intent(Welcomeactivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
