/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

import cs345.message.Message;
import cs345.message.MessageFormatter;

/**
 * This is the interface provided for Rooms.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Room extends Container {

    /**
     * Get the description for the room.
     * @return the Message containing the description.
     */
    Message getDescription();

	void setName(String name);

	//void setDescription(Message desc);

	void addpath(Term vocab, Room to);

	Room getRoom(Word pathName);

	void setBoundSelect(Message desc, Room newRoom);
	
	Message getBriefDescription();

	void setBriefDescription(Message brief);

	
}
