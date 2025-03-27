import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the abstract Space class.
 * Since Space is abstract, we'll test it through a concrete subclass.
 */
public class SpaceTest {

    // Concrete implementation of Space for testing
    private class TestSpace extends Space {
        public TestSpace(String name, int position, String type) {
            super(name, position, type);
        }

        // No need to override methods for this test
    }

    private TestSpace space;
    private Player player;

    @Before
    public void setUp() {
        space = new TestSpace("Test Space", 15, "Test Type");
        player = new Player("Test Player");
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Space", space.getName());
        assertEquals(15, space.getPosition());
        assertEquals("Test Type", space.getType());
        assertNull(space.getOwner());
    }

    @Test
    public void testSetAndGetOwner() {
        assertNull(space.getOwner());
        space.setOwner(player);
        assertEquals(player, space.getOwner());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Space", space.getName());
    }

    @Test
    public void testGetType() {
        assertEquals("Test Type", space.getType());
    }

    @Test
    public void testGetColorGroup() {
        // Default color group is null in Space class
        assertNull(space.getColorGroup());
    }

    @Test
    public void testPlayerOnSpecialSpace() {
        // This method just prints to console, so verify it doesn't throw an exception
        space.playerOnSpecialSpace();
    }

    @Test
    public void testPlayerOnProperty() {
        // This method just prints to console, so verify it doesn't throw an exception
        space.playerOnProperty();
    }

    @Test
    public void testPlayerOnRailroad() {
        // This method just prints to console, so verify it doesn't throw an exception
        space.playerOnRailroad();
    }

    @Test
    public void testPlayerOnCardSpace() {
        // This method just prints to console, so verify it doesn't throw an exception
        space.playerOnCardSpace();
    }

    @Test
    public void testToString() {
        String expected = "15: Test Space (Test Type)";
        assertEquals(expected, space.toString());
    }

    @Test
    public void testGetPosition() {
        assertEquals(15, space.getPosition());
    }

    @Test
    public void testSetPosition() {
        space.setPosition(20);
        assertEquals(20, space.getPosition());
    }

    @Test
    public void testSpaceEquality() {
        // Create another space with the same position
        TestSpace samePositionSpace = new TestSpace("Different Name", 15, "Another Type");
        // Create another space with a different position
        TestSpace differentPositionSpace = new TestSpace("Test Space", 16, "Test Type");

        // Spaces with same position and name should be considered "equal" for many game purposes
        assertEquals(space.getPosition(), samePositionSpace.getPosition());
        assertNotEquals(space.getPosition(), differentPositionSpace.getPosition());
    }

    @Test
    public void testInheritanceRelationship() {
        // Test that our test space is indeed a Space
        assertTrue(space instanceof Space);
    }
}