package control;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import miscellaneous.Misc;
import model.*;
import view.*;

public class Controller implements ActionListener {
    private int id;

    private View view;
    private Model model;
    private MiddleMenu middleMenu;
    private OnlineMenu onlineMenu;
    private MainMenu mainMenu;
    private MenuBar menuBar;
    private boolean canPlay;
    private boolean isPlaying;

    private ArrayList<String> enemyShotCoordinates;
    private ArrayList<String> playerShotCoordinates;
    private int dimension;

    private int onlineId;

    private int serverPort;
    private String serverUrl;
    private boolean isOnlineMode;

    /**
     * Constructs a new Controller object for managing the game logic.
     *
     * @param view  The View component of the game.
     * @param model The Model component of the game.
     */
    public Controller(View view, Model model) {
        id = 1; // represent the controller (player id)
        dimension = 1;

        this.view = view;
        view.addController(this);
        view.setDimension(dimension);

        this.model = model;
        model.setDimension(dimension);

        mainMenu = view.getMainMenu();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (isChildOfMainMenu(source)) {
            mainMenuActionPerformed(event);

        } else if (isChildOfMiddleMenu(source)) {
            middleMenuActionPerformed(event);

        } else if (isChildOfMenuBar(source)) {
            menuBarActionPerformed(event);

        } else if (isChildOfOnlineMenu(source)) {
            onlineMenuActionPerformed(event);

        } else if (source instanceof Coordinate && isOnlineMode) {

            String tempCoordinate = ((Coordinate) event.getSource()).getName();
            try {
                shootOnline(tempCoordinate);
            } catch (Exception e) {
                System.out.println(e);
                view.showConnectionError();

            }

        } else if (source instanceof Coordinate) {
            if (canPlay && isPlaying) {
                coordinateActionPerfomed(event);
            }
        }

    }

    // ==================== FOR MAIN-MENU ====================

    /**
     * Checks if the given source is a child component of the main menu.
     *
     * @param source The source object to be checked.
     * @return True if the source is a child of the main menu, otherwise false.
     */
    public boolean isChildOfMainMenu(Object source) {
        if (source instanceof Component) {
            Component component = (Component) source;
            return SwingUtilities.isDescendingFrom(component, mainMenu);
        }
        return false;
    }

    /**
     * Handles the actions triggered from the main menu.
     *
     * @param event The ActionEvent associated with the action.
     */
    public void mainMenuActionPerformed(ActionEvent event) {
        if (event.getSource() == mainMenu.getSingleButton()) {
            view.setGameFrame();
            menuBar = view.getMenuBar();
            middleMenu = view.getMiddleMenu();
        } else if (event.getSource() == mainMenu.getMultiButton()) {
            view.showOnlineMenu();
            onlineMenu = view.getOnlineMenu();
        } else if (event.getSource() == mainMenu.getExtrasButton()) {
            view.showExtrasdialog();
        }
    }

    /**
     * Checks if the given source is a child component of the middle menu.
     *
     * @param source The source object to be checked.
     * @return True if the source is a child of the middle menu, otherwise false.
     */
    public boolean isChildOfMiddleMenu(Object source) {
        if (source instanceof Component) {
            Component component = (Component) source;
            return SwingUtilities.isDescendingFrom(component, middleMenu);
        }
        return false;
    }

    /**
     * Handles the actions triggered from the middle menu.
     *
     * @param event The ActionEvent associated with the action.
     */
    public void middleMenuActionPerformed(ActionEvent event) {
        if (event.getSource() == middleMenu.getLanguageBox()) {
            middleMenu.setLanguage();
        } else if (event.getSource() == middleMenu.getDimensionBox()) {
            handleDimensionChange();
        } else if (event.getSource() == middleMenu.getRandButton()) {
            handleRandomizeShips();
        } else if (event.getSource() == middleMenu.getStartButton()) {
            handleStartGame();
        } else if (event.getSource() == middleMenu.getResetButton()) {
            handleResetGame();
        } else if (event.getSource() == middleMenu.getDesignButton()) {
            handleDesignGame();
        }
    }

    /**
     * Handles the change in dimension selection.
     */
    public void handleDimensionChange() {
        dimension = Integer.parseInt((String) middleMenu.getDimensionBox().getSelectedItem());
        view.setNewBoards(dimension);
        model.setDimension(dimension);
    }

    /**
     * Handles the randomization of ships for both players.
     */
    public void handleRandomizeShips() {
        view.resetBoard();

        model.randomizeShip(this.id);
        view.setRandomizedShips(this.id, model.getRandomizedShips(this.id));
        playerShotCoordinates = new ArrayList<>(dimension * 2);

        model.randomizeShip(2);
        view.setRandomizedShips(2, model.getRandomizedShips(2));
        enemyShotCoordinates = new ArrayList<>(dimension * 2);

        canPlay = true;
    }

    /**
     * Handles starting the game when the player is ready.
     */
    public void handleStartGame() {
        canPlay = view.isReady();
        if (canPlay) {
            middleMenu.setStartState();
            isPlaying = true;
        }
    }

    /**
     * Handles resetting the game to its initial state.
     */
    public void handleResetGame() {
        middleMenu.setResetState();
        view.resetBoard();
        canPlay = isPlaying = false;
    }

    /**
     * Handles entering design mode for ship placement.
     */
    public void handleDesignGame() {
        view.resetBoard();

        model.randomizeShip(2);

        view.setRandomizedShips(2, model.getRandomizedShips(2));

        playerShotCoordinates = new ArrayList<>(dimension * 2);
        enemyShotCoordinates = new ArrayList<>(dimension * 2);

        Ship[] generatedShips = model.getGeneratedShips(this.id);

        view.enterDesginMode(generatedShips);
    }

    /**
     * Checks if the given source is a child component of the menu bar.
     *
     * @param source The source object to be checked.
     * @return True if the source is a child of the menu bar, otherwise false.
     */
    public boolean isChildOfMenuBar(Object source) {
        if (source instanceof Component) {
            return source instanceof JMenuItem;
        }
        return false;
    }

    /**
     * Handles the actions triggered from the menu bar.
     *
     * @param event The ActionEvent associated with the action.
     */
    public void menuBarActionPerformed(ActionEvent event) {
        if (event.getSource() == menuBar.getHitColorItem()) {
            view.setHitColor();
        } else if (event.getSource() == menuBar.getMissedColorItem()) {
            view.setMissedColor();
        } else if (event.getSource() == menuBar.getUnselectedColorItem()) {
            view.setUnselectedColor();
        }
    }

    /**
     * Handles the coordinate action performed during the game.
     *
     * @param event The ActionEvent associated with the coordinate.
     */
    public void coordinateActionPerfomed(ActionEvent event) {
        // Only after the shot was executed successfully, like not repeating on the
        // already shot coordinate for example, or if the game is done
        String coordinateName = ((Coordinate) event.getSource()).getName();
        if (shoot(2, coordinateName)) {
            shootAtPlayer();
        }
    }

    /**
     * Shoots at the specified target with the given shot coordinate.
     *
     * @param targetId       The ID of the target (1 for player, 2 for enemy).
     * @param shotCoordinate The coordinate where the shot is being fired.
     * @return True if the shot was valid and executed, false otherwise.
     */
    public boolean shoot(int targetId, String shotCoordinate) {

        ArrayList<String> currentShotCoorrdinates = targetId == 1 ? playerShotCoordinates : enemyShotCoordinates;

        if (!currentShotCoorrdinates.contains(shotCoordinate)) {
            boolean hit = model.receiveShot(targetId, shotCoordinate);
            Ship currentShip = model.getCurrentShip();
            String result = "";

            if (hit && currentShip.isShipDestroyed()) {

                int health = model.getBoardHealth(targetId);

                view.setDestroyedCoordinates(targetId, currentShip);

                view.updateHealth(targetId, health);

                if (health == 0) {
                    isPlaying = canPlay = false;
                    result = "LOSE";
                    view.updateHistory(targetId, shotCoordinate, result);
                    view.stopTimer();
                    return false;
                } else {
                    result = "Destroyed";
                }
                view.updateHistory(targetId, shotCoordinate, result);

            } else if (hit) {
                view.setHit(targetId, shotCoordinate);
                result = "Hit";
            } else {
                view.setMissed(targetId, shotCoordinate);
                result = "Missed";
            }
            view.updateHistory(targetId, shotCoordinate, result);
            currentShotCoorrdinates.add(shotCoordinate);
            return true;
        }
        return false;

    }

    /**
     * Initiates a shot at the player's board by the computer opponent.
     */
    public void shootAtPlayer() {
        String coordinateName = "";

        do {
            Random rand = new Random();
            int randRow = 1 + rand.nextInt(2 * dimension);
            int randCol = 1 + rand.nextInt(2 * dimension);
            coordinateName = Misc.ALPHABET[randCol] + Integer.toString(randRow);

        } while (playerShotCoordinates.contains(coordinateName));
        shoot(this.id, coordinateName);
    }

    /**
     * Checks if the given source is a child component of the online menu.
     *
     * @param source The source object to be checked.
     * @return True if the source is a child of the online menu, otherwise false.
     */
    public boolean isChildOfOnlineMenu(Object source) {
        if (source instanceof Component) {
            Component component = (Component) source;
            return SwingUtilities.isDescendingFrom(component, onlineMenu);
        }
        return false;
    }

    /**
     * Handles the actions triggered from the online menu.
     *
     * @param event The ActionEvent associated with the action.
     */
    public void onlineMenuActionPerformed(ActionEvent event) {
        if (event.getSource() == onlineMenu.getConnectButton()) {
            handleConnectButton();
        } else if (event.getSource() == mainMenu.getMultiButton()) {
            view.showOnlineMenu();
        } else if (event.getSource() == mainMenu.getExtrasButton()) {
            view.showExtrasdialog();
        }
    }

    /**
     * Handles the connect button action in the online menu.
     */
    public void handleConnectButton() {
        try {
            String url = onlineMenu.getUrl();
            int port = Integer.parseInt(onlineMenu.getPort());

            connectToServer(url, port);
            view.disposeOnlineMenu();
        } catch (Exception e) {
            view.showAddressError();
        }
    }

    /**
     * Connects to the server using the specified URL and port, retrieves necessary
     * data,
     * and initializes the online game.
     *
     * @param url  The URL of the server.
     * @param port The port number for the connection.
     * @throws Exception If an error occurs during the connection.
     */
    public void connectToServer(String url, int port) throws Exception {

        Socket socket = null;
        ObjectInputStream objectInputStream = null;
        DataInputStream dataInputStream = null;
        isOnlineMode = true;
        serverUrl = url;
        serverPort = port;

        socket = new Socket(url, port);

        objectInputStream = new ObjectInputStream(socket.getInputStream());

        Ship[] playerShips = (Ship[]) objectInputStream.readObject();

        Ship[] enemyShips = (Ship[]) objectInputStream.readObject();

        dataInputStream = new DataInputStream(socket.getInputStream());

        dimension = dataInputStream.readInt(); // Read the integer value

        onlineId = dataInputStream.readInt();

        view.setDimension(dimension);

        view.setGameFrame();

        view.setRandomizedShips(this.id, playerShips);

        view.setRandomizedShips(2, enemyShips);

        view.setOnlineGame();

        menuBar = view.getMenuBar();

        middleMenu = view.getMiddleMenu();

        if (objectInputStream != null) {
            objectInputStream.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
        if (socket != null) {
            socket.close();
        }

    }

    /**
     * Shoots at the opponent's board in the online game.
     *
     * @param coordinate The coordinate where the shot is being fired.
     * @throws Exception If an error occurs during the shooting process.
     */
    public void shootOnline(String coordinate) throws Exception {
        Socket socket = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;

        try {
            socket = new Socket(serverUrl, serverPort);
            int targetId = this.onlineId == 1 ? 2 : 1;

            // Send the target id to server
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeInt(targetId);
            dataOutputStream.flush();

            // Send the coordinate to the server
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(coordinate);
            objectOutputStream.flush();

            dataInputStream = new DataInputStream(socket.getInputStream());
            int resultCode = dataInputStream.readInt();

            objectInputStream = new ObjectInputStream(socket.getInputStream());

            Ship currentShip = (Ship) objectInputStream.readObject();

            if (resultCode == Misc.HIT_CODE) {
                view.setHit(2, coordinate);
                view.setDestroyedCoordinates(2, currentShip);
            } else if (resultCode == Misc.MISSED_CODE) {
                view.setMissed(2, coordinate);
            }

            Object obj = objectInputStream.readObject();
            String temp = obj == null ? "" : (String) obj;

            view.updateBoard(temp, dataInputStream.readBoolean());

            view.updateHealth(2, dataInputStream.readInt());

            view.updateHealth(1, dataInputStream.readInt());

            view.updateHistory((String) objectInputStream.readObject());

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        } finally {
            // Close resources in a finally block to ensure they're closed regardless of
            // exceptions
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }

}
