import java.util.List;
import java.util.Map;

/**
 * Interface to represent the state of a Monopoly game.
 * Tracks players, board, dice, cards and overall game status.
 */
public interface GameState {
    // Player management
    List<Player> getPlayers();

    Player getCurrentPlayer();

    void nextTurn();

    // Board state
    Gameboard getBoard();

    Map<Integer, String> getPropertyOwnership();

    // Dice operations
    int rollDice();

    int[] getDiceValues();

    // Card operations
    String drawChanceCard();

    String drawCommunityChestCard();

    // Money operations
    void transferMoney(Player from, Player to, int amount);

    void collectFromBank(Player player, int amount);

    void payToBank(Player player, int amount);

    // Game status
    boolean isGameActive();

    boolean isPlayerInJail(Player player);

    void sendToJail(Player player);

    void releaseFromJail(Player player);

    // Display methods
    void displayGameState();

    void displayPlayerStatus(Player player);
}

    
