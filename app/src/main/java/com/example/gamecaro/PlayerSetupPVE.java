package com.example.gamecaro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayerSetupPVE extends AppCompatActivity {

    private EditText player1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.player_setup_pve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        player1 = findViewById(R.id.player1EditText);
    }

    public void submitButtonClick(View view){
        String player1Name = player1.getText().toString();

        if(player1Name.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập tên người chơi", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GameDisplayPVE.class);
        intent.putExtra("PLAYER_NAME", player1Name);
        startActivity(intent);
    }
}