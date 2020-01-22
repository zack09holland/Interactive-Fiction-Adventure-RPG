/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

import cs345.message.Message;

/**
 * This is the interface that must be implemented by all GameObjects.
 *
 * GameObjects and inanimate things that are scatted about the game.
 * GameObjects are kept in Containers. GameObjects have the following
 * attributes:
 *   1. A name which is a String.
 *   2. A Term which is used to decide whether a command refers to a
 *      GameObject. If the user were to enter the command "get coin". Coin
 *      would refer to a particular GameObject if the Word object coin is
 *      in the Term for that object.
 *   3. A short word or phrase which is the inventory description. This
 *      is used for messages like "you are carrying a gold coin." The
 *      inventory description would be "a gold coin".
 *   4. A short sentence which is the here-is description. This description
 *      is used when entering a room to describe objects that are in the
 *      room. For example, "There is a gold coin here."
 *   5. A longer sentence or sentences providing a detailed description of
 *      the GameObject which is the long description. For example, "This
 *      is a beautiful gold US double eagle dated 1900."
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface GameObject extends Container {

	/**
	 * Check to see if the given Word identifies this GameObject.
	 *
	 * The Word identifies this GameObject if the Word is in the Term
	 * for this GameObject.
	 *
	 * @param w  the word to be matched.
	 * @return  true if the word identifies this item.
	 */
	boolean match(Word w);

	/**
	 * Get the inventory (short phrase) description.
	 * @return  the description
	 */
	Message getInventoryDesc();

	/**
	 * Get the here is (short sentence) description.
	 * @return  the desciption
	 */
	Message getHereIsDesc();

	/**
	 * Get the long (examine) description.
	 * @return  the description
	 */
	Message getLongDesc();

	void setBoundSelect(GameObject gameObj);
}
