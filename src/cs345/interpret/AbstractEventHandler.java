/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.interpret;

import cs345.game.Game;

/**
 * This is the interface for an EventHandler used by the Command Parser.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public abstract class AbstractEventHandler implements EventHandler {

    private EventType type;

    /**
     * Construct an EventHandler.
     * @param type the EventType that is handled by this handler
     */
    public AbstractEventHandler(EventType type) {
        this.type = type;
    }

    /**
     * Return the type of event handled by this handler.
     *
     * @return the EventType
     */
    public EventType getEventType() {
        return type;
    }

    /**
     * Handle an event.
     *
     * @param game the game in which this event is occurring
     * @param event the event to be handled
     */
    public abstract void handle(Game game, Event event);
}
