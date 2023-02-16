package com.kilomelo.pa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LoadingSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_splash);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoadingSplash.class);
        context.startActivity(intent);
    }

}