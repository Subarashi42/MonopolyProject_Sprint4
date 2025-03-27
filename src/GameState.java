import java.util.List;
import java.util.Map;

/**
 * Interface to represent the state of a Monopoly game.
 * Tracks players, board, dice, cards and overall game status.
 */
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

    public GameState() {
            this.players = new Player();  // Initialize players
            this.board = new Gameboard();  // Initialize board
            this.dice = new Dice();
            this.communityChestCards = new CommunityChestCards();
            this.chanceCards = new ChanceCards();
            this.isInJail = new HashMap<>();
            for (Player player : players) {
                isInJail.put(player, false);
            }
            this.gameActive = true;
            this.currentPlayerIndex = 0;
            communityChestCards.cards();
            chanceCards.cards();
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
            currentPlayer.takeTurn(currentPlayer, gameboard, players);

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

    public void skipBankrupt(Player alice) {
        players.remove(alice);
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

    
