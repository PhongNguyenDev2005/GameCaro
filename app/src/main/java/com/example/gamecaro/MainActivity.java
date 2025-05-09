package com.example.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ButtonChoiPvP (View view){
        Intent intent = new Intent(this,PlayerSetup.class);
        startActivity(intent);
    }

    // Trong MainActivity.java
    public void ButtonChoiPvE (View view){
        Intent intent = new Intent(this, PlayerSetupPVE.class);
        startActivity(intent);
    }

    public void openCustomize(View view) {
        Intent intent = new Intent(this, CustomizeActivity.class);
        startActivity(intent);
    }
}