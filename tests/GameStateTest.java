import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {

    private GameState gameState;
    private Player player1;
    private Player player2;
    private Gameboard gameboard;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        gameboard = new Gameboard();
    }
    @Test
    void testInitializePlayers() {
        // Test that the players have been initialized
        assertEquals(2, gameState.getPlayers().size());
        assertEquals("Player 1", gameState.getPlayers().get(0).getName());
        assertEquals("Player 2", gameState.getPlayers().get(1).getName());
    }
}
