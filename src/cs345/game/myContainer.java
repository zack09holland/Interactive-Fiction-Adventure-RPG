package cs345.game;

/**
 * Container super-interface for Room, GameObject, Player
 *
 */
public class myContainer extends myAbstractProperties{
	private int state = 0;
	
	
	
	public int getState() {		
		return state;
	}

	
	public void setState(int state) {
		this.state = state;

	}

}
