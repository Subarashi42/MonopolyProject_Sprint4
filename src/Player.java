import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Monopoly game.
 * Manages the player's money, position, properties, and game actions.
 */
public class Player {
    private String name;
    private int money;
    private int position;
    private int turnCounter;
    private String token;
    private List<Property> properties;
    private boolean hasGetOutOfJailFreeCard;
    private int turnsInJail;
    public Dice dice;  // Made public for easier access

    /**
     * Constructs a new player with the given name.
     *
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.money = 1500; // Starting money for the player
        this.position = 0;  // Starting position on the gameboard (GO)
        this.turnCounter = 0; // Number of turns the player has taken
        this.token = null; // Token starts as null
        this.properties = new ArrayList<>();
        this.hasGetOutOfJailFreeCard = false;
        this.turnsInJail = 0;
        this.dice = new Dice();
    }

    /**
     * Gets the player's name.
     *
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's token.
     *
     * @return The player's token
     */
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

    /**
     * Gets the player's current money amount.
     *
     * @return The player's money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Gets the player's current position on the board.
     *
     * @return The player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the player's position on the board.
     *
     * @param position The new position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Adds money to the player's account.
     *
     * @param amount The amount to add
     */
    public void addMoney(int amount) {
        this.money += amount;
        System.out.println(name + " received $" + amount + ". New balance: $" + money);
    }

    /**
     * Subtracts money from the player's account if they have enough.
     *
     * @param amount The amount to subtract
     * @return true if the money was successfully subtracted, false if the player doesn't have enough
     */
    public boolean subtractMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            System.out.println(name + " paid $" + amount + ". New balance: $" + money);
            return true;
        }
        System.out.println(name + " doesn't have enough money to pay $" + amount + "!");
        return false;
    }

    /**
     * Gets the player's current balance.
     *
     * @return The player's money
     */
    public int getBalance() {
        return money;
    }

    /**
     * Gets a string representation of the player's tokens and position.
     *
     * @return A string with the player's information
     */
    public String getTokens() {
        return name + " has $" + money + " and is on space " + position;
    }

    /**
     * Handles buying property if the player has enough money.
     *
     * @param property The property to buy
     * @return true if the property was successfully bought, false otherwise
     */
    public boolean buyProperty(Property property) {
        if (money >= property.getPrice()) {
            money -= property.getPrice();
            property.setOwner(this);
            properties.add(property);
            System.out.println(name + " bought " + property.getName() + " for $" + property.getPrice() +
                    ". New balance: $" + money);
            return true;
        } else {
            System.out.println(name + " does not have enough money to buy " + property.getName());
            return false;
        }
    }

    /**
     * Pays rent to another player.
     *
     * @param owner The property owner receiving the rent
     * @param amount The amount of rent to pay
     * @return true if the rent was paid, false if the player is bankrupt
     */
    public boolean payRent(Player owner, int amount) {
        if (money >= amount) {
            money -= amount;
            owner.receiveRent(amount);
            System.out.println(name + " paid $" + amount + " rent to " + owner.getName() +
                    ". New balance: $" + money);
            return true;
        } else {
            System.out.println(name + " is bankrupt and cannot pay $" + amount + " to " + owner.getName());
            return false; // Player is bankrupt
        }
    }

    /**
     * Receives rent from another player.
     *
     * @param amount The amount of rent received
     */
    public void receiveRent(int amount) {
        money += amount;
    }

    /**
     * Determines if the player should go to jail based on dice rolls.
     *
     * @return true if the player should go to jail, false otherwise
     */
    public boolean shouldGoToJail() {
        return dice.getConsecutiveDoubles() == 3;
    }

    /**
     * Moves the player on the board, handling passing Go.
     *
     * @param rollDice The number of spaces to move
     * @param gameboard The game board
     */
    public void move(int rollDice, Gameboard gameboard) {
        int oldPosition = position;
        position = (position + rollDice) % gameboard.getSpaces().size();

        // Check if player passed Go
        if (position < oldPosition && oldPosition + rollDice >= gameboard.getSpaces().size()) {
            System.out.println(name + " passed Go and collects $200!");
            addMoney(200);
        }

        System.out.println(name + " moved from " + oldPosition + " to " + position +
                " (" + gameboard.getspace(position).getName() + ")");
    }

    /**
     * Takes a turn for the player.
     *
     * @param gameboard The game board
     * @param gameState The current game state
     */
    public void takeTurn(Gameboard gameboard, GameState gameState) {
        System.out.println("\n" + name + " is taking their turn.");

        // Increment turn counter for this player
        turnCounter++;

        // Check if player is in jail
        if (gameState.isPlayerInJail(this)) {
            handleJailTurn(gameState);
            return;
        }

        // Roll the dice and move
        int roll = dice.rollDice();
        System.out.println(name + " rolled " + dice.getDie1Value() + " + " + dice.getDie2Value() + " = " + roll);

        // Check for three doubles (go to jail)
        if (dice.getDie1Value() == dice.getDie2Value()) {
            System.out.println(name + " rolled doubles!");

            if (shouldGoToJail()) {
                System.out.println(name + " rolled three consecutive doubles and is going to jail!");
                JailSpace.goToJail(this, gameState);
                return;
            }
        } else {
            // Reset doubles counter if player didn't roll doubles
            dice.resetConsecutiveDoubles();
        }

        // Move the player
        move(roll, gameboard);

        // Land on space and handle its effect
        Space currentSpace = gameboard.getspace(position);
        handleLandingOnSpace(currentSpace, gameState);

        // If player rolled doubles, they get another turn (unless they're in jail)
        if (dice.getDie1Value() == dice.getDie2Value() && !gameState.isPlayerInJail(this)) {
            System.out.println(name + " gets another turn for rolling doubles!");
            takeTurn(gameboard, gameState);
        }
    }

    /**
     * Handles a player's turn when they are in jail.
     *
     * @param gameState The current game state
     */
    private void handleJailTurn(GameState gameState) {
        System.out.println(name + " is in Jail (Turn " + (turnsInJail + 1) + " in jail)");
        turnsInJail++;

        // Option 1: Pay to get out
        if (money >= 50 && turnsInJail <= 3) {
            System.out.println(name + " pays $50 to get out of Jail.");
            subtractMoney(50);
            gameState.releaseFromJail(this);
            turnsInJail = 0;

            // Roll and move after getting out
            int roll = dice.rollDice();
            System.out.println(name + " rolled " + dice.getDie1Value() + " + " + dice.getDie2Value() + " = " + roll);
            move(roll, gameState.getBoard());

            // Handle the new space
            Space currentSpace = gameState.getBoard().getspace(position);
            handleLandingOnSpace(currentSpace, gameState);
            return;
        }

        // Option 2: Use Get Out of Jail Free card
        if (hasGetOutOfJailFreeCard) {
            System.out.println(name + " uses a Get Out of Jail Free card.");
            hasGetOutOfJailFreeCard = false;
            gameState.releaseFromJail(this);
            turnsInJail = 0;

            // Roll and move after getting out
            int roll = dice.rollDice();
            System.out.println(name + " rolled " + dice.getDie1Value() + " + " + dice.getDie2Value() + " = " + roll);
            move(roll, gameState.getBoard());

            // Handle the new space
            Space currentSpace = gameState.getBoard().getspace(position);
            handleLandingOnSpace(currentSpace, gameState);
            return;
        }

        // Option 3: Try to roll doubles
        int roll = dice.rollDice();
        System.out.println(name + " rolled " + dice.getDie1Value() + " + " + dice.getDie2Value() + " = " + roll);

        if (dice.getDie1Value() == dice.getDie2Value()) {
            System.out.println(name + " rolled doubles and gets out of Jail!");
            gameState.releaseFromJail(this);
            turnsInJail = 0;
            move(roll, gameState.getBoard());

            // Handle the new space
            Space currentSpace = gameState.getBoard().getspace(position);
            handleLandingOnSpace(currentSpace, gameState);
        } else if (turnsInJail >= 3) {
            // After 3 turns, player must pay and get out
            System.out.println(name + " has been in Jail for 3 turns and must pay $50 to get out.");
            subtractMoney(50);
            gameState.releaseFromJail(this);
            turnsInJail = 0;
            move(roll, gameState.getBoard());

            // Handle the new space
            Space currentSpace = gameState.getBoard().getspace(position);
            handleLandingOnSpace(currentSpace, gameState);
        } else {
            System.out.println(name + " stays in Jail.");
        }
    }

    /**
     * Handles landing on different types of spaces.
     *
     * @param space The space the player landed on
     * @param gameState The current game state
     */
    private void handleLandingOnSpace(Space space, GameState gameState) {
        System.out.println(name + " landed on " + space.getName());

        if (space instanceof Property) {
            handlePropertySpace((Property) space);
        } else if (space instanceof RailroadSpace) {
            RailroadSpace railroad = (RailroadSpace) space;
            railroad.onLand(this, gameState);
        } else if (space instanceof SpecialSpace) {
            handleSpecialSpace((SpecialSpace) space, gameState);
        }
    }

    /**
     * Handles landing on a property space.
     *
     * @param property The property landed on
     */
    private void handlePropertySpace(Property property) {
        if (property.isOwned()) {
            if (property.getOwner() != this) {
                int rent = property.getRent();
                System.out.println(name + " must pay $" + rent + " rent to " + property.getOwner().getName());
                payRent(property.getOwner(), rent);
            } else {
                System.out.println(name + " owns this property.");
            }
        } else {
            System.out.println("Property is unowned and costs $" + property.getPrice());
            // Automatic buying for now - in a full game, you'd ask the player
            if (money >= property.getPrice()) {
                buyProperty(property);
            }
        }
    }

    /**
     * Handles landing on a special space.
     *
     * @param specialSpace The special space landed on
     * @param gameState The current game state
     */
    private void handleSpecialSpace(SpecialSpace specialSpace, GameState gameState) {
        switch (specialSpace.getType()) {
            case "Start":
                // Player landed directly on Go
                addMoney(200);
                System.out.println(name + " landed on Go and collects $200.");
                break;
            case "Tax":
                int taxAmount = specialSpace.getName().equals("Luxury Tax") ? 100 : 200;
                System.out.println(name + " pays $" + taxAmount + " in taxes.");
                subtractMoney(taxAmount);
                break;
            case "Chance":
                String chanceCard = gameState.drawChanceCard();
                System.out.println(name + " drew a Chance card: " + chanceCard);
                handleCardEffect(chanceCard, gameState);
                break;
            case "Community Chest":
                String communityCard = gameState.drawCommunityChestCard();
                System.out.println(name + " drew a Community Chest card: " + communityCard);
                handleCardEffect(communityCard, gameState);
                break;
            case "Free Parking":
                System.out.println(name + " landed on Free Parking. Nothing happens.");
                break;
            case "Go To Jail":
                System.out.println(name + " landed on Go To Jail!");
                JailSpace.goToJail(this, gameState);
                break;
            case "Jail":
                System.out.println(name + " is just visiting Jail.");
                break;
            case "Utility":
                // Basic implementation for utilities
                System.out.println(name + " landed on " + specialSpace.getName() + ". Utilities not fully implemented.");
                break;
            case "Railroad":
                // This shouldn't happen if RailroadSpace is implemented correctly
                System.out.println(name + " landed on " + specialSpace.getName() + ". Railroads should be handled separately.");
                break;
            default:
                System.out.println(name + " landed on " + specialSpace.getName());
                break;
        }
    }

    /**
     * Handles the effects of Chance and Community Chest cards.
     * This is a simplified implementation that handles a few common card effects.
     *
     * @param cardText The text of the card
     * @param gameState The current game state
     */
    private void handleCardEffect(String cardText, GameState gameState) {
        // Simple pattern matching for card effects
        if (cardText.contains("Advance to Go")) {
            position = 0;
            addMoney(200);
            System.out.println(name + " advances to Go and collects $200");
        } else if (cardText.contains("Go to Jail")) {
            JailSpace.goToJail(this, gameState);
        } else if (cardText.contains("Get out of Jail Free")) {
            hasGetOutOfJailFreeCard = true;
            System.out.println(name + " received a Get Out of Jail Free card");
        } else if (cardText.contains("collect") || cardText.contains("Collect") || cardText.contains("receive") || cardText.contains("Receive")) {
            // Extract amount and add to player
            String[] words = cardText.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].startsWith("$")) {
                    try {
                        int amount = Integer.parseInt(words[i].substring(1));
                        addMoney(amount);
                        break;
                    } catch (NumberFormatException e) {
                        // Not a valid number, continue
                    }
                }
            }
        } else if (cardText.contains("pay") || cardText.contains("Pay")) {
            // Extract amount and subtract from player
            String[] words = cardText.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].startsWith("$")) {
                    try {
                        int amount = Integer.parseInt(words[i].substring(1));
                        subtractMoney(amount);
                        break;
                    } catch (NumberFormatException e) {
                        // Not a valid number, continue
                    }
                }
            }
        }
        // More complex card effects would need more detailed implementations
    }

    /**
     * Deducts money from the player's account.
     * Alias for subtractMoney for compatibility.
     *
     * @param amount The amount to deduct
     */
    public void deductMoney(int amount) {
        subtractMoney(amount);
    }

    /**
     * Gets all properties owned by this player.
     *
     * @return The list of properties owned by the player
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Sets whether this player has a Get Out of Jail Free card.
     *
     * @param hasCard Whether the player has the card
     */
    public void setHasGetOutOfJailFreeCard(boolean hasCard) {
        this.hasGetOutOfJailFreeCard = hasCard;
    }

    /**
     * Checks if the player has a Get Out of Jail Free card.
     *
     * @return true if the player has the card, false otherwise
     */
    public boolean hasGetOutOfJailFreeCard() {
        return hasGetOutOfJailFreeCard;
    }

    /**
     * Gets the number of turns the player has been in jail.
     *
     * @return The number of turns in jail
     */
    public int getTurnsInJail() {
        return turnsInJail;
    }

    /**
     * Sets the number of turns the player has been in jail.
     *
     * @param turns The number of turns
     */
    public void setTurnsInJail(int turns) {
        this.turnsInJail = turns;
    }

    /**
     * Checks if the player is bankrupt (has no money).
     *
     * @return true if the player is bankrupt, false otherwise
     */
    public boolean isBankrupt() {
        return money <= 0;
    }

    /**
     * Returns a string representation of the player.
     *
     * @return A string representation of the player
     */
    @Override
    public String toString() {
        return name + " ($" + money + ", Position: " + position +
                ", Token: " + token + ", Properties: " + properties.size() + ")";
    }
}