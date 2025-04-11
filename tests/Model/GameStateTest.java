package Model;

import Model.Board.Bank;
import Model.Board.Dice;
import Model.Board.Gameboard;
import Model.Board.Player;
import Model.Property.Property;
import Model.Spaces.RailroadSpace;
import Model.Spaces.Space;
import Model.Spaces.UtilitySpace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for the GameState class
 * Provides comprehensive testing for all methods and edge cases
 */
public class GameStateTest {

    private GameState gameState;
    private Gameboard board;
    private Player player1;
    private Player player2;
    private List<Player> players;
    private Bank bank;
    private Dice mockDice;

    /**
     * Setup method to initialize test objects before each test
     */
    @Before
    public void setUp() {
        // Create game board
        board = new Gameboard();

        // Create players with zero initial money to avoid initialization issues
        player1 = new Player("Test Player 1");
        player2 = new Player("Test Player 2");

        // Reset player money to ensure tests start with a clean state
        resetPlayerMoney();

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        // Create bank and set initial properties
        bank = new Bank();
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            properties.add(new Property("Test Property " + i, i, 100, "Red"));
        }
        bank.setAvailableProperties(properties);

        // Create game state and set bank
        gameState = new GameState(players, board);
        gameState.setBank(bank);

        // Create mock dice for tests that need controlled dice rolls
        mockDice = new Dice();
    }

    /**
     * Helper method to reset player money to zero
     */
    private void resetPlayerMoney() {
        player1.subtractMoney(player1.getMoney());
        player2.subtractMoney(player2.getMoney());
        assertEquals(0, player1.getMoney());
        assertEquals(0, player2.getMoney());
    }

    /**
     * Test initialization of GameState
     */
    @Test
    public void testInitialization() {
        // Test initial game state
        assertEquals(players, gameState.getPlayers());
        assertEquals(board, gameState.getBoard());
        assertEquals(0, gameState.getCurrentPlayerIndex());
        assertEquals(player1, gameState.getCurrentPlayer());
        assertTrue(gameState.isGameActive());
        assertFalse(gameState.isPlayerInJail(player1));
        assertFalse(gameState.isPlayerInJail(player2));
    }

    /**
     * Test game initialization
     */
    @Test
    public void testInitializeGame() {
        // Reset player money before initialization
        resetPlayerMoney();

        // Initialize game
        gameState.initializeGame();

        // Check starting money was given
        assertEquals(1500, player1.getMoney());
        assertEquals(1500, player2.getMoney());

        // Check game state was reset
        assertTrue(gameState.isGameActive());
        assertEquals(0, gameState.getCurrentPlayerIndex());
        assertFalse(gameState.isPlayerInJail(player1));
        assertFalse(gameState.isPlayerInJail(player2));
    }

    /**
     * Test nextTurn method
     */
    @Test
    public void testNextTurn() {
        // Check initial player is player1
        assertEquals(player1, gameState.getCurrentPlayer());

        // Move to next turn
        gameState.nextTurn();

        // Check current player is now player2
        assertEquals(player2, gameState.getCurrentPlayer());

        // Move to next turn again
        gameState.nextTurn();

        // Check it cycles back to player1
        assertEquals(player1, gameState.getCurrentPlayer());
    }

    /**
     * Test rollDice method
     */
    @Test
    public void testRollDice() {
        // Set predictable dice
        gameState.setDice(mockDice);

        // Roll dice
        int roll = gameState.rollDice();

        // Check roll is within valid range (2-12)
        assertTrue(roll >= 2 && roll <= 12);

        // Check dice values match the roll
        int[] diceValues = gameState.getDiceValues();
        assertEquals(2, diceValues.length);
        assertEquals(roll, diceValues[0] + diceValues[1]);
    }

    /**
     * Test jail functionality
     */
    @Test
    public void testJailFunctionality() {
        // Test player not in jail initially
        assertFalse(gameState.isPlayerInJail(player1));

        // Send player to jail
        gameState.sendToJail(player1);

        // Check player is in jail
        assertTrue(gameState.isPlayerInJail(player1));
        assertEquals(10, player1.getPosition()); // Jail position

        // Release player from jail
        gameState.releaseFromJail(player1);

        // Check player is no longer in jail
        assertFalse(gameState.isPlayerInJail(player1));
        assertEquals(0, player1.getTurnsInJail());
    }

    /**
     * Test drawing Chance card
     */
    @Test
    public void testDrawChanceCard() {
        // Draw a chance card
        String cardDescription = gameState.drawChanceCard();

        // Check card description is not empty
        assertNotNull(cardDescription);
        assertFalse(cardDescription.isEmpty());
    }

    /**
     * Test drawing Community Chest card
     */
    @Test
    public void testDrawCommunityChestCard() {
        // Draw a community chest card
        String cardDescription = gameState.drawCommunityChestCard();

        // Check card description is not empty
        assertNotNull(cardDescription);
        assertFalse(cardDescription.isEmpty());
    }

    /**
     * Test returning Get Out of Jail Free card
     */
    @Test
    public void testReturnGetOutOfJailFreeCard() {
        // Get initial deck sizes
        int initialChanceSize = gameState.getChanceCardDeck().size();
        int initialCommunityChestSize = gameState.getCommunityChestCardDeck().size();

        // Return cards to each deck
        gameState.returnGetOutOfJailFreeCard("Chance");
        gameState.returnGetOutOfJailFreeCard("Community Chest");

        // Check decks have one more card each
        assertEquals(initialChanceSize + 1, gameState.getChanceCardDeck().size());
        assertEquals(initialCommunityChestSize + 1, gameState.getCommunityChestCardDeck().size());
    }

    /**
     * Test transferring money between players
     */
    @Test
    public void testTransferMoney() {
        // Reset player money then set to known values
        resetPlayerMoney();
        player1.addMoney(1000);
        player2.addMoney(500);

        // Transfer money
        boolean result = gameState.transferMoney(player1, player2, 300);

        // Check result and money was transferred
        assertTrue(result);
        assertEquals(700, player1.getMoney());
        assertEquals(800, player2.getMoney());

        // Try to transfer more money than player has
        result = gameState.transferMoney(player1, player2, 800);

        // Check result and money was not transferred
        assertFalse(result);
        assertEquals(700, player1.getMoney());
        assertEquals(800, player2.getMoney());
    }

    /**
     * Test collecting money from bank
     */
    @Test
    public void testCollectFromBank() {
        // Give player some initial money
        player1.addMoney(500);

        // Collect money from bank
        gameState.collectFromBank(player1, 200);

        // Check money was added
        assertEquals(700, player1.getMoney());
    }

    /**
     * Test paying money to bank
     */
    @Test
    public void testPayToBank() {
        // Give player some initial money
        player1.addMoney(500);

        // Pay to bank
        boolean result = gameState.payToBank(player1, 200);

        // Check result and money was paid
        assertTrue(result);
        assertEquals(300, player1.getMoney());

        // Try to pay more money than player has
        result = gameState.payToBank(player1, 500);

        // Check result and money was not paid
        assertFalse(result);
        assertEquals(300, player1.getMoney());
    }

    /**
     * Test handling player bankruptcy
     */
    @Test
    public void testHandlePlayerBankruptcy() {
        // Give player1 a property
        Property property = new Property("Test Property", 1, 200, "Brown");
        property.setOwner(player1);
        player1.getProperties().add(property);

        // Make player bankrupt
        player1.subtractMoney(player1.getMoney()); // Remove all money

        // Handle bankruptcy
        gameState.handlePlayerBankruptcy(player1);

        // Check player was removed
        assertFalse(players.contains(player1));
        assertEquals(1, players.size());

        // Check property was returned to bank (owner set to null)
        assertNull(property.getOwner());

        // Check if game is over with only one player
        assertFalse(gameState.isGameActive());
    }

    /**
     * Test getters and setters
     */
    @Test
    public void testGettersAndSetters() {
        // Test dice getter/setter
        Dice newDice = new Dice();
        gameState.setDice(newDice);
        assertEquals(newDice, gameState.getDice());

        // Test players getter/setter
        List<Player> newPlayers = new ArrayList<>();
        Player player3 = new Player("Test Player 3");
        newPlayers.add(player3);
        gameState.setPlayers(newPlayers);
        assertEquals(newPlayers, gameState.getPlayers());

        // Test board getter/setter
        Gameboard newBoard = new Gameboard();
        gameState.setBoard(newBoard);
        assertEquals(newBoard, gameState.getBoard());

        // Test current player index getter/setter
        gameState.setCurrentPlayerIndex(0);
        assertEquals(0, gameState.getCurrentPlayerIndex());

        // Test game active getter/setter
        gameState.setGameActive(false);
        assertFalse(gameState.isGameActive());
    }

    /**
     * Test Chance and Community Chest decks
     */
    @Test
    public void testGetCardDecks() {
        // Check decks are not null
        assertNotNull(gameState.getChanceCardDeck());
        assertNotNull(gameState.getCommunityChestCardDeck());

        // Check decks have cards
        assertFalse(gameState.getChanceCardDeck().isEmpty());
        assertFalse(gameState.getCommunityChestCardDeck().isEmpty());
    }

    /**
     * Test property ownership map
     */
    @Test
    public void testGetPropertyOwnership() {
        // Get property ownership map
        Map<Integer, String> propertyOwnership = gameState.getPropertyOwnership();

        // Check map is not null
        assertNotNull(propertyOwnership);
    }

    /**
     * Test bank getter and setter
     */
    @Test
    public void testGetBank() {
        // Check bank is correct
        assertEquals(bank, gameState.getBank());
    }

    /**
     * Test setting an invalid current player index
     */
    @Test
    public void testSetInvalidCurrentPlayerIndex() {
        // Set initial players
        gameState.setPlayers(players);

        // Try to set invalid index (too high)
        gameState.setCurrentPlayerIndex(999);

        // Should not change as it's invalid
        assertEquals(0, gameState.getCurrentPlayerIndex());

        // Try to set invalid index (negative)
        gameState.setCurrentPlayerIndex(-1);

        // Should not change as it's invalid
        assertEquals(0, gameState.getCurrentPlayerIndex());
    }

    /**
     * Test getting current player with different indices
     */
    @Test
    public void testGetCurrentPlayerWithDifferentIndices() {
        // Initial player should be player1
        assertEquals(player1, gameState.getCurrentPlayer());

        // Set to player2
        gameState.setCurrentPlayerIndex(1);
        assertEquals(player2, gameState.getCurrentPlayer());

        // Set back to player1
        gameState.setCurrentPlayerIndex(0);
        assertEquals(player1, gameState.getCurrentPlayer());
    }

    /**
     * Test edge case: initialize game with no players
     */
    @Test
    public void testInitializeGameWithNoPlayers() {
        // Create game state with empty players list
        List<Player> emptyPlayers = new ArrayList<>();
        GameState emptyGameState = new GameState(emptyPlayers, board);
        emptyGameState.setBank(bank);

        // Initialize game should not throw exceptions
        emptyGameState.initializeGame();

        // Game should still be active
        assertTrue(emptyGameState.isGameActive());
    }

    /**
     * Test setting different types of dice
     */
    @Test
    public void testSetVariousDiceTypes() {
        // Create dice that always returns the same value
        Dice fixedDice = new Dice() {
            @Override
            public int rollDice() {
                return 8;
            }

            @Override
            public int getDie1Value() {
                return 4;
            }

            @Override
            public int getDie2Value() {
                return 4;
            }
        };

        // Set the fixed dice
        gameState.setDice(fixedDice);

        // Roll and check results
        int roll = gameState.rollDice();
        assertEquals(8, roll);

        int[] diceValues = gameState.getDiceValues();
        assertEquals(4, diceValues[0]);
        assertEquals(4, diceValues[1]);
    }

    /**
     * Test multiple players in jail simultaneously
     */
    @Test
    public void testMultiplePlayersInJail() {
        // Send both players to jail
        gameState.sendToJail(player1);
        gameState.sendToJail(player2);

        // Check both are in jail
        assertTrue(gameState.isPlayerInJail(player1));
        assertTrue(gameState.isPlayerInJail(player2));

        // Release just one player
        gameState.releaseFromJail(player1);

        // Check player1 is out but player2 still in
        assertFalse(gameState.isPlayerInJail(player1));
        assertTrue(gameState.isPlayerInJail(player2));
    }

    @Test
    public void testHandlePlayerBankruptcyWithRailroadsAndUtilities() {
        // Create a test player with properties, railroads, and utilities
        Player bankruptPlayer = new Player("Bankrupt");
        players.add(bankruptPlayer);

        // Add some properties to the player
        Property property = new Property("Test Property", 1, 200, "Brown");
        property.setOwner(bankruptPlayer);
        bankruptPlayer.getProperties().add(property);

        // Find a railroad and utility to assign to the player
        RailroadSpace railroad = null;
        UtilitySpace utility = null;

        for (Space space : board.getSpaces()) {
            if (space instanceof RailroadSpace && railroad == null) {
                railroad = (RailroadSpace) space;
                railroad.setOwner(bankruptPlayer);
            } else if (space instanceof UtilitySpace && utility == null) {
                utility = (UtilitySpace) space;
                utility.setOwner(bankruptPlayer);
            }

            if (railroad != null && utility != null) break;
        }

        // Make player bankrupt
        bankruptPlayer.subtractMoney(1500); // Remove all money

        // Make sure these were set properly
        assertNotNull("Railroad should have been found and owned by player", railroad);
        assertNotNull("Utility should have been found and owned by player", utility);
        assertEquals(bankruptPlayer, railroad.getOwner());
        assertEquals(bankruptPlayer, utility.getOwner());

        // Handle bankruptcy
        gameState.handlePlayerBankruptcy(bankruptPlayer);

        // Check player was removed
        assertFalse(players.contains(bankruptPlayer));

        // Check property was returned to bank
        assertNull("Property owner should be null after bankruptcy", property.getOwner());

        // Check railroad and utility owners are reset
        assertNull("Railroad owner should be null after bankruptcy", railroad.getOwner());
        assertNull("Utility owner should be null after bankruptcy", utility.getOwner());
    }
}