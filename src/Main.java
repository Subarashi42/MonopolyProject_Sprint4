import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class to run the Monopoly game.
 */
public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to Monopoly!");

    // Initialize the game components
    Gameboard board = new Gameboard();
    Tokens.initializeTokens();
    Houses houses = new Houses();
    Hotels hotels = new Hotels();
    Bank bank = new Bank();

    // Create players
    List<Player> players = createPlayers();

    // Let players choose tokens
    assignPlayerTokens(players);

    // Initialize game state
    GameState gameState = new GameState(players, board);

    // Main game loop
    playGame(gameState, houses, hotels, bank);

    // End of game
    announceWinner(players);
  }

  /**
   * Creates players for the game.
   *
   * @return A list of players
   */
  private static List<Player> createPlayers() {
    Scanner scanner = new Scanner(System.in);
    List<Player> players = new ArrayList<>();

    System.out.println("How many players? (2-8)");
    int numPlayers = 0;

    // Input validation
    while (numPlayers < 2 || numPlayers > 8) {
      try {
        numPlayers = Integer.parseInt(scanner.nextLine());
        if (numPlayers < 2 || numPlayers > 8) {
          System.out.println("Please enter a number between 2 and 8.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number.");
      }
    }

    // Create player objects
    for (int i = 1; i <= numPlayers; i++) {
      System.out.println("Enter name for Player " + i + ":");
      String name = scanner.nextLine();
      players.add(new Player(name));
    }

    return players;
  }

  /**
   * Assigns tokens to players.
   *
   * @param players The list of players
   */
  private static void assignPlayerTokens(List<Player> players) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("\nChoose your tokens:");

    for (Player player : players) {
      boolean validTokenSelected = false;

      while (!validTokenSelected) {
        Tokens.displayAvailableTokens();
        System.out.println(player.getName() + ", choose a token:");
        String token = scanner.nextLine();

        validTokenSelected = player.chooseToken(token);
        if (!validTokenSelected) {
          System.out.println("Token not available. Please choose another.");
        }
      }
    }
  }

  /**
   * Main game loop.
   *
   * @param gameState The game state
   * @param houses The houses object
   * @param hotels The hotels object
   * @param bank The bank object
   */
  private static void playGame(GameState gameState, Houses houses, Hotels hotels, Bank bank) {
    Scanner scanner = new Scanner(System.in);
    int turnCount = 0;
    int maxTurns = 20; // Limit for demonstration purposes

    System.out.println("\nStarting the game!");

    // Main game loop
    while (gameState.isGameActive() && turnCount < maxTurns && gameState.getPlayers().size() > 1) {
      Player currentPlayer = gameState.getCurrentPlayer();
      System.out.println("\n--- Turn " + (turnCount + 1) + " ---");
      gameState.displayGameState();

      System.out.println("\nPress Enter for " + currentPlayer.getName() + " to take their turn...");
      scanner.nextLine();

      // Player takes their turn
      currentPlayer.takeTurn(gameState.getBoard(), gameState);

      // Check for bankrupt players
      if (currentPlayer.isBankrupt()) {
        System.out.println(currentPlayer.getName() + " is bankrupt and out of the game!");
        gameState.getPlayers().remove(currentPlayer);
        if (gameState.getPlayers().size() == 1) {
          System.out.println("\nGame over! " + gameState.getPlayers().get(0).getName() + " wins!");
          break;
        }
      } else {
        // Move to next player
        gameState.nextTurn();
      }

      turnCount++;
    }

    if (turnCount >= maxTurns) {
      System.out.println("\nReached maximum number of turns. Game ends.");
    }
  }

  /**
   * Announces the winner of the game.
   *
   * @param players The list of remaining players
   */
  private static void announceWinner(List<Player> players) {
    if (players.size() == 1) {
      System.out.println("\n" + players.get(0).getName() + " is the winner!");
    } else if (players.size() > 1) {
      // Find player with most assets
      Player richestPlayer = players.get(0);
      int highestValue = calculatePlayerValue(richestPlayer);

      for (int i = 1; i < players.size(); i++) {
        Player player = players.get(i);
        int value = calculatePlayerValue(player);
        if (value > highestValue) {
          richestPlayer = player;
          highestValue = value;
        }
      }

      System.out.println("\nGame over! " + richestPlayer.getName() +
              " wins with total assets worth $" + highestValue + "!");
    } else {
      System.out.println("\nNo winner determined.");
    }
  }

  /**
   * Calculates a player's total value (money + property values).
   *
   * @param player The player
   * @return The player's total value
   */
  private static int calculatePlayerValue(Player player) {
    int value = player.getMoney();

    // Add property values
    for (Property property : player.getProperties()) {
      value += property.getPrice();
      // Add house/hotel values
      if (property.hasHotel()) {
        value += 5 * Houses.getHousePrice(property.getColorGroup());
      } else {
        value += property.getHouses() * Houses.getHousePrice(property.getColorGroup());
      }
    }

    return value;
  }

  /**
   * Simple demonstration mode with pre-defined moves.
   *
   * @param gameState The game state
   */
  private static void runDemonstration(GameState gameState) {
    System.out.println("\nRunning demonstration mode...");

    // Pre-defined sequence of turns for demonstration
    for (int i = 0; i < 10; i++) {
      Player currentPlayer = gameState.getCurrentPlayer();
      System.out.println("\n--- Turn " + (i + 1) + " ---");

      // Player takes their turn
      currentPlayer.takeTurn(gameState.getBoard(), gameState);

      // Move to next player
      gameState.nextTurn();

      // Short pause between turns
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}