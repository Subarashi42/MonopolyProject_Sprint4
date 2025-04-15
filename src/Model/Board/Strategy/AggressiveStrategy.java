/**
 * Represents an aggressive strategy for a computer-controlled player in the game.
 * This strategy prioritizes buying properties with a higher probability.
 * Author: Ronell Washington
 */

package Model.Board.Strategy;

import Model.Board.ComputerPlayer;
import Model.Board.Gameboard;
import Model.GameState;
import Model.Property.Property;

import java.util.Random;

public class AggressiveStrategy implements ComputerPlayerStrategy {

    private Property property;

    /**
     * Author: Ronell Washington
     * Executes the aggressive strategy for the computer player.
     * The player has an 80% chance of buying a property.
     *
     * @param gameboard The gameboard on which the game is played.
     * @param gameState The current state of the game.
     * @param player The computer player executing the strategy.
     */
    @Override
    public void executeStrategy(Gameboard gameboard, GameState gameState, ComputerPlayer player) {
        Random rand = new Random();
        if (rand.nextInt(100) < 80) {
            System.out.print("Aggressive strategy: Buying property.");
            player.buyProperty(property);
        } else {
            System.out.println("Aggressive strategy: Not buying property.");
        }

    }
}
