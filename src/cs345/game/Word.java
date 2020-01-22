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
 * This is the interface implemented by words.
 *
 * A word is a String of letters that represents part of the vocabulary
 * of the game. Words come in two varieties, prefix words and exact words.
 * A prefix word is one where the user can enter a prefix of the full
 * word and have that match the word. An exact word is one where the user
 * must enter the full word in order to match.
 *
 * It is assumed that the String for the word is all lower case letters. In
 * all cases, the String returned by toString must be all lower case.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Word {

    /**
     * Return the string for the word represented by this Word object. The
     * string must be all lower case letters.
     *
     * @return the String for the word
     */
    String toString();

	void setWord(String lowerCase);

	void setMatch(MatchType match);

	String getWord();

	MatchType getMatch();

	MatchType match(String s);

	void addAbbreviation(String string);

	List<String> getwordAbbs();
}
