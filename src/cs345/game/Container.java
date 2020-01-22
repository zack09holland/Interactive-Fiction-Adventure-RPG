/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.game;

import java.util.Collection;

import cs345.interpret.PropertyObject;

/**
 * This interface must be implemented by all containers.
 *
 * A container is an object (the Player, a Room, or a GameObject) that
 * can contain (other) GameObjects.
 *
 * In addition to containing other objects, containers have a name and
 * a state. The name is a String identifying the container and the state
 * is an int that indicates changes in the container. The state might,
 * for example, reflect whether an switch is on or off.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */

public interface Container extends PropertyObject  {

    /**
     * Return the state of this container.
     *
     * @return the state
     */
    public int getState();

    /**
     * Set the state of this container to the given value.
     *
     * @param state the new value for the state
     */
    public void setState(int state);

    /**
     * Add the given object to the container.
     *
     * It is assumed that the object has been removed from any other
     * container before being added to this one.
     * 
     * @param obj  the object to be added to the container.
     */
    void addObject(GameObject obj);
	
    /**
     * Remove the given object from the container.
     *
     * It is assumed that this container currently contains the object.
     * 
     * @param obj  the object to be removed from the container.
     */
    void removeObject(GameObject obj);
	
    /**
     * Return true if the given object is in the container.
     * 
     * @param obj  the object to be checked
     * @return  true if the object is in the container
     */
    boolean contains(GameObject obj);
	
    /**
     * Return a collection of all the objects in the container.
     * 
     * @return  the collection
     */
    Collection<GameObject> getContents();
}
