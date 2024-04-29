import java.util.Scanner;

public class SudokuSolver {

    private static final int GRID_SIZE;
    static {
        GRID_SIZE = 9;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] board = {
                {0, 0, 8, 0, 0, 0, 0, 0, 0},
                {7, 6, 0, 2, 1, 0, 0, 0, 8},
                {0, 2, 0, 0, 3, 7, 0, 0, 0},
                {0, 4, 0, 5, 0, 0, 0, 0, 6},
                {3, 0, 6, 0, 0, 0, 8, 0, 9},
                {2, 0, 0, 0, 0, 3, 0, 4, 0},
                {0, 0, 0, 1, 6, 0, 0, 2, 0},
                {5, 0, 0, 0, 9, 2, 0, 1, 7},
                {0, 0, 0, 0, 0, 0, 9, 0, 0}
        };

        System.out.println(Print.SOLVE);
        printBoard(board);
        System.out.println(Print.SHOW);
        String showSolution = scanner.nextLine();

        if (solve(board)) {
            System.out.println(Print.SUCCESS);
            printBoard(board);
        } else System.out.println(Print.INVALID);
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
        for (int row = 0; row < GRID_SIZE; row++){
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
}

class Print {
    public static final String DIV = "_________________________________";
    public static final String SOLVE = "------- Attempt to solve -------\n";
    public static final String SUCCESS = "-- Puzzle solved succesfully! --\n";
    public static final String INVALID = "Invalid puzzle input.";
    public static final String SHOW = "\n--> Press the Enter key to show the solution.\n";
}