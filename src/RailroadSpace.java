public class RailroadSpace extends Space {
    private int price;
    private Player owner;
    private final int BASE_RENT = 25;

    /**
     * Constructs a railroad space on the Monopoly board.
     *
     * @param name     The name of the railroad
     * @param position The position on the board
     */
    public RailroadSpace(String name, int position) {
        super(name, position, "Railroad");
        this.price = 200; // Standard price for railroads in Monopoly
        this.owner = null;
    }

    /**
     * Gets the price of the railroad.
     *
     * @return The price of the railroad
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the owner of the railroad.
     *
     * @param owner The player who owns the railroad
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner of the railroad.
     *
     * @return The owner of the railroad
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Checks if the railroad is owned.
     *
     * @return True if the railroad is owned, false otherwise
     */
    public boolean isOwned() {
        return owner != null;
    }

    /**
     * Calculates the rent based on how many railroads the owner has.
     * The rent doubles for each railroad owned.
     *
     * @param gameState The current game state
     * @return The rent amount to be paid
     */
    public int calculateRent(GameState gameState) {
        if (owner == null) {
            return 0;
        }

        // Count how many railroads the owner has
        int railroadCount = 0;
        for (Space space : gameState.getBoard().getSpaces()) {
            if (space instanceof RailroadSpace) {
                RailroadSpace railroad = (RailroadSpace) space;
                if (railroad.getOwner() == owner) {
                    railroadCount++;
                }
            }
        }

        // Calculate rent: 25 for 1 railroad, 50 for 2, 100 for 3, 200 for 4
        return BASE_RENT * (int) Math.pow(2, railroadCount - 1);
    }

    /**
     * Handles what happens when a player lands on this railroad.
     *
     * @param player    The player who landed on the railroad
     * @param gameState The current game state
     */
    public void onLand(Player player, GameState gameState) {
        System.out.println(player.getName() + " landed on " + name);

        if (!isOwned()) {
            System.out.println(name + " is not owned. It costs $" + price);
            // Logic for player to decide to buy would be handled elsewhere
        } else if (owner != player) {
            int rent = calculateRent(gameState);
            System.out.println(player.getName() + " must pay $" + rent + " to " + owner.getName());
            player.payRent(owner, rent);
        } else {
            System.out.println("You own this railroad.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - Price: $" + price + ", Owner: " +
                (owner != null ? owner.getName() : "None");
    }

    /**
     * Override the playerOnRailroad method from Space class
     */
    @Override
    public void playerOnRailroad() {
        System.out.println("Player landed on railroad " + name);
    }
}