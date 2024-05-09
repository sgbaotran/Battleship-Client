package view;

import java.awt.*;
import javax.swing.*;

import control.*;

import java.util.*;

import miscellaneous.Misc;
import model.Ship;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The Board class represents a game board. It is an abstract class that
 * provides common functionality and properties
 * for both the player's and computer's boards.
 * It handles the creation of coordinate labels and buttons, as well as the
 * progress bar and labels.
 * Subclasses of Board must implement the addCoordinate method to add specific
 * coordinate buttons to the board.
 * 
 * @author [Author]
 */
public class Board extends JPanel {
    protected boolean isReady = false;
    protected int id;
    protected Map<String, Coordinate> coordinatesMap;
    protected Coordinate[][] coordinatesArray;
    protected Controller controller;
    protected int dimension;
    protected Color[] themeColorSet;
    protected Color[] colorSet;
    protected Ship currentShip;
    private int count;
    private Ship[] generatedShips;

    /**
     * Constructs a new Board instance with the specified dimension, colors, name,
     * and controller.
     * 
     * @param dimension  the dimension of the board
     * @param colors     an array of colors for the board
     * @param name       the name of the board
     * @param masterMind the controller for the board
     */
    public Board(int id, int dimensionGiven, Color[] colorSet, Controller masterMind) {

        this.id = id;

        this.colorSet = colorSet;

        dimension = dimensionGiven;

        int numCell = (dimension * 2) + 1;

        controller = masterMind;

        coordinatesMap = new HashMap<>();

        coordinatesArray = new Coordinate[numCell][numCell];

        setPreferredSize(new Dimension(480, 480));

        setLayout(new GridLayout(numCell, numCell));

        themeColorSet = id == 1 ? Misc.PLAYER_BOARD_COLOR : Misc.ENEMY_BOARD_COLOR;

        setBackground(themeColorSet[1]);

        createBoardCells(numCell);

    }

    public void createBoardCells(int numCell) {
        // Add coordinate labels and buttons to the inner board panel
        int cellSize = 480 / numCell;
        for (int row = 0; row < numCell; row++) {
            for (int col = 0; col < numCell; col++) {
                if (row == 0) {
                    addAlphabetLabel(col, cellSize);
                } else if (col == 0) {
                    addNumericLabel(row, cellSize);
                } else {
                    addCoordinate(row, col, cellSize);
                }
            }
        }
    }

    /**
     * Adds an alphabet coordinate label to the inner board panel.
     *
     * @param col      the column index
     * @param cellSize the size of the button
     * @param color    the color of the label
     */
    public void addAlphabetLabel(int col, int cellSize) {
        JLabel label = new JLabel(Character.toString(Misc.ALPHABET[col]), SwingConstants.CENTER);
        label.setFont(new Font("Verdana", Font.PLAIN, cellSize / 5));
        label.setBackground(themeColorSet[0]);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(cellSize, cellSize));
        add(label);
    }

    /**
     * Adds a numeric coordinate label to the inner board panel.
     *
     * @param row        the row index
     * @param buttonSize the size of the button
     * @param color      the color of the label
     */
    public void addNumericLabel(int row, int cellSize) {
        JLabel label = new JLabel(Integer.toString(row), SwingConstants.CENTER);
        label.setFont(new Font("Verdana", Font.PLAIN, cellSize / 5));
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(cellSize, cellSize));
        add(label);
    }

    /**
     * Adds a coordinate button to the inner board panel.
     *
     * @param row      the row index
     * @param col      the column index
     * @param cellSize the size of the button
     */
    public void addCoordinate(int row, int col, int cellSize) {
        int buttonSize = cellSize - (cellSize / 20);
        int containerPadding = buttonSize / 40;
        JPanel coordinateContainer = new JPanel(null);

        coordinateContainer.setSize(new Dimension(buttonSize, buttonSize));

        coordinateContainer.setOpaque(false);

        Coordinate coordinate = new Coordinate(colorSet, row, col);

        coordinate.setBounds(containerPadding, containerPadding, buttonSize, buttonSize);

        coordinateContainer.add(coordinate);

        if (id == 2) {

            coordinate.addActionListener(controller);
        }

        coordinatesArray[row][col] = coordinate;

        coordinatesMap.put(coordinate.getName(), coordinate);

        add(coordinateContainer);
    }

    /**
     * Sets the coordinates of the randomized ships on the board.
     *
     * @param ships The array of ships to set.
     */
    public void setRandomizedShips(Ship[] ships) {
        for (Ship ship : ships) {
            int shipCol = ship.getCol();
            int shipRow = ship.getRow();
            int shipLength = ship.getLength();
            if (ship.isHorizontal()) {
                for (int i = shipCol; i < shipCol + shipLength; i++) {
                    coordinatesArray[shipRow][i].setOccupied(shipLength);
                }
            } else {
                for (int i = shipRow; i < shipRow + shipLength; i++) {
                    coordinatesArray[i][shipCol].setOccupied(shipLength);
                }
            }
        }
        isReady = true;
        repaint();
        revalidate();
    }

    /**
     * Sets the coordinates of the destroyed ship on the board.
     *
     * @param ship The ship to set destroyed coordinates for.
     */
    public void setDestroyedCoordinates(Ship ship) {
        for (String coordinate : ship.getCoordinates()) {
            coordinatesMap.get(coordinate).setDestroyed();
        }
    }

    /**
     * Sets the hit status of a coordinate on the board.
     *
     * @param coordinateName The name of the coordinate.
     */
    public void setHit(String coordinateName) {
        coordinatesMap.get(coordinateName).setHit();
    }

    /**
     * Sets the missed status of a coordinate on the board.
     *
     * @param coordinateName The name of the coordinate.
     */
    public void setMissed(String coordinateName) {
        coordinatesMap.get(coordinateName).setMissed();
    }

    /**
     * Enters the design mode for placing ships on the board.
     *
     * @param generatedShips The array of generated ships to place.
     */
    public void enterDesginMode(Ship[] generatedShips) {
        count = 0;
        isReady = false;
        this.generatedShips = generatedShips;
        for (Coordinate coordinate : coordinatesMap.values()) {
            coordinate.enterDesginMode(mouseAdapter);
        }
        currentShip = generatedShips[count];
    }

    /**
     * Exits the design mode after placing ships on the board.
     */
    public void exitDesignMode() {
        for (Coordinate coordinate : coordinatesMap.values()) {
            coordinate.exitDesignMode();
        }
        isReady = true;
    }

    private boolean isAppropriateForShip;

    private MouseAdapter mouseAdapter = new MouseAdapter() {

        public void mouseEntered(MouseEvent e) {
            Coordinate coordinate = (Coordinate) e.getSource();
            int row = coordinate.getRow();
            int column = coordinate.getColumn();

            int shipStart, shipEnd;
            if (currentShip.isHorizontal()) {
                shipStart = column;
                shipEnd = column + currentShip.getLength();
            } else {
                shipStart = row;
                shipEnd = row + currentShip.getLength();
            }

            for (int i = shipStart; i < shipEnd; i++) {

                String coordinateName = Misc.ALPHABET[currentShip.isHorizontal() ? i : column] + ""
                        + (currentShip.isHorizontal() ? row : i);
                Coordinate tempCoordinate = coordinatesMap.get(coordinateName);

                if (tempCoordinate == null || tempCoordinate.isOccupied()) {
                    for (int j = shipStart; j < i; j++) {
                        String coordinateName_1 = Misc.ALPHABET[currentShip.isHorizontal() ? j : column] + ""
                                + (currentShip.isHorizontal() ? row : j);
                        coordinatesMap.get(coordinateName_1).setBackground(Color.RED);
                    }
                    isAppropriateForShip = false;
                    return;
                }
                tempCoordinate.setHover();
            }
            isAppropriateForShip = true;

        }

        @Override
        public void mouseExited(MouseEvent e) {
            Coordinate coordinate = (Coordinate) e.getSource();
            int row = coordinate.getRow();
            int column = coordinate.getColumn();

            int shipStart, shipEnd;

            if (currentShip.isHorizontal()) {
                shipStart = column;
                shipEnd = column + currentShip.getLength();
            } else {
                shipStart = row;
                shipEnd = row + currentShip.getLength();
            }

            if (!coordinate.isOccupied()) {
                for (int i = shipStart; i < shipEnd; i++) {
                    String coordinateName = Misc.ALPHABET[currentShip.isHorizontal() ? i : column] + ""
                            + (currentShip.isHorizontal() ? row : i);
                    Coordinate tempCoordinate = coordinatesMap.get(coordinateName);
                    if (tempCoordinate != null && !tempCoordinate.isOccupied()) {
                        tempCoordinate.setUnhover();
                    }
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            Coordinate coordinate = (Coordinate) e.getSource();

            int row = coordinate.getRow();
            int column = coordinate.getColumn();
            int shipStart = currentShip.isHorizontal() ? column : row;
            int shipEnd = shipStart + currentShip.getLength();

            if (e.getButton() == MouseEvent.BUTTON3) {

                for (int i = shipStart + 1; i < shipEnd; i++) {
                    String coordinateName = Misc.ALPHABET[currentShip.isHorizontal() ? i : column] + "" +
                            (currentShip.isHorizontal() ? row : i);
                    Coordinate tempCoordinate = coordinatesMap.get(coordinateName);

                    if (tempCoordinate != null && !tempCoordinate.isOccupied()) {
                        System.out.println(coordinateName);
                    }
                }

                currentShip.switchAlignment();
                shipStart = currentShip.isHorizontal() ? column : row;
                shipEnd = shipStart + currentShip.getLength();

                for (int i = shipStart; i < shipEnd; i++) {
                    String coordinateName = Misc.ALPHABET[currentShip.isHorizontal() ? i : column] + "" +
                            (currentShip.isHorizontal() ? row : i);
                    Coordinate tempCoordinate = coordinatesMap.get(coordinateName);

                    if (tempCoordinate == null || tempCoordinate.isOccupied()) {
                        for (int j = shipStart; j < i; j++) {
                            String coordinateName_1 = Misc.ALPHABET[currentShip.isHorizontal() ? j : column] + "" +
                                    (currentShip.isHorizontal() ? row : j);
                            coordinatesMap.get(coordinateName_1).setBackground(Color.RED);
                        }
                        isAppropriateForShip = false;
                        return;
                    }
                    tempCoordinate.setHover();
                }

            } else if (e.getButton() == MouseEvent.BUTTON1) {

                if (isAppropriateForShip) {
                    try {
                        for (int i = shipStart; i < shipEnd; i++) {
                            String coordinateName = Misc.ALPHABET[currentShip.isHorizontal() ? i : column] + ""
                                    + (currentShip.isHorizontal() ? row : i);
                            Coordinate tempCoordinate = coordinatesMap.get(coordinateName);

                            tempCoordinate.setOccupied(currentShip.getLength());
                        }
                        currentShip.setCoordinates(coordinate.getRow(), coordinate.getColumn());
                        currentShip = generatedShips[++count];
                    } catch (Exception ex) {
                        System.out.println("Out of ship");
                        exitDesignMode();
                    }
                }
            }

        }

    };

    /**
     * Sets the color for unselected coordinates on the board.
     *
     * @param color The color to set.
     */
    public void setUnselectedColor(Color color) {
        for (Coordinate coordinate : coordinatesMap.values()) {
            coordinate.setUnselectedColor(color);
        }
    }

    /**
     * Sets the color for hit coordinates on the board.
     *
     * @param color The color to set.
     */
    public void setHitColor(Color color) {
        for (Coordinate coordinate : coordinatesMap.values()) {
            coordinate.setHitColor(color);
        }
    }

    /**
     * Sets the color for missed coordinates on the board.
     *
     * @param color The color to set.
     */
    public void setMissedColor(Color color) {
        for (Coordinate coordinate : coordinatesMap.values()) {
            coordinate.setMissedColor(color);
        }
    }

    /**
     * Checks if the board is ready for gameplay.
     *
     * @return True if the board is ready, false otherwise.
     */
    public boolean isReady() {
        return isReady;
    }

}
