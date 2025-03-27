import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

/**
 * Test class for the ChanceCards class.
 */
public class ChanceCardsTest {
    private ChanceCards chanceCards;

    @Before
    public void setUp() {
        chanceCards = new ChanceCards();
        chanceCards.cards(); // Initialize cards
    }

    @Test
    public void testInitialization() {
        // Verify that the cards map is initialized and contains expected number of cards
        Map<String, String> cards = chanceCards.getChanceCards();
        assertNotNull("Cards map should not be null", cards);
        assertEquals("Should have 16 Chance cards", 16, cards.size());
    }

    @Test
    public void testCardContent() {
        // Test that specific cards have the expected content
        Map<String, String> cards = chanceCards.getChanceCards();

        // Check a few specific cards to ensure content is as expected
        assertEquals("Take a ride on the Reading Railroad. If you pass Go, collect $200.", cards.get("Card1"));
        assertEquals("Advance to Go (Collect $200).", cards.get("Card4"));
        assertEquals("Bank pays you dividend of $50.", cards.get("Card5"));
        assertEquals("This Card May Be Kept Until Needed or Sold. Get Out of Jail Free.", cards.get("Card8"));
        assertEquals("Go Directly to Jail. Do Not Pass Go, Do Not Collect $200.", cards.get("Card12"));
    }

    @Test
    public void testShuffleCards() {
        // Test that shuffle returns a valid card text
        String cardText = chanceCards.shuffleCards();
        assertNotNull("Shuffled card should not be null", cardText);
        assertFalse("Shuffled card should not be empty", cardText.isEmpty());

        // Verify the returned card is one of the cards in the deck
        Map<String, String> cards = chanceCards.getChanceCards();
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
            results[i] = chanceCards.shuffleCards();
        }

        // Count unique results - should have several different cards
        int uniqueCards = countUniqueStrings(results);

        // We should get at least 8 different cards in 100 shuffles (probabilistic test)
        assertTrue("Should get multiple different cards when shuffling repeatedly", uniqueCards >= 8);
    }

    @Test
    public void testSetChanceCards() {
        // Test setting a new cards map
        Map<String, String> originalCards = chanceCards.getChanceCards();

        // Create a temporary backup
        Map<String, String> tempCards = originalCards;

        // Set the cards map to null
        chanceCards.setChanceCards(null);
        assertNull(chanceCards.getChanceCards());

        // Restore the original cards map
        chanceCards.setChanceCards(tempCards);
        assertEquals(tempCards, chanceCards.getChanceCards());
    }

    @Test
    public void testChestAndCardSpotImplementation() {
        // Test that the ChanceCards class properly implements the ChestAndCardSpot interface
        assertTrue(chanceCards instanceof ChestAndCardSpot);

        // Test interface methods
        ChestAndCardSpot cardSpot = chanceCards;

        // Call cards() method and verify it initializes the map
        cardSpot.cards();
        assertNotNull(chanceCards.getChanceCards());
        assertEquals(16, chanceCards.getChanceCards().size());

        // Call shuffleCards() method and verify it returns a valid card
        String card = ChestAndCardSpot.shuffleCards();
        assertNotNull(card);
        assertFalse(card.isEmpty());
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