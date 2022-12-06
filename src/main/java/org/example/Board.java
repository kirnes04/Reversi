package org.example;

import java.util.ArrayList;


/**
 * The field on which the game is played.
 */
public class Board {

    protected static final int BOARD_SIZE = 8;

    private static final char EMPTY_CELL = ' ';

    protected static final char BLACK_CHIP = 'b';

    protected static final char WHITE_CHIP = 'w';

    private static final char POSSIBLE_CHIP = 'p';

    protected final char[][] grid;

    /**
     * Board constructor.
     */
    Board() {
        grid = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                grid[i][j] = EMPTY_CELL;
            }
        }
        grid[3][3] = WHITE_CHIP;
        grid[4][4] = WHITE_CHIP;
        grid[3][4] = BLACK_CHIP;
        grid[4][3] = BLACK_CHIP;
    }

    /**
     * Board constructor on char[][].
     * @param grid char[][] (field).
     */
    Board(char[][] grid) {
        this.grid = grid;
    }

    /**
     * Displays bord in the console
     */
    void displayBoard() {
        System.out.println("---------------------------------");
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.out.print("| ");
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.print(grid[i][j] + " | ");
            }
            System.out.println("\n---------------------------------");
        }
    }

    /**
     * displayBoard overload. Displays board with possible moves.
     * @param moves list of possible moves.
     */
    void displayBoard(ArrayList<Integer> moves) {
        var tmp_grid = new char[BOARD_SIZE][];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            tmp_grid[i] = grid[i].clone();
        }
        for (int i = 0; i < moves.size(); i += 2) {
            tmp_grid[moves.get(i)][moves.get((i + 1))] = POSSIBLE_CHIP;
        }
        System.out.println("\n\nPossible moves are illustrated with the letter p");
        System.out.println("---------------------------------");
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.out.print("| ");
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.print(tmp_grid[i][j] + " | ");
            }
            System.out.println("\n---------------------------------");
        }
    }

    /**
     * Finds all the possible moves.
     * @param curPlayer the color of the current player.
     * @return the list of possible moves.
     */
    ArrayList<Integer> possibleMoves(char curPlayer) {
        var moves = new ArrayList<Integer>(4);
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (isValid(i, j, curPlayer)) {
                    moves.add(i);
                    moves.add(j);
                }
            }
        }
        return moves;
    }

    /**
     * Checks if the move is valid.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     * @return true, if the move is possible.
     */
    private boolean isValid(int i, int j, char curPlayer) {
        char chip;
        if (grid[i][j] != EMPTY_CELL)
            return false;
        if (curPlayer == BLACK_CHIP) {
            chip = WHITE_CHIP;
        } else {
            chip = BLACK_CHIP;
        }
        if (i != 0) {
            if (grid[i - 1][j] == chip && goUp(i, j, curPlayer)) {
                return true;
            }
            if (j != 7) {
                if (grid[i - 1][j + 1] == chip && goUpRight(i, j, curPlayer)) {
                    return true;
                }
            }
        }
        if (j != 0) {
            if (grid[i][j - 1] == chip && goLeft(i, j, curPlayer)) {
                return true;
            }
            if (i != 0) {
                if (grid[i - 1][j - 1] == chip && goUpLeft(i, j, curPlayer)) {
                    return true;
                }
            }
        }
        if (i != 7) {
            if (grid[i + 1][j] == chip && goDown(i, j, curPlayer)) {
                return true;
            }
            if (j != 0) {
                if (grid[i + 1][j - 1] == chip && goDownLeft(i, j, curPlayer)) {
                    return true;
                }
            }
        }
        if (j != 7) {
            if (grid[i][j + 1] == chip && goRight(i, j, curPlayer)) {
                return true;
            }
            if (i != 7) {
                return grid[i + 1][j + 1] == chip && goDownRight(i, j, curPlayer);
            }
        }
        return false;
    }

    /**
     * Scans to the right of the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip on the right of the chosen cell.
     */
    private boolean goRight(int i, int j, char chip) {
        for (int k = j + 2; k < BOARD_SIZE; ++k) {
            if (grid[i][k] == EMPTY_CELL) {
                return false;
            }
            if (grid[i][k] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans to the left and down from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip under and in the left of the chosen cell.
     */
    private boolean goDownLeft(int i, int j, char chip) {
        int temp_j = j - 1;
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            if (temp_j < 1)
                continue;
            --temp_j;
            if (grid[k][temp_j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][temp_j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans down from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip under the chosen cell.
     */
    private boolean goDown(int i, int j, char chip) {
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            if (grid[k][j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans to the left of the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip on the left of the chosen cell.
     */
    private boolean goLeft(int i, int j, char chip) {
        for (int k = j - 2; k > -1; --k) {
            if (grid[i][k] == EMPTY_CELL) {
                return false;
            }
            if (grid[i][k] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans to the right and up from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip above and in the right of the chosen cell.
     */
    private boolean goUpRight(int i, int j, char chip) {
        int temp_j = j + 1;
        for (int k = i - 2; k > -1; --k) {
            if (temp_j > 6)
                continue;
            ++temp_j;
            if (grid[k][temp_j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][temp_j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans up from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip above the chosen cell.
     */
    private boolean goUp(int i, int j, char chip) {
        for (int k = i - 2; k > -1; --k) {
            if (grid[k][j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans to the right and down from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip under and in the right of the chosen cell.
     */
    private boolean goDownRight(int i, int j, char chip) {
        int temp_j = j + 1;
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            if (temp_j > 6)
                continue;
            ++temp_j;
            if (grid[k][temp_j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][temp_j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scans to the left and up from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param chip the color of the required chip.
     * @return true, if there is players chip above and in the left of the chosen cell.
     */
    boolean goUpLeft(int i, int j, char chip) {
        int temp_j = j - 1;
        for (int k = i - 2; k > -1; --k) {
            if (temp_j < 1)
                continue;
            --temp_j;
            if (grid[k][temp_j] == EMPTY_CELL) {
                return false;
            }
            if (grid[k][temp_j] == chip) {
                return true;
            }
        }
        return false;
    }

    /**
     *Checks if the board is filled up.
     * @return true, if the board has no empty cells.
     */
    boolean isFull() {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (grid[i][j] == EMPTY_CELL)
                    return false;
            }
        }
        return true;
    }

    /**
     * Function that implements move logic (place a chip and change opponent's)
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    void changeCells(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        char chip;
        if (curPlayer == BLACK_CHIP) {
            chip = WHITE_CHIP;
        } else {
            chip = BLACK_CHIP;
        }
        if (i != 0) {
            if (grid[i - 1][j] == chip) {
                changeUp(i, j, curPlayer);
            }
            if (j != 7) {
                if (grid[i - 1][j + 1] == chip) {
                    changeUpRight(i, j, curPlayer);
                }
            }
        }
        if (j != 0) {
            if (grid[i][j - 1] == chip) {
                changeLeft(i, j, curPlayer);
            }
            if (i != 0) {
                if (grid[i - 1][j - 1] == chip) {
                    ChangeUpLeft(i, j, curPlayer);
                }
            }
        }
        if (i != 7) {
            if (grid[i + 1][j] == chip) {
                changeDown(i, j, curPlayer);
            }
            if (j != 0) {
                if (grid[i + 1][j - 1] == chip) {
                    ChangeDownLeft(i, j, curPlayer);
                }
            }
        }
        if (j != 7) {
            if (grid[i][j + 1] == chip && goRight(i, j, curPlayer)) {
                changeRight(i, j, curPlayer);
            }
            if (i != 7) {
                if (grid[i + 1][j + 1] == chip) {
                    changeDownRight(i, j, curPlayer);
                }
            }
        }
    }

    /**
     * Changes the chips to the right and lower from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeDownRight(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            for (int l = j + 2; l < BOARD_SIZE; ++l) {
                if (grid[k][l] == curPlayer) {
                    for (int m = i + 1; m < k; ++m) {
                        for (int n = j + 1; n < l; ++n) {
                            grid[m][n] = curPlayer;
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Changes the chips to the right from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeRight(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = j + 2; k < BOARD_SIZE; ++k) {
            if (grid[i][k] == curPlayer) {
                for (int l = j + 1; l < k; ++l) {
                    grid[i][l] = curPlayer;
                }
                return;
            }
        }
    }

    /**
     * Changes the chips to the left and lower from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void ChangeDownLeft(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            for (int l = j - 2; l > -1; --l) {
                if (grid[k][l] == curPlayer) {
                    for (int m = i + 1; m < k; ++m) {
                        for (int n = j - 1; n > l; --n) {
                            grid[m][n] = curPlayer;
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Changes the chips lower than the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeDown(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i + 2; k < BOARD_SIZE; ++k) {
            if (grid[k][j] == curPlayer) {
                for (int l = i + 1; l < k; ++l) {
                    grid[l][j] = curPlayer;
                }
                return;
            }
        }
    }

    /**
     * Changes the chips to the left and higher from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void ChangeUpLeft(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i - 2; k > -1; --k) {
            for (int l = j - 2; l > -1; --l) {
                if (grid[k][l] == curPlayer) {
                    for (int m = i - 1; m > k; --m) {
                        for (int n = j - 1; n > l; --n) {
                            grid[m][n] = curPlayer;
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Changes the chips to the left from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeLeft(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = j - 2; k > - 1; --k) {
            if (grid[i][k] == curPlayer) {
                for (int l = j - 1; l > k; --l) {
                    grid[i][l] = curPlayer;
                }
                return;
            }
        }
    }

    /**
     * Changes the chips to the right and higher from the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeUpRight(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i - 2; k > -1; --k) {
            for (int l = j + 2; l < BOARD_SIZE; ++l) {
                if (grid[k][l] == Board.EMPTY_CELL) {
                    return;
                } else if (grid[k][l] == curPlayer) {
                    grid[i - 1][j + 1] = curPlayer;
                }
                grid[k][l] = curPlayer;
            }
        }
        for (int k = i - 2; k > -1; --k) {
            for (int l = j + 2; l < BOARD_SIZE; ++l) {
                if (grid[k][l] == curPlayer) {
                    for (int m = i - 1; m > k; --m) {
                        for (int n = j + 1; n < l; ++n) {
                            grid[m][n] = curPlayer;
                        }
                    }
                    return;
                }
            }
        }
    }

    /**
     * Changes the chips higher than the chosen cell.
     * @param i the row of the chosen cell.
     * @param j the column of the chosen cell.
     * @param curPlayer the color of the current player.
     */
    private void changeUp(int i, int j, char curPlayer) {
        grid[i][j] = curPlayer;
        for (int k = i - 2; k > - 1; --k) {
            if (grid[k][j] == curPlayer) {
                for (int l = i - 1; l > k; --l) {
                    grid[l][j] = curPlayer;
                }
                return;
            }
        }
    }

    /**
     * Counts the chips on the board.
     * @param color the color of counted chips.
     * @return the amount of 'color' chips
     */
    protected int countChips(char color) {
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                if (grid[i][j] == color) {
                    ++counter;
                }
            }
        }
        return counter;
    }
}
