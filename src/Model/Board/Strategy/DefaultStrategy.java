/**
 * Represents the default strategy for a computer-controlled player in the game.
 * This strategy has a balanced approach with a 50% chance of buying a property.
 *
 * Author: Ronell Washington
 */
package Model.Board.Strategy;

import Model.Board.ComputerPlayer;
import Model.Board.Gameboard;
import Model.GameState;
import Model.Property.Property;

import java.util.Random;

public class DefaultStrategy implements ComputerPlayerStrategy {
    private Property property;

    /**
     * Author: Ronell Washington
     * Executes the default strategy for the computer player.
     * The player has a 50% chance of buying a property.
     *
     * @param gameboard The gameboard on which the game is played.
     * @param gameState The current state of the game.
     * @param player The computer player executing the strategy.
     */
    @Override
    public void executeStrategy(Gameboard gameboard, GameState gameState, ComputerPlayer player) {
        Random rand = new Random();
        if (rand.nextInt(100) < 50) {
            System.out.print("Default strategy: Buying property.");
            player.buyProperty(property);
        } else {
            System.out.println("Default strategy: Not buying property.");
        }

    }

}
