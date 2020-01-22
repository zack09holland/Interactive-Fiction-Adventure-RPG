package cs345.game;

import java.util.ArrayList;
import java.util.Collection;

public class Players extends myContainer  implements Player {
	private String name;
	private Room location;
	private Game game;
	private Collection<GameObject> gameObjects =  new ArrayList<GameObject>();
	
	Players(Game game, String name){
		this.setName(name);
		this.game =game;
		
	}
	



	@Override
	public void addObject(GameObject obj) {
		gameObjects.add(obj);
		

	}

	@Override
	public void removeObject(GameObject obj) {
		if(gameObjects != null){
			gameObjects.remove(obj);
		}

	}

	@Override
	public boolean contains(GameObject obj) {
		return gameObjects.contains(obj);
	}

	@Override
	public Collection<GameObject> getContents() {
		
		return gameObjects;
	}

	@Override
	// Get the players location;
	public Room getLocation() {
		
		return this.location;
	}

	@Override
	public void apportTo(Room room) {
		this.location = room;

	}

	@Override
	public void startAt(Room room) {
		this.location = room;

	}

	@Override
	public boolean moveOnPath(Word pathName) {
		Room r = this.location.getRoom(pathName);
		if(r!=null){
			this.location = r;
			return true;
		}
		
		
		return false;
	}

	@Override
	public void lookAround() {
		//Game objects in current room
		this.location.getDescription().print(game.messageOut);
				
				for(GameObject gameobj : this.location.getContents()){							
					gameobj.getHereIsDesc().print(game.messageOut);
				}
				game.messageOut.println();
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	

}
