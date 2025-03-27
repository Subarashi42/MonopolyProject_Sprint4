import java.util.List;

public class Player {
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

    public boolean shouldGoToJail() {
        return dice.getConsecutiveDoubles() == 3;
    }

    public void move(Player player, int rollDice, Gameboard gameboard) {
        int newPosition = (player.getPosition() + rollDice) % gameboard.getSpaces().size();
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
}
