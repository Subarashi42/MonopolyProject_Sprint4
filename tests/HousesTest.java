import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Houses class.
 */
public class HousesTest {
    private Houses houses;

    @Before
    public void setUp() {
        houses = new Houses();
    }

    @Test
    public void testInitialization() {
        // A new Houses object should have the default number of houses (32)
        Assert.assertEquals(32, houses.getAvailableHouses());
    }

    @Test
    public void testUseHouses() {
        // Test using houses successfully
        Assert.assertTrue(houses.useHouses(5));
        Assert.assertEquals(27, houses.getAvailableHouses());

        // Test using more houses
        Assert.assertTrue(houses.useHouses(10));
        Assert.assertEquals(17, houses.getAvailableHouses());

        // Test using all remaining houses
        Assert.assertTrue(houses.useHouses(17));
        Assert.assertEquals(0, houses.getAvailableHouses());

        // Test attempting to use more houses than available
        Assert.assertFalse(houses.useHouses(1));
        Assert.assertEquals(0, houses.getAvailableHouses());
    }

    @Test
    public void testReturnHouses() {
        // Use some houses first
        houses.useHouses(20);
        Assert.assertEquals(12, houses.getAvailableHouses());

        // Return some houses
        houses.returnHouses(5);
        Assert.assertEquals(17, houses.getAvailableHouses());

        // Return all used houses
        houses.returnHouses(15);
        Assert.assertEquals(32, houses.getAvailableHouses());

        // Test returning more houses than were used (should cap at max houses)
        houses.returnHouses(10);
        Assert.assertEquals(32, houses.getAvailableHouses()); // Should still be 32
    }

    @Test
    public void testReset() {
        // Use some houses
        houses.useHouses(25);
        Assert.assertEquals(7, houses.getAvailableHouses());

        // Reset houses
        houses.reset();
        Assert.assertEquals(32, houses.getAvailableHouses());
    }

    @Test
    public void testGetHousePrice() {
        // Test house prices for different color groups
        Assert.assertEquals(50, Houses.getHousePrice("brown"));
        Assert.assertEquals(50, Houses.getHousePrice("light blue"));

        Assert.assertEquals(100, Houses.getHousePrice("pink"));
        Assert.assertEquals(100, Houses.getHousePrice("orange"));

        Assert.assertEquals(150, Houses.getHousePrice("red"));
        Assert.assertEquals(150, Houses.getHousePrice("yellow"));

        Assert.assertEquals(200, Houses.getHousePrice("green"));
        Assert.assertEquals(200, Houses.getHousePrice("dark blue"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHousePriceInvalidColor() {
        // Test getting house price for an invalid color group
        Houses.getHousePrice("purple");
    }

    @Test
    public void testIsShortage() {
        // Initially, there should be no shortage
        Assert.assertFalse(houses.isShortage());

        // Use houses until we have less than 4 left
        houses.useHouses(29);
        Assert.assertEquals(3, houses.getAvailableHouses());
        Assert.assertTrue(houses.isShortage());

        // Return houses to resolve shortage
        houses.returnHouses(1);
        Assert.assertEquals(4, houses.getAvailableHouses());
        Assert.assertFalse(houses.isShortage());
    }

    @Test
    public void testToString() {
        String expected = "Houses available: 32 out of 32";
        Assert.assertEquals(expected, houses.toString());

        houses.useHouses(12);
        expected = "Houses available: 20 out of 32";
        Assert.assertEquals(expected, houses.toString());
    }
}