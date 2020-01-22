package cs345.message;

import java.util.ArrayList;
import java.util.List;

/* Get the first message object
 * 
 * */


public class SelectMsg extends AbstractMsg implements Message {
	private List<Message> message = new ArrayList<Message>();

		
	public SelectMsg(Message ... args){		
		for(Message m : args){
			message.add(m);
		}		
	}

	@Override
	public String getString(Object... args) {
		return message.get(0).getString(args);
	}
	
	@Override
	public String getAltString(int alt, Object... args) {
		
		return message.get(alt).getString(args);
	}

	

}
