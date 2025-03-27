import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the GoSpace class.
 */
public class GoSpaceTest {
    private GoSpace goSpace;
    private Player player;
    private Gameboard board;
    private GameState gameState;

    @Before
    public void setUp() {
        goSpace = new GoSpace();
        player = new Player("Test Player");
        board = new Gameboard();
        List<Player> players = new ArrayList<>();
        players.add(player);
        gameState = new GameState(players, board);
    }

    @Test
    public void testConstructor() {
        assertEquals("Go", goSpace.getName());
        assertEquals(0, goSpace.getPosition());
        assertEquals("Start", goSpace.getType());
    }

    @Test
    public void testOnLand() {
        // Set initial money
        int initialMoney = player.getMoney();

        // Player lands on Go
        goSpace.onLand(player, gameState);

        // Player should collect $200
        assertEquals(initialMoney + 200, player.getMoney());
    }

    @Test
    public void testOnPass() {
        // Set initial money
        int initialMoney = player.getMoney();

        // Player passes Go
        GoSpace.onPass(player, gameState);

        // Player should collect $200
        assertEquals(initialMoney + 200, player.getMoney());
    }

    @Test
    public void testMoveToGo() {
        // Set player's position to something other than Go
        player.setPosition(10);

        // Set initial money
        int initialMoney = player.getMoney();

        // Move player to Go
        GoSpace.moveToGo(player, gameState);

        // Verify player's position is now 0 (Go)
        assertEquals(0, player.getPosition());

        // Player should collect $200
        assertEquals(initialMoney + 200, player.getMoney());
    }

    @Test
    public void testPlayerOnSpecialSpace() {
        // This method just prints to console, so verify it doesn't throw an exception
        goSpace.playerOnSpecialSpace();
    }

    @Test
    public void testInheritance() {
        assertTrue(goSpace instanceof SpecialSpace);
        assertTrue(goSpace instanceof Space);
    }

    @Test
    public void testMultipleVisitsToGo() {
        // Test that a player receives money each time they land on Go
        int initialMoney = player.getMoney();

        // Land on Go twice
        goSpace.onLand(player, gameState);
        goSpace.onLand(player, gameState);

        // Player should collect $200 twice (total $400)
        assertEquals(initialMoney + 400, player.getMoney());
    }

    @Test
    public void testPassGoAndLandOnGo() {
        // Test that a player gets paid for both passing Go and landing on Go
        int initialMoney = player.getMoney();

        // First pass Go
        GoSpace.onPass(player, gameState);

        // Then land on Go
        goSpace.onLand(player, gameState);

        // Player should collect $200 twice (total $400)
        assertEquals(initialMoney + 400, player.getMoney());
    }
}