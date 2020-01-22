/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.message;

/**
 * A message that can be output using a MessageFormatter object.
 *
 * Messages can be output in one of two ways. A message can be a complete
 * message or a fragment. A message that is output using a call to print
 * (or one of its variants) is assumed to be a fragment that will be
 * followed by additional messages to make a complete message. A message
 * that is output using a call to println is assumed to be either a
 * complete message or the end of a message that may be preceded by
 * message fragments.
 *
 * Messages can also have alternatives and arguments. A message with
 * alternatives logically consists of multiple messages, one of which
 * is chosen as the actual output. Arguments to a message are Objects
 * (effectively Strings) which provided information to be substituted
 * in appropriate places in the message.
 *
 * Methods are provided that allow any or all of print versus println,
 * alternatives and arguments to be used with a message. In general, if
 * any information is supplied that is not required, that information
 * is ignored. So, if arguments are supplied when no arguments are
 * required, the arguments are ignored.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Message {

    /**
     * This method returns the message as a String.
     *
     * This method returns the String that would result from a call
     * to print with the given arguments.
     *
     * @param args The objects for filling in parameters
     * @return the string representing the message.
     */
    String getString(Object... args);
    
    /**
     * Output the message as a fragment with the given additional
     * arguments substituted.
     *
     * The arguments are objects. The toString() method will be called
     * on the objects to produce the value to be printed.
     *
     * This method can be called with no arguments if no arguments are
     * required. If arguments are supplied for a message that does not
     * require arguments the arguments are ignored.
     *
     * @param out  A stream where the message is to be output.
     * @param args The objects for filling in parameters.
     */
    void print(MessageFormatter out, Object... args);

    /**
     * Output the message as a complete message with the given additional
     * arguments substituted.
     *
     * Arguments are processed as described in the print method, above.
     *
     * @param out  A formatter where the message is to be output.
     * @param args The objects for filling in parameters.
     */
    void println(MessageFormatter out, Object... args);

    /**
     * This method returns the message as a String.
     *
     * This method returns the String that would result from a call
     * to print with the given arguments.
     *
     * The int alt chooses the alternative to be output. A message without
     * alternatives is treated as a single alternative. If 0 <= alt < n,
     * where n is the number of alternatives, then the given alternative
     * is output. If alt < 0, the first (zeroth) alternative is output. If
     * alt >= n, the last alternative (n-1) is output.
     *
     * @param alt The alternative to be returned
     * @param args The objects for filling in parameters
     * @return the string representing the message.
     */
    String getAltString(int alt, Object... args);

    /**
     * Output the specified alternative message as a fragment with the
     * given additional arguments substituted.
     *
     * Arguments are processed as described in the print method, above.
     *
     * Alternatives are processed as described in the getAltString method,
     * above.
     *
     * @param out A stream where the message is to be output
     * @param alt The alternative to be output.
     * @param args The objects for filling in parameters.
     */
    void altPrint(int alt, MessageFormatter out, Object... args);

    /**
     * Output the specified alternative message as a complete message
     * with the given additional arguments substituted.
     *
     * Arguments are processed as described in the getAltString method,
     * above.
     *
     * @param out  A formatter where the message is to be output
     * @param alt The alternative to be output
     * @param args The objects for filling in parameters
     */
    void altPrintln(int alt, MessageFormatter out, Object... args);
}
