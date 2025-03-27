import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Player class.
 */
public class PlayerTest {
    private Player player;
    private Gameboard board;
    private GameState gameState;

    @Before
    public void setUp() {
        // Initialize components for testing
        player = new Player("Test Player");
        board = new Gameboard();
        List<Player> players = new ArrayList<>();
        players.add(player);
        gameState = new GameState(players, board);
        Tokens.initializeTokens();
    }

    @Test
    public void testPlayerInitialization() {
        // Test player is initialized with correct values
        assertEquals("Test Player", player.getName());
        assertEquals(1500, player.getMoney()); // Starting money
        assertEquals(0, player.getPosition()); // Starting position (GO)
        assertNull(player.getToken()); // No token assigned initially
        assertEquals(0, player.getProperties().size()); // No properties owned
        assertFalse(player.isBankrupt());
        assertFalse(player.hasGetOutOfJailFreeCard());
    }

    @Test
    public void testChooseToken() {
        // Test choosing an available token
        assertTrue(player.chooseToken("Top Hat"));
        assertEquals("Top Hat", player.getToken());
        assertFalse(Tokens.isTokenAvailable("Top Hat")); // Token should now be unavailable

        // Try to choose the same token for another player
        Player player2 = new Player("Player 2");
        assertFalse(player2.chooseToken("Top Hat"));
        assertNull(player2.getToken()); // Player 2 should not have a token
    }

    @Test
    public void testMoneyTransactions() {
        // Test adding money
        player.addMoney(500);
        assertEquals(2000, player.getMoney());

        // Test subtracting money (successful)
        boolean success = player.subtractMoney(300);
        assertTrue(success);
        assertEquals(1700, player.getMoney());

        // Test subtracting more money than player has
        success = player.subtractMoney(2000);
        assertFalse(success);
        assertEquals(1700, player.getMoney()); // Money shouldn't change
    }

    @Test
    public void testBuyProperty() {
        Property property = new Property("Test Property", 5, 100, 10, "Red");

        // Test buying property (successful)
        boolean bought = player.buyProperty(property);
        assertTrue(bought);
        assertEquals(player, property.getOwner());
        assertEquals(1400, player.getMoney()); // 1500 - 100
        assertTrue(player.getProperties().contains(property));

        // Test buying property with insufficient funds
        player.subtractMoney(1350); // Leave player with $50
        Property expensiveProperty = new Property("Expensive Property", 39, 400, 50, "Blue");
        bought = player.buyProperty(expensiveProperty);
        assertFalse(bought);
        assertNull(expensiveProperty.getOwner());
        assertEquals(50, player.getMoney()); // Money shouldn't change
    }

    @Test
    public void testPayRent() {
        Player owner = new Player("Owner");

        // Test successful rent payment
        boolean paid = player.payRent(owner, 100);
        assertTrue(paid);
        assertEquals(1400, player.getMoney()); // 1500 - 100
        assertEquals(1600, owner.getMoney()); // 1500 + 100

        // Test rent payment with insufficient funds
        paid = player.payRent(owner, 2000);
        assertFalse(paid);
        assertEquals(1400, player.getMoney()); // Should remain unchanged
        assertEquals(1600, owner.getMoney()); // Should remain unchanged
    }

    @Test
    public void testMovement() {
        // Test basic movement
        player.setPosition(0); // Start at GO
        player.move(8, board);
        assertEquals(8, player.getPosition());

        // Test passing GO
        player.setPosition(39); // Last space
        player.move(3, board);
        assertEquals(2, player.getPosition()); // Should wrap around
        assertEquals(1700, player.getMoney()); // Should get $200 for passing GO
    }

    @Test
    public void testJailStatus() {
        // Test jail-related methods
        assertEquals(0, player.getTurnsInJail());

        // Simulate going to jail
        player.setTurnsInJail(1);
        assertEquals(1, player.getTurnsInJail());

        // Simulate getting out of jail
        player.setTurnsInJail(0);
        assertEquals(0, player.getTurnsInJail());

        // Test Get Out of Jail Free card
        assertFalse(player.hasGetOutOfJailFreeCard());
        player.setHasGetOutOfJailFreeCard(true);
        assertTrue(player.hasGetOutOfJailFreeCard());
    }

    @Test
    public void testDice() {
        // Test the dice object
        assertNotNull(player.dice);

        // Test rolling dice
        int roll = player.dice.rollDice();
        assertTrue(roll >= 2 && roll <= 12); // Roll should be between 2 and 12

        // Test getting die values
        int die1 = player.dice.getDie1Value();
        int die2 = player.dice.getDie2Value();
        assertTrue(die1 >= 1 && die1 <= 6);
        assertTrue(die2 >= 1 && die2 <= 6);
        assertEquals(die1 + die2, roll);
    }

    @Test
    public void testBankruptcy() {
        assertFalse(player.isBankrupt());

        // Make player bankrupt by taking away all money
        player.subtractMoney(1500);
        assertEquals(0, player.getMoney());
        assertTrue(player.isBankrupt());
    }

    @Test
    public void testGetTokensMethod() {
        // Test getTokens method which returns player info
        String expectedPattern = "Test Player has \\$1500 and is on space 0";
        assertTrue(player.getTokens().matches(expectedPattern));

        // Change position and money
        player.setPosition(10);
        player.addMoney(200);
        expectedPattern = "Test Player has \\$1700 and is on space 10";
        assertTrue(player.getTokens().matches(expectedPattern));
    }

    @Test
    public void testToString() {
        // Test toString method
        String expected = "Test Player ($1500, Position: 0, Token: null, Properties: 0)";
        assertEquals(expected, player.toString());

        // Change player state
        player.chooseToken("Battleship");
        player.buyProperty(new Property("Baltic Avenue", 3, 60, 4, "Brown"));
        player.setPosition(3);

        expected = "Test Player ($1440, Position: 3, Token: Battleship, Properties: 1)";
        assertEquals(expected, player.toString());
    }
}