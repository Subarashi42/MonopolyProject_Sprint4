import java.util.*;

public class GameState {
    private List<Player> players;
    private int currentPlayerIndex;
    private Gameboard board;
    private Dice dice;
    private CommunityChestCards communityChestCards;
    private ChanceCards chanceCards;
    private Map<Player, Boolean> isInJail;
    private boolean gameActive;
    private Bank bank; // Added Bank reference

    public GameState(List<Player> players, Gameboard board) {
        this.players = players; // Initialize players
        this.board = board;  // Use the provided board
        this.dice = new Dice();
        this.communityChestCards = new CommunityChestCards();
        this.chanceCards = new ChanceCards();
        this.isInJail = new HashMap<>();
        for (Player player : this.players) {
            isInJail.put(player, false);
        }
        this.gameActive = true;
        this.currentPlayerIndex = 0;
        communityChestCards.cards();
        chanceCards.cards();
        // Bank will be set separately
    }

    /**
     * Gets the bank for this game.
     *
     * @return The bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Sets the bank for this game.
     *
     * @param bank The bank to use
     */
    public void setBank(Bank bank) {
        this.bank = bank;
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
            currentPlayer.takeTurn(gameboard, this);

            // Move to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Gameboard getBoard() {
        return board;
    }

    public Map<Integer, String> getPropertyOwnership() {
        return board.getPropertyOwnership();
    }

    public int rollDice() {
        return dice.rollDice();
    }

    public int[] getDiceValues() {
        return new int[]{dice.getDie1Value(), dice.getDie2Value()};
    }

    public String drawChanceCard() {
        return chanceCards.shuffleCards();
    }

    public String drawCommunityChestCard() {
        return communityChestCards.shuffleCards();
    }

    public void transferMoney(Player from, Player to, int amount) {
        from.deductMoney(amount);
        to.addMoney(amount);
    }

    public void collectFromBank(Player player, int amount) {
        player.addMoney(amount);
    }

    public void payToBank(Player player, int amount) {
        player.deductMoney(amount);
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public boolean isPlayerInJail(Player player) {
        return isInJail.getOrDefault(player, false);
    }

    public void sendToJail(Player player) {
        isInJail.put(player, true);
    }

    public void releaseFromJail(Player player) {
        isInJail.put(player, false);
    }

    public void displayGameState() {
        System.out.println("Game State: ");
        for (Player player : players) {
            displayPlayerStatus(player);
        }
    }

    public void displayPlayerStatus(Player player) {
        System.out.println(player.getName() + ": $" + player.getMoney() +
                (isPlayerInJail(player) ? " (In Jail)" : ""));
    }

    /**
     * Handles a player going bankrupt.
     * This includes returning all properties to the bank and removing the player from the game.
     *
     * @param player The bankrupt player
     */
    public void handlePlayerBankruptcy(Player player) {
        System.out.println(player.getName() + " is bankrupt and out of the game!");

        // Return all properties to the bank
        for (Property property : player.getProperties()) {
            property.setOwner(null);
            property.setHouses(0);
            property.setHasHotel(false);
            property.setMortgaged(false);
        }

        // Remove player from the game
        players.remove(player);
        isInJail.remove(player);

        // Check if game is over
        if (players.size() == 1) {
            System.out.println(players.get(0).getName() + " wins the game!");
            gameActive = false;
        }
    }

    public void skipBankrupt(Player player) {
        players.remove(player);
    }

    public Object getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public CommunityChestCards getCommunityChestCards() {
        return communityChestCards;
    }

    public ChanceCards getChanceCards() {
        return chanceCards;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setBoard(Gameboard board) {
        this.board = board;
    }

    public Collection<Object> getIsInJail() {
        return Collections.singleton(isInJail.values());
    }

    public boolean getGameActive() {
        return gameActive;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}