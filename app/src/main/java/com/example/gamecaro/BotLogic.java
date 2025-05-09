package com.example.gamecaro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotLogic {
    private final GameLogic game;
    private final Random random;
    private static final int DELAY_MS = 500;
    private static final int BOARD_SIZE = 15;
    private static final int WIN_CONDITION = 5;

    public BotLogic(GameLogic game) {
        this.game = game;
        this.random = new Random();
    }

    public void makeMove() {
        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[][] board = game.getGameBoard();

        // 1. Kiểm tra nước đi thắng ngay
        int[] winningMove = findWinningMove(board, 2);
        if (winningMove != null) {
            game.updateGameBoard(winningMove[0] + 1, winningMove[1] + 1);
            return;
        }

        // 2. Chặn nước đi thắng của đối thủ
        int[] blockingMove = findWinningMove(board, 1);
        if (blockingMove != null) {
            game.updateGameBoard(blockingMove[0] + 1, blockingMove[1] + 1);
            return;
        }

        // 3. Chặn các mô hình nguy hiểm của đối thủ
        int[] dangerMove = findDangerousPatterns(board);
        if (dangerMove != null) {
            game.updateGameBoard(dangerMove[0] + 1, dangerMove[1] + 1);
            return;
        }

        // 4. Tạo các mô hình tấn công cho bot
        int[] attackMove = findAttackOpportunity(board);
        if (attackMove != null) {
            game.updateGameBoard(attackMove[0] + 1, attackMove[1] + 1);
            return;
        }

        // 5. Đánh vào vị trí chiến lược
        int[] strategicMove = findStrategicMove(board);
        if (strategicMove != null) {
            game.updateGameBoard(strategicMove[0] + 1, strategicMove[1] + 1);
            return;
        }

        // 6. Nếu không có nước nào tốt hơn, đánh ngẫu nhiên
        List<int[]> emptyCells = getEmptyCells(board);
        if (!emptyCells.isEmpty()) {
            int[] randomMove = emptyCells.get(random.nextInt(emptyCells.size()));
            game.updateGameBoard(randomMove[0] + 1, randomMove[1] + 1);
        }
    }

    private int[] findWinningMove(int[][] board, int player) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == 0 && wouldWin(board, r, c, player)) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    private boolean wouldWin(int[][] board, int row, int col, int player) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;

            // Đếm về phía trước
            for (int i = 1; i < WIN_CONDITION; i++) {
                int r = row + dir[0] * i;
                int c = col + dir[1] * i;
                if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE || board[r][c] != player) {
                    break;
                }
                count++;
            }

            // Đếm về phía sau
            for (int i = 1; i < WIN_CONDITION; i++) {
                int r = row - dir[0] * i;
                int c = col - dir[1] * i;
                if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE || board[r][c] != player) {
                    break;
                }
                count++;
            }

            if (count >= WIN_CONDITION) {
                return true;
            }
        }
        return false;
    }

    private int[] findDangerousPatterns(int[][] board) {
        // Kiểm tra các mô hình nguy hiểm của đối thủ (player 1)
        List<int[]> dangerMoves = new ArrayList<>();

        // 1. Chặn các hàng 4 ô mở (không bị chặn 2 đầu)
        dangerMoves.addAll(findPattern(board, new int[]{1,1,1,1,0}));
        dangerMoves.addAll(findPattern(board, new int[]{0,1,1,1,1}));
        dangerMoves.addAll(findPattern(board, new int[]{1,1,1,0,1}));
        dangerMoves.addAll(findPattern(board, new int[]{1,1,0,1,1}));
        dangerMoves.addAll(findPattern(board, new int[]{1,0,1,1,1}));

        // 2. Chặn các hàng 3 ô mở
        dangerMoves.addAll(findPattern(board, new int[]{0,1,1,1,0}));

        // 3. Chặn các hàng 2 ô có khoảng cách mở
        dangerMoves.addAll(findPattern(board, new int[]{0,1,0,1,0}));

        if (!dangerMoves.isEmpty()) {
            return dangerMoves.get(random.nextInt(dangerMoves.size()));
        }

        return null;
    }

    private List<int[]> findPattern(int[][] board, int[] pattern) {
        List<int[]> matches = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        int patternLength = pattern.length;
        int player = 1; // Đối thủ

        for (int[] dir : directions) {
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    boolean match = true;
                    int emptyPos = -1;

                    // Kiểm tra xem mẫu có khớp không
                    for (int i = 0; i < patternLength; i++) {
                        int nr = r + dir[0] * i;
                        int nc = c + dir[1] * i;

                        if (nr < 0 || nr >= BOARD_SIZE || nc < 0 || nc >= BOARD_SIZE) {
                            match = false;
                            break;
                        }

                        if (pattern[i] == 1 && board[nr][nc] != player) {
                            match = false;
                            break;
                        }

                        if (pattern[i] == 0 && board[nr][nc] != 0) {
                            match = false;
                            break;
                        }

                        if (pattern[i] == 0) {
                            emptyPos = i;
                        }
                    }

                    if (match && emptyPos != -1) {
                        int nr = r + dir[0] * emptyPos;
                        int nc = c + dir[1] * emptyPos;
                        matches.add(new int[]{nr, nc});
                    }
                }
            }
        }

        return matches;
    }

    private int[] findAttackOpportunity(int[][] board) {
        // Tìm cơ hội tấn công cho bot (player 2)
        List<int[]> attackMoves = new ArrayList<>();

        // 1. Tạo hàng 4 ô
        attackMoves.addAll(findPattern(board, new int[]{2,2,2,2,0}));
        attackMoves.addAll(findPattern(board, new int[]{0,2,2,2,2}));
        attackMoves.addAll(findPattern(board, new int[]{2,2,2,0,2}));
        attackMoves.addAll(findPattern(board, new int[]{2,2,0,2,2}));
        attackMoves.addAll(findPattern(board, new int[]{2,0,2,2,2}));

        // 2. Tạo hàng 3 ô mở
        attackMoves.addAll(findPattern(board, new int[]{0,2,2,2,0}));

        // 3. Tạo hàng 2 ô có khoảng cách mở
        attackMoves.addAll(findPattern(board, new int[]{0,2,0,2,0}));

        if (!attackMoves.isEmpty()) {
            return attackMoves.get(random.nextInt(attackMoves.size()));
        }

        return null;
    }

    private int[] findStrategicMove(int[][] board) {
        int center = BOARD_SIZE / 2;

        // Ưu tiên chiếm trung tâm nếu còn trống
        if (board[center][center] == 0) {
            return new int[]{center, center};
        }

        // Ưu tiên các ô gần các ô đã đánh
        int[] nearMove = findNearOccupiedMove(board);
        if (nearMove != null) {
            return nearMove;
        }

        return null;
    }

    private int[] findNearOccupiedMove(int[][] board) {
        List<int[]> emptyCells = getEmptyCells(board);
        List<int[]> nearMoves = new ArrayList<>();
        List<int[]> veryNearMoves = new ArrayList<>(); // Các ô ngay sát ô đã đánh

        for (int[] cell : emptyCells) {
            if (isVeryNearOccupied(board, cell[0], cell[1])) {
                veryNearMoves.add(cell);
            } else if (isNearOccupied(board, cell[0], cell[1])) {
                nearMoves.add(cell);
            }
        }

        if (!veryNearMoves.isEmpty()) {
            return veryNearMoves.get(random.nextInt(veryNearMoves.size()));
        }

        if (!nearMoves.isEmpty()) {
            return nearMoves.get(random.nextInt(nearMoves.size()));
        }

        return null;
    }

    private boolean isNearOccupied(int[][] board, int row, int col) {
        // Kiểm tra trong bán kính 2 ô
        for (int r = Math.max(0, row-2); r <= Math.min(BOARD_SIZE-1, row+2); r++) {
            for (int c = Math.max(0, col-2); c <= Math.min(BOARD_SIZE-1, col+2); c++) {
                if (board[r][c] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isVeryNearOccupied(int[][] board, int row, int col) {
        // Kiểm tra 8 ô xung quanh (bán kính 1 ô)
        for (int r = Math.max(0, row-1); r <= Math.min(BOARD_SIZE-1, row+1); r++) {
            for (int c = Math.max(0, col-1); c <= Math.min(BOARD_SIZE-1, col+1); c++) {
                if (board[r][c] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<int[]> getEmptyCells(int[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == 0) {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }
        return emptyCells;
    }
}