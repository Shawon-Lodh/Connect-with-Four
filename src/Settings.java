import javax.swing.*;
import java.awt.*;

/**
 * A class that describes the game settings
 *
 * @author Dmitriy Stepanov
 */
public class Settings extends JFrame {
    private final JComboBox<String> game_Mode;
    private final JComboBox<Integer> levelAI1;
    private final JComboBox<Integer> levelAI2;
    private final JComboBox<String> player1_Color;
    private final JComboBox<String> player2_Color;
    private final JButton apply;
    private final JButton cancel;

    /**
     * Constructor - creating a new settings window
     *
     * @param windowIcon - icon to the application
     * @see Settings#Settings(Image)
     */
    public Settings(Image windowIcon) {
        super("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(400, 280);
        setIconImage(windowIcon);
        setLocationRelativeTo(null);
        setResizable(false);

        int selectedMode = GameParameters.gameMode;
        int level1 = GameParameters.levelAI1 - 1;
        int level2 = GameParameters.levelAI2 - 1;
        int selectedPlayer1Color = GameParameters.player1Color;
        int selectedPlayer2Color = GameParameters.player2Color;

        JLabel gameModeLabel = new JLabel("Game mode");
        gameModeLabel.setBounds(25, 25, 175, 20);
        add(gameModeLabel);

        JLabel levelAI1Label = new JLabel("AI Level");
        levelAI1Label.setBounds(25, 55, 175, 20);
        add(levelAI1Label);

        JLabel levelAI2Label = new JLabel("AI Level (AI vs AI)");
        levelAI2Label.setBounds(25, 85, 175, 20);
        add(levelAI2Label);

        JLabel player1ColorLabel = new JLabel("Player 1 color");
        player1ColorLabel.setBounds(25, 115, 175, 20);
        add(player1ColorLabel);

        JLabel player2ColorLabel = new JLabel("Player 2 color");
        player2ColorLabel.setBounds(25, 145, 175, 20);
        add(player2ColorLabel);

        game_Mode = new JComboBox<>();
        String[] modes = {"Player vs AI", "Player vs Player", "AI vs AI"};
        addStringToList(game_Mode, modes);
        chooseGameMode(game_Mode, selectedMode);
        add(game_Mode);
        game_Mode.setBounds(195, 25, 160, 20);

        levelAI1 = new JComboBox<>();
        int[] numbers = {1, 2, 3, 4, 5, 6, 7};
        addNumberToList(levelAI1, numbers);
        levelAI1.setSelectedIndex(level1);
        add(levelAI1);
        levelAI1.setBounds(195, 55, 160, 20);

        levelAI2 = new JComboBox<>();
        addNumberToList(levelAI2, numbers);
        levelAI2.setSelectedIndex(level2);
        add(levelAI2);
        levelAI2.setBounds(195, 85, 160, 20);

        player1_Color = new JComboBox<>();
        String[] colors = {"Red", "Yellow", "Black", "Green", "Orange", "Purple"};
        addStringToList(player1_Color, colors);
        choosePlayerColor(player1_Color, selectedPlayer1Color);
        add(player1_Color);
        player1_Color.setBounds(195, 115, 160, 20);

        player2_Color = new JComboBox<>();
        addStringToList(player2_Color, colors);
        choosePlayerColor(player2_Color, selectedPlayer2Color);
        add(player2_Color);
        player2_Color.setBounds(195, 145, 160, 20);

        apply = new JButton("Apply");
        add(apply);
        apply.setBounds(65, 200, 100, 30);
        apply.setBackground(Color.lightGray);
        apply.addActionListener(e -> {
            if (e.getSource() == apply) {
                try {
                    int gameMode = game_Mode.getSelectedIndex() + 1;
                    int maxDepth1 = (int) levelAI1.getSelectedItem();
                    int maxDepth2 = (int) levelAI2.getSelectedItem();
                    int player1Color = player1_Color.getSelectedIndex() + 1;
                    int player2Color = player2_Color.getSelectedIndex() + 1;

                    if (player1Color == player2Color) {
                        ImageIcon warn = new ImageIcon(Game.loadImage("/warn.png"));
                        JOptionPane.showMessageDialog(null,
                                "Player 1 and Player 2 cannot have the same color of checkers!",
                                "ERROR", JOptionPane.ERROR_MESSAGE, warn);
                        return;
                    }

                    GameParameters.gameMode = gameMode;
                    GameParameters.levelAI1 = maxDepth1;
                    GameParameters.levelAI2 = maxDepth2;
                    GameParameters.player1Color = player1Color;
                    GameParameters.player2Color = player2Color;

                    ImageIcon startgame = new ImageIcon(Game.loadImage("/start.png"));
                    JOptionPane.showMessageDialog(null,
                            "Game settings have been changed.\nThe changes will be applied " +
                                    "in the next new game.",
                            "Notice", JOptionPane.INFORMATION_MESSAGE, startgame);
                    dispose();
                } catch (Exception err) {
                    System.err.println("ERROR : " + err.getMessage());
                }
            }
        });

        cancel = new JButton("Cancel");
        add(cancel);
        cancel.setBounds(175, 200, 100, 30);
        cancel.setBackground(Color.lightGray);
        cancel.addActionListener(e -> {
            if (e.getSource() == cancel) {
                dispose();
            }
        });
    }

    private void addNumberToList(JComboBox<Integer> number, int[] property) {
        for (int num : property) {
            number.addItem(num);
        }
    }

    private void addStringToList(JComboBox<String> word, String[] property) {
        for (String str : property) {
            word.addItem(str);
        }
    }

    private void chooseGameMode(JComboBox<String> game_mode, int selectedMode) {
        switch (selectedMode) {
            case Constants.PLAYER_VS_AI:
                game_mode.setSelectedIndex(Constants.PLAYER_VS_AI - 1);
                break;
            case Constants.PLAYER_VS_PLAYER:
                game_mode.setSelectedIndex(Constants.PLAYER_VS_PLAYER - 1);
                break;
            case Constants.AI_VS_AI:
                game_mode.setSelectedIndex(Constants.AI_VS_AI - 1);
                break;
        }
    }

    private void choosePlayerColor(JComboBox<String> player_color, int selectedPlayerColor) {
        switch (selectedPlayerColor) {
            case Constants.RED:
                player_color.setSelectedIndex(Constants.RED - 1);
                break;
            case Constants.YELLOW:
                player_color.setSelectedIndex(Constants.YELLOW - 1);
                break;
            case Constants.BLACK:
                player_color.setSelectedIndex(Constants.BLACK - 1);
                break;
            case Constants.GREEN:
                player_color.setSelectedIndex(Constants.GREEN - 1);
                break;
            case Constants.ORANGE:
                player_color.setSelectedIndex(Constants.ORANGE - 1);
                break;
            case Constants.PURPLE:
                player_color.setSelectedIndex(Constants.PURPLE - 1);
                break;
        }
    }
}
