/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

/**
 * This is the interface provided for Players.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface Player extends Container {

    /**
     * Return the room that is the player's current location.
     *
     * @return the current location
     */
    public Room getLocation();
    
    /**
     * Move the player to the designated location.
     *
     * @param room the room to move the player to.
     */
    public void apportTo(Room room);

    /**
     * Start the player at the given room. Used to initialize the player
     * at the start of the game.
     *
     * @param room the location to start the player.
     */
    public void startAt(Room room);

    /**
     * Move the player along the designated path.
     *
     * The pathName parameter is a Word object that must match the Term
     * associated with some Path that originates at this Room. If such a
     * Path exists, the player's current location is change to the
     * destination of the Path.
     *
     * @param pathName the Word designating the path to use
     * @return true if the player successfully moves, false otherwise
     */
    boolean moveOnPath(Word pathName);

    /**
     * Output a description of the current location of the player.
     *
     * The description should consist of the description of the Room that
     * is the Player's current location and the "here is" description of
     * any GameObjects that are located in that Room.
     */
    void lookAround();

	
}
