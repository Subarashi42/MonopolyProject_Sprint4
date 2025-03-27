/**
 * Represents a property space on the Monopoly board.
 * Properties can be bought, sold, and developed with houses and hotels.
 */
public class Property extends Space {
    private int price;
    private int rent;
    private String colorGroup;
    private Player owner;
    private int houses;
    private boolean hasHotel;

    /**
     * Constructs a new property on the Monopoly board.
     *
     * @param name The name of the property
     * @param position The position on the board
     * @param price The purchase price of the property
     * @param rent The base rent of the property
     * @param colorGroup The color group the property belongs to
     */
    public Property(String name, int position, int price, int rent, String colorGroup) {
        super(name, position, "Property");
        this.price = price;
        this.rent = rent;
        this.colorGroup = colorGroup;
        this.owner = null;  // No owner initially
        this.houses = 0;
        this.hasHotel = false;
    }

    /**
     * Sets the owner of the property.
     *
     * @param owner The player who owns the property
     */
    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner of the property.
     *
     * @return The owner of the property
     */
    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * Gets the name of the property.
     *
     * @return The name of the property
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the price of the property.
     *
     * @return The price of the property
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the color group of the property.
     *
     * @return The color group of the property
     */
    public String getColorGroup() {
        return colorGroup;
    }

    /**
     * Adds a house to the property if possible.
     *
     * @return True if a house was added, false otherwise
     */
    public boolean addHouse() {
        if (houses < 4 && !hasHotel) {
            houses++;
            return true;
        } else if (houses == 4 && !hasHotel) {
            houses = 0;
            hasHotel = true;
            return true;
        } else {
            System.out.println(name + " already has a hotel!");
            return false;
        }
    }

    /**
     * Removes a house from the property if possible.
     *
     * @return True if a house was removed, false otherwise
     */
    public boolean removeHouse() {
        if (houses > 0) {
            houses--;
            return true;
        } else if (hasHotel) {
            hasHotel = false;
            houses = 4;
            return true;
        } else {
            System.out.println(name + " has no houses or hotels to remove!");
            return false;
        }
    }

    /**
     * Checks if the property is owned.
     *
     * @return True if the property is owned, false otherwise
     */
    public boolean isOwned() {
        return owner != null;
    }

    /**
     * Handles what happens when a player lands on this property.
     *
     * @param player The player who landed on the property
     * @param gameState The current game state
     */
    public void onLand(Player player, GameState gameState) {
        System.out.println(player.getName() + " landed on " + name);

        if (isOwned()) {
            if (owner != player) {
                int calculatedRent = calculateRent(gameState);
                System.out.println(player.getName() + " must pay $" + calculatedRent + " rent to " + owner.getName());
                player.payRent(owner, calculatedRent);
            } else {
                System.out.println(player.getName() + " owns this property.");
            }
        } else {
            System.out.println(name + " is not owned. It costs $" + price);
            // Logic for player to decide to buy would be handled elsewhere
            if (player.getMoney() >= price) {
                boolean wantToBuy = true; // In a real game, this would be a player decision
                if (wantToBuy) {
                    player.buyProperty(this);
                }
            } else {
                System.out.println(player.getName() + " cannot afford to buy " + name);
            }
        }
    }

    /**
     * Calculates the rent based on houses, hotels, and monopoly ownership.
     *
     * @param gameState The current game state
     * @return The calculated rent
     */
    public int calculateRent(GameState gameState) {
        if (houses == 0 && !hasHotel) {
            // Check if all properties in the color group are owned by the same player
            if (gameState.getBoard().playerOwnsAllInColorGroup(owner, colorGroup)) {
                return rent * 2; // Double rent for a monopoly
            }
            return rent;
        } else if (hasHotel) {
            // Hotel rent values are specific for each property, but for simplicity:
            return rent * 5; // Typically much higher than 4 houses
        } else {
            // House rent increases progressively
            switch(houses) {
                case 1: return rent * 5;
                case 2: return rent * 15;
                case 3: return rent * 30;
                case 4: return rent * 40;
                default: return rent;
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " - Price: $" + price + ", Rent: $" + rent + ", Color: " + colorGroup +
                ", Owner: " + (owner != null ? owner.getName() : "None") +
                ", Houses: " + houses + ", Hotel: " + (hasHotel ? "Yes" : "No");
    }

    /**
     * Checks if the property has a hotel.
     *
     * @return True if the property has a hotel, false otherwise
     */
    public boolean hasHotel() {
        return hasHotel;
    }

    /**
     * Gets the number of houses on the property.
     *
     * @return The number of houses
     */
    public int getHouses() {
        return houses;
    }

    /**
     * Sets the number of houses on the property.
     *
     * @param houses The new number of houses
     */
    public void setHouses(int houses) {
        this.houses = houses;
    }

    /**
     * Gets the base rent of the property.
     *
     * @return The base rent
     */
    public int getRent() {
        return rent;
    }

    /**
     * Sets the base rent of the property.
     *
     * @param rent The new base rent
     */
    public void setRent(int rent) {
        this.rent = rent;
    }

    /**
     * Sets whether the property has a hotel.
     *
     * @param hasHotel True if the property should have a hotel, false otherwise
     */
    public void setHasHotel(boolean hasHotel) {
        this.hasHotel = hasHotel;
    }

    /**
     * Gets the mortgage value of the property.
     *
     * @return The mortgage value (half the purchase price)
     */
    public int getMortgageValue() {
        return price / 2;
    }

    /**
     * Gets the cost to unmortgage the property.
     *
     * @return The unmortgage cost (mortgage value plus 10% interest)
     */
    public int getUnmortgageCost() {
        return (int)(getMortgageValue() * 1.1);
    }

    @Override
    public void playerOnProperty() {
        System.out.println("Player landed on property " + name);
    }
}