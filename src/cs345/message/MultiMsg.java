package cs345.message;

import java.util.ArrayList;

import java.util.List;


public class MultiMsg extends AbstractMsg implements Message {
	 
	private List<Message> message = new ArrayList<Message>();
	
	
	public MultiMsg(Message ... args){
		for(Message m : args){
			message.add(m);
		}
				
	}
	
	@Override 
	public String getString(Object... args) {	
		StringBuilder buf = new StringBuilder();
			for(Message m : message){
				buf.append(m.getString(args));							
			}
					return buf.toString();		
	}



	@Override
	public String getAltString(int alt, Object... args) {
		StringBuilder buf = new StringBuilder();
		for(Message m : message){
			buf.append(m.getAltString(alt,args));							
		}
				return buf.toString();		
}
	



}
