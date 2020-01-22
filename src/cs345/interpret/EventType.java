/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.interpret;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides types for Events.
 *
 * This class is similar to enumerations in the sense that there are a
 * finite number of unique named instances.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class EventType {
	static Map<String,EventType> type = new HashMap<String,EventType>();
	

    /**
     * Return the EventType object with the given name.
     *
     * It is an error if there is no EventType object with the given name.
     * New EventType objects are created by calls to makeNewEventType.
     *
     * @param name the name of the type
     * @return the unique EventType object with that name
     */
    public static EventType getInstance(String name) {
    	return makeNewEventType(name);
  		
    	}
      

    /**
     * Return a new EventType with the given name or the existing EventType
     * object is there is already one with that name.
     *
     * As implied, by the description, it is not an error to calls this method
     * twice with the same name.
     *
     * @param name the name of the type
     * @return EvemtType object with that name
     */
    public static EventType makeNewEventType(String name) {
    	if(type.containsKey(name)){
    		return type.get(name);
    	}
    	
    	EventType temp = new EventType(name);
    	type.put(name, temp);
    	return  temp;
    	
    }
   
    private String name;

    private EventType(String name) {
        this.name = name;        
                      
    }
    

	/**
     * @return the name of the event type
     */
    @Override public String toString() {
        return name;
    }
}
