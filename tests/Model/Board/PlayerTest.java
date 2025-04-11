package Model.Board;

import Model.GameState;
import Model.Property.Property;
import Model.Spaces.RailroadSpace;
import Model.Spaces.UtilitySpace;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the Player class
 */
public class PlayerTest {

    private Player player;
    private Gameboard gameboard;
    private GameState gameState;

    private Dice mockDice;


    @Test
    public void testChooseTokenAlreadyTaken() {
        // Reset tokens first
        Tokens.initializeTokens();

        // First player chooses a token
        assertTrue(player.chooseToken("Race Car"));

        // Create another player
        Player player2 = new Player("Player 2");

        // Second player should not be able to choose the same token
        assertFalse(player2.chooseToken("Race Car"));
    }

    @Test
    public void testMortgageComplexScenarios() {
        // Create a property
        Property property = new Property("Test Property", 1, 200, "Brown");
        property.setOwner(player);
        player.getProperties().add(property);

        // Cannot mortgage property with houses
        property.setHouses(1);
        assertFalse(player.mortgageProperty(property));

        // Remove houses
        property.setHouses(0);

        // Mortgage the property
        assertTrue(player.mortgageProperty(property));
        assertTrue(property.isMortgaged());
        assertTrue(player.getMortgagedProperties().contains(property));

        // Cannot mortgage an already mortgaged property
        assertFalse(player.mortgageProperty(property));
    }

    @Test
    public void testUnmortgageProperty() {
        // Create a property
        Property property = new Property("Test Property", 1, 200, "Brown");
        property.setOwner(player);
        player.getProperties().add(property);

        // Mortgage the property
        player.mortgageProperty(property);

        // Add money to unmortgage
        player.addMoney(property.getUnmortgageCost());

        // Unmortgage the property
        assertTrue(player.unmortgageProperty(property));
        assertFalse(property.isMortgaged());
        assertFalse(player.getMortgagedProperties().contains(property));

        // Cannot unmortgage a property not mortgaged
        assertFalse(player.unmortgageProperty(property));
    }



    @Test
    public void testInvalidCardEffect() {
        int initialMoney = player.getMoney();
        int initialPosition = player.getPosition();

        // Test an invalid or unrecognized card effect
        player.processCardEffect("Some random card effect", gameState);

        // Verify no changes occurred
        assertEquals(initialMoney, player.getMoney());
        assertEquals(initialPosition, player.getPosition());
    }



    @Test
    public void testChooseTokenEdgeCases() {
        // Reset tokens
        Tokens.initializeTokens();

        // Choose all tokens
        String[] tokens = Tokens.TOKENS;
        for (String token : tokens) {
            Player tempPlayer = new Player("Temp Player");
            assertTrue(tempPlayer.chooseToken(token));
        }

        // Verify no more tokens are available
        Player finalPlayer = new Player("Final Player");
        assertFalse(finalPlayer.chooseToken("Any Token"));
    }


    @Test
    public void testReceiveRentMultipleTimes() {
        int initialMoney = player.getMoney();
        int[] rentAmounts = {50, 75, 100};

        for (int rentAmount : rentAmounts) {
            player.receiveRent(rentAmount);
        }

        int totalRentReceived = 50 + 75 + 100;
        assertEquals(initialMoney + totalRentReceived, player.getMoney());
    }

    @Before
    public void setUp() {
        // Create a new player before each test
        player = new Player("Test Player");
    }

    @Test
    public void testInitialization() {
        assertEquals("Test Player", player.getName());
        assertEquals(1500, player.getMoney()); // Starting money should be 1500
        assertEquals(0, player.getPosition()); // Starting position should be 0 (GO)
        assertNull(player.getToken()); // Token should be null initially
        assertEquals(0, player.getProperties().size()); // Should have no properties initially
        assertFalse(player.hasGetOutOfJailFreeCard()); // Should not have a Get Out of Jail Free card initially
        assertEquals(0, player.getTurnsInJail()); // Should not be in jail initially
    }

    @Test
    public void testAddMoney() {
        // Test adding money to the player
        int initialMoney = player.getMoney();
        player.addMoney(500);
        assertEquals(initialMoney + 500, player.getMoney());
    }

    @Test
    public void testSubtractMoney() {
        // Test subtracting money when player has enough
        int initialMoney = player.getMoney();
        boolean result = player.subtractMoney(500);
        assertTrue("Player should have enough money", result);
        assertEquals(initialMoney - 500, player.getMoney());

        // Test subtracting money when player doesn't have enough
        result = player.subtractMoney(2000);
        assertFalse("Player should not have enough money", result);
        assertEquals(initialMoney - 500, player.getMoney()); // Money should not have changed
    }

    @Test
    public void testGetSet() {
        // Test various getters and setters

        // Position
        player.setPosition(10);
        assertEquals(10, player.getPosition());

        // Token
        player.setToken("Hat");
        assertEquals("Hat", player.getToken());

        // Jail Card
        player.setHasGetOutOfJailFreeCard(true);
        assertTrue(player.hasGetOutOfJailFreeCard());

        // Turns in Jail
        player.setTurnsInJail(2);
        assertEquals(2, player.getTurnsInJail());
    }

    @Test
    public void testMove() {
        // Test moving the player
        Gameboard board = new Gameboard();
        player.setPosition(0); // Start at GO
        player.move(7, board); // Move 7 spaces
        assertEquals(7, player.getPosition());

        // Test moving past GO (should wrap around)
        player.setPosition(39); // Last space
        player.move(3, board); // Move 3 spaces, wrapping to position 2
        assertEquals(2, player.getPosition());
    }

    @Test
    public void testBuyProperty() {
        // Create a property
        Property property = new Property("Test Property", 3, 200, "Brown");

        // Test buying the property
        int initialMoney = player.getMoney();
        boolean result = player.buyProperty(property);

        assertTrue("Player should be able to buy the property", result);
        assertEquals(initialMoney - 200, player.getMoney());
        assertEquals(player, property.getOwner());
        assertTrue(player.getProperties().contains(property));

        // Test buying a property that's too expensive
        // Use "Dark Blue" instead of "Blue" as it appears to be a valid color group
        Property expensiveProperty = new Property("Expensive Property", 5, 2000, "Dark Blue");

        // Create a player with little money for this test
        Player poorPlayer = new Player("Poor Player");
        // Subtract most of the money to make them poor
        poorPlayer.subtractMoney(1400);

        result = poorPlayer.buyProperty(expensiveProperty);
        assertFalse("Player should not be able to buy the expensive property", result);
        // Money should not change from the unsuccessful purchase
        assertEquals(100, poorPlayer.getMoney());
        assertNull(expensiveProperty.getOwner()); // Property should not have an owner
        assertFalse(poorPlayer.getProperties().contains(expensiveProperty));
    }

    @Test
    public void testBuyRailroad() {
        // Create a railroad
        RailroadSpace railroad = new RailroadSpace("Test Railroad", 5);

        // Test buying the railroad
        int initialMoney = player.getMoney();
        boolean result = player.buyRailroad(railroad);

        assertTrue("Player should be able to buy the railroad", result);
        assertEquals(initialMoney - railroad.getPrice(), player.getMoney());
        assertEquals(player, railroad.getOwner());
    }

    @Test
    public void testBuyUtility() {
        // Create a utility
        UtilitySpace utility = new UtilitySpace("Test Utility", 12);

        // Test buying the utility
        int initialMoney = player.getMoney();
        boolean result = player.buyUtility(utility);

        assertTrue("Player should be able to buy the utility", result);
        assertEquals(initialMoney - utility.getPrice(), player.getMoney());
        assertEquals(player, utility.getOwner());
    }

    @Test
    public void testPayRent() {
        // Create another player to receive rent
        Player owner = new Player("Owner");

        // Test paying rent
        int initialMoney = player.getMoney();
        int initialOwnerMoney = owner.getMoney();
        int rentAmount = 50;

        boolean result = player.payRent(owner, rentAmount);

        assertTrue("Player should be able to pay rent", result);
        assertEquals(initialMoney - rentAmount, player.getMoney());
        assertEquals(initialOwnerMoney + rentAmount, owner.getMoney());
    }

    @Test
    public void testReceiveRent() {
        // Test receiving rent
        int initialMoney = player.getMoney();
        int rentAmount = 50;

        player.receiveRent(rentAmount);

        assertEquals(initialMoney + rentAmount, player.getMoney());
    }

    @Test
    public void testIsBankrupt() {
        // Test bankruptcy status
        assertFalse("Player should not be bankrupt initially", player.isBankrupt());

        // Create a player and make them bankrupt through subtraction
        Player bankruptPlayer = new Player("Bankrupt Player");
        bankruptPlayer.subtractMoney(1500); // Subtract all money
        assertTrue("Player should be bankrupt with 0 money", bankruptPlayer.isBankrupt());
    }

    @Test
    public void testShouldGoToJail() {
        // Test jail conditions - this test needs to set up dice rolls
        // to test it properly, we'd need to do more complex mocking
        // This basic test just verifies the initial state
        assertFalse(player.shouldGoToJail());
    }

    @Test
    public void testToString() {
        // Test string representation contains expected values
        player.setToken("Hat");
        String playerString = player.toString();

        assertTrue(playerString.contains(player.getName()));
        assertTrue(playerString.contains(String.valueOf(player.getMoney())));
        assertTrue(playerString.contains(String.valueOf(player.getPosition())));
        assertTrue(playerString.contains(player.getToken()));
    }

    @Test
    public void testCardEffects() {
        // Create a game state and board for testing
        Gameboard testBoard = new Gameboard();
        List<Player> testPlayers = new ArrayList<>();
        Player testPlayer = new Player("Test Player");
        Player otherPlayer = new Player("Other Player");
        testPlayers.add(testPlayer);
        testPlayers.add(otherPlayer);
        GameState testGameState = new GameState(testPlayers, testBoard);

        // Test Advance to Go
        int initialMoney = testPlayer.getMoney();
        testPlayer.processCardEffect("Advance to Go. Collect $200.", testGameState);
        assertEquals(0, testPlayer.getPosition());
        assertEquals(initialMoney + 200, testPlayer.getMoney());

        // Test Go to Jail
        testPlayer.processCardEffect("Go to Jail. Go directly to Jail.", testGameState);
        assertEquals(10, testPlayer.getPosition());
        assertTrue(testGameState.isPlayerInJail(testPlayer));

    }
    /**
     * Simplified test for handleJailTurn method
     */
    public class HandleJailTurnTest {
        private Player player;
        private Gameboard gameboard;
        private GameState gameState;

        @Before
        public void setUp() {
            // Create game components
            gameboard = new Gameboard();
            player = new Player("Jail Test Player");
            List<Player> players = new ArrayList<>();
            players.add(player);
            gameState = new GameState(players, gameboard);
        }

        /**
         * Helper method to invoke private handleJailTurn method via reflection
         */
        private void invokeHandleJailTurn() throws Exception {
            Method method = player.getClass().getDeclaredMethod("handleJailTurn", GameState.class);
            method.setAccessible(true);
            method.invoke(player, gameState);
        }

        @Test
        public void testPayToGetOutOfJail() throws Exception {
            // Send player to jail
            gameState.sendToJail(player);
            assertTrue(gameState.isPlayerInJail(player));

            // Ensure player has enough money
            player.addMoney(100);
            int initialMoney = player.getMoney();

            // Invoke handleJailTurn
            invokeHandleJailTurn();

            // Verify player is out of jail
            assertFalse(gameState.isPlayerInJail(player));

            // Verify money is deducted
            assertTrue(player.getMoney() < initialMoney);
        }

        @Test
        public void testUseGetOutOfJailFreeCard() throws Exception {
            // Send player to jail
            gameState.sendToJail(player);
            assertTrue(gameState.isPlayerInJail(player));

            // Give player a Get Out of Jail Free card
            player.setHasGetOutOfJailFreeCard(true);

            // Invoke handleJailTurn
            invokeHandleJailTurn();

            // Verify player is out of jail
            assertFalse(gameState.isPlayerInJail(player));

            // Verify card is used
            assertFalse(player.hasGetOutOfJailFreeCard());
        }

        @Test
        public void testIncrementTurnsInJail() throws Exception {
            // Send player to jail
            gameState.sendToJail(player);
            assertTrue(gameState.isPlayerInJail(player));

            // Remember initial turns in jail
            int initialTurns = player.getTurnsInJail();

            // Invoke handleJailTurn
            invokeHandleJailTurn();

            // Verify turns in jail incremented
            assertEquals(initialTurns + 1, player.getTurnsInJail());
        }

        @Test
        public void testMandatoryReleaseAfterThreeTurns() throws Exception {
            // Send player to jail
            gameState.sendToJail(player);

            // Set turns in jail to 3
            player.setTurnsInJail(3);

            // Ensure player has enough money
            player.addMoney(100);
            int initialMoney = player.getMoney();

            // Invoke handleJailTurn
            invokeHandleJailTurn();

            // Verify player is out of jail
            assertFalse(gameState.isPlayerInJail(player));

            // Verify money is deducted
            assertTrue(player.getMoney() < initialMoney);
        }


    }


}