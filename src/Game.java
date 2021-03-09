import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

/**
 * A class that describes the game's graphical interface
 *
 * @author Dmitriy Stepanov
 */
public class Game {
    private static final int numOfRows = Constants.NUM_OF_ROWS;
    private static final int numOfColumns = Constants.NUM_OF_COLUMNS;
    private static final int inARow = Constants.IN_A_ROW;

    private static Board board;
    private static JFrame game;
    private static JLayeredPane boardCheckers;
    private static JButton[] nums;
    private static JLabel message;
    private static AI PC;

    private static final Stack<Board> undoBoards = new Stack<>();
    private static final Stack<JLabel> undoChecker = new Stack<>();
    private static final Stack<Board> redoBoards = new Stack<>();
    private static final Stack<JLabel> redoChecker = new Stack<>();

    private static JMenuItem undo;
    private static JMenuItem redo;

    private static final String TITLE_HELP = "Help";
    private static final String TXTHELP = "<html><center><H2>Help</H2></center><center>A game for " +
            "two, in which the players first choose the color of the chips, and then take turns " +
            "dropping the chips into the cells of the vertical board.<br> " +
            "The goal of the game is to place four chips of your own color horizontally, vertically, " +
            "or diagonally before the opponent in a row. There are variants of the game with a " +
            "field of different sizes, with chips in the form of disks or balls.</center></html>";

    private static final String TITLE_ABOUT = "About";
    private static final String TXTABOUT = "<html><center><H2>About</H2></center><center>American " +
            "Milton Bradley patented this game in 1974 under the name Connect Four. Robert Bell " +
            "described it under the name Four balls and dated it to the reign of King Edward " +
            "(1901-1910). At that time, the set for the game was a wooden box from a bottle of " +
            "cognac Remy Martin with a hinged lid, on which eight vertical guides and a " +
            "sliding bottom were placed in a row, and balls made of beech and mahogany were used as " +
            "chips. Bell noted that, judging by the workmanship, this is clearly a copy from an " +
            "older set. There is evidence that the great navigator James Cook adored her so much that " +
            "he always took her with him on a voyage. He and his companions – the naturalist Joseph " +
            "Banks and the botanist Daniel Solander – could spend hours playing it during long sea " +
            "crossings, for which the sailors, not without malice, called the box with " +
            "balls Captain's wife («Captain's Mistress»).</center></html>";

    public static Image windowIcon;
    private static JFrame moreInform;

    /**
     * Constructor - creating a new game
     *
     * @see Game#Game()
     */
    public Game() {
        nums = new JButton[numOfColumns];
        for (int i = 0; i < numOfColumns; i++) {
            nums[i] = new JButton(i + 1 + "");
            nums[i].setBackground(Color.lightGray);
            nums[i].setFocusable(false);
        }
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Game.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static URL load(String path) {
        URL input = Game.class.getResource(path);
        if (input == null) {
            input = Game.class.getResource("/" + path);
        }
        return input;
    }

    private static void addMenu() {
        windowIcon = loadImage("/connectfour.png");
        Game.game.setIconImage(windowIcon);

        JMenuBar gameMenu = new JMenuBar();
        JMenu game = new JMenu("Game");
        gameMenu.add(game);

        JMenuItem newgame = new JMenuItem("New Game");
        KeyStroke ctrlNKeyStroke = KeyStroke.getKeyStroke("control N");
        newgame.setAccelerator(ctrlNKeyStroke);
        game.add(newgame);
        newgame.addActionListener(e -> createNewGame());

        undo = new JMenuItem("Undo");
        KeyStroke ctrlUKeyStroke = KeyStroke.getKeyStroke("control U");
        undo.setAccelerator(ctrlUKeyStroke);
        game.add(undo);
        undo.addActionListener(e -> undo());

        redo = new JMenuItem("Redo");
        KeyStroke ctrlRKeyStroke = KeyStroke.getKeyStroke("control R");
        redo.setAccelerator(ctrlRKeyStroke);
        game.add(redo);
        redo.addActionListener(e -> redo());

        JMenuItem settings = new JMenuItem("Settings");
        KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
        settings.setAccelerator(ctrlSKeyStroke);
        game.add(settings);
        settings.addActionListener(e -> {
            Settings gamesettings = new Settings(windowIcon);
            gamesettings.setVisible(true);
        });

        JMenuItem helpGame = new JMenuItem("Help");
        KeyStroke ctrlHKeyStroke = KeyStroke.getKeyStroke("control H");
        helpGame.setAccelerator(ctrlHKeyStroke);
        game.add(helpGame);
        helpGame.addActionListener(e -> {
            informGame(TITLE_HELP, TXTHELP, 410, 370);
            addPicture(moreInform);
        });

        JMenuItem aboutGame = new JMenuItem("About");
        KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke("control A");
        aboutGame.setAccelerator(ctrlAKeyStroke);
        game.add(aboutGame);
        game.addSeparator();
        aboutGame.addActionListener(e -> informGame(TITLE_ABOUT, TXTABOUT, 370, 420));

        JMenuItem exitGame = new JMenuItem("Exit");
        KeyStroke ctrlQKeyStroke = KeyStroke.getKeyStroke("control Q");
        exitGame.setAccelerator(ctrlQKeyStroke);
        game.add(exitGame);
        exitGame.addActionListener(e -> System.exit(0));

        Game.game.setJMenuBar(gameMenu);
        Game.game.setVisible(true);
    }

    // Creating a form template for help and about the game
    private static void informGame(String name, String text, int width, int height) {
        moreInform = new JFrame(name);
        JLabel txtMessage = new JLabel(text);
        txtMessage.setHorizontalAlignment(SwingConstants.CENTER);
        txtMessage.setVerticalAlignment(SwingConstants.TOP);
        txtMessage.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        moreInform.setIconImage(windowIcon);
        moreInform.setSize(width, height);
        moreInform.setLocationRelativeTo(null);
        moreInform.setVisible(true);
        moreInform.add(txtMessage);
    }

    private static void addPicture(JFrame MoreInform) {
        JLabel jlImage = new JLabel(new ImageIcon(loadImage("/connect4.png")));
        MoreInform.add(jlImage);
        MoreInform.setLayout(new GridLayout(2, 1));
    }

    public static JLayeredPane createLayeredBoard() {
        boardCheckers = new JLayeredPane();
        boardCheckers.setPreferredSize(new Dimension(570, 525));
        ImageIcon imageBoard = new ImageIcon(load("Board.png"));
        JLabel imageBoardLabel = new JLabel(imageBoard);
        imageBoardLabel.setBounds(20, 20, imageBoard.getIconWidth(), imageBoard.getIconHeight());
        boardCheckers.add(imageBoardLabel, 0, 1);
        return boardCheckers;
    }

    private static void undo() {
        if (!undoBoards.isEmpty()) {
            if (GameParameters.gameMode == Constants.PLAYER_VS_PLAYER) {
                try {
                    board.setGameOver(false);
                    setAllButtonsEnabled(true);
                    if (game.getKeyListeners().length == 0) {
                        game.addKeyListener(gameKey);
                    }
                    JLabel previousChecker = undoChecker.pop();
                    redoBoards.push(new Board(board));
                    redoChecker.push(previousChecker);
                    board = undoBoards.pop();
                    boardCheckers.remove(previousChecker);
                    message.setText("Turn: " + board.getTurn());
                    game.paint(game.getGraphics());
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("No move has been made yet!");
                    System.err.flush();
                }
            } else if (GameParameters.gameMode == Constants.PLAYER_VS_AI) {
                try {
                    board.setGameOver(false);
                    setAllButtonsEnabled(true);
                    if (game.getKeyListeners().length == 0)
                        game.addKeyListener(gameKey);
                    JLabel previousAiChecker = undoChecker.pop();
                    JLabel previousHumanChecker = undoChecker.pop();
                    redoBoards.push(new Board(board));
                    redoChecker.push(previousAiChecker);
                    redoChecker.push(previousHumanChecker);
                    board = undoBoards.pop();
                    boardCheckers.remove(previousAiChecker);
                    boardCheckers.remove(previousHumanChecker);
                    message.setText("Turn: " + board.getTurn());
                    game.paint(game.getGraphics());
                } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
                    System.err.println("No move has been made yet!");
                    System.err.flush();
                }
            }
            if (undoBoards.isEmpty()) {
                undo.setEnabled(false);
            }
            redo.setEnabled(true);
        }
    }

    private static void redo() {
        if (!redoBoards.isEmpty()) {
            if (GameParameters.gameMode == Constants.PLAYER_VS_PLAYER) {
                try {
                    board.setGameOver(false);
                    setAllButtonsEnabled(true);
                    if (game.getKeyListeners().length == 0) {
                        game.addKeyListener(gameKey);
                    }
                    JLabel redoCheckerLabel = redoChecker.pop();
                    undoBoards.push(new Board(board));
                    undoChecker.push(redoCheckerLabel);
                    board = new Board(redoBoards.pop());
                    boardCheckers.add(redoCheckerLabel, 0, 0);
                    message.setText("Turn: " + board.getTurn());
                    game.paint(game.getGraphics());
                    boolean isGameOver = board.checkForGameOver();
                    if (isGameOver) {
                        gameOver();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("There is no move to redo!");
                    System.err.flush();
                }
            } else if (GameParameters.gameMode == Constants.PLAYER_VS_AI) {
                try {
                    board.setGameOver(false);
                    setAllButtonsEnabled(true);
                    if (game.getKeyListeners().length == 0)
                        game.addKeyListener(gameKey);
                    JLabel redoAiChecker = redoChecker.pop();
                    JLabel redoHumanChecker = redoChecker.pop();
                    undoBoards.push(new Board(board));
                    undoChecker.push(redoAiChecker);
                    undoChecker.push(redoHumanChecker);
                    board = new Board(redoBoards.pop());
                    boardCheckers.add(redoAiChecker, 0, 0);
                    boardCheckers.add(redoHumanChecker, 0, 0);
                    message.setText("Turn: " + board.getTurn());
                    game.paint(game.getGraphics());
                    boolean isGameOver = board.checkForGameOver();
                    if (isGameOver) {
                        gameOver();
                    }
                } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
                    System.err.println("There is no move to redo!");
                    System.err.flush();
                }
            }
            if (redoBoards.isEmpty())
                redo.setEnabled(false);
            undo.setEnabled(true);
        }
    }

    public static void createNewGame() {
        if (GameParameters.gameMode != Constants.AI_VS_AI) {
            setAllButtonsEnabled(true);
        }

        board = new Board();
        undoBoards.clear();
        undoChecker.clear();
        redoBoards.clear();
        redoChecker.clear();

        if (game != null) game.dispose();
        game = new JFrame("Connect Four");
        centerWindow(game, 570, 525);
        Component compMainWindowContents = createContentComponents();
        game.getContentPane().add(compMainWindowContents, BorderLayout.CENTER);

        if (game.getKeyListeners().length == 0) {
            game.addKeyListener(gameKey);
        }

        game.setFocusable(true);
        game.pack();

        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        game.add(tools, BorderLayout.PAGE_END);
        message = new JLabel("Turn: " + board.getTurn());
        tools.add(message);
        addMenu();

        if (GameParameters.gameMode == Constants.PLAYER_VS_AI) {
            PC = new AI(GameParameters.levelAI1, Constants.PLAYER2);
        } else if (GameParameters.gameMode == Constants.AI_VS_AI) {
            setAllButtonsEnabled(false);

            AI PC1 = new AI(GameParameters.levelAI1, Constants.PLAYER1);
            AI PC2 = new AI(GameParameters.levelAI2, Constants.PLAYER2);

            while (!board.isGameOver()) {
                aiMove(PC1);
                if (!board.isGameOver()) {
                    aiMove(PC2);
                }
            }
        }
    }

    public static void centerWindow(Window frame, int width, int height) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dimension.getWidth() - frame.getWidth() - width) / 2;
        int y = (int) (dimension.getHeight() - frame.getHeight() - height) / 2;
        frame.setLocation(x, y);
    }

    // finding someone of the players is playing next, and makes a move on the Board
    public static void makeMove(int col) {
        board.setOverflow(false);
        int previousRow = board.getLastMove().getRow();
        int previousCol = board.getLastMove().getColumn();
        int previousLetter = board.getLastPlayer();

        if (board.getLastPlayer() == Constants.PLAYER2) {
            board.makeMove(col, Constants.PLAYER1);
        } else {
            board.makeMove(col, Constants.PLAYER2);
        }

        if (board.isOverflow()) {
            board.getLastMove().setRow(previousRow);
            board.getLastMove().setColumn(previousCol);
            board.setLastPlayer(previousLetter);
            undoBoards.pop();
        }
    }

    public static void placeChecker(int color, int row, int col) {
        String colorString = GameParameters.getColorNameByNumber(color);
        ImageIcon checkerIcon = new ImageIcon(load("checkers/" + colorString + ".png"));
        JLabel checker = new JLabel(checkerIcon);
        checker.setBounds(27 + (75 * col), 27 + (75 * row),
                checkerIcon.getIconWidth(), checkerIcon.getIconHeight());
        boardCheckers.add(checker, 0, 0);
        undoChecker.push(checker);

        try {
            if (GameParameters.gameMode == Constants.AI_VS_AI) {
                Thread.sleep(200);
                game.paint(game.getGraphics());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean game() {
        message.setText("Turn: " + board.getTurn());
        int row = board.getLastMove().getRow();
        int col = board.getLastMove().getColumn();
        int currentPlayer = board.getLastPlayer();

        if (currentPlayer == Constants.PLAYER1) {
            placeChecker(GameParameters.player1Color, row, col);
        }

        if (currentPlayer == Constants.PLAYER2) {
            placeChecker(GameParameters.player2Color, row, col);
        }

        boolean isGameOver = board.checkForGameOver();

        if (isGameOver) {
            gameOver();
        }

        undo.setEnabled(true);
        redoBoards.clear();
        redoChecker.clear();
        redo.setEnabled(false);
        return isGameOver;
    }

    public static void aiMove(AI ai) {
        Move aiMove = ai.miniMaxAlphaBeta(board);
        board.makeMove(aiMove.getColumn(), ai.getAiPlayer());
        game();
    }

    public static void setAllButtonsEnabled(boolean b) {
        if (b) {
            for (int i = 0; i < nums.length; i++) {
                JButton button = nums[i];
                int column = i;

                if (button.getActionListeners().length == 0) {
                    button.addActionListener(e -> {
                        undoBoards.push(new Board(board));
                        makeMove(column);

                        if (!board.isOverflow()) {
                            boolean isGameOver = game();
                            if (GameParameters.gameMode == Constants.PLAYER_VS_AI && !isGameOver) {
                                aiMove(PC);
                            }
                        }

                        game.requestFocusInWindow();
                    });
                }
            }
        } else {
            for (JButton num : nums) {
                for (ActionListener NumListener : num.getActionListeners()) {
                    num.removeActionListener(NumListener);
                }
            }
        }
    }

    public static Component createContentComponents() {
        JPanel boardNumbers = new JPanel();
        boardNumbers.setLayout(new GridLayout(1, numOfColumns, numOfRows, 4));
        boardNumbers.setBorder(BorderFactory.createEmptyBorder(2, 22, 2, 22));

        for (JButton num : nums) {
            boardNumbers.add(num);
        }

        boardCheckers = createLayeredBoard();
        JPanel gameField = new JPanel();
        gameField.setLayout(new BorderLayout());
        gameField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gameField.add(boardNumbers, BorderLayout.NORTH);
        gameField.add(boardCheckers, BorderLayout.CENTER);
        game.setResizable(false);
        return gameField;
    }

    public static void gameOver() {
        board.setGameOver(true);
        int choice = 0;
        ImageIcon win = new ImageIcon(loadImage("/win.png"));

        if (board.getWinner() == Constants.PLAYER1) {
            switch (GameParameters.gameMode) {
                case Constants.PLAYER_VS_AI:
                    choice = JOptionPane.showConfirmDialog(null,
                            "You win! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
                case Constants.PLAYER_VS_PLAYER:
                    choice = JOptionPane.showConfirmDialog(null,
                            "Player 1 wins! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
                case Constants.AI_VS_AI:
                    choice = JOptionPane.showConfirmDialog(null,
                            "AI 1 wins! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
            }
        } else if (board.getWinner() == Constants.PLAYER2) {
            switch (GameParameters.gameMode) {
                case Constants.PLAYER_VS_AI:
                    choice = JOptionPane.showConfirmDialog(null,
                            "Computer AI wins! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
                case Constants.PLAYER_VS_PLAYER:
                    choice = JOptionPane.showConfirmDialog(null,
                            "Player 2 wins! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
                case Constants.AI_VS_AI:
                    choice = JOptionPane.showConfirmDialog(null,
                            "AI 2 wins! Start a new game?",
                            "Victory", JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, win);
                    break;
            }
        } else if (board.checkForDraw()) {
            ImageIcon drawn = new ImageIcon(loadImage("/drawn.png"));
            choice = JOptionPane.showConfirmDialog(null,
                    "It's a draw! Start a new game?",
                    "Drawn game", JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, drawn);
        }
        setAllButtonsEnabled(false);

        for (KeyListener GameKey : game.getKeyListeners()) {
            game.removeKeyListener(GameKey);
        }

        if (choice == JOptionPane.YES_OPTION) {
            createNewGame();
        }
    }

    public static KeyAdapter gameKey = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            String keyText = KeyEvent.getKeyText(e.getKeyCode());

            for (int i = 0; i < Constants.NUM_OF_COLUMNS; i++) {
                if (keyText.equals(i + 1 + "")) {
                    undoBoards.push(new Board(board));
                    makeMove(i);

                    if (!board.isOverflow()) {
                        boolean isGameOver = game();
                        if (GameParameters.gameMode == Constants.PLAYER_VS_AI && !isGameOver) {
                            aiMove(PC);
                        }
                    }
                    break;
                }
            }

            if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) &&
                    (e.getKeyCode() == KeyEvent.VK_U)) {
                undo();
            } else if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) &&
                    (e.getKeyCode() == KeyEvent.VK_R)) {
                redo();
            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game connect4 = new Game();
            createNewGame();
        });
    }
}