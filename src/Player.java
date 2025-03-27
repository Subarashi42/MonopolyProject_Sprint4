import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Player implements List<Player> {
    private String name;
    private int money;
    private int position;
    private int turnCounter;
    private String token;
    private Dice dice = new Dice();

    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money for the player
        this.position = 0;  // Starting position on the gameboard
        this.turnCounter = 0; // Number of turns the player has taken
        this.token = null; // Token starts as null
    }

    public Player() {
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    /**
     * Allows the player to choose a token. Ensures they pick an available one.
     *
     * @param chosenToken The token the player wants to use
     * @return true if the token was successfully chosen, false otherwise
     */
    public boolean chooseToken(String chosenToken) {
        if (Tokens.isTokenAvailable(chosenToken)) {
            this.token = chosenToken;
            Tokens.assignToken(chosenToken);
            System.out.println(name + " has chosen the token: " + chosenToken);
            return true;
        } else {
            System.out.println("Token " + chosenToken + " is already taken! Choose another.");
            return false;
        }
    }

    public int getMoney() {
        return money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public boolean subtractMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            return true;
        }
        return false;
    }

    public int getBalance() {
        return money;
    }

    public String getTokens() {
        return name + " has $" + money + " and is on space " + position;
    }

    /**
     * Handles buying property if the player has enough money.
     */
    public void buyProperty(Property property) {
        if (money >= property.getPrice()) {
            money -= property.getPrice();
            property.setOwner(this);
            System.out.println(name + " bought " + property.getName());
        } else {
            System.out.println(name + " does not have enough money to buy " + property.getName());
        }
    }

    public void payRent(Player owner, int amount) {
        money -= amount;
        owner.receiveRent(amount);
        System.out.println(name + " paid $" + amount + " rent to " + owner.getName());
    }

    public void receiveRent(int amount) {
        money += amount;
    }

    public boolean shouldGoToJail() {
        return dice.getConsecutiveDoubles() == 3;
    }

    public void move(Player player, int rollDice, Gameboard gameboard) {
        int newPosition = (player.getPosition() + rollDice) % gameboard.size();
        player.setPosition(newPosition);
    }

    public void handleRollingDoubles(Player player, Gameboard gameboard) {
        int rollDice = dice.rollDice();

        if (shouldGoToJail()) {
            System.out.println(player.getName() + " rolled three consecutive doubles and is going to jail!");
            player.setPosition(10);  // Jail position
            dice.resetConsecutiveDoubles();
        }

        if (dice.getDie1Value() == dice.getDie2Value()) {
            System.out.println(player.getName() + " rolled doubles! Roll again.");
            move(player, rollDice, gameboard);
        }

        if (player.getPosition() == 10 && dice.getDie1Value() != dice.getDie2Value()) {
            System.out.println(player.getName() + " is in jail and did not roll doubles. Skip turn.");
            return;
        }

        if (player.getPosition() == 10 && dice.getDie1Value() == dice.getDie2Value()) {
            System.out.println(player.getName() + " rolled doubles and is leaving jail.");
            move(player, rollDice, gameboard);
        }
    }

    public void takeTurn(Player player, Gameboard gameboard, List<Player> players) {
        Player playerWithLeastTurns = players.get(0);

        for (Player currentPlayer : players) {
            if (currentPlayer.turnCounter < playerWithLeastTurns.turnCounter) {
                playerWithLeastTurns = currentPlayer;
            }
        }

        playerWithLeastTurns.turnCounter++;
        System.out.println(playerWithLeastTurns.getName() + " is starting their turn.");
    }

    private void handleSpace(Player player, Space space, List<Player> players) {
        if (space instanceof Property) {
            Property property = (Property) space;
            if (property.getOwner() == null) {
                System.out.println(space.getName() + " is unowned. Price: $" + property.getPrice());
                if (player.getMoney() >= property.getPrice()) {
                    player.buyProperty(property);
                } else {
                    System.out.println(player.getName() + " cannot afford " + space.getName() + ".");
                }
            } else if (!property.getOwner().equals(player)) {
                int rent = property.getRent();
                System.out.println(player.getName() + " needs to pay $" + rent + " rent to " + property.getOwner().getName());
                if (player.getMoney() >= rent) {
                    player.payRent(property.getOwner(), rent);
                } else {
                    System.out.println(player.getName() + " is bankrupt and cannot pay rent!");
                    players.remove(player);
                }
            }
        } else if (space instanceof SpecialSpace) {
            SpecialSpace specialSpace = (SpecialSpace) space;
            switch (specialSpace.getType()) {
                case "Tax":
                    player.subtractMoney(200);
                    System.out.println(player.getName() + " paid $200 in taxes.");
                    break;
                case "Chance":
                case "Community Chest":
                    System.out.println("Draw card from " + specialSpace.getType() + " deck.");
                    break;
                case "Jail":
                    System.out.println(player.getName() + " is just visiting Jail.");
                    break;
                case "Go To Jail":
                    System.out.println(player.getName() + " has been sent to Jail!");
                    player.setPosition(10);
                    break;
                case "Free Parking":
                    System.out.println(player.getName() + " is on Free Parking.");
                    break;
            }
        }
    }

    public void deductMoney(int amount) {
        money -= amount;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Player> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Player player) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Player> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Player> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Player get(int index) {
        return null;
    }

    @Override
    public Player set(int index, Player element) {
        return null;
    }

    @Override
    public void add(int index, Player element) {


    }

    @Override
    public Player remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Player> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Player> listIterator(int index) {
        return null;
    }

    @Override
    public List<Player> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    public void decreaseBalance(int price) {
        money -= price;
    }

    public boolean isInJail() {
        if (position == 10) {
            return true;
        }
        else {
            return false;
        }
    }

    public void getOutOfJail() {
        position = 10;
    }

    public void increaseBalance(int i) {
        money += i;
    }
}
