package org.example;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.max;

/**
 * Reversi (othello) game class
 */
public class Reversi {
    private int mode;
    private int bestScore = -1;
    private Board board;

    char curPlayer = Board.BLACK_CHIP;

    /**
     * Displays the start menu.
     */
    void startMenu() {
        System.out.println("""
                \n
                Hello! You are playing Reversi game.
                If you want to play versus second player, type "player".
                If you want to play versus computer, type "easy" or "medium"
                If you want to see your best score, type "best\"""");
        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        while (!(choice.equals("player") || choice.equals("easy") || choice.equals("medium") || choice.equals("best"))) {
            System.out.println("Wrong input. Please, try again.");
            choice = in.nextLine();
        }
        switch (choice) {
            case "player" -> {
                mode = 2;
                startPVP();
            }
            case "easy" -> {
                mode = 0;
                startEasy();
            }
            case "medium" -> {
                mode = 1;
                startMedium();
            }
            default -> {
                displayBest();
                startMenu();
            }
        }
    }

    /**
     * Starts the game versus an easy bot.
     */
    private void startEasy() {
        board = new Board();
        while (!this.gameOver()) {
            int impossibleTurns = 0;
            board.displayBoard();
            System.out.println("It is your turn. Please, choose the cell you want to place your chip.");
            try {
                userTurn();
            } catch (ReversiException ex) {
                System.out.println(ex.getMessage());
                ++impossibleTurns;
            }
            curPlayer = curPlayer == 'b' ? 'w' : 'b';
            try {
                computerTurn();
            } catch (ReversiException ex) {
                System.out.println(ex.getMessage());
                ++impossibleTurns;
            }
            if (impossibleTurns == 2) {
                System.out.println("No possible moves for the both players!");
                break;
            }
            curPlayer = curPlayer == 'b' ? 'w' : 'b';
        }
        this.gameResults();
    }

    /**
     * Starts a game versus a clever bot (will be developed later..).
     */
    private void startMedium() {
        System.out.println("The clever bot has fallen ill. Please, try this option later.");
        startMenu();
    }

    /**
     * Starts a player versus player game.
     */
    private void startPVP() {
        board = new Board();
        while (!this.gameOver()) {
            int impossibleTurns = 0;
            board.displayBoard();
            System.out.println("It is the first Player's turn. Please, choose the cell you want to place your chip.");
            try {
                userTurn();
            } catch (ReversiException ex) {
                System.out.println(ex.getMessage());
                ++impossibleTurns;
            }
            curPlayer = curPlayer == 'b' ? 'w' : 'b';
            System.out.println("It is the second Player's turn. Please, choose the cell you want to place your chip.");
            try {
                userTurn();
            } catch (ReversiException ex) {
                System.out.println(ex.getMessage());
                ++impossibleTurns;
            }
            if (impossibleTurns == 2) {
                System.out.println("No possible moves for the both players!");
                break;
            }
            curPlayer = curPlayer == 'b' ? 'w' : 'b';
        }
        this.gameResults();
    }

    /**
     * Checks the state of the game.
     * @return if the board is filled up.
     */
    private boolean gameOver() {
        return board.isFull();
    }

    /**
     * Implements User's turn.
     * @throws ReversiException no possible moves.
     */
    private void userTurn() throws ReversiException {
        ArrayList<Integer> moves = board.possibleMoves(curPlayer);
        if (moves.isEmpty()) {
            throw new ReversiException("No possible moves, the turn passes to the other player");
        }
        board.displayBoard(moves);
        displayMoves(moves);
        int usersTurn = readUserTurn();

        while (usersTurn < 1 || usersTurn > (moves.size() / 2)) {
            usersTurn = readUserTurn();
        }
        int move = (usersTurn - 1) * 2;
        board.changeCells(moves.get(move), moves.get(move + 1), curPlayer);
        System.out.print("\nNow the board looks that way. It's the second player's turn!\n");
        board.displayBoard();
    }

    /**
     * Display the list of possible moves.
     * @param moves the list of possible moves.
     */
    static void displayMoves(ArrayList<Integer> moves) {
        System.out.printf("You have %d possible moves, they are presented bellow:", moves.size() / 2);
        for (int i = 0; i < moves.size(); i += 2) {
            System.out.printf("\n%d. %d %d", i / 2 + 1, moves.get(i) + 1, moves.get(i + 1) + 1);
        }
    }

    /**
     * Read user's choice from the console
     * @return user's choice.
     */
    private Integer readUserTurn() {
        System.out.print("\nMake your move, entering the number of the suitable move\n");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    /**
     * Implements Computer's turn.
     * @throws ReversiException
     */
    private void computerTurn() throws ReversiException {
        ArrayList<Integer> moves = board.possibleMoves(curPlayer);
        if (moves.isEmpty()) {
            throw new ReversiException("No possible moves, the turn passes to the other player");
        }
        double[] values = new double[moves.size() / 2];
        double maxx = -10e9;
        int i;
        for (i = 0; i < moves.size(); i += 2) {
            double sum = AI.evaluate(moves.get(i), moves.get(i + 1), board, curPlayer);
            values[i / 2] = sum;
            maxx = max(maxx, sum);
        }
        for (i = 0; i < values.length; ++i) {
            if (values[i] == maxx) {
                break;
            }
        }
        board.changeCells(moves.get(i * 2), moves.get(i * 2 + 1), curPlayer);
        System.out.printf("\nThe computer made his move on the cell (%d, %d)\n",
                moves.get(i * 2) + 1, moves.get(i * 2 + 1) + 1);
    }

    /**
     * Displays the results of the game.
     */
    private void gameResults() {
        System.out.println("The game is over.\n");
        int white = board.countChips(Board.WHITE_CHIP);
        int black = board.countChips(Board.BLACK_CHIP);
        if (white > black) {
            System.out.printf("The winner is white player. His score is %d.\n", white);
        } else if (black > white) {
            System.out.printf("The winner is black player. His score is %d.\n", black);
        } else {
            System.out.printf("The game has ended with a tie. The score of both players is %d.\n", white);
        }
        if (mode == 0)
            bestScore = max(black, bestScore);
        System.out.println("If you want to play once more, press enter. If you want to exit, type \"exit\"");
        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        if (choice.equals("exit")) {
            System.out.println("Goodbye!");
        } else {
            startMenu();
        }
    }

    /**
     * Displays the best score.
     */
    private void displayBest() {
        if (bestScore == -1) {
            System.out.println("You have not played versus a computer yet, so you best score is 0!");
        } else {
            System.out.printf("Congratulations! Your best score is %d.\n", bestScore);
        }
    }
}
