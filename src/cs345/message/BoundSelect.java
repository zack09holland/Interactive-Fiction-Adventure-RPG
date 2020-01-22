package cs345.message;

import cs345.game.Container;


/**
 * make message object of messages based on state of game objects
 *
 */
public class BoundSelect extends AbstractMsg implements Message {
	private int state;
	private Message mess;
	
	public BoundSelect(Message m, Container cont){
		this.mess = m;
		this.state = cont.getState();
		
	}

	@Override
	public String getString(Object... args) {		
		return mess.getAltString(state,args);
	}

	@Override
	public String getAltString(int alt, Object... args) {		
		 return mess.getAltString(state,args);
	}

}
