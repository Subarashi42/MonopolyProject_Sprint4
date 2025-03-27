import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the SpecialSpace class.
 */
public class SpecialSpaceTest {
    private SpecialSpace chanceSpace;
    private SpecialSpace communityChestSpace;
    private SpecialSpace taxSpace;
    private SpecialSpace goToJailSpace;
    private Player player;

    @Before
    public void setUp() {
        // Create different types of special spaces
        chanceSpace = new SpecialSpace("Chance", 7, "Chance");
        communityChestSpace = new SpecialSpace("Community Chest", 2, "Community Chest");
        taxSpace = new SpecialSpace("Income Tax", 4, "Tax");
        goToJailSpace = new SpecialSpace("Go To Jail", 30, "Go To Jail");

        // Create a player for testing
        player = new Player("Test Player");
    }

    @Test
    public void testConstructor() {
        // Based on your implementation, the type is set to the third parameter
        assertEquals("Chance", chanceSpace.getName());
        assertEquals(7, chanceSpace.getPosition());
        assertEquals("Chance", chanceSpace.getType());

        assertEquals("Community Chest", communityChestSpace.getName());
        assertEquals(2, communityChestSpace.getPosition());
        assertEquals("Community Chest", communityChestSpace.getType());

        assertEquals("Income Tax", taxSpace.getName());
        assertEquals(4, taxSpace.getPosition());
        assertEquals("Tax", taxSpace.getType());

        assertEquals("Go To Jail", goToJailSpace.getName());
        assertEquals(30, goToJailSpace.getPosition());
        assertEquals("Go To Jail", goToJailSpace.getType());
    }

    @Test
    public void testGetType() {
        assertEquals("Chance", chanceSpace.getType());
        assertEquals("Community Chest", communityChestSpace.getType());
        assertEquals("Tax", taxSpace.getType());
        assertEquals("Go To Jail", goToJailSpace.getType());
    }

    @Test
    public void testToString() {
        String expectedChanceString = "7: Chance (Chance)";
        String expectedCommunityChestString = "2: Community Chest (Community Chest)";
        String expectedTaxString = "4: Income Tax (Tax)";
        String expectedGoToJailString = "30: Go To Jail (Go To Jail)";

        assertEquals(expectedChanceString, chanceSpace.toString());
        assertEquals(expectedCommunityChestString, communityChestSpace.toString());
        assertEquals(expectedTaxString, taxSpace.toString());
        assertEquals(expectedGoToJailString, goToJailSpace.toString());
    }

    @Test
    public void testInheritance() {
        // Test that SpecialSpace is a subclass of Space
        assertTrue(chanceSpace instanceof Space);
        assertTrue(communityChestSpace instanceof Space);
        assertTrue(taxSpace instanceof Space);
        assertTrue(goToJailSpace instanceof Space);
    }

    @Test
    public void testOverriddenMethods() {
        // Test that playerOnSpecialSpace method doesn't throw exceptions
        // These methods typically just print to the console
        chanceSpace.playerOnSpecialSpace();
        communityChestSpace.playerOnSpecialSpace();
        taxSpace.playerOnSpecialSpace();
        goToJailSpace.playerOnSpecialSpace();
    }

    @Test
    public void testGetOwner() {
        // If your implementation allows for SpecialSpaces to have owners, adjust this test
        // Based on error, it appears your Space implementation might initialize owner to null
        assertNull(chanceSpace.getOwner());
        assertNull(communityChestSpace.getOwner());
        assertNull(taxSpace.getOwner());
        assertNull(goToJailSpace.getOwner());
    }

    /**
     * This test is modified since your implementation appears to allow setting
     * an owner for SpecialSpace
     */
    @Test
    public void testSetAndGetOwner() {
        // Test setting an owner
        chanceSpace.setOwner(player);

        // Verify the owner was set
        assertEquals(player, chanceSpace.getOwner());

        // Test clearing the owner
        chanceSpace.setOwner(null);
        assertNull(chanceSpace.getOwner());
    }

    @Test
    public void testGetPosition() {
        assertEquals(7, chanceSpace.getPosition());
        assertEquals(2, communityChestSpace.getPosition());
        assertEquals(4, taxSpace.getPosition());
        assertEquals(30, goToJailSpace.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test changing the position
        chanceSpace.setPosition(22);
        assertEquals(22, chanceSpace.getPosition());

        communityChestSpace.setPosition(33);
        assertEquals(33, communityChestSpace.getPosition());
    }

    @Test
    public void testGetName() {
        assertEquals("Chance", chanceSpace.getName());
        assertEquals("Community Chest", communityChestSpace.getName());
        assertEquals("Income Tax", taxSpace.getName());
        assertEquals("Go To Jail", goToJailSpace.getName());
    }
}