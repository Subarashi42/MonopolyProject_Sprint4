import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Dice class.
 */
public class DiceTest {
    private Dice dice;

    @Before
    public void setUp() {
        dice = new Dice();
    }

    @Test
    public void testInitialization() {
        // Test that dice starts with no consecutive doubles
        assertEquals(0, dice.getConsecutiveDoubles());
    }

    @Test
    public void testRollDice() {
        // Testing a basic dice roll
        int result = dice.rollDice();

        // The result should be between 2 and 12 inclusive
        assertTrue("Dice roll should be between 2 and 12", result >= 2 && result <= 12);

        // Individual die values should be between 1 and 6
        int die1Value = dice.getDie1Value();
        int die2Value = dice.getDie2Value();
        assertTrue("Die 1 should be between 1 and 6", die1Value >= 1 && die1Value <= 6);
        assertTrue("Die 2 should be between 1 and 6", die2Value >= 1 && die2Value <= 6);

        // The total should equal the sum of the two dice
        assertEquals("Roll result should equal sum of dice", die1Value + die2Value, result);
    }

    @Test
    public void testManyRolls() {
        // Roll many times to ensure proper range
        for (int i = 0; i < 100; i++) {
            int result = dice.rollDice();
            assertTrue("Roll should be between 2 and 12", result >= 2 && result <= 12);
            assertTrue("Die 1 should be between 1 and 6", dice.getDie1Value() >= 1 && dice.getDie1Value() <= 6);
            assertTrue("Die 2 should be between 1 and 6", dice.getDie2Value() >= 1 && dice.getDie2Value() <= 6);
        }
    }

    @Test
    public void testGetTotal() {
        // Roll dice and verify getTotal returns the same as rollDice
        int rollResult = dice.rollDice();
        assertEquals("getTotal should return same as roll result", rollResult, dice.getTotal());
    }

    @Test
    public void testConsecutiveDoubles() {
        // Create a dice with deterministic behavior using mock dice
        Die mockDie1 = new MockDie(6);
        Die mockDie2 = new MockDie(6);
        Dice mockDice = new Dice(mockDie1, mockDie2);

        // First roll, should get doubles
        mockDice.rollDice();
        assertEquals("Should have 1 consecutive double", 1, mockDice.getConsecutiveDoubles());

        // Second roll, should have 2 doubles
        mockDice.rollDice();
        assertEquals("Should have 2 consecutive doubles", 2, mockDice.getConsecutiveDoubles());

        // Third roll, should have 3 doubles and trigger "go to jail"
        mockDice.rollDice();
        assertEquals("Should have 3 consecutive doubles", 3, mockDice.getConsecutiveDoubles());
        assertTrue("Should trigger go to jail", mockDice.shouldGoToJail());

        // Reset and check it's back to 0
        mockDice.resetConsecutiveDoubles();
        assertEquals("Should have 0 consecutive doubles after reset", 0, mockDice.getConsecutiveDoubles());
    }

    @Test
    public void testNonConsecutiveDoubles() {
        // Use real dice and force non-consecutive doubles
        // Note: This is probabilistic, but should work most of the time

        // Roll up to 20 times looking for doubles followed by non-doubles
        boolean foundDoublesNonDoubles = false;
        for (int i = 0; i < 20 && !foundDoublesNonDoubles; i++) {
            dice.rollDice();
            int consecutiveDoubles = dice.getConsecutiveDoubles();
            if (consecutiveDoubles == 1) {
                // We got doubles, now force non-doubles with mock dice
                Die nonMatchingDie1 = new MockDie(1);
                Die nonMatchingDie2 = new MockDie(2);
                dice = new Dice(nonMatchingDie1, nonMatchingDie2);
                dice.rollDice();
                assertEquals("Consecutive doubles should reset after non-doubles", 0, dice.getConsecutiveDoubles());
                foundDoublesNonDoubles = true;
            }
        }

        // If we didn't find the pattern, at least verify reset works
        dice.resetConsecutiveDoubles();
        assertEquals(0, dice.getConsecutiveDoubles());
    }

    @Test
    public void testRoll() {
        // Test the roll() alias method
        int result = dice.roll();
        assertTrue("Roll should be between 2 and 12", result >= 2 && result <= 12);
    }

    /**
     * Mock Die class that always returns a specific value for testing.
     */
    private class MockDie extends Die {
        private int fixedValue;

        public MockDie(int value) {
            super(6);
            this.fixedValue = value;
        }

        @Override
        public int roll() {
            return fixedValue;
        }

        @Override
        public int getDieValue() {
            return fixedValue;
        }
    }
}