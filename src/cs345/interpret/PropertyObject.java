/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.interpret;

import cs345.game.myAbstractProperties;

/**
 * This interface is implemented by objects that have properties.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public interface PropertyObject {

    /**
     * Get the property with the given name. It is an error if there is
     * no property with the given name.
     *
     * @param name the name of the property
     * @return the value of the property with the given name.
     * @throws BadPropException 
     */
    public Object getProperty(String name) ;

    /**
     * Set the property with the given name. If the property does not exist
     * it is created.
     *
     * @param name the name of the property
     * @param value the value to set the property
     */
    public void setProperty(String name, Object value);

    /**
     * Test to see if there is a property with the given name.
     *
     * @param name the name of the property
     * @return true if the property exists
     */
    public boolean hasProperty(String name);
}
