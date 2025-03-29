import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the TitleDeedCard class.
 */
public class TitleDeedCardTest {
    private TitleDeedCard brownDeed;
    private TitleDeedCard blueDeed;

    @Before
    public void setUp() {
        // Create test title deed cards for different properties
        int[] brownHouseRents = {10, 30, 90, 160};
        int[] blueHouseRents = {35, 175, 500, 700};

        brownDeed = new TitleDeedCard("Baltic Avenue", "Brown", 60, 4,
                brownHouseRents, 250, 50);

        blueDeed = new TitleDeedCard("Boardwalk", "Dark Blue", 400, 50,
                blueHouseRents, 2000, 200);
    }

    @Test
    public void testInitialization() {
        // Test that properties are correctly initialized
        Assert.assertEquals("Baltic Avenue", brownDeed.getPropertyName());
        Assert.assertEquals("Brown", brownDeed.getColorGroup());
        Assert.assertEquals(60, brownDeed.getPrice());
        Assert.assertEquals(4, brownDeed.getBaseRent());
        Assert.assertEquals(50, brownDeed.getHouseCost());

        Assert.assertEquals("Boardwalk", blueDeed.getPropertyName());
        Assert.assertEquals("Dark Blue", blueDeed.getColorGroup());
        Assert.assertEquals(400, blueDeed.getPrice());
        Assert.assertEquals(50, blueDeed.getBaseRent());
        Assert.assertEquals(200, blueDeed.getHouseCost());
    }

    @Test
    public void testRentValues() {
        // Test house rents for brown property
        Assert.assertEquals(10, brownDeed.getHouseRent(1));
        Assert.assertEquals(30, brownDeed.getHouseRent(2));
        Assert.assertEquals(90, brownDeed.getHouseRent(3));
        Assert.assertEquals(160, brownDeed.getHouseRent(4));
        Assert.assertEquals(250, brownDeed.getHotelRent());

        // Test house rents for blue property
        Assert.assertEquals(35, blueDeed.getHouseRent(1));
        Assert.assertEquals(175, blueDeed.getHouseRent(2));
        Assert.assertEquals(500, blueDeed.getHouseRent(3));
        Assert.assertEquals(700, blueDeed.getHouseRent(4));
        Assert.assertEquals(2000, blueDeed.getHotelRent());
    }

    @Test
    public void testHouseCosts() {
        // Test house and hotel costs
        Assert.assertEquals(50, brownDeed.getHouseCost());
        Assert.assertEquals(50, brownDeed.getHotelCost()); // Hotel cost same as house cost

        Assert.assertEquals(200, blueDeed.getHouseCost());
        Assert.assertEquals(200, blueDeed.getHotelCost());
    }

    @Test
    public void testMortgageValues() {
        // Test mortgage value (half of property price)
        Assert.assertEquals(30, brownDeed.getMortgageValue());
        Assert.assertEquals(200, blueDeed.getMortgageValue());

        // Test unmortgage cost (mortgage value plus 10% interest)
        Assert.assertEquals(33, brownDeed.getUnmortgageCost());
        Assert.assertEquals(220, blueDeed.getUnmortgageCost());
    }

    @Test
    public void testMortgageStatus() {
        // Test initial mortgage status
        Assert.assertFalse(brownDeed.isMortgaged());

        // Test setting mortgage status
        brownDeed.setMortgaged(true);
        Assert.assertTrue(brownDeed.isMortgaged());

        brownDeed.setMortgaged(false);
        Assert.assertFalse(brownDeed.isMortgaged());
    }

    @Test
    public void testInvalidHouseRent() {
        // Test getting rent for invalid house count
        Assert.assertEquals(4, brownDeed.getHouseRent(0)); // Should return base rent
        Assert.assertEquals(4, brownDeed.getHouseRent(5)); // Should return base rent
        Assert.assertEquals(4, brownDeed.getHouseRent(-1)); // Should return base rent
    }

    @Test
    public void testToString() {
        // Test that toString produces a string containing key information
        String deedString = brownDeed.toString();

        Assert.assertTrue(deedString.contains("Baltic Avenue"));
        Assert.assertTrue(deedString.contains("Brown"));
        Assert.assertTrue(deedString.contains("$60"));
        Assert.assertTrue(deedString.contains("$4"));
        Assert.assertTrue(deedString.contains("$30"));

        // Test mortgage status in string
        brownDeed.setMortgaged(true);
        String mortgagedDeedString = brownDeed.toString();
        Assert.assertTrue(mortgagedDeedString.contains("MORTGAGED"));
        Assert.assertTrue(mortgagedDeedString.contains("$33"));
    }
}