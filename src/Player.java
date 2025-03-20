import java.util.List;

public class Player {
    private String name;
    private int money;
    private int position;
    private Die die;

    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money for the player
    }

    public String getName() {
        return name;
    }

    // moved from Gameboard class and altered
    public void buyProperty(Property property) {
        if (money >= property.getPrice()) {
            money -= property.getPrice();
            property.setOwner(this);
            System.out.println(name + " bought " + property.getName());
        } else{
            System.out.println(name + " does not have enough money to buy " + property.getName());
        }
    }

    public void payRent(Player owner, int amount){
        money -= amount;
        owner.receiveRent(amount);
        System.out.println(name + " paid $" + amount + " rent to " + owner.getName());
    }

    public void receiveRent(int amount){
        money += amount;
    }

    /**
     * Rolls the player's dice and returns the value.
     *
     * @return the value of the dice roll
     */
    public int rollDice() {
        if (die == null) {
            die = new Die(6);
        }
        return die.roll();
    }

//    // moved from Gameboard class
//    // is this necessary anymore?
//    public void upgradeProperty(int position) {
//        if (spaces.get(position) instanceof Property) {
//            Property prop = (Property) spaces.get(position);
//            prop.addHouse();
//            System.out.println("Upgraded " + prop.name + " to " + prop.houses + " houses, Hotel: " + (prop.hasHotel ? "Yes" : "No"));
//        } else {
//            System.out.println("This space is not a property.");
//        }
//    }

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

    //gameloop handling code below
    /**
     * Starts the game loop where players take turns in the correct order.
     * The game continues until all but one player is bankrupt.
     */
    public void startGame(List<Player> players, Gameboard gameboard) {
        boolean gameActive = true;
        int currentPlayerIndex = 0;

        while (gameActive) {
            Player currentPlayer = players.get(currentPlayerIndex);

            // Skip bankrupt players
            if (currentPlayer.getMoney() <= 0) {
                players.remove(currentPlayer);
                if (players.size() == 1) {
                    System.out.println(players.get(0).getName() + " wins the game!");
                    break;
                }
                currentPlayerIndex = currentPlayerIndex % players.size();
                continue;
            }

            System.out.println("It's " + currentPlayer.getName() + "'s turn.");
            takeTurn(currentPlayer, gameboard, players);

            // Move to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /**
     * Manages a single player's turn, including dice rolls and space interactions.
     */
    public void takeTurn(Player player, Gameboard gameboard, List<Player> players) {
        int roll = player.rollDice();
        System.out.println(player.getName() + " rolled a " + roll + ".");

        // Move player and print position
        int newPosition = (player.getPosition() + roll) % gameboard.getSpaces().size();
        player.setPosition(newPosition);
        System.out.println(player.getName() + " moved to " + gameboard.getspace(newPosition).getName() + ".");

        handleSpace(player, gameboard.getspace(newPosition), players);
    }

    /**
     * Handles interactions based on the type of space a player lands on.
     */
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
            /* Should move this to the special space class */
        } else if (space instanceof SpecialSpace) {
            SpecialSpace specialSpace = (SpecialSpace) space;
            switch (specialSpace.getType()) {
                case "Tax":
                    player.subtractMoney(200);  // Example tax amount
                    System.out.println(player.getName() + " paid $200 in taxes.");
                    break;
                case "Chance":
                case "Community Chest":
                    System.out.println("Draw card from " + specialSpace.getType() + " deck."); // Placeholder
                    break;
                case "Jail":
                    System.out.println(player.getName() + " is just visiting Jail.");
                    break;
                case "Go To Jail":
                    System.out.println(player.getName() + " has been sent to Jail!");
                    player.setPosition(10);  // Jail position
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
}
