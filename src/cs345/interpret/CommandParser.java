/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.interpret;

import java.io.IOException;

import cs345.game.Word;

/**
 * This is the interface implemented by a Command Parser.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface CommandParser {

    /**
     * Names for standard events for use by the Command Parser and others.
     *
     *   game.init -- once before the first command is run
     *   command.exec -- after every call to an action
     *     properties: word1 and word2, the two words of the command
     */
    static final String GAME_INIT_EVENT = "game.init";
    static final String COMMAND_EXEC_EVENT = "command.exec";

    /**
     * This method sets the exit flag.
     *
     * Setting the exit flag to true will cause the parser to exit before
     * the next command is to be executed. If the flag is set to true, the
     * command parser will complete all actions that are part of executing
     * the current command and before any actions, such as asking the user
     * for input, that are part of the next command.
     * 
     * @param exit if true, will cause the parser to exit after completion
     *             of execution of the current command
     * @return the previous value the exit flag
     */
    boolean setExit(boolean exit);

    /**
     * Add a handler to the parser.
     *
     * Add the given handler to the set of event handlers for this parser.
     * Note that handlers are ordered. If there is more than one handler
     * for the same event, the handlers must be invoked in the given order.
     *
     * @param handler the EventHandler to be added
     */
    void addHandler(EventHandler handler);

    /**
     * Remove a handler from the parser.
     *
     * @param handler the EventHandler to be removed
     */
    void removeHandler(EventHandler handler);

    /**
     * Queue an event for later execution.
     *
     * @param event the Event to be queued
     */
    void queueEvent(Event event);

    /**
     * Run the game.
     *
     * The game to be run was provided during construction of the command
     * parser object.
     *
     * @throws IOException if there is an IOException reading input or
     *                     producing output.
     */
    void run() throws IOException;

	boolean ProcessLine(String s);

	Word findWord(String s);
}
