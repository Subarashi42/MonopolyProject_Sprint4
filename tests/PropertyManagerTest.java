import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Test class for the PropertyManager class.
 */
public class PropertyManagerTest {
    private PropertyManager propertyManager;
    private Property property1;
    private Property property2;
    private Property property3;

    @Before
    public void setUp() {
        propertyManager = new PropertyManager();

        // Create some test properties
        property1 = new Property("Baltic Avenue", 3, 60, 4, "Brown");
        property2 = new Property("Oriental Avenue", 6, 100, 6, "Light Blue");
        property3 = new Property("Boardwalk", 39, 400, 50, "Dark Blue");
    }

    @Test
    public void testInitialization() {
        // A new PropertyManager should have no properties
        Assert.assertNull(propertyManager.getProperty(1)); // Should return null for any position
    }

    @Test
    public void testAddProperty() {
        // Add a property
        propertyManager.addProperty(property1);

        // Should be able to retrieve it by position
        Property retrieved = propertyManager.getProperty(3);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(property1, retrieved);

        // Add more properties
        propertyManager.addProperty(property2);
        propertyManager.addProperty(property3);

        // Verify all properties can be retrieved
        Assert.assertEquals(property1, propertyManager.getProperty(3));
        Assert.assertEquals(property2, propertyManager.getProperty(6));
        Assert.assertEquals(property3, propertyManager.getProperty(39));
    }

    @Test
    public void testRemoveProperty() {
        // Add properties
        propertyManager.addProperty(property1);
        propertyManager.addProperty(property2);
        propertyManager.addProperty(property3);

        // Remove one property
        propertyManager.removeProperty(6);

        // Verify it's gone
        Assert.assertNull(propertyManager.getProperty(6));

        // Other properties should still be there
        Assert.assertEquals(property1, propertyManager.getProperty(3));
        Assert.assertEquals(property3, propertyManager.getProperty(39));
    }

    @Test
    public void testGetNonExistentProperty() {
        // Add some properties
        propertyManager.addProperty(property1);
        propertyManager.addProperty(property3);

        // Try to get a property at a position where none exists
        Property nonExistent = propertyManager.getProperty(10);
        Assert.assertNull(nonExistent);
    }

    @Test
    public void testDisplayProperties() {
        // Capture System.out to verify display
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Add properties
            propertyManager.addProperty(property1);
            propertyManager.addProperty(property2);

            // Display properties
            propertyManager.displayProperties();

            // Check if output contains property information
            String output = outContent.toString();
            Assert.assertTrue(output.contains("Baltic Avenue"));
            Assert.assertTrue(output.contains("Oriental Avenue"));
            Assert.assertFalse(output.contains("Boardwalk")); // Not added yet

            // Add another property and display again
            propertyManager.addProperty(property3);
            outContent.reset();
            propertyManager.displayProperties();

            // Check output again
            output = outContent.toString();
            Assert.assertTrue(output.contains("Baltic Avenue"));
            Assert.assertTrue(output.contains("Oriental Avenue"));
            Assert.assertTrue(output.contains("Boardwalk")); // Now it should be there
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }

    @Test
    public void testAddDuplicateProperties() {
        // Add a property
        propertyManager.addProperty(property1);

        // Create a duplicate property with the same position
        Property duplicateProperty = new Property("Duplicate Baltic", 3, 70, 5, "Brown");
        propertyManager.addProperty(duplicateProperty);

        // Both properties should be in the list
        // When retrieving by position, we'll get the first one that matches
        Property retrieved = propertyManager.getProperty(3);
        Assert.assertNotNull(retrieved);

        // Note: The exact behavior depends on the implementation of getProperty
        // If it returns the first match in a list, we'd get property1.
        // If it returns the last added, we'd get duplicateProperty.
        // The test just verifies that something is returned.
    }

    @Test
    public void testRemoveNonExistentProperty() {
        // Add some properties
        propertyManager.addProperty(property1);
        propertyManager.addProperty(property3);

        // Try to remove a property that doesn't exist
        propertyManager.removeProperty(10);

        // Existing properties should still be there
        Assert.assertEquals(property1, propertyManager.getProperty(3));
        Assert.assertEquals(property3, propertyManager.getProperty(39));
    }
}