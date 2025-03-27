import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the GameState class.
 */
public class GameStateTest {

    private GameState gameState;
    private Player player1;
    private Player player2;
    private Gameboard gameboard;

    @BeforeEach
    void setUp() {
        // Create players
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        // Create list of players
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        // Create gameboard
        gameboard = new Gameboard();

        // Initialize GameState with required parameters
        gameState = new GameState(players, gameboard);
    }

    @Test
    void testInitializePlayers() {
        // Test that the players have been initialized
        assertEquals(2, gameState.getPlayers().size());
        assertEquals("Player 1", gameState.getPlayers().get(0).getName());
        assertEquals("Player 2", gameState.getPlayers().get(1).getName());
    }

    // You can add more test methods here

    @Test
    void testCurrentPlayer() {
        // Test that the current player is the first player
        assertEquals(player1, gameState.getCurrentPlayer());
    }

    @Test
    void testNextTurn() {
        // Test that the next turn changes the current player
        gameState.nextTurn();
        assertEquals(player2, gameState.getCurrentPlayer());
    }

    @Test
    void testJailStatus() {
        // Test jail functionality
        assertEquals(false, gameState.isPlayerInJail(player1));
        gameState.sendToJail(player1);
        assertEquals(true, gameState.isPlayerInJail(player1));
        gameState.releaseFromJail(player1);
        assertEquals(false, gameState.isPlayerInJail(player1));
    }
}