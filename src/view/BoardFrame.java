package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import miscellaneous.Misc;
import model.Ship;

/**
 * The BoardFrame class represents a panel that contains the game board,
 * progress bar, and player label.
 */
public class BoardFrame extends JPanel {

    private JPanel gaugePanel;
    private JProgressBar progressBar;
    private JLabel progressLabel;
    private JLabel playerLabel;
    private Board board;
    private int id;

    /**
     * Constructs a BoardFrame object with the specified player ID and player name.
     *
     * @param id         The ID of the player (1 or 2).
     * @param playerName The name of the player.
     */
    public BoardFrame(int id, String playerName) {
        this.id = id;
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setPreferredSize(new Dimension(480, 600));
        setVisible(true);
        setGaugePanel(playerName);
        add(gaugePanel);
        if (id == 1) {
            setBackground(Misc.PLAYER_BOARDFRAME_COLOR);
        } else {
            setBackground(Misc.ENEMY_BOARDFRAME_COLOR);
        }
    }

    /**
     * Initializes the gauge panel with the given player name.
     *
     * @param playerName The name of the player associated with the gauge panel.
     */
    private void setGaugePanel(String playerName) {
        gaugePanel = new JPanel();
        setPreferredSize(new Dimension(480, 200));
        gaugePanel.setOpaque(false);
        initProgressBar();
        setPlayerLabel(playerName);
        gaugePanel.add(playerLabel);
        gaugePanel.add(progressBar);
    }

    /**
     * Initializes the progress bar with default settings.
     */
    private void initProgressBar() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(360, 40));
        progressBar.setValue(0);

        progressLabel = new JLabel("0%");
        progressLabel.setForeground(Color.white);
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Sets the label for the player's name.
     *
     * @param playerName The name of the player to be displayed on the label.
     */
    private void setPlayerLabel(String playerName) {
        playerLabel = new JLabel(playerName, JLabel.CENTER);
        playerLabel.setPreferredSize(new Dimension(100, 80));
        playerLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        playerLabel.setForeground(Color.white);
    }

    /**
     * Sets the game board to be displayed within this gauge panel.
     *
     * @param board The game board to be displayed.
     */
    public void setBoard(Board board) {
        this.board = board;
        if (getComponent(0) instanceof Board) {
            remove(0);
        }
        add(board, 0);
        setHealth(0);
        revalidate();
        repaint();
    }

    /**
     * Sets the health value of the board's progress bar.
     *
     * @param health The health value to set.
     */
    public void setHealth(int health) {
        progressBar.setValue(health);
    }

    /**
     * Gets the game board associated with this GaugePanel.
     *
     * @return The game board displayed within this GaugePanel.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the randomized ships on the game board and updates health to 100%.
     *
     * @param ships The array of ships to be placed on the board.
     */
    public void setRandomizedShips(Ship[] ships) {
        board.setRandomizedShips(ships);
        setHealth(100);
    }

    /**
     * Sets the coordinates of a destroyed ship on the game board.
     *
     * @param ship The ship that was destroyed.
     */
    public void setDestroyedCoordinates(Ship ship) {
        board.setDestroyedCoordinates(ship);
    }

    /**
     * Marks a hit at the specified coordinate on the game board.
     *
     * @param coordinateName The name of the coordinate where the hit occurred.
     */
    public void setHit(String coordinateName) {
        board.setHit(coordinateName);
    }

    /**
     * Marks a miss at the specified coordinate on the game board.
     *
     * @param coordinateName The name of the coordinate where the miss occurred.
     */
    public void setMissed(String coordinateName) {
        board.setMissed(coordinateName);
    }

    /**
     * Enters design mode on the game board with the provided generated ships.
     * Updates health to 100%.
     *
     * @param generatedShips The array of ships generated for design mode.
     */
    public void enterDesginMode(Ship[] generatedShips) {
        board.enterDesginMode(generatedShips);
        setHealth(100);
    }

    /**
     * Sets the color for unselected cells on the game board.
     *
     * @param color The color to be used for unselected cells.
     */
    public void setUnselectedColor(Color color) {
        board.setUnselectedColor(color);
    }

    /**
     * Sets the color for hit cells on the game board.
     *
     * @param color The color to be used for hit cells.
     */
    public void setHitColor(Color color) {
        board.setHitColor(color);
    }

    /**
     * Sets the color for missed cells on the game board.
     *
     * @param color The color to be used for missed cells.
     */
    public void setMissedColor(Color color) {
        board.setMissedColor(color);
    }

    /**
     * Checks if the game board is ready for play (all ships placed).
     *
     * @return True if the game board is ready for play, false otherwise.
     */
    public boolean isReady() {
        return board.isReady();
    }

    /**
     * Gets the player name associated with this GaugePanel.
     *
     * @return The name of the player displayed on the label.
     */
    public String getPlayerName() {
        return playerLabel.getText();
    }

}
