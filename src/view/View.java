package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import control.Controller;
import miscellaneous.Misc;
import model.Ship;

public class View {
    protected int dimension;
    protected JFrame gameFrame;
    protected MainMenu mainMenu;
    protected OnlineMenu onlineMenu;

    public OnlineMenu getOnlineMenu() {
        return onlineMenu;
    }

    protected Controller controller;
    protected BoardFrame enemyBoardFrame;
    protected BoardFrame playerBoardFrame;
    protected MiddleMenu middleMenu;
    protected MenuBar menuBar;
    protected Color[] colorSet;

    public View() {
        colorSet = new Color[] { Misc.UNSELECTED_COLOR, Misc.HIT_COLOR, Misc.MISSED_COLOR };
        mainMenu = new MainMenu();
    }

    /**
     * This method is called only once when user have a chooses to start the game,
     * it will
     * call other methods to remove the Menu and start to initialize the components
     * of a standard game UI
     */
    public void setGameFrame() {
        initGameComponents();
        disposeMainMenu();
        gameFrame = new JFrame("Battleship");
        gameFrame.setJMenuBar(menuBar);
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setSize(1200, 650);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(enemyBoardFrame, BorderLayout.EAST);
        gameFrame.add(playerBoardFrame, BorderLayout.WEST);
        gameFrame.add(middleMenu, BorderLayout.CENTER);
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
    }

    /**
     * Initializes the game components including menu bar, player board, enemy
     * board, and middle menu.
     */
    public void initGameComponents() {
        menuBar = new MenuBar(controller);

        playerBoardFrame = new BoardFrame(1, "Player 1");
        playerBoardFrame.setBoard(new Board(1, dimension, colorSet, controller));

        enemyBoardFrame = new BoardFrame(2, "Player 2");
        enemyBoardFrame.setBoard(new Board(2, dimension, colorSet, controller));

        middleMenu = new MiddleMenu(controller);
    }

    /**
     * Sets the game board dimension.
     *
     * @param dimension The new dimension of the game board.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Updates the game boards with a new dimension.
     *
     * @param dimension The new dimension of the game board.
     */
    public void setNewBoards(int dimension) {
        this.dimension = dimension;
        playerBoardFrame.setBoard(new Board(1, dimension, colorSet, controller));
        enemyBoardFrame.setBoard(new Board(2, dimension, colorSet, controller));
    }

    /**
     * Resets the game boards and clears the history.
     */
    public void resetBoard() {
        playerBoardFrame.setBoard(new Board(1, dimension, colorSet, controller));
        enemyBoardFrame.setBoard(new Board(2, dimension, colorSet, controller));
        middleMenu.clearHistory();
    }

    /**
     * Sets the coordinates of a destroyed ship on the appropriate board.
     *
     * @param boardId The ID of the board (1 for player, 2 for enemy).
     * @param ship    The ship that was destroyed.
     */
    public void setDestroyedCoordinates(int boardId, Ship ship) {
        if (boardId == 1 && ship.isShipDestroyed()) {
            playerBoardFrame.setDestroyedCoordinates(ship);
        } else if (boardId == 2 && ship.isShipDestroyed()) {
            enemyBoardFrame.setDestroyedCoordinates(ship);
        }
    }

    /**
     * Sets randomized ships on the specified board.
     *
     * @param boardId The ID of the board (1 for player, 2 for enemy).
     * @param ships   The array of ships to be placed on the board.
     */
    public void setRandomizedShips(int boardId, Ship[] ships) {
        BoardFrame tempBoard = boardId == 1 ? playerBoardFrame : enemyBoardFrame;
        enemyBoardFrame.setBoard(new Board(2, dimension, colorSet, controller));
        tempBoard.setRandomizedShips(ships);
    }

    /**
     * Disposes the main menu window.
     */
    public void disposeMainMenu() {
        mainMenu.dispose();
    }

    /**
     * Retrieves the middle menu.
     *
     * @return The middle menu component.
     */
    public MiddleMenu getMiddleMenu() {
        return middleMenu;
    }

    /**
     * Retrieves the main menu.
     *
     * @return The main menu component.
     */
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    /**
     * Displays a dialog indicating that there are no Easter eggs.
     */
    public void showExtrasdialog() {
        String message = "Unfortunately, there are no easter eggs!!!";
        JOptionPane.showMessageDialog(null, message, "Extras", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Retrieves the game board dimension.
     *
     * @return The dimension of the game board.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Updates the specified board with a "hit" indication at the given coordinate.
     *
     * @param boardId        The ID of the board (1 for player, 2 for enemy).
     * @param shotCoordinate The coordinate where the shot was fired.
     */
    public void setHit(int boardId, String shotCoordinate) {
        if (boardId == 1) {
            playerBoardFrame.setHit(shotCoordinate);
        } else {
            enemyBoardFrame.setHit(shotCoordinate);
        }
    }

    /**
     * Updates the specified board with a "missed" indication at the given
     * coordinate.
     *
     * @param boardId        The ID of the board (1 for player, 2 for enemy).
     * @param shotCoordinate The coordinate where the shot was fired.
     */
    public void setMissed(int boardId, String shotCoordinate) {
        if (boardId == 1) {
            playerBoardFrame.setMissed(shotCoordinate);
        } else {
            enemyBoardFrame.setMissed(shotCoordinate);
        }
    }

    /**
     * Updates the health indicator on the specified board.
     *
     * @param boardId     The ID of the board (1 for player, 2 for enemy).
     * @param boardHealth The updated health value for the board.
     */
    public void updateHealth(int boardId, int boardHealth) {
        if (boardId == 1) {
            playerBoardFrame.setHealth(boardHealth);
        } else {
            enemyBoardFrame.setHealth(boardHealth);
        }
    }

    /**
     * Updates the game history with a new shot result on the specified board.
     *
     * @param boardId        The ID of the board (1 for player, 2 for enemy).
     * @param shotCoordinate The coordinate where the shot was fired.
     * @param result         The result of the shot (hit or missed).
     */
    public void updateHistory(int boardId, String shotCoordinate, String result) {
        middleMenu.updateHistory(boardId, shotCoordinate, result);
    }

    /**
     * Updates the game history with a new record.
     *
     * @param record The new record to add to the history.
     */
    public void updateHistory(String record) {
        middleMenu.updateHistory(record);
    }

    /**
     * Enters the ship design mode with the provided generated ships.
     *
     * @param generatedShips The array of ships to be used in design mode.
     */
    public void enterDesginMode(Ship[] generatedShips) {
        middleMenu.getStartButton().setEnabled(false);
        playerBoardFrame.enterDesginMode(generatedShips);
        middleMenu.getStartButton().setEnabled(true);
    }

    /**
     * Retrieves the menu bar component.
     *
     * @return The menu bar component.
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Stops the timer in the middle menu.
     */
    public void stopTimer() {
        middleMenu.stopTimer();
    }

    public void setUnselectedColor() {
        Color color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
        colorSet[0] = color;
        enemyBoardFrame.setUnselectedColor(color);
        playerBoardFrame.setUnselectedColor(color);
    }

    /**
     * Shows a color picker dialog and sets the hit color for both player and enemy
     * boards.
     */
    public void setHitColor() {
        Color color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
        colorSet[1] = color;
        enemyBoardFrame.setHitColor(color);
        playerBoardFrame.setHitColor(color);
    }

    /**
     * Shows a color picker dialog and sets the missed color for both player and
     * enemy boards.
     */
    public void setMissedColor() {
        Color color = JColorChooser.showDialog(null, "Pick a color", Color.BLACK);
        colorSet[2] = color;
        enemyBoardFrame.setMissedColor(color);
        playerBoardFrame.setMissedColor(color);
    }

    /**
     * Checks if the player's board is ready (all ships placed).
     *
     * @return True if the player's board is ready, false otherwise.
     */
    public boolean isReady() {
        return playerBoardFrame.isReady();
    }

    /**
     * Shows the online menu and initializes its controller.
     */
    public void showOnlineMenu() {
        disposeMainMenu();
        onlineMenu = new OnlineMenu();
        onlineMenu.addController(controller);
    }

    /**
     * Sets the main controller for the application.
     *
     * @param controller The main controller to be set.
     */
    public void addController(Controller controller) {
        this.controller = controller;
        mainMenu.addController(controller);
    }

    /**
     * Shows a dialog indicating an address error while joining a socket.
     */
    public void showAddressError() {
        JOptionPane.showMessageDialog(null, "Cannot join socket, check your port!!!", "Error",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a dialog indicating a connection error with the server.
     */
    public void showConnectionError() {
        JOptionPane.showMessageDialog(null, "Cannot communicate with server", "Error",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sets the game mode to online mode.
     */
    public void setOnlineGame() {
        middleMenu.setOnlineGame();
    }

    /**
     * Retrieves the player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerBoardFrame.getPlayerName();
    }

    /**
     * Disposes of the online menu.
     */
    public void disposeOnlineMenu() {
        onlineMenu.dispose();
    }

    /**
     * Updates the player's board based on a coordinate and hit status.
     *
     * @param coordinate The coordinate to update on the player's board.
     * @param hit        True if the coordinate is a hit, false if it's a miss.
     */
    public void updateBoard(String coordinate, boolean hit) {
        if (!coordinate.equals("")) {
            if (hit) {
                playerBoardFrame.setHit(coordinate);
            } else {
                playerBoardFrame.setMissed(coordinate);
            }
        }
    }
}
