/**
 * ComputerPlayerStrategy.java
 * This interface defines a strategy for computer players in a game.
 * It contains a single method, executeStrategy, which takes a Gameboard,
 * GameState, and ComputerPlayer as parameters.
 *
 * @author Ronell Washington
 */

package Model.Board.Strategy;

import Model.Board.ComputerPlayer;
import Model.Board.Gameboard;
import Model.GameState;

public interface ComputerPlayerStrategy {
    void executeStrategy(Gameboard gameboard, GameState gameState, ComputerPlayer player);
}
