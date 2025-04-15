/**
 * Author: Ronell Washington
 *
 * Represents a computer-controlled player in the game.
 * The computer player uses different strategies to make decisions
 * based on its current financial state.
 *
 */
package Model.Board;

import Model.Board.Strategy.AggressiveStrategy;
import Model.Board.Strategy.ComputerPlayerStrategy;
import Model.Board.Strategy.DefaultStrategy;
import Model.Board.Strategy.DefensiveStrategy;
import Model.GameState;

/**
 * Author: Ronell Washington
 * This class represents a computer player in the game.
 */

public class ComputerPlayer extends Player {
    private ComputerPlayerStrategy strategy;

    /**
     * Author: Ronell Washington
     * Constructs a new `ComputerPlayer` with the specified name.
     * Initializes the player with a default strategy.
     *
     * @param name The name of the computer player.
     */
    public ComputerPlayer(String name) {
        super(name);
        this.strategy = new DefaultStrategy();
    }


    /**
     * Author: Ronell Washington
     * Executes the computer player's turn.
     * The player evaluates its current strategy and performs actions
     * based on the selected strategy.
     *
     * @param gameboard The gameboard on which the game is played.
     * @param gameState The current state of the game.
     */
    @Override
    public void takeTurn(Gameboard gameboard, GameState gameState) {
        super.takeTurn(gameboard, gameState);
        evaluateStrategy();
        strategy.executeStrategy(gameboard, gameState, this);
    }


    /**
     * Author: Ronell Washington
     * Evaluates and updates the computer player's strategy based on its current state.
     * The strategy is determined by the player's money:
     * - Aggressive strategy if money is greater than 1000.
     * - Defensive strategy if money is less than 500.
     * - Default strategy otherwise.
     */
    private void evaluateStrategy() {
        if(getMoney() > 1000) {
            strategy = new AggressiveStrategy();
        } else if(getMoney() < 500) {
            strategy = new DefensiveStrategy();
        } else {
            strategy = new DefaultStrategy();
        }

    }
}
