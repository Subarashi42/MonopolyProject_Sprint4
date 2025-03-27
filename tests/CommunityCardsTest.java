import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

/**
 * Test class for the CommunityChestCards class.
 */
public class CommunityCardsTest {
    private CommunityChestCards communityChestCards;

    @Before
    public void setUp() {
        communityChestCards = new CommunityChestCards();
        communityChestCards.cards(); // Initialize cards
    }

    @Test
    public void testInitialization() {
        // Verify that the cards map is initialized and contains expected number of cards
        Map<String, String> cards = communityChestCards.getCommunityChestCards();
        assertNotNull("Cards map should not be null", cards);
        assertEquals("Should have 16 Community Chest cards", 16, cards.size());
    }

    @Test
    public void testCardContent() {
        // Test that specific cards have the expected content
        Map<String, String> cards = communityChestCards.getCommunityChestCards();

        // Check a few specific cards to ensure content is as expected
        assertEquals("Advance to Go (Collect $200).", cards.get("Card1"));
        assertEquals("Bank error in your favor. Collect $200.", cards.get("Card2"));
        assertEquals("Doctor's fees. Pay $50.", cards.get("Card3"));
        assertEquals("Get out of Jail Free.", cards.get("Card5"));
        assertEquals("Go to Jail. Go directly to Jail. Do not pass Go. Do not collect $200.", cards.get("Card6"));
    }

    @Test
    public void testShuffleCards() {
        // Test that shuffle returns a valid card text
        String cardText = communityChestCards.shuffleCards();
        assertNotNull("Shuffled card should not be null", cardText);
        assertFalse("Shuffled card should not be empty", cardText.isEmpty());

        // Verify the returned card is one of the cards in the deck
        Map<String, String> cards = communityChestCards.getCommunityChestCards();
        boolean foundMatch = false;

        for (String cardContent : cards.values()) {
            if (cardContent.equals(cardText)) {
                foundMatch = true;
                break;
            }
        }

        assertTrue("Shuffled card should match one of the cards in the deck", foundMatch);
    }

    @Test
    public void testMultipleShuffles() {
        // Test multiple shuffles to ensure randomness
        // We'll track how many different cards we get in 100 shuffles

        int shuffleCount = 100;
        String[] results = new String[shuffleCount];

        for (int i = 0; i < shuffleCount; i++) {
            results[i] = communityChestCards.shuffleCards();
        }

        // Count unique results - should have several different cards
        int uniqueCards = countUniqueStrings(results);

        // We should get at least 8 different cards in 100 shuffles (probabilistic test)
        assertTrue("Should get multiple different cards when shuffling repeatedly", uniqueCards >= 8);
    }

    @Test
    public void testSetCommunityChestCards() {
        // Test setting a new cards map
        Map<String, String> originalCards = communityChestCards.getCommunityChestCards();

        // Create a temporary backup
        Map<String, String> tempCards = originalCards;

        // Set the cards map to null
        communityChestCards.setCommunityChestCards(null);
        assertNull(communityChestCards.getCommunityChestCards());

        // Restore the original cards map
        communityChestCards.setCommunityChestCards(tempCards);
        assertEquals(tempCards, communityChestCards.getCommunityChestCards());
    }

    /**
     * Helper method to count unique strings in an array.
     */
    private int countUniqueStrings(String[] array) {
        java.util.Set<String> uniqueSet = new java.util.HashSet<>();
        for (String s : array) {
            uniqueSet.add(s);
        }
        return uniqueSet.size();
    }
}