import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Gameboard class.
 */
public class GameboardTest {
    private Gameboard board;
    private Player player;

    @Before
    public void setUp() {
        // Initialize board and player before each test
        board = new Gameboard();
        player = new Player("Test Player");
    }

    @Test
    public void testBoardInitialization() {
        // Verify board has correct number of spaces
        assertEquals(40, board.getSpaces().size());

        // Verify specific spaces are in the correct positions
        Space goSpace = board.getspace(0);
        assertEquals("Go", goSpace.getName());
        assertEquals("Start", goSpace.type);

        Space jailSpace = board.getspace(10);
        assertEquals("Jail", jailSpace.getName());
        assertEquals("Jail", jailSpace.type);

        Space freeParkingSpace = board.getspace(20);
        assertEquals("Free Parking", freeParkingSpace.getName());
        assertEquals("Free Parking", freeParkingSpace.type);

        Space goToJailSpace = board.getspace(30);
        assertEquals("Go To Jail", goToJailSpace.getName());
        assertEquals("Go To Jail", goToJailSpace.type);

        // Verify some properties
        Space mediterraneanAve = board.getspace(1);
        assertTrue(mediterraneanAve instanceof Property);
        assertEquals("Mediterranean Avenue", mediterraneanAve.getName());
        assertEquals("Brown", ((Property)mediterraneanAve).getColorGroup());

        Space parkPlace = board.getspace(37);
        assertTrue(parkPlace instanceof Property);
        assertEquals("Park Place", parkPlace.getName());
        assertEquals("Dark Blue", ((Property)parkPlace).getColorGroup());

        // Verify some railroads
        Space readingRailroad = board.getspace(5);
        assertTrue(readingRailroad instanceof RailroadSpace);
        assertEquals("Reading Railroad", readingRailroad.getName());

        // Verify some utilities
        Space electricCompany = board.getspace(12);
        assertTrue(electricCompany instanceof UtilitySpace);
        assertEquals("Electric Company", electricCompany.getName());
    }

    @Test
    public void testGetPropertiesByColorGroup() {
        // Test getting all properties in the "Brown" color group
        List<Property> brownProperties = board.getPropertiesByColorGroup("Brown");
        assertEquals(2, brownProperties.size());
        assertEquals("Mediterranean Avenue", brownProperties.get(0).getName());
        assertEquals("Baltic Avenue", brownProperties.get(1).getName());

        // Test getting all properties in the "Dark Blue" color group
        List<Property> darkBlueProperties = board.getPropertiesByColorGroup("Dark Blue");
        assertEquals(2, darkBlueProperties.size());
        assertEquals("Park Place", darkBlueProperties.get(0).getName());
        assertEquals("Boardwalk", darkBlueProperties.get(1).getName());
    }

    @Test
    public void testGetRailroads() {
        List<RailroadSpace> railroads = board.getRailroads();
        assertEquals(4, railroads.size());
        assertEquals("Reading Railroad", railroads.get(0).getName());
        assertEquals("Pennsylvania Railroad", railroads.get(1).getName());
        assertEquals("B. & O. Railroad", railroads.get(2).getName());
        assertEquals("Short Line", railroads.get(3).getName());
    }

    @Test
    public void testGetUtilities() {
        List<UtilitySpace> utilities = board.getUtilities();
        assertEquals(2, utilities.size());
        assertEquals("Electric Company", utilities.get(0).getName());
        assertEquals("Water Works", utilities.get(1).getName());
    }

    @Test
    public void testPlayerOwnsAllInColorGroup() {
        // Initially player owns nothing
        assertFalse(board.playerOwnsAllInColorGroup(player, "Brown"));

        // Player buys one brown property
        Property mediterraneanAve = (Property)board.getspace(1);
        mediterraneanAve.setOwner(player);
        assertFalse(board.playerOwnsAllInColorGroup(player, "Brown"));

        // Player buys the second brown property -> should now own all in the group
        Property balticAve = (Property)board.getspace(3);
        balticAve.setOwner(player);
        assertTrue(board.playerOwnsAllInColorGroup(player, "Brown"));

        // Verify player doesn't own all in another color group
        assertFalse(board.playerOwnsAllInColorGroup(player, "Dark Blue"));
    }

    @Test
    public void testLandingOnProperty() {
        // Create GameState for tests that require it
        List<Player> players = new ArrayList<>();
        players.add(player);
        GameState gameState = new GameState(players, board);

        // Test landing on property
        Property mediterraneanAve = (Property)board.getspace(1);
        mediterraneanAve.onLand(player, gameState);

        // Just verifies the method runs without errors
        // More detailed tests for property behavior would be in PropertyTest
    }
}