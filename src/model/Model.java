package model;

import java.util.Random;

/**
 * The Model class represents the game logic and data management.
 */
public class Model {
    private int dimension;
    private int leftShipCount;
    private Ship[] leftShips;
    private String[][] leftCoordinates;

    private int rightShipCount
    ;
    private Ship[] rightShips;
    private String[][] rightCoordinates;

    private int maxShipCount;
    private Ship currentShip;

    private static final int INVALID_SHOT = 0;
    private static final int HIT = 1;
    private static final int MISSED = 2;

    /**
     * Randomizes ships for both players.
     */
    public void randomizeShips() {
        leftCoordinates = new String[dimension * 2 + 1][dimension * 2 + 1];
        rightCoordinates = new String[dimension * 2 + 1][dimension * 2 + 1];

        randomizeShip(1);
        randomizeShip(2);
    }

    /**
     * Randomizes ships for the specified board.
     *
     * @param boardId The board ID (1 or 2).
     */
    public void randomizeShip(int boardId) {
        String[][] currentCoordinates = boardId == 1 ? leftCoordinates : rightCoordinates;

        generateShips(boardId);
        Random rand = new Random();

        System.out.println("Eh??");
        for (Ship ship : boardId == 1 ? leftShips : rightShips) {
            System.out.println("Eh2??");
            boolean placed = false;
            while (!placed) {
                int randRow = 1 + rand.nextInt(2 * dimension);
                int randCol = 1 + rand.nextInt(2 * dimension);

                if (isSuitableForShip(currentCoordinates, ship, randRow, randCol)) {
                    ship.setCoordinates(randRow, randCol);
                    setSelectedCoordinate(currentCoordinates, ship);
                    placed = true;
                }
            }
        }
        System.out.println("Eh3??");

        leftShipCount = rightShipCount = maxShipCount = boardId == 1 ? leftShips.length : rightShips.length;
    }

    /**
     * Checks if a given position is suitable for placing a ship.
     *
     * @param currentCoordinates The current coordinates array.
     * @param ship               The ship to be placed.
     * @param randRow            The random row position.
     * @param randCol            The random column position.
     * @return True if the position is suitable, false otherwise.
     */
    public boolean isSuitableForShip(String[][] currentCoordinates, Ship ship, int randRow, int randCol) {
        if (ship.isHorizontal()) {
            if (randCol + ship.getLength() > currentCoordinates[randRow].length) {
                return false; // Range exceeds the board dimensions
            }
            for (int i = randCol; i < randCol + ship.getLength(); i++) {
                if (currentCoordinates[randRow][i] != null) {
                    return false; // Range contains a coordinate that is already occupied
                }
            }

        } else {
            if (randRow + ship.getLength() > currentCoordinates.length) {
                return false; // Range exceeds the board dimensions
            }
            for (int i = randRow; i < randRow + ship.getLength(); i++) {
                if (currentCoordinates[i][randCol] != null) {
                    return false; // Range contains a coordinate that is already occupied
                }
            }
        }
        return true; // Range is suitable for placing a ship of the given length
    }

    /**
     * Generates ships for the specified board based on the dimension.
     *
     * @param boardId The board ID (1 or 2).
     */
    public void generateShips(int boardId) {
        // Implementation for generateShips method
    }

    /**
     * Sets the selected coordinate on the board.
     *
     * @param currentCoordinates The current coordinates array.
     * @param ship               The ship to set the coordinates for.
     */
    public void setSelectedCoordinate(String[][] currentCoordinates, Ship ship) {
        // Implementation for setSelectedCoordinate method
    }

    /**
     * Gets the randomized ships for the specified board.
     *
     * @param boardId The board ID (1 or 2).
     * @return The array of randomized ships.
     */
    public Ship[] getRandomizedShips(int boardId) {
        return boardId == 1 ? leftShips : rightShips;
    }
    // ==================== FOR GAME FUNCTIONALITY ====================

    /**
     * the right-hand board will receive shot from the left-hand player, thus
     * explains the nane
     * 
     * @param coordinate
     */

    public boolean receiveShot(int targetId, String coordinate) {
        boolean hit = false;

        for (Ship ship : targetId == 1 ? leftShips : rightShips) {
            for (String tempCoordinate : ship.getCoordinates()) {
                if (coordinate.equals(tempCoordinate)) {
                    hit = true;
                    ship.decreaseHealth();

                    currentShip = ship;

                    updateShipCount(targetId);

                    return hit;
                }
            }
        }
        currentShip = null;
        return hit;
    }

    /**
     * Updates the ship count when a ship is destroyed.
     *
     * @param id The player ID.
     */
    public void updateShipCount(int id) {
        if (currentShip.isShipDestroyed()) {
            if (id == 1) {
                leftShipCount--;
            } else {
                rightShipCount--;
            }
        }
    }

    /**
     * Calculates and returns the health percentage of the specified board.
     *
     * @param id The board ID (1 or 2).
     * @return The health percentage of the board.
     */
    public int getBoardHealth(int id) {
        int nominator = (id == 1 ? leftShipCount : rightShipCount);
        float b = (((float) nominator / maxShipCount)) * 100;
        int c = (int) b;
        return c;
    }

    /**
     * Gets the currently selected ship.
     *
     * @return The current ship.
     */
    public Ship getCurrentShip() {
        return currentShip;
    }

    /**
     * Sets the dimension of the game board.
     *
     * @param dimension The dimension to set.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Generates and returns an array of ships for the specified board.
     *
     * @param boardId The board ID (1 or 2).
     * @return The array of generated ships.
     */
    public Ship[] getGeneratedShips(int boardId) {
        int index = 0;
        Ship[] currentBoardShips = new Ship[(dimension + 1) * dimension / 2];

        if (boardId == 1)
            leftShips = currentBoardShips;
        else
            rightShips = currentBoardShips;

        for (int i = dimension; i >= 1; i--) {
            for (int j = 0; j < dimension - i + 1; j++) {
                currentBoardShips[index++] = new Ship(i, true);
            }
        }

        return currentBoardShips;
    }
}
