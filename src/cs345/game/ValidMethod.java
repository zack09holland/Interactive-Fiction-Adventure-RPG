/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.game;

/**
 * This is the interface provides a method to check the validity of an
 * action in a game.
 *
 * This method is very similar to the ActionMethod.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface ValidMethod {

    public boolean isValid(Game game, Word w1, Word w2);

}