import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokensTest {

    @BeforeEach
    void setUp() {
        Tokens.initializeTokens(); // Reset tokens before each test
    }

    @Test
    void testInitializeTokens() {
        // Verify that all tokens are initialized and available
        assertTrue(Tokens.isTokenAvailable("Top Hat"));
        assertTrue(Tokens.isTokenAvailable("Iron"));
        assertTrue(Tokens.isTokenAvailable("Boot"));
        assertTrue(Tokens.isTokenAvailable("Battleship"));
        assertTrue(Tokens.isTokenAvailable("Cannon"));
        assertTrue(Tokens.isTokenAvailable("Race Car"));
        assertTrue(Tokens.isTokenAvailable("Scottie Dog"));
        assertTrue(Tokens.isTokenAvailable("Wheelbarrow"));
        assertTrue(Tokens.isTokenAvailable("Thimble"));
    }

    @Test
    void testIsTokenAvailable() {
        // Test that tokens are available initially
        assertTrue(Tokens.isTokenAvailable("Battleship"));

        // Assign the token to simulate the user picking it
        Tokens.assignToken("Battleship");

        // Test that after assigning a token, it's no longer available
        assertFalse(Tokens.isTokenAvailable("Battleship"));
    }

    @Test
    void testAssignToken() {
        // Test that assigning an available token works
        assertTrue(Tokens.assignToken("Cannon"));
        assertFalse(Tokens.isTokenAvailable("Cannon")); // It should no longer be available

        // Test that assigning an unavailable token returns false
        assertFalse(Tokens.assignToken("Cannon"));
    }

    @Test
    void testAssignTokenWhenNoTokensAvailable() {
        // Assign all tokens
        for (String token : Tokens.TOKENS) {
            Tokens.assignToken(token);
        }

        // Check that no tokens are available now
        for (String token : Tokens.TOKENS) {
            assertFalse(Tokens.isTokenAvailable(token));
        }

        // Try assigning a new token and verify it's not possible
        assertNull(Tokens.assignToken());
    }

    @Test
    void testDisplayAvailableTokens() {
        // This will only test if the display is working.
        // We can't capture printed output easily in a test without additional setup.
        Tokens.displayAvailableTokens();
    }

    @Test
    void testAssignToken2() {
        // Test that assigning a token works
        char[] token = Tokens.assignToken();
        assertNotNull(token);
        assertFalse(Tokens.isTokenAvailable(new String(token)));

        // Test that assigning all tokens works
        for (int i = 0; i < Tokens.TOKENS.length - 1; i++) {
            assertNotNull(Tokens.assignToken());
        }

        // Test that no tokens are available after assigning all
        assertNull(Tokens.assignToken());
    }
 @Test
    void testGetOwner() {
        Tokens token = new Tokens();
        assertNull(token.getOwner());
    }

    @Test
    void testGetBoardPosition() {
        Tokens token = new Tokens();
        assertEquals(0, token.getBoardPosition());
    }
    @Test
    void testSetOwner() {
        Tokens token = new Tokens();
        token.setOwner("Player 1");
        assertEquals("Player 1", token.getOwner());
    }
}
