package Model.Board.Strategy;

import Model.Board.ComputerPlayer;
/**
 * Represents a defensive strategy for a computer-controlled player in the game.
 * This strategy prioritizes saving money and has a lower probability of buying properties.
 *
 * Author: Ronell Washington
 */

import Model.Board.Gameboard;
import Model.GameState;
import Model.Property.Property;

import java.util.Random;

public class DefensiveStrategy implements ComputerPlayerStrategy{

    private Property property;

    /**
     * Author: Ronell Washington
     * Executes the defensive strategy for the computer player.
     * The player has a 20% chance of buying a property.
     *
     * @param gameboard The gameboard on which the game is played.
     * @param gameState The current state of the game.
     * @param player The computer player executing the strategy.
     */
    @Override
    public void executeStrategy(Gameboard gameboard, GameState gameState, ComputerPlayer player) {
        Random rand = new Random();
        if (rand.nextInt(100) < 20) {
            player.buyProperty(property);
        } else {
            System.out.println("Defensive strategy: Not buying property.");

        }

    }
}
