import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    System.out.println("=== MONOPOLY GAME DEMO ===\n");

    // Initialize game components
    Gameboard board = new Gameboard();
    Tokens.initializeTokens();
    Scanner scanner = new Scanner(System.in);

    // Create players
    Player player1 = new Player("Player 1");
    Player player2 = new Player("Player 2");

    // Demo token selection
    System.out.println("Available tokens: " + Tokens.getavailabletokens());
    demoTokenSelection(player1, player2);

    // Demo board movement and property purchase
    System.out.println("\n=== BOARD MOVEMENT DEMO ===");
    demoMovement(board, player1, player2);

    // Demo property management
    System.out.println("\n=== PROPERTY MANAGEMENT DEMO ===");
    demoPropertyManagement(board, player1);

    // Demo other components
    System.out.println("\n=== OTHER COMPONENTS DEMO ===");
    JailSpace jail = new JailSpace();
    demoJail(jail, player2);

    Hotels hotels = new Hotels();
    Houses houses = new Houses();
    Money money = new Money();
    demoMoneyTransactions(money, player1, player2);

    scanner.close();
    System.out.println("\nDemo completed!");
  }

  private static void demoTokenSelection(Player player1, Player player2) {
    // Demo selecting tokens for players
    boolean success = Tokens.chooseToken(player1, "Race Car");
    System.out.println(player1.getName() + " chose the Race Car token: " + (success ? "Success!" : "Failed!"));

    success = Tokens.chooseToken(player2, "Top Hat");
    System.out.println(player2.getName() + " chose the Top Hat token: " + (success ? "Success!" : "Failed!"));

    // Show remaining tokens
    System.out.println("Remaining tokens: " + Tokens.getavailabletokens());
  }

  private static void demoMovement(Gameboard board, Player player1, Player player2) {
    // Move player1 to position 1 (Mediterranean Avenue)
    movePlayer(board, player1, 1);

    // Move player2 to position 3 (Baltic Avenue)
    movePlayer(board, player2, 3);

    // Move player1 to Jail (position 10)
    movePlayer(board, player1, 10);
  }

  private static void movePlayer(Gameboard board, Player player, int position) {
    // Update token position
    Tokens.moveToken(player, position);

    Space space = board.getspace(position);
    System.out.println(player.getName() + " moved to " + space.getName());

    // If it's a property, try to buy it
    if (space instanceof Property) {
      Property property = (Property) space;
      if (!property.isOwned() && player.getBalance() >= property.getPrice()) {
        property.setOwner(player);
        player.decreaseBalance(property.getPrice());
        System.out.println(player.getName() + " purchased " + property.getName() +
                " for $" + property.getPrice());
      }
    }
  }

  private static void demoPropertyManagement(Gameboard board, Player player) {
    // Assume player owns Mediterranean Avenue (position 1)
    Property property = (Property) board.getspace(1);
    if (property.getOwner() == player) {
      System.out.println(player.getName() + " owns " + property.getName());

      // Add houses
      for (int i = 0; i < 4; i++) {
        property.addHouse();
        System.out.println("Added house to " + property.getName() +
                " - Now has " + property.getHouses() + " houses");
      }

      // Add hotel (5th house converts to hotel)
      property.addHouse();
      System.out.println("Added hotel to " + property.getName() +
              " - Has hotel: " + property.hasHotel());
    }
  }

  private static void demoJail(JailSpace jail, Player player) {
    System.out.println(player.getName() + " is going to jail!");
    jail.sendToJail(player);
    System.out.println("Player in jail: " + jail.isInJail(player));

    System.out.println("Attempting to get out of jail...");
    jail.getOutOfJail(player);
    System.out.println("Player in jail: " + jail.isInJail(player));
  }

  private static void demoMoneyTransactions(Money money, Player player1, Player player2) {
    int startingBalance1 = player1.getBalance();
    int startingBalance2 = player2.getBalance();

    System.out.println(player1.getName() + " initial balance: $" + startingBalance1);
    System.out.println(player2.getName() + " initial balance: $" + startingBalance2);

    // Transfer $100 from player1 to player2
    money.transfer(player1, player2, 100);

    System.out.println("After transferring $100:");
    System.out.println(player1.getName() + " new balance: $" + player1.getBalance());
    System.out.println(player2.getName() + " new balance: $" + player2.getBalance());
  }
}