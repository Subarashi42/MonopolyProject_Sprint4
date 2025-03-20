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

    public GameState(List<Player> players, Gameboard board) {
        this.players = players;
        this.board = board;
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
}

    
