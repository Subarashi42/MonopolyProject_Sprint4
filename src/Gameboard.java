import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the gameboard for the Monopoly game.
 * Contains a list of spaces that represent the different spaces on the board.
 */
public class Gameboard {
    private List<Space> spaces;
    private Map<Integer, String> propertyOwnership;

    /**
     * Constructs a new gameboard with all spaces initialized.
     */
    public Gameboard() {
        spaces = new ArrayList<>();
        propertyOwnership = new HashMap<>();
        initializeBoard();
    }

    /**
     * Initializes all spaces on the board.
     */
    private void initializeBoard() {
        // Initialize special spaces with specific implementations
        spaces.add(new GoSpace()); // Position 0
        spaces.add(new Property("Mediterranean Avenue", 1, 60, 2, "Brown"));
        spaces.add(new SpecialSpace("Community Chest", 2, "Community Chest"));
        spaces.add(new Property("Baltic Avenue", 3, 60, 4, "Brown"));
        spaces.add(new SpecialSpace("Income Tax", 4, "Tax"));
        spaces.add(new RailroadSpace("Reading Railroad", 5));
        spaces.add(new Property("Oriental Avenue", 6, 100, 6, "Light Blue"));
        spaces.add(new SpecialSpace("Chance", 7, "Chance"));
        spaces.add(new Property("Vermont Avenue", 8, 100, 6, "Light Blue"));
        spaces.add(new Property("Connecticut Avenue", 9, 120, 8, "Light Blue"));
        spaces.add(new JailSpace()); // Position 10
        spaces.add(new Property("St. Charles Place", 11, 140, 10, "Pink"));
        spaces.add(new UtilitySpace("Electric Company", 12));
        spaces.add(new Property("States Avenue", 13, 140, 10, "Pink"));
        spaces.add(new Property("Virginia Avenue", 14, 160, 12, "Pink"));
        spaces.add(new RailroadSpace("Pennsylvania Railroad", 15));
        spaces.add(new Property("St. James Place", 16, 180, 14, "Orange"));
        spaces.add(new SpecialSpace("Community Chest", 17, "Community Chest"));
        spaces.add(new Property("Tennessee Avenue", 18, 180, 14, "Orange"));
        spaces.add(new Property("New York Avenue", 19, 200, 16, "Orange"));
        spaces.add(new FreeParkingSpace()); // Position 20
        spaces.add(new Property("Kentucky Avenue", 21, 220, 18, "Red"));
        spaces.add(new SpecialSpace("Chance", 22, "Chance"));
        spaces.add(new Property("Indiana Avenue", 23, 220, 18, "Red"));
        spaces.add(new Property("Illinois Avenue", 24, 240, 20, "Red"));
        spaces.add(new RailroadSpace("B. & O. Railroad", 25));
        spaces.add(new Property("Atlantic Avenue", 26, 260, 22, "Yellow"));
        spaces.add(new Property("Ventnor Avenue", 27, 260, 22, "Yellow"));
        spaces.add(new UtilitySpace("Water Works", 28));
        spaces.add(new Property("Marvin Gardens", 29, 280, 24, "Yellow"));
        spaces.add(new SpecialSpace("Go To Jail", 30, "Go To Jail"));
        spaces.add(new Property("Pacific Avenue", 31, 300, 26, "Green"));
        spaces.add(new Property("North Carolina Avenue", 32, 300, 26, "Green"));
        spaces.add(new SpecialSpace("Community Chest", 33, "Community Chest"));
        spaces.add(new Property("Pennsylvania Avenue", 34, 320, 28, "Green"));
        spaces.add(new RailroadSpace("Short Line", 35));
        spaces.add(new SpecialSpace("Chance", 36, "Chance"));
        spaces.add(new Property("Park Place", 37, 350, 35, "Dark Blue"));
        spaces.add(new SpecialSpace("Luxury Tax", 38, "Tax"));
        spaces.add(new Property("Boardwalk", 39, 400, 50, "Dark Blue"));
    }

    /**
     * Gets a space at a specific position on the board.
     *
     * @param position The position of the space
     * @return The space at that position
     */
    public Space getspace(int position) {
        if (position >= 0 && position < spaces.size()) {
            return spaces.get(position);
        }
        return null;
    }

    /**
     * Prints all spaces on the board.
     */
    public void printBoard() {
        for (Space space : spaces) {
            System.out.println(space);
        }
    }

    /**
     * Gets all spaces on the board.
     *
     * @return The list of all spaces
     */
    public List<Space> getSpaces() {
        return spaces;
    }

    /**
     * Sets the list of spaces on the board.
     *
     * @param spaces The new list of spaces
     */
    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    /**
     * Gets the property ownership map.
     *
     * @return The property ownership map
     */
    public Map<Integer, String> getPropertyOwnership() {
        return propertyOwnership;
    }

    /**
     * Sets the property ownership map.
     *
     * @param propertyOwnership The new property ownership map
     */
    public void setPropertyOwnership(Map<Integer, String> propertyOwnership) {
        this.propertyOwnership = propertyOwnership;
    }

    /**
     * Gets all properties of a specific color group.
     *
     * @param colorGroup The color group to search for
     * @return A list of properties in that color group
     */
    public List<Property> getPropertiesByColorGroup(String colorGroup) {
        List<Property> propertiesInGroup = new ArrayList<>();

        for (Space space : spaces) {
            if (space instanceof Property) {
                Property property = (Property) space;
                if (property.getColorGroup().equals(colorGroup)) {
                    propertiesInGroup.add(property);
                }
            }
        }

        return propertiesInGroup;
    }

    /**
     * Gets all railroads on the board.
     *
     * @return A list of all railroad spaces
     */
    public List<RailroadSpace> getRailroads() {
        List<RailroadSpace> railroads = new ArrayList<>();

        for (Space space : spaces) {
            if (space instanceof RailroadSpace) {
                railroads.add((RailroadSpace) space);
            }
        }

        return railroads;
    }

    /**
     * Gets all utilities on the board.
     *
     * @return A list of all utility spaces
     */
    public List<UtilitySpace> getUtilities() {
        List<UtilitySpace> utilities = new ArrayList<>();

        for (Space space : spaces) {
            if (space instanceof UtilitySpace) {
                utilities.add((UtilitySpace) space);
            }
        }

        return utilities;
    }

    /**
     * Checks if a player owns all properties in a color group.
     *
     * @param player The player to check
     * @param colorGroup The color group to check
     * @return True if the player owns all properties in the color group, false otherwise
     */
    public boolean playerOwnsAllInColorGroup(Player player, String colorGroup) {
        List<Property> propertiesInGroup = getPropertiesByColorGroup(colorGroup);

        for (Property property : propertiesInGroup) {
            if (property.getOwner() != player) {
                return false;
            }
        }

        return !propertiesInGroup.isEmpty();
    }
}