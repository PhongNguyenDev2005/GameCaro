package com.example.gamecaro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {

    private final int boardColor;
    private final int XColor;
    private final int OColor;
    private final int winningLineColor;

    private boolean winningLine = false;

    private GameLogic game;

    private int cellSize = getWidth()/15; // Thay đổi từ 3 sang 15

    private final Paint paint = new Paint();

    public void setGameLogic(GameLogic game) {
        this.game = game;
    }

    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic(false);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard,0,0);

        try {
            boardColor = a.getInteger(R.styleable.TicTacToeBoard_boardColor,0);
            XColor = a.getInteger(R.styleable.TicTacToeBoard_XColor,0);
            OColor = a.getInteger(R.styleable.TicTacToeBoard_OColor,0);
            winningLineColor = a.getInteger(R.styleable.TicTacToeBoard_winningLineColor,0);
        } finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width,height);

        int dimension = Math.min(getMeasuredWidth(),getMeasuredHeight());
        cellSize = dimension/15; // Thay đổi từ 3 sang 15

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); // đảm bảo nền từ layout hoặc setBackground sẽ hiển thị

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK); // màu khung bàn cờ
        paint.setStrokeWidth(4);

        canvas.drawColor(Color.argb(153, 215, 204, 200));

        drawGameBoard(canvas);    // Vẽ các đường kẻ 15x15
        drawMarkers(canvas);      // Vẽ quân cờ

        if (winningLine) {
            paint.setColor(winningLineColor);
            drawWinningLine(canvas); // Vẽ đường thắng
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            int row = (int) Math.ceil(y / cellSize);
            int col = (int) Math.ceil(x / cellSize);

            if (!winningLine) {
                if (game.updateGameBoard(row, col)) {
                    invalidate();

                    if (game.winnerCheck()) {
                        winningLine = true;
                        invalidate();
                    } else {
                        // Nếu chơi với bot và chưa kết thúc game
                        if (game.isVsBot() && !winningLine) {
                            // Thêm Handler để tạo delay
                            new android.os.Handler().postDelayed(() -> {
                                game.setPlayer(2); // Chuyển sang bot
                                game.botMove(); // Bot thực hiện nước đi
                                invalidate();

                                if (game.winnerCheck()) {
                                    winningLine = true;
                                    invalidate();
                                }

                                game.setPlayer(1); // Chuyển lại người chơi
                            }, 1000); // Delay 1 giây (1000 milliseconds)
                        } else {
                            // Logic PvP thông thường
                            if (game.getPlayer() % 2 == 0) {
                                game.setPlayer(game.getPlayer() - 1);
                            } else {
                                game.setPlayer(game.getPlayer() + 1);
                            }
                        }
                    }
                }
            }

            invalidate();
            return true;
        }
        return false;
    }

    private void drawGameBoard(Canvas canvas){
        paint.setColor(boardColor);
        paint.setStrokeWidth(5); // Giảm độ dày đường kẻ cho bàn cờ lớn hơn

        // Vẽ các đường kẻ ngang và dọc
        for(int c=1; c<15; c++){
            canvas.drawLine(cellSize*c, 0, cellSize*c, canvas.getWidth(), paint);
        }

        for(int r=1; r<15; r++){
            canvas.drawLine(0, cellSize*r, canvas.getWidth(), cellSize*r, paint);
        }
    }

    private void drawMarkers(Canvas canvas){
        for (int r=0; r<15; r++){
            for (int c=0; c<15; c++){
                if(game.getGameBoard()[r][c] != 0){
                    if(game.getGameBoard()[r][c] == 1){
                        drawX(canvas,r,c);
                    }
                    else {
                        drawO(canvas,r,c);
                    }
                }
            }
        }
    }

    private void drawX(Canvas canvas, int row, int col){
        paint.setColor(XColor);
        paint.setStrokeWidth(10); // Điều chỉnh độ dày nét vẽ X

        // Vẽ X lớn hơn nhưng vẫn nằm trong ô
        float padding = cellSize * 0.15f;
        canvas.drawLine(
                col * cellSize + padding,
                row * cellSize + padding,
                (col + 1) * cellSize - padding,
                (row + 1) * cellSize - padding,
                paint);

        canvas.drawLine(
                (col + 1) * cellSize - padding,
                row * cellSize + padding,
                col * cellSize + padding,
                (row + 1) * cellSize - padding,
                paint);
    }

    private void drawO(Canvas canvas, int row, int col){
        paint.setColor(OColor);
        paint.setStrokeWidth(10); // Điều chỉnh độ dày nét vẽ O
        paint.setStyle(Paint.Style.STROKE);

        float padding = cellSize * 0.15f;
        canvas.drawOval(
                col * cellSize + padding,
                row * cellSize + padding,
                (col + 1) * cellSize - padding,
                (row + 1) * cellSize - padding,
                paint);
    }

    private void drawHorizontalLine(Canvas canvas, int row, int col){
        paint.setStrokeWidth(10); // Tăng độ dày đường chiến thắng
        canvas.drawLine(
                col * cellSize + cellSize/2f,
                row * cellSize + cellSize/2f,
                (col + GameLogic.WIN_CONDITION) * cellSize - cellSize/2f,
                row * cellSize + cellSize/2f,
                paint);
    }

    private void drawVerticalLine(Canvas canvas, int row, int col){
        paint.setStrokeWidth(10);
        canvas.drawLine(
                col * cellSize + cellSize/2f,
                row * cellSize + cellSize/2f,
                col * cellSize + cellSize/2f,
                (row + GameLogic.WIN_CONDITION) * cellSize - cellSize/2f,
                paint);
    }

    private void drawDiagonalLinePos(Canvas canvas){
        paint.setStrokeWidth(10);
        canvas.drawLine(
                0 + cellSize/2f,
                canvas.getHeight() - cellSize/2f,
                canvas.getWidth() - cellSize/2f,
                0 + cellSize/2f,
                paint);
    }

    private void drawDiagonalLineNeg(Canvas canvas){
        paint.setStrokeWidth(10);
        canvas.drawLine(
                0 + cellSize/2f,
                0 + cellSize/2f,
                canvas.getWidth() - cellSize/2f,
                canvas.getHeight() - cellSize/2f,
                paint);
    }

    private void drawWinningLine(Canvas canvas) {
        if (game == null || game.getWinType() == null) return;

        int[] winType = game.getWinType();
        int row = winType[0];  // Hàng đầu tiên (0-14)
        int col = winType[1];  // Cột đầu tiên (0-14)
        int direction = winType[2]; // 1: ngang, 2: dọc, 3: chéo \, 4: chéo /
        int winLength = 5; // Cố định 5 cho caro

        paint.setColor(winningLineColor);
        paint.setStrokeWidth(15);

        // Tính toán tọa độ chính xác
        float startX, startY, endX, endY;
        float padding = cellSize * 0.2f;
        float center = cellSize / 2f;

        switch (direction) {
            case 1: // Ngang
                startX = col * cellSize + padding;
                startY = row * cellSize + center;
                endX = (col + winLength - 1) * cellSize + (cellSize - padding);
                endY = startY;
                break;

            case 2: // Dọc
                startX = col * cellSize + center;
                startY = row * cellSize + padding;
                endX = startX;
                endY = (row + winLength - 1) * cellSize + (cellSize - padding);
                break;

            case 3: // Chéo chính \
                startX = col * cellSize + padding;
                startY = row * cellSize + padding;
                endX = (col + winLength - 1) * cellSize + (cellSize - padding);
                endY = (row + winLength - 1) * cellSize + (cellSize - padding);
                break;

            case 4: // Chéo phụ /
                startX = col * cellSize + center;
                startY = (row) * cellSize + center;
                endX = (col + winLength - 1) * cellSize + center;
                endY = (row - (winLength - 1)) * cellSize + center;
                break;

            default:
                return;
        }

        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void setUpGame(Button playAgain, Button home, TextView playerDisplay, String[] names) {
        // Hiển thị nút luôn
        playAgain.setVisibility(View.VISIBLE);
        home.setVisibility(View.VISIBLE);

        game.setPlayAgainBTN(playAgain);
        game.setHomeBTN(home);
        game.setPlayerTurn(playerDisplay);
        game.setPlayerNames(names);
    }

    public void resetGame(){
        if (game != null) {
            game.resetGame();
        }
        winningLine = false; // Đây là biến của TicTacToeBoard
        invalidate();
    }
}
