package Model.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Comprehensive test class for the Tokens class to achieve 100% coverage.
 */
public class TokensTest {

    private Tokens tokens;
    private Player player;

    @Before
    public void setUp() {
        // Initialize available tokens before each test
        Tokens.initializeTokens();
        player = new Player("Test Player");
        tokens = new Tokens(); // Create an instance for testing object methods
    }

    @After
    public void tearDown() {
        // Reset tokens after each test to avoid test interference
        Tokens.initializeTokens();
    }

    // Static method tests (previously covered)
    @Test
    public void testInitializeTokens() {
        Tokens.initializeTokens();
        assertTrue(Tokens.isTokenAvailable("Top Hat"));
        assertTrue(Tokens.isTokenAvailable("Thimble"));
    }

    @Test
    public void testIsTokenAvailable() {
        assertTrue(Tokens.isTokenAvailable("Top Hat"));
        Tokens.assignToken("Top Hat");
        assertFalse(Tokens.isTokenAvailable("Top Hat"));
    }

    @Test
    public void testAssignTokenString() {
        assertTrue(Tokens.assignToken("Top Hat"));
        assertFalse(Tokens.assignToken("Top Hat")); // Already taken
    }

    @Test
    public void testAssignTokenNoArgs() {
        // Reset tokens
        Tokens.initializeTokens();

        // Assign tokens
        for (int i = 0; i < Tokens.TOKENS.length; i++) {
            char[] token = Tokens.assignToken();
            assertNotNull(token);
        }

        // One more attempt should return null
        char[] token = Tokens.assignToken();
        assertNull(token);
    }

    @Test
    public void testGetAvailableTokens() {
        String availableTokens = Tokens.getavailabletokens();
        assertTrue(availableTokens.contains("Top Hat"));

        // Assign all tokens
        for (String token : Tokens.TOKENS) {
            Tokens.assignToken(token);
        }

        assertEquals("No tokens available", Tokens.getavailabletokens());
    }

    @Test
    public void testChooseToken() {
        // This method always returns false in the current implementation
        assertFalse(Tokens.chooseToken(player, "Race Car"));
    }

    @Test
    public void testMoveToken() {
        // This method is a no-op (does nothing), so just call it to cover the method
        Tokens.moveToken(player, 5);
    }

    // Instance method tests
    @Test
    public void testConstructor() {
        assertNull(tokens.getOwner());
        assertEquals(0, tokens.getBoardPosition());
    }

    @Test
    public void testSetAndGetOwner() {
        tokens.setOwner("Test Owner");
        assertEquals("Test Owner", tokens.getOwner());
    }

    @Test
    public void testSetAndGetBoardPosition() {
        tokens.setBoardPosition(10);
        assertEquals(10, tokens.getBoardPosition());
    }

    @Test
    public void testToString() {
        tokens.setOwner("Test Player");
        tokens.setBoardPosition(5);

        String tokenString = tokens.toString();
        assertTrue(tokenString.contains("Test Player"));
        assertTrue(tokenString.contains("5"));
    }

    // Additional edge case and coverage tests
    @Test
    public void testTokensConstant() {
        // Verify the TOKENS array contents
        String[] expectedTokens = {
                "Top Hat", "Thimble", "Iron", "Boot", "Battleship",
                "Cannon", "Race Car", "Scottie Dog", "Wheelbarrow"
        };
        assertArrayEquals(expectedTokens, Tokens.TOKENS);
    }

    @Test
    public void testAssignTokenCoverageEdgeCases() {
        // Exhaust all tokens
        for (String token : Tokens.TOKENS) {
            assertTrue(Tokens.assignToken(token));
        }

        // Try to assign an already assigned token
        assertFalse(Tokens.assignToken("Top Hat"));
    }

    @Test
    public void testGetAvailableTokensAfterAssignment() {
        // Assign all but one token
        for (int i = 0; i < Tokens.TOKENS.length - 1; i++) {
            Tokens.assignToken(Tokens.TOKENS[i]);
        }

        // Get available tokens
        String availableTokens = Tokens.getavailabletokens();
        assertTrue(availableTokens.contains(Tokens.TOKENS[Tokens.TOKENS.length - 1]));
    }

    @Test
    public void testDisplayAvailableTokens() {
        // Capture System.out
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(outContent));

        try {
            // Call display method
            Tokens.displayAvailableTokens();

            // Verify output contains available tokens
            assertTrue(outContent.toString().contains("Available tokens:"));
            for (String token : Tokens.TOKENS) {
                assertTrue(outContent.toString().contains(token));
            }
        } finally {
            System.setOut(originalOut);
        }
    }

}