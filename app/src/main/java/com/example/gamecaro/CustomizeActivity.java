package com.example.gamecaro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CustomizeActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "CaroPrefs";
    public static final String BACKGROUND_KEY = "background";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
    }

    public void setBackground1(View view) {
        saveBackground(R.drawable.bg1); // bg1 là hình nền tùy chọn
    }

    public void setBackground2(View view) {
        saveBackground(R.drawable.bg2);
    }

    public void setBackground3(View view) {
        saveBackground(R.drawable.bg3);
    }

    public void setBackground4(View view) {
        saveBackground(R.drawable.bg4);
    }

    public void setBackground5(View view) {
        saveBackground(R.drawable.bg5);
    }

    public void setBackground6(View view) {
        saveBackground(R.drawable.bg6);
    }

    public void buttonHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void saveBackground(int resId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(BACKGROUND_KEY, resId).apply();
        Toast.makeText(this, "Đã chọn nền thành công!", Toast.LENGTH_SHORT).show();
    }
}
