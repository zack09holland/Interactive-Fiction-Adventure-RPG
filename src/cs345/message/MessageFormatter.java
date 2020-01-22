/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.message;

/**
 * This is the interface provided for a MessageFormatter for a game.
 *
 * A MessageFormatter is used to create a message to the user. Conceptually,
 * a MessageFormatter maintains a buffer containing the initial part of a
 * message. When a message fragment is passed to the formatter (print), the
 * fragment is added to the buffer. When a message is passed to the formatter
 * (println), the message is added to the buffer, the buffer is output, and
 * the buffer is cleared.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface MessageFormatter {

    /**
     * Add the String s as a message fragment to the current message.
     *
     * @param s  the String to be added.
     */
    public void print(String s);
    
    /**
     * Add the String s to the current message (if the current message is
     * empty, s is the entire message), output the message to the user,
     * and start a new message.
     *
     * @param s the String to be output
     */
    public void println(String s);
    
    /**
     * This is the same as println(""). Any message currently in the
     * buffer is output.
     */
    public void println();

    /**
     * If if there is a current, non-empty, partial message, finish it as
     * if println() was called.
     *
     * If there is no current partial message, do nothing.
     */
    public void startLine();

    /**
     * Output the String s to the user as a prompt for user input. If there
     * is any partial message currently in the buffer, s will be added to
     * the end of that message to create the prompt.
     *
     * It is assumed that the next action after this message will be to ask
     * for user input that will cause the next output to start at the
     * beginning of a new line.
     */
    public void prompt(String s);
}
