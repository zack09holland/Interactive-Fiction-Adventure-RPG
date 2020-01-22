/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

import java.util.List;

/**
 * This is the interface that must be implemented by Term objects.
 *
 * A term is a set of words. Terms are used in the game to provide
 * the words that "name" something. So,
 *   (1) an Action has two associated terms which are give the Words
 *       that can be used for the command
 *   (2) a Path between rooms is "named" by a Term giving the Words
 *       that can be used to designate that Path
 *   (3) a GameObject is "named" by a Term giving the Words the user
 *       can use to pick up, drop, etc. that object.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Term {

    /**
     * Add the given Word to this Term.
     *
     * This method should only be called during construction of the Game
     * object.
     *
     * @param word the Word to be added
     */
    void addWord(Word word);

    /**
     * Return true if the given Word is in the term.
     *
     * @param w the Word to be checked
     * @return true if w is in the Term.
     */
    boolean contains(Word w);

	List<Word> getWords();
}
