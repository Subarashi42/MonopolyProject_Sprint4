package Model.Board;

import Model.Property.Property;
import Model.Spaces.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Comprehensive test class for the Gameboard class
 */
public class GameboardTest {

    private Gameboard gameboard;
    private Player player;

    @Before
    public void setUp() {
        gameboard = new Gameboard();
        player = new Player("Test Player");
    }

    @Test
    public void testInitialization() {
        // Verify total number of spaces
        List<Space> spaces = gameboard.getSpaces();
        assertEquals(40, spaces.size());

        // Verify specific spaces
        Space goSpace = gameboard.getspace(0);
        assertEquals("Go", goSpace.getName());

        Space jailSpace = gameboard.getspace(10);
        assertEquals("Jail", jailSpace.getName());
    }

    @Test
    public void testGetspace() {
        // Test valid space retrieval
        Space space = gameboard.getspace(1);
        assertNotNull(space);
        assertEquals("Mediterranean Avenue", space.getName());

        // Test out of bounds retrieval
        assertNull(gameboard.getspace(-1));
        assertNull(gameboard.getspace(40));
    }

    @Test
    public void testPrintBoard() {
        // Capture system output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Call print method
            gameboard.printBoard();

            // Verify output is not empty
            String output = outContent.toString();
            assertFalse(output.isEmpty());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testGetSpaces() {
        List<Space> spaces = gameboard.getSpaces();
        assertNotNull(spaces);
        assertEquals(40, spaces.size());
    }

    @Test
    public void testSetSpaces() {
        List<Space> newSpaces = new ArrayList<>();
        newSpaces.add(new GoSpace());

        gameboard.setSpaces(newSpaces);

        assertEquals(newSpaces, gameboard.getSpaces());
    }

    @Test
    public void testPropertyOwnership() {
        // Test initial property ownership map
        Map<Integer, String> originalMap = gameboard.getPropertyOwnership();
        assertNotNull(originalMap);

        // Create and set a new property ownership map
        Map<Integer, String> newMap = new HashMap<>();
        newMap.put(1, "Test Owner");

        gameboard.setPropertyOwnership(newMap);

        assertEquals(newMap, gameboard.getPropertyOwnership());
    }

    @Test
    public void testGetPropertiesByColorGroup() {
        // Test Brown color group
        List<Property> brownProperties = gameboard.getPropertiesByColorGroup("Brown");
        assertNotNull(brownProperties);

        // Verify Brown properties
        boolean mediterraneanFound = false;
        boolean balticFound = false;

        for (Property prop : brownProperties) {
            if (prop.getName().equals("Mediterranean Avenue")) mediterraneanFound = true;
            if (prop.getName().equals("Baltic Avenue")) balticFound = true;
        }

        assertTrue(mediterraneanFound);
        assertTrue(balticFound);
    }

    @Test
    public void testGetRailroads() {
        List<RailroadSpace> railroads = gameboard.getRailroads();

        assertEquals(4, railroads.size());

        // Verify specific railroads
        boolean readingFound = false;
        boolean pennFound = false;
        boolean boFound = false;
        boolean shortLineFound = false;

        for (RailroadSpace railroad : railroads) {
            switch (railroad.getName()) {
                case "Reading Railroad":
                    readingFound = true;
                    break;
                case "Pennsylvania Railroad":
                    pennFound = true;
                    break;
                case "B. & O. Railroad":
                    boFound = true;
                    break;
                case "Short Line":
                    shortLineFound = true;
                    break;
            }
        }

        assertTrue(readingFound);
        assertTrue(pennFound);
        assertTrue(boFound);
        assertTrue(shortLineFound);
    }

    @Test
    public void testGetUtilities() {
        List<UtilitySpace> utilities = gameboard.getUtilities();

        assertEquals(2, utilities.size());

        // Verify specific utilities
        boolean electricFound = false;
        boolean waterFound = false;

        for (UtilitySpace utility : utilities) {
            if (utility.getName().equals("Electric Company")) electricFound = true;
            if (utility.getName().equals("Water Works")) waterFound = true;
        }

        assertTrue(electricFound);
        assertTrue(waterFound);
    }

    @Test
    public void testPlayerOwnsAllInColorGroup() {
        // Create brown properties
        List<Property> brownProperties = gameboard.getPropertiesByColorGroup("Brown");

        // Initially, player should not own color group
        assertFalse(gameboard.playerOwnsAllInColorGroup(player, "Brown"));

        // Set player as owner of all brown properties
        for (Property prop : brownProperties) {
            prop.setOwner(player);
        }

        // Now player should own the color group
        assertTrue(gameboard.playerOwnsAllInColorGroup(player, "Brown"));

        // Test with null player
        assertFalse(gameboard.playerOwnsAllInColorGroup(null, "Brown"));

        // Test with empty color group
        assertFalse(gameboard.playerOwnsAllInColorGroup(player, ""));
    }

    @Test
    public void testPlayerOwnsAllInColorGroup_EmptyGroup() {
        // Test with a non-existent color group
        assertFalse(gameboard.playerOwnsAllInColorGroup(player, "Nonexistent Color"));
    }
}