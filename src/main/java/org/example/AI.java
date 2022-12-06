package org.example;

/**
 * Artificial Intelligence class (evaluates the move).
 */
public class AI implements IEvaluatable {
    /**
     * Static function that evaluates the move.
     * @param i row of chosen cell.
     * @param j column of chosen cell.
     * @param board the field on which the game is played.
     * @param curPlayer the color of current player.
     * @return the evaluation of the move.
     */
    static double evaluate(int i, int j, Board board, char curPlayer) {
        double ss = 0;
        double sum = 0;
        var tmp_grid = new char[Board.BOARD_SIZE][];
        for (int k = 0; k < Board.BOARD_SIZE; ++k) {
            tmp_grid[k] = board.grid[k].clone();
        }
        Board board_copy = new Board(tmp_grid);
        board_copy.changeCells(i, j, curPlayer);
        for (int k = 0; k < Board.BOARD_SIZE; ++k) {
            for (int l = 0; l < Board.BOARD_SIZE; ++l) {
                if (board.grid[k][l] != board_copy.grid[k][l]) {
                    if (i == k && l == j) {
                        if (i == 0 || j == 0 || i == 7 || j == 7) {
                            if (i == 0 && j == 0 || i == 0 && j == 7 || i == 7 && j == 0 || i == 7 && j == 7) {
                                ss = 0.8;
                            } else {
                                ss = 0.4;
                            }
                        }
                    } else {
                        if (i == 0 || j == 0 || i == 7 || j == 7) {
                            sum += 2;
                        } else {
                            ++sum;
                        }
                    }
                }
            }
        }
        sum += ss;
        return sum;
    }
}
