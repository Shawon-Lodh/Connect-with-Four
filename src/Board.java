import java.util.ArrayList;

/**
 * A class that describes the playing field
 *
 * @author Dmitriy Stepanov
 */
public class Board {
    private static final int numOfRows = Constants.NUM_OF_ROWS;
    private static final int numOfColumns = Constants.NUM_OF_COLUMNS;
    private static final int inARow = Constants.IN_A_ROW;

    private Move lastMove;
    private int lastPlayer;
    private int winner;
    private final int[][] gameBoard;

    private boolean overflow;
    private boolean gameOver;
    private int turn;

    /**
     * Constructor - creating a new playing field
     *
     * @see Board#Board()
     */
    public Board() {
        this.lastMove = new Move();
        this.lastPlayer = Constants.PLAYER2;
        this.winner = Constants.EMPTY;
        this.gameBoard = new int[7][8];
        this.overflow = false;
        this.gameOver = false;
        this.turn = 0;

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                gameBoard[i][j] = Constants.EMPTY;
            }
        }
    }

    /**
     * Constructor - creating a new playing field
     *
     * @param board - game board
     * @see Board#Board(Board)
     */
    public Board(Board board) {
        lastMove = board.getLastMove();
        lastPlayer = board.getLastPlayer();
        winner = board.getWinner();
        this.overflow = board.isOverflow();
        this.gameOver = board.isGameOver();
        this.turn = board.getTurn();
        int N1 = board.getGameBoard().length;
        int N2 = board.getGameBoard()[0].length;
        this.gameBoard = new int[N1][N2];

        for (int i = 0; i < N1; i++) {
            for (int j = 0; j < N2; j++) {
                this.gameBoard[i][j] = board.getGameBoard()[i][j];
            }
        }
    }

    public void makeMove(int col, int player) {
        try {
            this.lastMove = new Move(getEmptyRowPosition(col), col);
            this.lastPlayer = player;
            this.gameBoard[getEmptyRowPosition(col)][col] = player;
            this.turn++;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Column " + (col + 1) + " is full!");
            setOverflow(true);
        }
    }

    // search across the board
    public boolean canMove(int row, int col) {
        return (row > -1) && (col > -1) && (row <= numOfRows - 1) && (col <= numOfColumns - 1);
    }

    public boolean checkFullColumn(int col) {
        return gameBoard[0][col] != Constants.EMPTY;
    }

    public int getEmptyRowPosition(int col) {
        int rowPosition = -1;

        for (int row = 0; row < numOfRows; row++) {
            if (gameBoard[row][col] == Constants.EMPTY) {
                rowPosition = row;
            }
        }

        return rowPosition;
    }

    public ArrayList<Board> getChildren(int letter) {
        ArrayList<Board> children = new ArrayList<>();

        for (int col = 0; col < numOfColumns; col++) {
            if (!checkFullColumn(col)) {
                Board child = new Board(this);
                child.makeMove(col, letter);
                children.add(child);
            }
        }

        return children;
    }

    // evaluation of the positions of players
    public int evaluate() {
        int player1Score = 0;
        int player2Score = 0;

        if (checkWinState()) {
            if (winner == Constants.PLAYER1)
                player1Score = (int) Math.pow(10, (inARow - 2));
            else if (winner == Constants.PLAYER2)
                player2Score = (int) Math.pow(10, (inARow - 2));
        }

        for (int i = 0; i < inARow - 2; i++) {
            player1Score += countNInARow(i + 2, Constants.PLAYER1) * Math.pow(10, i);
            player2Score += countNInARow(i + 2, Constants.PLAYER2) * Math.pow(10, i);
        }

        return player1Score - player2Score;
    }

    public boolean checkWinState() {
        int times4InARowPlayer1 = countNInARow(inARow, Constants.PLAYER1);

        if (times4InARowPlayer1 > 0) {
            setWinner(Constants.PLAYER1);
            return true;
        }

        int times4InARowPlayer2 = countNInARow(inARow, Constants.PLAYER2);

        if (times4InARowPlayer2 > 0) {
            setWinner(Constants.PLAYER2);
            return true;
        }

        setWinner(Constants.EMPTY);  // nobody wins
        return false;
    }

    public boolean checkForGameOver() {
        if (checkWinState()) {
            return true;
        }
        return checkForDraw();
    }

    public boolean checkForDraw() {
        if (gameOver)
            return false;

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfColumns; col++) {
                if (gameBoard[row][col] == Constants.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    // search for chips located horizontally, vertically or diagonally (2 directions)
    public int countNInARow(int N, int player) {
        int times = 0;

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                if (canMove(i, j + inARow - 1)) {
                    int k = 0;
                    while (k < N && gameBoard[i][j + k] == player) {
                        k++;
                    }

                    if (k == N) {
                        while (k < inARow && (gameBoard[i][j + k] == player ||
                                gameBoard[i][j + k] == Constants.EMPTY)) {
                            k++;
                        }
                        if (k == inARow) times++;
                    }
                }
            }
        }

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                if (canMove(i - inARow + 1, j)) {
                    int k = 0;
                    while (k < N && gameBoard[i - k][j] == player) {
                        k++;
                    }

                    if (k == inARow) {
                        while (k < inARow && (gameBoard[i - k][j] == player ||
                                gameBoard[i - k][j] == Constants.EMPTY)) {
                            k++;
                        }
                        if (k == inARow) times++;
                    }
                }
            }
        }

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                if (canMove(i + inARow - 1, j + inARow - 1)) {
                    int k = 0;
                    while (k < N && gameBoard[i + k][j + k] == player) {
                        k++;
                    }

                    if (k == inARow) {
                        while (k < inARow && (gameBoard[i + k][j + k] == player ||
                                gameBoard[i + k][j + k] == Constants.EMPTY)) {
                            k++;
                        }
                        if (k == inARow) times++;
                    }
                }
            }
        }

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                if (canMove(i - inARow + 1, j + inARow - 1)) {
                    int k = 0;
                    while (k < N && gameBoard[i - k][j + k] == player) {
                        k++;
                    }

                    if (k == inARow) {
                        while (k < inARow && (gameBoard[i - k][j + k] == player ||
                                gameBoard[i - k][j + k] == Constants.EMPTY)) {
                            k++;
                        }
                        if (k == inARow) times++;
                    }
                }
            }
        }
        return times;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setColumn(lastMove.getColumn());
        this.lastMove.setValue(lastMove.getValue());
    }

    public int getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int[][] gameBoard) {
        for (int i = 0; i < numOfRows; i++) {
            System.arraycopy(gameBoard[i], 0, this.gameBoard[i], 0, numOfColumns);
        }
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean isGameOver) {
        this.gameOver = isGameOver;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }
}