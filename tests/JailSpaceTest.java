import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the JailSpace class.
 */
public class JailSpaceTest {
    private JailSpace jailSpace;
    private Player player;
    private Gameboard board;
    private GameState gameState;

    @Before
    public void setUp() {
        jailSpace = new JailSpace();
        player = new Player("Test Player");
        board = new Gameboard();
        List<Player> players = new ArrayList<>();
        players.add(player);
        gameState = new GameState(players, board);
    }

    @Test
    public void testConstructor() {
        assertEquals("Jail", jailSpace.getName());
        assertEquals(10, jailSpace.getPosition());
        assertEquals("Jail", jailSpace.getType());
    }

    @Test
    public void testOnLandJustVisiting() {
        // Player lands on Jail but is not in jail (just visiting)
        int initialMoney = player.getMoney();
        jailSpace.onLand(player, gameState);

        // Money should not change
        assertEquals(initialMoney, player.getMoney());
        // Player should not be in jail
        assertFalse(gameState.isPlayerInJail(player));
    }

    @Test
    public void testGoToJail() {
        // Player is at position 5
        player.setPosition(5);

        // Send player to jail
        JailSpace.goToJail(player, gameState);

        // Player should be at position 10 (jail)
        assertEquals(10, player.getPosition());
        // Player should be in jail
        assertTrue(gameState.isPlayerInJail(player));
    }

    @Test
    public void testPayToGetOut() {
        // Send player to jail first
        JailSpace.goToJail(player, gameState);

        // Initial money
        int initialMoney = player.getMoney();

        // Pay to get out
        boolean success = jailSpace.payToGetOut(player, gameState);

        // Should be successful
        assertTrue(success);
        // Player should no longer be in jail
        assertFalse(gameState.isPlayerInJail(player));
        // Player should have paid $50
        assertEquals(initialMoney - 50, player.getMoney());
    }

    @Test
    public void testPayToGetOutInsufficientFunds() {
        // Send player to jail first
        JailSpace.goToJail(player, gameState);

        // Set player money to $40 (not enough to pay $50 fee)
        player.deductMoney(player.getMoney() - 40);

        // Attempt to pay to get out
        boolean success = jailSpace.payToGetOut(player, gameState);

        // Should fail
        assertFalse(success);
        // Player should still be in jail
        assertTrue(gameState.isPlayerInJail(player));
        // Player should still have $40
        assertEquals(40, player.getMoney());
    }

    @Test
    public void testPayToGetOutNotInJail() {
        // Player is not in jail
        int initialMoney = player.getMoney();

        // Attempt to pay to get out
        boolean success = jailSpace.payToGetOut(player, gameState);

        // Should fail
        assertFalse(success);
        // Money should not change
        assertEquals(initialMoney, player.getMoney());
    }

    @Test
    public void testTryRollForFreedomSuccessful() {
        // Send player to jail
        JailSpace.goToJail(player, gameState);

        // Create fake dice that will roll doubles
        Dice fakeDice = new Dice() {
            @Override
            public int rollDice() {
                return 4; // Return value doesn't matter for this test
            }

            @Override
            public int getDie1Value() {
                return 2; // Both dice return 2 (doubles)
            }

            @Override
            public int getDie2Value() {
                return 2;
            }
        };

        // Set the fake dice in game state
        gameState.setDice(fakeDice);

        // Try to roll for freedom
        boolean success = jailSpace.tryRollForFreedom(player, gameState);

        // Should be successful
        assertTrue(success);
        // Player should no longer be in jail
        assertFalse(gameState.isPlayerInJail(player));
        // Player should have moved based on the roll (position 10 + 4 = 14)
        assertEquals(14, player.getPosition());
    }

    @Test
    public void testTryRollForFreedomUnsuccessful() {
        // Send player to jail
        JailSpace.goToJail(player, gameState);

        // Create fake dice that won't roll doubles
        Dice fakeDice = new Dice() {
            @Override
            public int rollDice() {
                return 5; // Return value doesn't matter for this test
            }

            @Override
            public int getDie1Value() {
                return 2;
            }

            @Override
            public int getDie2Value() {
                return 3; // Different values, not doubles
            }
        };

        // Set the fake dice in game state
        gameState.setDice(fakeDice);

        // Try to roll for freedom
        boolean success = jailSpace.tryRollForFreedom(player, gameState);

        // Should fail
        assertFalse(success);
        // Player should still be in jail
        assertTrue(gameState.isPlayerInJail(player));
        // Position should not change
        assertEquals(10, player.getPosition());
    }

    @Test
    public void testTryRollForFreedomNotInJail() {
        // Player is not in jail
        boolean success = jailSpace.tryRollForFreedom(player, gameState);

        // Should fail
        assertFalse(success);
    }

    @Test
    public void testUseGetOutOfJailFreeCard() {
        // This test provides a basic structure, but the actual implementation
        // will depend on how your Get Out of Jail Free card system works

        // Send player to jail
        JailSpace.goToJail(player, gameState);

        // Since the implementation of useGetOutOfJailFreeCard() is a placeholder,
        // we can just verify it doesn't throw exceptions
        jailSpace.useGetOutOfJailFreeCard(player, gameState);
    }

    @Test
    public void testPlayerOnSpecialSpace() {
        // This method just prints to console, so verify it doesn't throw an exception
        jailSpace.playerOnSpecialSpace();
    }

    @Test
    public void testInheritance() {
        assertTrue(jailSpace instanceof SpecialSpace);
        assertTrue(jailSpace instanceof Space);
    }
}