/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.interpret;

import cs345.game.myAbstractProperties;

/**
 * An event in a game.
 *
 * This is a very simple object. However, Events are property objects
 * which means that arbitrary properties can be added to events.get
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class Event extends myAbstractProperties implements PropertyObject {

    private EventType type;

    /**
     * Create an Event. The Event is created with the event type.
     *
     * @param type the type of this event.
     */
    public Event(EventType type) {
        this.type = type;
    }

    /**
     * Get the type of the event.
     * @return the type of this event.
     */
    public EventType getType() {
        return type;
    }

	
}
