package Controller;

import Model.Board.Bank;
import Model.Board.Dice;
import Model.Board.Gameboard;
import Model.Board.Player;
import Model.GameState;
import Model.Property.Property;
import Model.Spaces.RailroadSpace;
import Model.Spaces.Space;
import Model.Spaces.UtilitySpace;

import java.util.List;

/**
 * Author: Marena Abboud 
 * This class is responsible for controlling the game board and managing the game state.
 * It handles the interactions between the players and the game board.
 */
public class BoardController {
    private GameState gameState;
    private Gameboard board;
    private Bank bank;
    private List<Player> players;
    private Dice dice;

    /**
     * Author: Marena Abboud
     * Constructs a new BoardController with the specified game components.
     *
     * @param gameState The current game state
     * @param board The game board
     * @param bank The bank
     * @param players The list of players
     */
    public BoardController(GameState gameState, Gameboard board, Bank bank, List<Player> players) {
        this.gameState = gameState;
        this.board = board;
        this.bank = bank;
        this.players = players;
        this.dice = gameState.getDice();
    }

    /**
     * Author: Marena Abboud
     * Initializes a new game.
     */
    public void initializeGame() {
        // Give starting money to each player
        for (Player player : players) {
            bank.giveStartingMoney(player);
        }

        // Set the bank's available properties
        List<Property> properties = new java.util.ArrayList<>();
        for (Space space : board.getSpaces()) {
            if (space instanceof Property) {
                properties.add((Property) space);
            }
        }
        bank.setAvailableProperties(properties);

        // Initialize game state
        gameState.setGameActive(true);
        gameState.setCurrentPlayerIndex(0);
    }

    /**
     * Author: Marena Abboud
     * Handles a player's turn.
     *
     * @param player The player whose turn it is
     * @return True if the player gets another turn (e.g., rolled doubles), false otherwise
     */
    public boolean handlePlayerTurn(Player player) {
        System.out.println("\n" + player.getName() + " is taking their turn.");

        // Check if player is in jail
        if (gameState.isPlayerInJail(player)) {
            return handleJailTurn(player);
        }

        // Roll the dice and move player
        int[] diceValues = rollDice();
        int roll = diceValues[0] + diceValues[1];
        boolean isDoubles = (diceValues[0] == diceValues[1]);

        System.out.println(player.getName() + " rolled " + diceValues[0] + " + " + diceValues[1] + " = " + roll);

        // Check for three consecutive doubles (go to jail)
        if (isDoubles) {
            System.out.println(player.getName() + " rolled doubles!");

            if (dice.shouldGoToJail()) {
                System.out.println(player.getName() + " rolled three consecutive doubles and is going to jail!");
                sendToJail(player);
                return false;
            }
        } else {
            // Reset doubles counter
            dice.resetConsecutiveDoubles();
        }

        // Move the player
        movePlayer(player, roll);

        // Handle the space the player landed on
        handleSpaceLanding(player);

        // Return whether player gets another turn due to doubles
        return isDoubles && !gameState.isPlayerInJail(player);
    }

    /**
     * Author: Marena Abboud
     * Handles a player's turn when they are in jail.
     *
     * @param player The player in jail
     * @return False (player's turn ends after jail processing)
     */
    private boolean handleJailTurn(Player player) {
        System.out.println(player.getName() + " is in Jail (Turn " + (player.getTurnsInJail() + 1) + " in jail)");
        player.setTurnsInJail(player.getTurnsInJail() + 1);

        // This would be expanded based on player's choice (UI interaction)
        // For now, implementing a simple strategy:

        // Option 1: Use Get Out of Jail Free card if available
        if (player.hasGetOutOfJailFreeCard()) {
            useGetOutOfJailCard(player);
            return false;
        }

        // Option 2: Pay to get out if can afford it
        if (player.getMoney() >= 50) {
            payToGetOutOfJail(player);
            return false;
        }

        // Option 3: Roll for doubles
        int[] diceValues = rollDice();
        boolean isDoubles = (diceValues[0] == diceValues[1]);

        System.out.println(player.getName() + " rolled " + diceValues[0] + " + " + diceValues[1]);

        if (isDoubles) {
            System.out.println(player.getName() + " rolled doubles and gets out of Jail!");
            releaseFromJail(player);
            movePlayer(player, diceValues[0] + diceValues[1]);
            handleSpaceLanding(player);
        } else if (player.getTurnsInJail() >= 3) {
            // After 3 turns, player must pay and get out if possible
            if (player.getMoney() >= 50) {
                System.out.println(player.getName() + " has been in Jail for 3 turns and must pay $50 to get out.");
                payToGetOutOfJail(player);
            } else {
                System.out.println(player.getName() + " cannot afford to pay the jail fee after 3 turns.");
                // Handle bankruptcy or other strategies here
            }
        } else {
            System.out.println(player.getName() + " stays in Jail.");
        }

        return false;
    }

    /**
     * Author: Marena Abboud
     * Rolls the dice.
     *
     * @return An array containing the values of both dice
     */
    public int[] rollDice() {
        int roll = dice.rollDice();
        return new int[]{dice.getDie1Value(), dice.getDie2Value()};
    }

    /**
     * Author: Marena Abboud
     * Moves a player on the board.
     *
     * @param player The player to move
     * @param steps The number of steps to move
     */
    public void movePlayer(Player player, int steps) {
        int oldPosition = player.getPosition();
        int newPosition = (oldPosition + steps) % board.getSpaces().size();

        // Check if player passed Go
        if (newPosition < oldPosition && oldPosition + steps >= board.getSpaces().size()) {
            System.out.println(player.getName() + " passed Go and collects $200!");
            bank.playerPassedGo(player);
        }

        player.setPosition(newPosition);
        System.out.println(player.getName() + " moved from " + oldPosition + " to " + newPosition +
                " (" + board.getspace(newPosition).getName() + ")");
    }

    /**
     * Author: Marena Abboud
     * Handles what happens when a player lands on a space.
     *
     * @param player The player who landed on a space
     */
    public void handleSpaceLanding(Player player) {
        Space currentSpace = board.getspace(player.getPosition());

        // Different actions based on space type
        if (currentSpace instanceof Property) {
            handlePropertyLanding(player, (Property) currentSpace);
        } else if (currentSpace instanceof RailroadSpace) {
            handleRailroadLanding(player, (RailroadSpace) currentSpace);
        } else if (currentSpace instanceof UtilitySpace) {
            handleUtilityLanding(player, (UtilitySpace) currentSpace);
        } else {
            // For other space types
            currentSpace.playerOnSpecialSpace();

            // Handle special spaces based on their type
            if (currentSpace.getType().equals("Chance")) {
                handleChanceCard(player);
            } else if (currentSpace.getType().equals("Community Chest")) {
                handleCommunityChestCard(player);
            } else if (currentSpace.getType().equals("Go To Jail")) {
                sendToJail(player);
            } else if (currentSpace.getType().equals("Tax")) {
                handleTaxSpace(player, currentSpace.getName());
            }
        }
    }

    /**
     * Author: Marena Abboud
     * Handles landing on a property space.
     *
     * @param player The player who landed on the property
     * @param property The property landed on
     */
    private void handlePropertyLanding(Player player, Property property) {
        System.out.println(player.getName() + " landed on " + property.getName());

        if (property.isOwned()) {
            if (property.getOwner() != player) {
                if (property.isMortgaged()) {
                    System.out.println(property.getName() + " is mortgaged, no rent is due.");
                } else {
                    int rent = property.calculateRent(gameState);
                    System.out.println(player.getName() + " must pay $" + rent + " rent to " + property.getOwner().getName());
                    player.payRent(property.getOwner(), rent);
                }
            } else {
                System.out.println(player.getName() + " owns this property.");
            }
        } else {
            // Property is not owned
            System.out.println(property.getName() + " is not owned. It costs $" + property.getPrice());

            // This would be expanded with UI interaction for buying decision
            // For demonstration, assume player buys if they can afford it
            if (player.getMoney() >= property.getPrice()) {
                bank.sellProperty(property, player);
            } else {
                System.out.println(player.getName() + " cannot afford " + property.getName());
                // Could implement auction here
            }
        }
    }

    /**
     * Author: Marena Abboud
     * Handles landing on a railroad space.
     *
     * @param player The player who landed on the railroad
     * @param railroad The railroad landed on
     */
    private void handleRailroadLanding(Player player, RailroadSpace railroad) {
        System.out.println(player.getName() + " landed on " + railroad.getName());

        if (railroad.isOwned()) {
            if (railroad.getOwner() != player) {
                int rent = railroad.calculateRent(gameState);
                System.out.println(player.getName() + " must pay $" + rent + " rent to " + railroad.getOwner().getName());
                player.payRent(railroad.getOwner(), rent);
            } else {
                System.out.println(player.getName() + " owns this railroad.");
            }
        } else {
            System.out.println(railroad.getName() + " is not owned. It costs $" + railroad.getPrice());

            // This would be expanded with UI interaction
            if (player.getMoney() >= railroad.getPrice()) {
                player.buyRailroad(railroad);
            } else {
                System.out.println(player.getName() + " cannot afford " + railroad.getName());
                // Could implement auction here
            }
        }
    }

    /**
     * Author: Marena Abboud
     * Handles landing on a utility space.
     *
     * @param player The player who landed on the utility
     * @param utility The utility landed on
     */
    private void handleUtilityLanding(Player player, UtilitySpace utility) {
        System.out.println(player.getName() + " landed on " + utility.getName());

        if (utility.isOwned()) {
            if (utility.getOwner() != player) {
                // Roll dice to determine rent
                int diceRoll = dice.rollDice();
                int rent = utility.calculateRent(diceRoll, gameState);

                System.out.println(player.getName() + " rolled " + diceRoll + " and must pay $" + rent + " to " + utility.getOwner().getName());
                player.payRent(utility.getOwner(), rent);
            } else {
                System.out.println(player.getName() + " owns this utility.");
            }
        } else {
            System.out.println(utility.getName() + " is not owned. It costs $" + utility.getPrice());

            // This would be expanded with UI interaction
            if (player.getMoney() >= utility.getPrice()) {
                player.buyUtility(utility);
            } else {
                System.out.println(player.getName() + " cannot afford " + utility.getName());
                // Could implement auction here
            }
        }
    }

    /**
     * Author: Marena Abboud
     * Handles drawing a Chance card.
     *
     * @param player The player who drew the card
     */
    private void handleChanceCard(Player player) {
        String cardText = gameState.drawChanceCard();
        System.out.println(player.getName() + " drew a Chance card: " + cardText);
        player.processCardEffect(cardText, gameState);
    }

    /**
     * Author: Marena Abboud
     * Handles drawing a Community Chest card.
     *
     * @param player The player who drew the card
     */
    private void handleCommunityChestCard(Player player) {
        String cardText = gameState.drawCommunityChestCard();
        System.out.println(player.getName() + " drew a Community Chest card: " + cardText);
        player.processCardEffect(cardText, gameState);
    }

    /**
     * Author: Marena Abboud
     * Handles landing on a tax space.
     *
     * @param player The player who landed on the tax space
     * @param taxSpaceName The name of the tax space
     */
    private void handleTaxSpace(Player player, String taxSpaceName) {
        int taxAmount = 0;

        if (taxSpaceName.equals("Income Tax")) {
            taxAmount = 200;
        } else if (taxSpaceName.equals("Luxury Tax")) {
            taxAmount = 100;
        }

        if (taxAmount > 0) {
            System.out.println(player.getName() + " must pay $" + taxAmount + " in taxes");
            player.subtractMoney(taxAmount);
        }
    }

    /**
     * Author: Marena Abboud
     * Sends a player to jail.
     *
     * @param player The player to send to jail
     */
    public void sendToJail(Player player) {
        player.setPosition(10); // Jail is at position 10
        gameState.sendToJail(player);
        System.out.println(player.getName() + " has been sent to Jail!");
    }

    /**
     * Author: Marena Abboud
     * Pays to get a player out of jail.
     *
     * @param player The player to release from jail
     */
    public void payToGetOutOfJail(Player player) {
        if (player.subtractMoney(50)) {
            gameState.releaseFromJail(player);
            System.out.println(player.getName() + " paid $50 to get out of Jail.");
        } else {
            System.out.println(player.getName() + " does not have enough money to pay the jail fee.");
        }
    }

    /**
     * Author: Marena Abboud
     * Uses a Get Out of Jail Free card to release a player from jail.
     *
     * @param player The player to release from jail
     */
    public void useGetOutOfJailCard(Player player) {
        if (player.hasGetOutOfJailFreeCard()) {
            player.setHasGetOutOfJailFreeCard(false);
            gameState.releaseFromJail(player);
            // Return the card to the appropriate deck
            // This would need logic to determine which deck it came from
            gameState.returnGetOutOfJailFreeCard("Chance"); // Default to Chance deck
            System.out.println(player.getName() + " used a Get Out of Jail Free card.");
        } else {
            System.out.println(player.getName() + " does not have a Get Out of Jail Free card.");
        }
    }

    /**
     * Author: Marena Abboud
     * Releases a player from jail.
     *
     * @param player The player to release from jail
     */
    public void releaseFromJail(Player player) {
        gameState.releaseFromJail(player);
        player.setTurnsInJail(0);
        System.out.println(player.getName() + " has been released from Jail!");
    }

    /**
     * Author: Marena Abboud
     * Processes the next turn in the game.
     *
     * @return True if the game continues, false if the game is over
     */
    public boolean nextTurn() {
        if (!gameState.isGameActive()) {
            return false;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        boolean getAnotherTurn = handlePlayerTurn(currentPlayer);

        // Check if player went bankrupt
        if (currentPlayer.isBankrupt()) {
            handlePlayerBankruptcy(currentPlayer);
            getAnotherTurn = false;
        }

        // Move to next player if needed
        if (!getAnotherTurn) {
            gameState.nextTurn();
            System.out.println("Turn ended. Current player: " + gameState.getCurrentPlayer().getName());
        } else {
            System.out.println(currentPlayer.getName() + " gets another turn for rolling doubles!");
        }

        return gameState.isGameActive();
    }

    /**
     * Author: Marena Abboud
     * Handles a player going bankrupt.
     *
     * @param player The bankrupt player
     */
    private void handlePlayerBankruptcy(Player player) {
        gameState.handlePlayerBankruptcy(player);
    }

    /**
     * Author: Marena Abboud
     * Gets the current game state.
     *
     * @return The current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Author: Marena Abboud
     * Gets the game board.
     *
     * @return The game board
     */
    public Gameboard getBoard() {
        return board;
    }

    /**
     * Author: Marena Abboud
     * Gets the bank.
     *
     * @return The bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Author: Marena Abboud
     * Gets the list of players.
     *
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Author: Marena Abboud
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return !gameState.isGameActive() || players.size() <= 1;
    }

    /**
     *
     * Author: Marena Abboud
     * Gets the winner of the game (if game is over).
     *
     * @return The winning player, or null if game is not over
     */
    public Player getWinner() {
        if (isGameOver() && players.size() == 1) {
            return players.get(0);
        }
        return null;
    }
}