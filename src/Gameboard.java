import java.util.*;

public class Gameboard {
/* this class represents the gameboard for the Monopoly game.
It has a list of spaces that represent the different spaces on the board.
 */
    private List<Space> spaces;
    private Map<Integer, String> propertyOwnership;

    public Gameboard() {
        spaces = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        spaces.add(new SpecialSpace("Go", 0, "Start"));
        spaces.add(new Property("Mediterranean Avenue", 1, 60));
        spaces.add(new SpecialSpace("Community Chest", 2, "Community Chest"));
        spaces.add(new Property("Baltic Avenue", 3, 60));
        spaces.add(new SpecialSpace("Income Tax", 4, "Tax"));
        spaces.add(new SpecialSpace("Reading Railroad", 5, "Railroad"));
        spaces.add(new Property("Oriental Avenue", 6, 100));
        spaces.add(new SpecialSpace("Chance", 7, "Chance"));
        spaces.add(new Property("Vermont Avenue", 8, 100));
        spaces.add(new Property("Connecticut Avenue", 9, 120));
        spaces.add(new SpecialSpace("Jail", 10, "Jail"));
        spaces.add(new Property("St. Charles Place", 11, 140));
        spaces.add(new SpecialSpace("Electric Company", 12, "Utility"));
        spaces.add(new Property("States Avenue", 13, 140));
        spaces.add(new Property("Virginia Avenue", 14, 160));
        spaces.add(new SpecialSpace("Pennsylvania Railroad", 15, "Railroad"));
        spaces.add(new Property("St. James Place", 16, 180));
        spaces.add(new SpecialSpace("Community Chest", 17, "Community Chest"));
        spaces.add(new Property("Tennessee Avenue", 18, 180));
        spaces.add(new Property("New York Avenue", 19, 200));
        spaces.add(new SpecialSpace("Free Parking", 20, "Free Parking"));
        spaces.add(new Property("Kentucky Avenue", 21, 220));
        spaces.add(new SpecialSpace("Chance", 22, "Chance"));
        spaces.add(new Property("Indiana Avenue", 23, 220));
        spaces.add(new Property("Illinois Avenue", 24, 240));
        spaces.add(new SpecialSpace("B. & O. Railroad", 25, "Railroad"));
        spaces.add(new Property("Atlantic Avenue", 26, 260));
        spaces.add(new Property("Ventnor Avenue", 27, 260));
        spaces.add(new SpecialSpace("Water Works", 28, "Utility"));
        spaces.add(new Property("Marvin Gardens", 29, 280));
        spaces.add(new SpecialSpace("Go To Jail", 30, "Go To Jail"));
        spaces.add(new Property("Pacific Avenue", 31, 300));
        spaces.add(new Property("North Carolina Avenue", 32, 300));
        spaces.add(new SpecialSpace("Community Chest", 33, "Community Chest"));
        spaces.add(new Property("Pennsylvania Avenue", 34, 320));
        spaces.add(new SpecialSpace("Short Line", 35, "Railroad"));
        spaces.add(new SpecialSpace("Chance", 36, "Chance"));
        spaces.add(new Property("Park Place", 37, 350));
        spaces.add(new SpecialSpace("Luxury Tax", 38, "Tax"));
        spaces.add(new Property("Boardwalk", 39, 400));
    }

    public Space getspace(int position) {
        return spaces.get(position);
    }

    public void printBoard() {
        for (Space space : spaces) {
            System.out.println(space);
        }
    }
    

    public static void main(String[] args) {
        Gameboard board = new Gameboard();
        board.printBoard();

        // Test getting a space
        System.out.println(board.getspace(0));
        System.out.println(board.getspace(10));
        System.out.println(board.getspace(20));
        System.out.println(board.getspace(30));
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    public Map<Integer, String> getPropertyOwnership() {
        return propertyOwnership;
    }

    public void setPropertyOwnership(Map<Integer, String> propertyOwnership) {
        this.propertyOwnership = propertyOwnership;
    }

    public int size() {
        return spaces.size();
    }

    public Gameboard getSpaces() {
        return this;
    }
    // Placeholder for future property management functionality
}

