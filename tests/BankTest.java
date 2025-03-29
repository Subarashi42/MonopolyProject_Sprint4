
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Bank class.
 */
public class BankTest {
    private Bank bank;
    private Player player;
    private Property testProperty;

    @Before
    public void setUp() {
        bank = new Bank();
        player = new Player("Test Player");

        // Create a test property for use in tests
        testProperty = new Property("Test Avenue", 5, 200, 10, "Brown");

        // Make property available for purchase
        List<Property> properties = new ArrayList<>();
        properties.add(testProperty);
        bank.setAvailableProperties(properties);
    }

    @Test
    public void testGiveStartingMoney() {
        // Test initial player money
        int initialMoney = player.getMoney();

        // Bank gives starting money to player
        bank.giveStartingMoney(player);

        // Player should now have double the starting money (1500 + 1500)
        Assert.assertEquals(initialMoney + 1500, player.getMoney());
    }

    @Test
    public void testPassGo() {
        // Test giving $200 for passing Go
        int initialMoney = player.getMoney();
        bank.playerPassedGo(player);
        Assert.assertEquals(initialMoney + 200, player.getMoney());
    }

    @Test
    public void testSellProperty() {
        // Initial state
        int initialPlayerMoney = player.getMoney();

        // Player buys property
        boolean result = bank.sellProperty(testProperty, player);

        // Verify property sale was successful
        Assert.assertTrue(result);
        Assert.assertEquals(initialPlayerMoney - testProperty.getPrice(), player.getMoney());
        Assert.assertEquals(player, testProperty.getOwner());
    }

    @Test
    public void testCannotSellPropertyIfPlayerCannotAfford() {
        // Set player money to less than property price
        while (player.getMoney() >= testProperty.getPrice()) {
            player.subtractMoney(100);
        }

        // Try to buy property
        boolean result = bank.sellProperty(testProperty, player);

        // Verify property sale failed
        Assert.assertFalse(result);
        Assert.assertNull(testProperty.getOwner());
    }

    @Test
    public void testAuctionProperty() {
        // This is harder to test as it involves random bids
        // We could mock the random number generator, but for simplicity
        // let's just verify it doesn't throw exceptions

        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(new Player("Player 2"));

        try {
            bank.auctionProperty(testProperty, players);
            // If we get here without exception, test passed
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}