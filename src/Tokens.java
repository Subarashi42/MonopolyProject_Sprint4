import java.util.ArrayList;
import java.util.Arrays;

public class Tokens {
    /*
     * This class represents the tokens for the Monopoly game.
     * It has a list of available tokens and ensures that players can only choose from those that remain.
     */

    public static final String[] TOKENS = {
            "Top Hat", "Thimble", "Iron", "Boot", "Battleship",
            "Cannon", "Race Car", "Scottie Dog", "Wheelbarrow"
    };

    private static ArrayList<String> availableTokens = new ArrayList<>();
    private String owner;
    private int boardPosition;

    // Initialize the available tokens
    public static void initializeTokens() {
        availableTokens.clear(); // Reset the list
        availableTokens.addAll(Arrays.asList(TOKENS));
    }

    // Checks if a token is still available for selection
    public static boolean isTokenAvailable(String token) {
        return availableTokens.contains(token);
    }

    // Assigns a token to a player if it's available
    public static boolean assignToken(String token) {
        if (availableTokens.contains(token)) {
            availableTokens.remove(token);
            return true;
        }
        return false;
    }

    // Displays the list of remaining available tokens
    public static void displayAvailableTokens() {
        System.out.println("Available tokens: " + availableTokens);
    }

    // Constructor for setting owner and board position
    public Tokens() {
        this.owner = null;
        this.boardPosition = 0;
    }

    // Getter for owner
    public String getOwner() {
        return owner;
    }

    // Getter for board position
    public int getBoardPosition() {
        return boardPosition;
    }

    // Setter for owner
    public void setOwner(String owner) {
        this.owner = owner;
    }

    // Setter for board position
    public void setBoardPosition(int boardPosition) {
        this.boardPosition = boardPosition;
    }

    // String representation of the token with owner and board position
    @Override
    public String toString() {
        return owner + " - " + boardPosition;
    }
}
