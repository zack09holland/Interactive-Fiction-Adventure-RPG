package cs345.game;

import java.util.HashMap;
import java.util.Map;

import cs345.interpret.PropertyObject;

public abstract class myAbstractProperties implements PropertyObject {
	private Map<String, Object> property = new HashMap<String, Object>();
	

	@Override
	public Object getProperty(String name)/*throws BadPropException*/ {	
		/*if(property.get(name) == null){
			throw new BadPropException("No property for name");
		}*/
		return property.get(name);
	}

	@Override
	public void setProperty(String name, Object value) {
		property.put(name, value);		
	}

	@Override
	public boolean hasProperty(String name) {		
		return property.containsValue(name);
	}
	
	/*public class BadPropException extends Exception {		
		   public BadPropException(String s) {
		        super(s);
		    }
		}*/

}
