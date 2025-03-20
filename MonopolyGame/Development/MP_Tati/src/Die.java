/* This class represents a die with a specified number of sides.
 * It allows rolling the die and retrieving the value of the last roll.
 * Author: Tati Curtis
 */

import java.util.Random;

public class Die {
    private int numberOfSides;
    private Random random;
    private int lastRollValue;

    public Die(int numberOfSides) {
        this.numberOfSides = numberOfSides;
        this.random = new Random();
    }

    public int roll(){
        lastRollValue = random.nextInt(numberOfSides) + 1;
        return lastRollValue;
    }

    public int getDieValue() {
        return lastRollValue;
    }
}
