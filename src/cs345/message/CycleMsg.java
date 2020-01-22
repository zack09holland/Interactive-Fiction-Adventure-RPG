package cs345.message;

import java.util.ArrayList;
import java.util.List;


/*Cycle through messages with counter
 * */
 
public class CycleMsg extends AbstractMsg implements Message {
	private List<Message> message = new ArrayList<Message>();
	int c = 0;
	
	public CycleMsg(Message ... args){		
		for(Message m : args){			
			message.add(m);
		}
			if (c >= message.size()){
				c = 0;
			}
		}
			
	
	

	@Override
	public String getString(Object... args) {
		String temp;		
		temp = message.get(c).getString(args);
		c++;
		if (c >= message.size()){
			c = 0;
		}
		return temp;
		
	}

	

	@Override
	public String getAltString(int alt, Object... args) {
		String temp;		
		temp = message.get(c).getAltString(alt,args);
		c++;
		if (c >= message.size()){
			c = 0;
		}
		return temp;
		
	}

	

}
