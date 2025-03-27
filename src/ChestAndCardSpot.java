public interface ChestAndCardSpot {
    /*
    This interface is used to define the methods that are used in the ChanceCards and CommunityChestCards classes.
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
