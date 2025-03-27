import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Die class.
 */
public class DieTest {
    private Die die;

    @Before
    public void setUp() {
        // Initialize a die with 6 sides
        die = new Die(6);
    }

    @Test
    public void testInitialization() {
        // Test that die value is 0 before first roll
        // Note: This assumes there's no initial roll in the constructor
        assertEquals(0, die.getDieValue());
    }

    @Test
    public void testRoll() {
        // Roll the die multiple times and verify the range
        for (int i = 0; i < 100; i++) {
            int value = die.roll();
            // Verify that the value is between 1 and 6 inclusive
            assertTrue("Die value should be between 1 and 6", value >= 1 && value <= 6);
            // Verify that getDieValue returns the same as the roll result
            assertEquals(value, die.getDieValue());
        }
    }

    @Test
    public void testDifferentSizedDie() {
        // Test a 12-sided die
        Die d12 = new Die(12);
        for (int i = 0; i < 50; i++) {
            int value = d12.roll();
            // Verify that the value is between 1 and 12 inclusive
            assertTrue("D12 value should be between 1 and 12", value >= 1 && value <= 12);
        }

        // Test a 20-sided die
        Die d20 = new Die(20);
        for (int i = 0; i < 50; i++) {
            int value = d20.roll();
            // Verify that the value is between 1 and 20 inclusive
            assertTrue("D20 value should be between 1 and 20", value >= 1 && value <= 20);
        }
    }

    @Test
    public void testDistribution() {
        // This test checks if the die produces a reasonably fair distribution
        // by rolling many times and ensuring all possible values appear
        int[] counts = new int[7]; // Index 0 will be unused
        int totalRolls = 1000;

        for (int i = 0; i < totalRolls; i++) {
            int value = die.roll();
            counts[value]++;
        }

        // Check that each value 1-6 appears at least once
        for (int i = 1; i <= 6; i++) {
            assertTrue("Value " + i + " should appear at least once in " + totalRolls + " rolls",
                    counts[i] > 0);
        }

        // For a fair die, each value should appear roughly totalRolls/6 times
        // Allow for some variance, but make sure it's not heavily biased
        double expectedCount = totalRolls / 6.0;
        double allowedVariance = 0.3 * expectedCount; // Allow 30% variance

        for (int i = 1; i <= 6; i++) {
            assertTrue("Value " + i + " should be reasonably distributed",
                    counts[i] > (expectedCount - allowedVariance) &&
                            counts[i] < (expectedCount + allowedVariance));
        }
    }
}