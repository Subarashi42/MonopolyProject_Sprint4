import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the RailroadSpace class.
 */
public class RailroadSpaceTest {
    private RailroadSpace railroad;
    private Player owner;
    private Player otherPlayer;
    private GameState gameState;
    private Gameboard board;

    @Before
    public void setUp() {
        // Create a test railroad
        railroad = new RailroadSpace("Reading Railroad", 5);

        // Create players
        owner = new Player("Owner");
        otherPlayer = new Player("Other Player");

        // Setup game state with the real board
        board = new Gameboard();
        List<Player> players = new ArrayList<>();
        players.add(owner);
        players.add(otherPlayer);
        gameState = new GameState(players, board);
    }

    @Test
    public void testConstructor() {
        assertEquals("Reading Railroad", railroad.getName());
        assertEquals(5, railroad.getPosition());
        assertEquals("Railroad", railroad.getType());
        assertEquals(200, railroad.getPrice());
        assertNull(railroad.getOwner());
    }

    @Test
    public void testSetAndGetOwner() {
        assertNull(railroad.getOwner());
        railroad.setOwner(owner);
        assertEquals(owner, railroad.getOwner());
    }

    @Test
    public void testIsOwned() {
        assertFalse(railroad.isOwned());
        railroad.setOwner(owner);
        assertTrue(railroad.isOwned());
    }

    @Test
    public void testCalculateRent() {
        // When not owned, rent should be 0
        assertNull(railroad.getOwner());
        assertEquals(0, railroad.calculateRent(gameState));

        // Set owner - with our fix, rent should be 25 for one railroad
        railroad.setOwner(owner);
        assertEquals(25, railroad.calculateRent(gameState));
    }

    @Test
    public void testOnLandUnowned() {
        // Initial money
        int initialMoney = otherPlayer.getMoney();

        // Land on unowned railroad
        railroad.onLand(otherPlayer, gameState);

        // Money shouldn't change since the buying logic is elsewhere
        assertEquals(initialMoney, otherPlayer.getMoney());
        assertNull(railroad.getOwner());
    }

    @Test
    public void testOnLandOwnedByOtherPlayer() {
        // First, we need to manually set up the railroad owner
        railroad.setOwner(owner);

        // Initial money for both players
        int otherInitialMoney = otherPlayer.getMoney();
        int ownerInitialMoney = owner.getMoney();

        // With the fixed implementation, rent should be 25
        int expectedRent = 25;

        // Land on railroad owned by someone else
        railroad.onLand(otherPlayer, gameState);

        // Other player should pay rent
        assertEquals(otherInitialMoney - expectedRent, otherPlayer.getMoney());
        // Owner should receive rent
        assertEquals(ownerInitialMoney + expectedRent, owner.getMoney());
    }

    @Test
    public void testOnLandOwnedBySelf() {
        // Set railroad owner
        railroad.setOwner(owner);

        // Initial money
        int initialMoney = owner.getMoney();

        // Land on own railroad
        railroad.onLand(owner, gameState);

        // Money should not change
        assertEquals(initialMoney, owner.getMoney());
    }

    @Test
    public void testToString() {
        String expectedString = "5: Reading Railroad (Railroad) - Price: $200, Owner: None";
        assertEquals(expectedString, railroad.toString());

        // Set owner
        railroad.setOwner(owner);
        expectedString = "5: Reading Railroad (Railroad) - Price: $200, Owner: Owner";
        assertEquals(expectedString, railroad.toString());
    }

    @Test
    public void testPlayerOnRailroad() {
        // This method just prints to console, so verify it doesn't throw an exception
        railroad.playerOnRailroad();
    }
}