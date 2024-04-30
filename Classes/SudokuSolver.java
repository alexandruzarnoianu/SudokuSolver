package Classes;

import java.util.Random;
import java.util.Scanner;

public class SudokuSolver {

    private static final int GRID_SIZE;
    private static final int DIF_EASY;
    private static final int DIF_MEDIUM;
    private static final int DIF_HARD;
    private static int[][] solvedBoard;

    static {
        GRID_SIZE = 9;
        DIF_EASY = 41;
        DIF_MEDIUM = 48;
        DIF_HARD = 55;
        solvedBoard = new int[GRID_SIZE][GRID_SIZE];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Print.NEW_GAME);
        String difficulty = scanner.nextLine();
        int[][] board;
        switch (difficulty) {
            case "1": {
                board = generateBoard(DIF_EASY);
                break;
            }
            case "2": {
                board = generateBoard(DIF_MEDIUM);
                break;
            }
            case "3": {
                board = generateBoard(DIF_HARD);
                break;
            }
            default: {
                System.out.println(Print.INVALID);
                board = generateBoard(DIF_EASY);
            }
        }
        System.out.println(Print.SOLVE);
        printBoard(board);
        System.out.println(Print.SHOW);
        String showSolution = scanner.nextLine();
        System.out.println(Print.SUCCESS);
        printBoard(solvedBoard);
    }

    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInColumn(int[][] board, int number, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInGrid(int[][] board, int number, int row, int col) {
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isPositionValid(int[][] board, int number, int row, int col) {
        return !isNumberInRow(board, number, row) &&
                !isNumberInColumn(board, number, col) &&
                !isNumberInGrid(board, number, row, col);
    }

    private static boolean solve(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int numTry = 1; numTry <= GRID_SIZE; numTry++) {
                        if (isPositionValid(board, numTry, row, col)) {
                            board[row][col] = numTry;
                            if (solve(board)) {
                                return true;
                            } else board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0 && col != GRID_SIZE - 1) {
                    System.out.print(" | ");
                }
                System.out.format(" %d ", board[row][col]);
            }
            System.out.println();
            if (row % 3 == 2 && row != GRID_SIZE - 1) {
                System.out.println(Print.DIV);
            }
        }
    }

    private static int[][] generateBoard(int difficulty) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        populateBoard(board);
        solve(board);
        cacheSolved(board);
        return makeSolvable(board, difficulty);
    }

    private static void populateBoard(int[][] board) {
        Random random = new Random();
        for (int row = 0; row < GRID_SIZE; row += 4) {
            for (int col = random.nextInt(3); col < GRID_SIZE; col += 1 + random.nextInt(3)) {
                int number = 1 + random.nextInt(9);
                if (board[row][col] == 0 && isPositionValid(board, number, row, col)) {
                    board[row][col] = number;
                }
            }
        }
    }

    private static int[][] makeSolvable(int[][] board, int difficulty) {
        Random rdm = new Random();
        int maxEmpty = difficulty;
        int maxPerRow = difficulty / 9 + 1;
        while (maxEmpty != 0) {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = rdm.nextInt(3); col < GRID_SIZE; col += 1 + rdm.nextInt(3)) {
                    if (board[row][col] != 0) {
                        board[row][col] = 0;
                        maxEmpty--;
                        maxPerRow--;
                    }
                    if (maxEmpty == 0 || maxPerRow == 0) {
                        break;
                    }
                }
                maxPerRow = difficulty / 9 + 1;
                if (maxEmpty == 0) {
                    return board;
                }
            }
        }
        return board;
    }

    private static void cacheSolved(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                solvedBoard[row][col] = board[row][col];
            }
        }
    }
}

class Print {
    public static final String DIV = "_________________________________";
    public static final String SOLVE = "------- Attempt to solve -------\n";
    public static final String SUCCESS = "-- Puzzle solved successfully! --\n";
    public static final String INVALID = "Invalid input. Let's start you on Easy.";
    public static final String SHOW = "\n--> Press the Enter key to show the solution.\n";
    public static final String NEW_GAME = "Hello and welcome to this Sudoku game. To start please chose the " +
            "difficulty:\n\n\t1 - Easy\n\t2 - Medium\n\t3 - Hard\n*Type No. + Enter (i.e. 1 (Enter))*";
}