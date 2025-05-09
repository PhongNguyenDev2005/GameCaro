package com.example.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayerSetup extends AppCompatActivity {

    private EditText player1, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.player_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        player1 = findViewById(R.id.TenNC1);
        player2 = findViewById(R.id.TenNC2);
    }

    public void buttonChoi(View view){
        Intent intent = new Intent(this, GameDisplay.class);

        String TenNC1 = player1.getText().toString();
        String TenNC2 = player2.getText().toString();

        intent.putExtra("PLAYER_NAMES", new String[] {TenNC1,TenNC2});
        startActivity(intent);
    }
}