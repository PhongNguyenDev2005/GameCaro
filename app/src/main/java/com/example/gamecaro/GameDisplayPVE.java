package com.example.gamecaro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameDisplayPVE extends AppCompatActivity {

    private TicTacToeBoard ticTacToeBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button playAgainBTN = findViewById(R.id.play_again);
        Button homeBTN = findViewById(R.id.home_button);
        TextView playerTurn = findViewById(R.id.player_display);

        String playerName = getIntent().getStringExtra("PLAYER_NAME");

        playAgainBTN.setVisibility(View.VISIBLE);
        homeBTN.setVisibility(View.VISIBLE);

        playerTurn.setText("Lượt của " + playerName);

        ticTacToeBoard = findViewById(R.id.ticTacToeBoard);

        // Khởi tạo game với chế độ PvE
        GameLogic game = new GameLogic(true);
        game.setPlayerNames(new String[]{playerName, "Bot"});
        ticTacToeBoard.setGameLogic(game);
        ticTacToeBoard.setUpGame(playAgainBTN, homeBTN, playerTurn, new String[]{playerName, "Bot"});

        SharedPreferences prefs = getSharedPreferences("CaroPrefs", MODE_PRIVATE);
        int bgResId = prefs.getInt("background", 0);

        View rootView = findViewById(R.id.main);
        if (bgResId != 0) {
            rootView.setBackgroundResource(bgResId);
        } else {
            rootView.setBackgroundColor(Color.parseColor("#D7CCC8")); // mặc định
        }
    }

    public void buttonChoiLai(View view){
        ticTacToeBoard.resetGame();
        ticTacToeBoard.invalidate();
    }

    public void buttonHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}