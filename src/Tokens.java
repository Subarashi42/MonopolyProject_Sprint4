/**
 * Tokens.java
 * This class represents the tokens for the Monopoly game.
 * It has a list of available tokens and ensures that players can only choose from those that remain.
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Tokens {
    /**
     * Author: Aiden Clare
     * This is a list of the available tokens.
     */

    public static final String[] TOKENS = {
            "Top Hat", "Thimble", "Iron", "Boot", "Battleship",
            "Cannon", "Race Car", "Scottie Dog", "Wheelbarrow"
    };
    /**
     * Author: Aiden Clare
     * this creates a list of the available tokens.
     */

    private static ArrayList<String> availableTokens = new ArrayList<>();
    private String owner;
    private int boardPosition;

    /**
     * Author: Aiden Clare
     * This method is used to initialize the available tokens.
     */

    // Initialize the available tokens
    public static void initializeTokens() {
        availableTokens.clear(); // Reset the list
        availableTokens.addAll(Arrays.asList(TOKENS));
    }

    /**
     * Author: Aiden Clare
     * This method is used to check if a token is available.
     * @param token
     * @return
     */

    // Checks if a token is still available for selection
    public static boolean isTokenAvailable(String token) {
        return availableTokens.contains(token);
    }

    /**
     * Author: Aiden Clare
     * This method is used to assign a token to a player.
     * @param token
     * @return
     */

    // Assigns a token to a player if it's available
    public static boolean assignToken(String token) {
        if (availableTokens.contains(token)) {
            availableTokens.remove(token);
            return true;
        }
        return false;
    }

    /**
     * Author: Aiden Clare
     * This method is used to remove a token from the available tokens.
     */

    // Displays the list of remaining available tokens
    public static void displayAvailableTokens() {
        System.out.println("Available tokens: " + availableTokens);
    }

    /**
     * Author: Aiden Clare
     * This method is used to get the list of available tokens.
     */

    // Constructor for setting owner and board position
    public Tokens() {
        this.owner = null;
        this.boardPosition = 0;
    }

    /**
     * Author: Aiden Clare
     * This method is used to assign a token to a player.
     * @return
     */

    public static char[] assignToken() {
        if (availableTokens.size() > 0) {
            return availableTokens.remove(0).toCharArray();
        }
        return null;
    }

    /**
     * Author: Aiden Clare
     * This method is used to choose a token for a player.
     * @param player1
     * @param raceCar
     * @return
     */

    public static boolean chooseToken(Player player1, String raceCar) {
        return false;
    }

    /**
     * Author: Aiden Clare
     * This method is used to move a token to a new position.
     * @param player
     * @param position
     */

    public static void moveToken(Player player, int position) {
    }

    /**
     * Author: Aiden Clare
     * This method is used to get the list of available tokens.
     * @return
     */

    public static String getavailabletokens() {
        if (availableTokens.size() > 0) {
            return availableTokens.toString();
        }
        else
        return "No tokens available";
    }

    /**
     * Author: Tati Curtis
     * This method is used to get the owner of the token.
     * @return
     */

    // Getter for owner
    public String getOwner() {
        return owner;
    }

    /**
     * Author: Tati Curtis
     * This method is used to get the board position of the token.
     * @return
     */

    // Getter for board position
    public int getBoardPosition() {
        return boardPosition;
    }

    /**
     * Author: Tati Curtis
     * This method is used to set the owner of the token.
     * @param owner
     */

    // Setter for owner
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Author: Tati Curtis
     * This method is used to set the board position of the token.
     * @param boardPosition
     */

    // Setter for board position
    public void setBoardPosition(int boardPosition) {
        this.boardPosition = boardPosition;
    }

    /**
     * Author: Tati Curtis
     * This method is used to get the string representation of the token.
     * @return
     */

    // String representation of the token with owner and board position
    @Override
    public String toString() {
        return owner + " - " + boardPosition;
    }
}
