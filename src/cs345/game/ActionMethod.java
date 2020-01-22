/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

/**
 * This is the interface provides a method to perform an action as part of a
 * game.
 *
 * In Java 8, this kind of interface is referred to as a "functional interface".
 * That is, the purpose of this interface is to make an object that
 * encapsulates a function. A typical call to a Builder to create an Action
 * might look like this:
 *
 *      builder.makeAction(tQuit, null, new ActionMethod() {
 *          @Override public void doAction(Game game, Word w1, Word w2) {
 *              game.parser.setExit(true);
 *              game.messageOut.println("Goodbye.");
 *          }
 *      });
 *
 * In Java 8 this code could look like:
 *
 *      builder.makeAction(tQuit, null,
 *          (game, w1, w2) -> {
 *              game.parser.setExit(true);
 *              game.messageOut.println("Goodbye.");
 *          }
 *      });
 *
 * What this code doing is to (1) define an anonymous class (a class that doesn't
 * have a name) that implements the ActionMethod interface, (2) create a single
 * instance of that anonymous class, and (3) passing that instance as an argument
 * to makeAction. The doAction method of ActionMethod is overridden in the
 * anonymous class.
 *
 * When an action is to be perfomed, the doAction method of the created anonymous
 * class instance is called.
 *
 * Note:Anonymous classes are very useful when you want to create "one of a kind"
 * classes with a single instance. This feature was added in Java 1.5. See the
 * Java language documentation or Java in a Nutshell, 5th or 6th edition for more
 * on this.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface ActionMethod {

    /**
     * Do an action.
     *
     * @param game the game object for the action
     * @param w1 the first word of the command that caused this action
     * @param w2 the second word of the command or null if this a one word command
     */
     void doAction(Game game, Word w1, Word w2);
    
}
