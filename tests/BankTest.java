import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Bank class.
 */
public class BankTest {
    private Bank bank;
    private Player player;

    @Before
    public void setUp() {
        bank = new Bank();
        player = new Player("Test Player");
    }

    @Test
    public void testInitialization() {
        // A new Bank object should have the default initialization values
        Assert.assertEquals(0, bank.getMoney());
        Assert.assertEquals(32, bank.getHouses());
        Assert.assertEquals(12, bank.getHotels());
        Assert.assertEquals(16, bank.getTokens());
        Assert.assertEquals(16, bank.getChestAndCardSpots());
    }

    @Test
    public void testMoneyManagement() {
        // Test initial money
        Assert.assertEquals(0, bank.getMoney());

        // Test adding money
        bank.addMoney(500);
        Assert.assertEquals(500, bank.getMoney());

        bank.addMoney(1000);
        Assert.assertEquals(1500, bank.getMoney());

        // Test removing money
        bank.removeMoney(300);
        Assert.assertEquals(1200, bank.getMoney());

        bank.removeMoney(1200);
        Assert.assertEquals(0, bank.getMoney());

        // Test setting money directly
        bank.setMoney(2000);
        Assert.assertEquals(2000, bank.getMoney());
    }

    @Test
    public void testHouseManagement() {
        // Test initial houses
        Assert.assertEquals(32, bank.getHouses());

        // Test removing houses
        bank.removeHouses(5);
        Assert.assertEquals(27, bank.getHouses());

        // Test adding houses back
        bank.addHouses(3);
        Assert.assertEquals(30, bank.getHouses());

        // Test setting houses directly
        bank.setHouses(20);
        Assert.assertEquals(20, bank.getHouses());
    }

    @Test
    public void testHotelManagement() {
        // Test initial hotels
        Assert.assertEquals(12, bank.getHotels());

        // Test removing hotels
        bank.removeHotels(4);
        Assert.assertEquals(8, bank.getHotels());

        // Test adding hotels back
        bank.addHotels(2);
        Assert.assertEquals(10, bank.getHotels());

        // Test setting hotels directly
        bank.setHotels(6);
        Assert.assertEquals(6, bank.getHotels());
    }

    @Test
    public void testTokenManagement() {
        // Test initial tokens
        Assert.assertEquals(16, bank.getTokens());

        // Test removing tokens
        bank.removeTokens(4);
        Assert.assertEquals(12, bank.getTokens());

        // Test adding tokens back
        bank.addTokens(2);
        Assert.assertEquals(14, bank.getTokens());

        // Test setting tokens directly
        bank.setTokens(8);
        Assert.assertEquals(8, bank.getTokens());
    }

    @Test
    public void testChestAndCardSpotManagement() {
        // Test initial chest and card spots
        Assert.assertEquals(16, bank.getChestAndCardSpots());

        // Test removing chest and card spots
        bank.removeChestAndCardSpots(6);
        Assert.assertEquals(10, bank.getChestAndCardSpots());

        // Test adding chest and card spots back
        bank.addChestAndCardSpots(3);
        Assert.assertEquals(13, bank.getChestAndCardSpots());

        // Test setting chest and card spots directly
        bank.setChestAndCardSpots(8);
        Assert.assertEquals(8, bank.getChestAndCardSpots());
    }

    @Test
    public void testGiveStartingMoney() {
        // Test initial player money
        Assert.assertEquals(1500, player.getMoney());

        // Bank gives starting money to player
        bank.giveStartingMoney(player);

        // Player should now have double the starting money (1500 + 1500)
        Assert.assertEquals(3000, player.getMoney());

        // Bank should have negative money (giving away money it didn't have)
        Assert.assertEquals(-1500, bank.getMoney());
    }

    @Test
    public void testCompleteGameCycle() {
        // Test a more complex scenario that simulates some game actions

        // 1. Bank gives starting money to player
        bank.giveStartingMoney(player);
        Assert.assertEquals(3000, player.getMoney());
        Assert.assertEquals(-1500, bank.getMoney());

        // 2. Player buys a few houses
        bank.removeHouses(3);
        bank.addMoney(150); // Assuming houses cost 50 each
        player.subtractMoney(150);
        Assert.assertEquals(29, bank.getHouses());
        Assert.assertEquals(-1350, bank.getMoney());
        Assert.assertEquals(2850, player.getMoney());

        // 3. Player buys a hotel
        bank.removeHotels(1);
        bank.addHouses(4); // Return 4 houses when upgrading to hotel
        bank.addMoney(100); // Assuming hotel costs 100
        player.subtractMoney(100);
        Assert.assertEquals(11, bank.getHotels());
        Assert.assertEquals(33, bank.getHouses());
        Assert.assertEquals(-1250, bank.getMoney());
        Assert.assertEquals(2750, player.getMoney());

        // 4. Player pays tax to bank
        bank.addMoney(200); // Tax payment
        player.subtractMoney(200);
        Assert.assertEquals(-1050, bank.getMoney());
        Assert.assertEquals(2550, player.getMoney());
    }
}