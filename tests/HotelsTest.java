import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Hotels class.
 */
public class HotelsTest {
    private Hotels hotels;

    @Before
    public void setUp() {
        hotels = new Hotels();
    }

    @Test
    public void testInitialization() {
        // A new Hotels object should have the default number of hotels (12)
        Assert.assertEquals(12, hotels.getAvailableHotels());
    }

    @Test
    public void testUseHotel() {
        // Test using hotels successfully
        Assert.assertTrue(hotels.useHotel());
        Assert.assertEquals(11, hotels.getAvailableHotels());

        // Use multiple hotels
        for (int i = 0; i < 11; i++) {
            Assert.assertTrue(hotels.useHotel());
        }
        Assert.assertEquals(0, hotels.getAvailableHotels());

        // Test attempting to use more hotels than available
        Assert.assertFalse(hotels.useHotel());
        Assert.assertEquals(0, hotels.getAvailableHotels());
    }

    @Test
    public void testReturnHotel() {
        // Use some hotels first
        for (int i = 0; i < 5; i++) {
            hotels.useHotel();
        }
        Assert.assertEquals(7, hotels.getAvailableHotels());

        // Return some hotels
        hotels.returnHotel();
        Assert.assertEquals(8, hotels.getAvailableHotels());

        hotels.returnHotel();
        Assert.assertEquals(9, hotels.getAvailableHotels());

        // Return all used hotels
        for (int i = 0; i < 3; i++) {
            hotels.returnHotel();
        }
        Assert.assertEquals(12, hotels.getAvailableHotels());

        // Test returning more hotels than were used (should cap at max hotels)
        hotels.returnHotel();
        Assert.assertEquals(12, hotels.getAvailableHotels()); // Should still be 12
    }

    @Test
    public void testReset() {
        // Use some hotels
        for (int i = 0; i < 8; i++) {
            hotels.useHotel();
        }
        Assert.assertEquals(4, hotels.getAvailableHotels());

        // Reset hotels
        hotels.reset();
        Assert.assertEquals(12, hotels.getAvailableHotels());
    }

    @Test
    public void testAreHotelsAvailable() {
        // Initially, hotels should be available
        Assert.assertTrue(hotels.areHotelsAvailable());

        // Use all hotels
        for (int i = 0; i < 12; i++) {
            hotels.useHotel();
        }

        // Now no hotels should be available
        Assert.assertFalse(hotels.areHotelsAvailable());

        // Return one hotel
        hotels.returnHotel();

        // Hotels should be available again
        Assert.assertTrue(hotels.areHotelsAvailable());
    }

    @Test
    public void testGetHotelPrice() {
        // Hotel prices should be the same as house prices for the same color group
        Assert.assertEquals(Houses.getHousePrice("brown"), Hotels.getHotelPrice("brown"));
        Assert.assertEquals(Houses.getHousePrice("light blue"), Hotels.getHotelPrice("light blue"));
        Assert.assertEquals(Houses.getHousePrice("pink"), Hotels.getHotelPrice("pink"));
        Assert.assertEquals(Houses.getHousePrice("orange"), Hotels.getHotelPrice("orange"));
        Assert.assertEquals(Houses.getHousePrice("red"), Hotels.getHotelPrice("red"));
        Assert.assertEquals(Houses.getHousePrice("yellow"), Hotels.getHotelPrice("yellow"));
        Assert.assertEquals(Houses.getHousePrice("green"), Hotels.getHotelPrice("green"));
        Assert.assertEquals(Houses.getHousePrice("dark blue"), Hotels.getHotelPrice("dark blue"));
    }

    @Test
    public void testGetHousesReturnedForHotel() {
        // In standard Monopoly, 4 houses are returned when upgrading to a hotel
        Assert.assertEquals(4, Hotels.getHousesReturnedForHotel());
    }

    @Test
    public void testToString() {
        String expected = "Hotels available: 12 out of 12";
        Assert.assertEquals(expected, hotels.toString());

        for (int i = 0; i < 5; i++) {
            hotels.useHotel();
        }
        expected = "Hotels available: 7 out of 12";
        Assert.assertEquals(expected, hotels.toString());
    }
}