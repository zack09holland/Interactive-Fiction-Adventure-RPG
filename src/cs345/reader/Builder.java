/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.reader;

import java.io.InputStream;

import cs345.game.*;
import cs345.message.Message;
import cs345.message.MessageFormatter;

/**
 * This is the interface for classes that build games.
 *
 * This interface is structured using the Builder design pattern. A client
 * of this interface will work by:
 *   1. Calling startBuild to begin construction of a Game object.
 *   2. Repeatedly calling make... methods to create the components of a
 *      game.
 *   3. Calling buildComplete to finish construction and get the complete
 *      Game object.
 *
 * The commandIn and messageOut attributes of the Game object are set by
 * the in and out parameters of the call to startBuild. The Builder is
 * also responsible for constructing a CommandParser object and saving
 * that in the parser field for the game.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Builder {

    /**
     * Begin building a game.
     *
     * This method is provided to perform setup and initialization when
     * beginning to build a game. This method will be called exactly once
     * to begin construction.
     * @param in the InputStream (probably System.in) the game should use
     *           for obtaining input.
     * @param out the MessageFormatter the game should use for output.
     */
    void startBuild(InputStream in, MessageFormatter out);

    /**
     * Finish building a game.
     *
     * Do any final cleanup that might be required on a Game object. This
     * method will be called exactly once after all make... calls to make
     * components of the game.
     * @return the completed Game object
     */
    Game buildComplete();

    /**
     * Make a Word and add it to the game.
     * 
     * @param word the string value for the word, all lower case
     * @param match the MatchType for the word, either MatchType.PREFIX or
     *              MatchType.EXACT
     * @return the created Word object
     */
    Word makeWord(String word, MatchType match);

    /**
     * Make an empty Term and add it to the game.
     *
     * Words will be added to the Term using the addWord method for the
     * Term. All words will be added before buildComplete is called.
     * 
     * @param name the String name for the Term
     * @return the created Term object
     */
    Term makeTerm(String name);

    /**
     * Make a Room and add it to the game.
     * 
     * @param name the String name for the Room
     * @param desc a Message describing the Room in detail
     * @param brief a Message briefly describing the Room
     * @return the created Room object
     */
    Room makeRoom(String name, Message desc, Message brief);

    /**
     * Make a Path between rooms.
     * 
     * @param vocab a Term for words that designate (name) the path
     * @param from the Room object where the path originates
     * @param to the Room object where the path terminates
     */
    void makePath(Term vocab, Room from, Room to);

    /**
     * Make an Action and add it to the set of all Actions.
     * 
     * @param term1 the first Term for this Action.
     * @param term2 the second Term for this Action or null if this action
     *              only takes a single Word.
     * @param priority the priority of the action
     * @param valid the ValidMethod which determines whether this action is
     *              valid at this time. null may be passed for valid, in which
     *              case the action is always valid.
     * @param action the ActionMethod object whose doAction method will be
     *               executed when this action is to be performed
     */
    void makeAction(Term term1, Term term2, int priority, ValidMethod valid, ActionMethod action);

   /**
     * Make a Player for the game.
     *
     * This method needs to set the thePlayer attribute of the game to the
     * created object.
     *
     * @param name the name of the player
     */
    Player makePlayer(String name);

    /**
     * Make a GameObject and add it to the game.
     * 
     * @param name a String giving the name of the object
     * @param term the Term that is  for the object
     * @param inventoryDesc a Message with the inventory description
     * @param hereIsDesc a Message with the "here is" description
     * @param longDesc a Message with the long description
     * @return the created GameObject
     */
    GameObject makeGameObject(
            String name, Term term,
            Message inventoryDesc, Message hereIsDesc, Message longDesc);
    
    /**
     * Make a message object from a String.
     * @param msg  the string containing the message.
     * @return the created message object.
     */
    Message makeMessage(String msg);

    /**
     * Make a message object from a sequence of other messages.
     * @param msgs  the messages making up the message.
     * @return the created message object.
     */
    Message makeMessage(Message... msgs);

    /**
     * Make an argument message that outputs a specific argument.
     * @param index  the index of the specific argument to be output.
     * @return the created argument message object.
     */
    Message makeArgMessage(int index);

    /**
     * Make a message from a list of other messages. The messages
     * in the list are alternatives. The printAlt message selects
     * the alternative to be printed.
     * @param msgs  the messages making up the message.
     * @return the created message object.
     */
    Message makeSelectMessage(Message... msgs);

    /**
     * Make a message that cycles through a collection of messages. The first
     * time the message is printed, the first of the messages will be used.
     * The second time, the second, and so on. After the last is printed,
     * the messages will cycle back to the first message.
     *
     * @param msgs  the messages making up the message.
     * @return the created argument cycle message object.
     */
    Message makeCycleMessage(Message... msgs);

    /**
     * Set a special message for use in the game.
     *
     * The following names for special messages are defined for use by the
     * interpreter:
     *   "word.unknown" -- a user input with no possible interpretation.
     *      Takes one argument, the String the user entered.
     *   "ambiguous.word" -- a user input with an ambigrous interpretation.
     *      That is, a prefix of multiple words. Takes one argument, the
     *      String the user entered.
     *   "command.toolong" -- a user input with too many words (other than
     *      noise words). Takes no arguments.
     *   "command.allnoise" -- a user input with all noise words. Takes no
     *      arguments.
     *   "command.unknown.one" -- a one word command not corresponding to
     *      any action. Takes one arguments, the word.
     *   "command.unknown.two" -- a two word command not corresponding to
     *      any actions. Takes twos arguments, the two words.
     */
    void setSpecialMessage(String name, Message msg);

    /**
     * Set the user input prompt string.
     *
     * @param prompt - the new prompt string
     */
    void setPrompt(String prompt);
}
