import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

/**
 * Test class for the Tokens class.
 */
public class TokensTest {

    @Before
    public void setUp() {
        Tokens.initializeTokens();
    }

    @After
    public void tearDown() {
        // Reset tokens after each test
        Tokens.initializeTokens();
    }

    @Test
    public void testInitialization() {
        // After initialization, all tokens should be available
        for (String token : Tokens.TOKENS) {
            assertTrue("Token " + token + " should be available after initialization",
                    Tokens.isTokenAvailable(token));
        }
    }

    @Test
    public void testAssignToken() {
        // Test assigning an available token
        String token = "Top Hat";
        assertTrue(Tokens.isTokenAvailable(token));
        boolean assigned = Tokens.assignToken(token);
        assertTrue("Should be able to assign an available token", assigned);
        assertFalse("Token should not be available after assigning",
                Tokens.isTokenAvailable(token));

        // Test assigning an already assigned token
        assigned = Tokens.assignToken(token);
        assertFalse("Should not be able to assign an already assigned token", assigned);

        // Test assigning all tokens
        for (String t : Tokens.TOKENS) {
            if (!t.equals(token)) { // Skip the already assigned token
                Tokens.assignToken(t);
            }
        }

        // Verify no tokens are available
        for (String t : Tokens.TOKENS) {
            assertFalse("No tokens should be available after assigning all",
                    Tokens.isTokenAvailable(t));
        }
    }

    @Test
    public void testTokenInstance() {
        // Test the Tokens constructor and instance methods
        Tokens tokenInstance = new Tokens();

        // Test initial state
        assertNull(tokenInstance.getOwner());
        assertEquals(0, tokenInstance.getBoardPosition());

        // Test setting and getting owner
        tokenInstance.setOwner("Player 1");
        assertEquals("Player 1", tokenInstance.getOwner());

        // Test setting and getting board position
        tokenInstance.setBoardPosition(10);
        assertEquals(10, tokenInstance.getBoardPosition());

        // Test toString method
        assertEquals("Player 1 - 10", tokenInstance.toString());
    }

    @Test
    public void testDisplayAvailableTokens() {
        // This method just prints to console, so we'll just make sure it doesn't throw an exception
        Tokens.displayAvailableTokens();

        // Assign some tokens
        Tokens.assignToken("Top Hat");
        Tokens.assignToken("Battleship");

        // Call display again
        Tokens.displayAvailableTokens();
    }
}