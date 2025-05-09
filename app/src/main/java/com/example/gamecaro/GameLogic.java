package com.example.gamecaro;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {
    private int[][] gameBoard;
    private static final int BOARD_SIZE = 15;
    public static final int WIN_CONDITION = 5;

    private String[] playerNames = {"Player 1","Player 2"};

    private int[] winType = {-1,-1,-1};

    private Button playAgainBTN;
    private Button homeBTN;
    private TextView playerTurn;

    private int player = 1;

    private boolean vsBot = false;
    private BotLogic bot;
    public boolean isVsBot() {
        return vsBot;
    }
    public int getWinCondition() {
        return WIN_CONDITION;
    }

    public GameLogic(boolean vsBot) {
        gameBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int r=0; r<BOARD_SIZE; r++){
            for (int c=0; c<BOARD_SIZE; c++){
                gameBoard[r][c] = 0;
            }
        }

        if (vsBot) {
            this.vsBot = true;
            this.bot = new BotLogic(this);
        }
    }

    public boolean updateGameBoard(int row, int col) {
        if (gameBoard[row-1][col-1] == 0){
            gameBoard[row-1][col-1] = player;

            if (vsBot) {
                if (player == 1) {
                    playerTurn.setText("Lượt của Bot");
                } else {
                    playerTurn.setText("Lượt của " + playerNames[0]);
                }
            } else {
                if (player == 1) {
                    playerTurn.setText("Lượt của " + (playerNames[1]));
                } else {
                    playerTurn.setText("Lượt của " + (playerNames[0]));
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    public void botMove() {
        if (vsBot && player == 2) {
            bot.makeMove();
        }
    }


    public boolean winnerCheck() {
        // Kiểm tra hàng ngang
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c <= BOARD_SIZE - WIN_CONDITION; c++) {
                if (checkLine(r, c, 0, 1)) { // 0,1 = hướng ngang
                    setWinType(r, c, 1); // 1 = hàng ngang
                    endGame(playerNames[player-1] + " Thắng!!!");
                    return true;
                }
            }
        }

        // Kiểm tra hàng dọc
        for (int c = 0; c < BOARD_SIZE; c++) {
            for (int r = 0; r <= BOARD_SIZE - WIN_CONDITION; r++) {
                if (checkLine(r, c, 1, 0)) { // 1,0 = hướng dọc
                    setWinType(r, c, 2); // 2 = hàng dọc
                    endGame(playerNames[player-1] + " Thắng!!!");
                    return true;
                }
            }
        }

        // Kiểm tra đường chéo chính
        for (int r = 0; r <= BOARD_SIZE - WIN_CONDITION; r++) {
            for (int c = 0; c <= BOARD_SIZE - WIN_CONDITION; c++) {
                if (checkLine(r, c, 1, 1)) { // 1,1 = hướng chéo chính
                    setWinType(r, c, 3); // 3 = đường chéo chính
                    endGame(playerNames[player-1] + " Thắng!!!");
                    return true;
                }
            }
        }

        // Kiểm tra đường chéo phụ
        for (int r = WIN_CONDITION - 1; r < BOARD_SIZE; r++) {
            for (int c = 0; c <= BOARD_SIZE - WIN_CONDITION; c++) {
                if (checkLine(r, c, -1, 1)) { // -1,1 = hướng chéo phụ
                    setWinType(r, c, 4); // 4 = đường chéo phụ
                    endGame(playerNames[player-1] + " Thắng!!!");
                    return true;
                }
            }
        }

        // Kiểm tra hòa
        if (isBoardFull()) {
            endGame("Hòa !!!");
            return true;
        }

        return false;
    }

    private boolean checkLine(int row, int col, int rowStep, int colStep) {
        int first = gameBoard[row][col];
        if (first == 0) return false;

        for (int i = 1; i < WIN_CONDITION; i++) {
            if (gameBoard[row + i*rowStep][col + i*colStep] != first) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoardFull() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (gameBoard[r][c] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setWinType(int row, int col, int type) {
        winType = new int[]{row, col, type};
    }

    private void endGame(String message) {
        playerTurn.setText(message);
        if (playAgainBTN != null) playAgainBTN.setVisibility(View.VISIBLE);
        if (homeBTN != null) homeBTN.setVisibility(View.VISIBLE);
    }


    public void resetGame() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                gameBoard[r][c] = 0;
            }
        }

        player = 1;
        winType = new int[]{-1, -1, -1}; // Reset winType

        // Không xử lý winningLine ở đây vì nó thuộc về TicTacToeBoard
        playerTurn.setText("Lượt của " + playerNames[0]);
    }

    public void setPlayAgainBTN(Button playAgainBTN) {
        this.playAgainBTN = playAgainBTN;
    }

    public void setHomeBTN(Button homeBTN) {
        this.homeBTN = homeBTN;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public int[] getWinType() {
        return winType;
    }
}
