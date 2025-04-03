/**
 * Author: Tati Curtis & Aiden Clare
 * This interface is used to define the methods that are used in the ChanceCards and CommunityChestCards classes.
 * It contains a method to shuffle cards and a method to display the cards.
 */
public interface ChestAndCardSpot {
    /**
     * Author : Tati Curtis
     * This method is used to create the chance cards.
     * It initializes the chanceCards map with the card name and description.
     */

public void cards();

    public static String shuffleCards() {
        if (Math.random() < 0.5) {
            return "Advance to Go (Collect $200).";
        } else {
            return "Bank error in your favor. Collect $200.";
        }
    }


}
