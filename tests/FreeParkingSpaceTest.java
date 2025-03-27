import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
// Explicitly use JUnit 4 Assert to avoid ambiguity
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the FreeParkingSpace class.
 */
public class FreeParkingSpaceTest {
    private FreeParkingSpace freeParkingSpace;
    private Player player;
    private Gameboard board;
    private GameState gameState;

    @Before
    public void setUp() {
        freeParkingSpace = new FreeParkingSpace();
        player = new Player("Test Player");
        board = new Gameboard();
        List<Player> players = new ArrayList<>();
        players.add(player);
        gameState = new GameState(players, board);
    }

    @Test
    public void testConstructor() {
        assertEquals("Free Parking", freeParkingSpace.getName());
        assertEquals(20, freeParkingSpace.getPosition());
        assertEquals("Free Parking", freeParkingSpace.type);
        assertEquals(0, freeParkingSpace.getMoneyPool());
    }

    @Test
    public void testOnLandStandardRules() {
        // Test landing on Free Parking with standard rules (no money collection)
        int initialMoney = player.getMoney();
        freeParkingSpace.onLand(player, gameState);

        // Player money should not change
        assertEquals(initialMoney, player.getMoney());
    }

    @Test
    public void testOnLandHouseRules() {
        // Add money to pool
        freeParkingSpace.addMoneyToPool(500);
        assertEquals(500, freeParkingSpace.getMoneyPool());

        // Test landing on Free Parking with house rules (collect money from pool)
        int initialMoney = player.getMoney();
        freeParkingSpace.onLand(player, gameState, true);

        // Player should collect the money pool
        assertEquals(initialMoney + 500, player.getMoney());

        // Money pool should be reset to 0
        assertEquals(0, freeParkingSpace.getMoneyPool());
    }

    @Test
    public void testMoneyPool() {
        // Test adding money to pool
        assertEquals(0, freeParkingSpace.getMoneyPool());

        freeParkingSpace.addMoneyToPool(100);
        assertEquals(100, freeParkingSpace.getMoneyPool());

        freeParkingSpace.addMoneyToPool(150);
        assertEquals(250, freeParkingSpace.getMoneyPool());
    }

    @Test
    public void testMoneyCollectionWithEmptyPool() {
        // Test landing on Free Parking with house rules but empty pool
        int initialMoney = player.getMoney();
        freeParkingSpace.onLand(player, gameState, true);

        // Player money should not change (pool is empty)
        assertEquals(initialMoney, player.getMoney());
    }

    @Test
    public void testPlayerOnSpecialSpace() {
        // This method just prints to console, so just verify it doesn't throw an exception
        freeParkingSpace.playerOnSpecialSpace();
    }

    @Test
    public void testInheritance() {
        assertTrue(freeParkingSpace instanceof SpecialSpace);
        assertTrue(freeParkingSpace instanceof Space);
    }
}