package cs345.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cs345.message.BoundSelect;
import cs345.message.Message;

public class GameObjects extends myContainer implements GameObject {
	private String name;
	private Term command;
	private Message shortDescribe;
	private Message hereIs;
	private Message longDescribe;
	
	private Set<GameObject> gameObj = new HashSet<GameObject>();
	
	GameObjects(String name, Term command, Message shortDescribe, Message hereIs, Message longDescribe){
		this.name = name;
		this.command = command;
		this.shortDescribe = shortDescribe;
		this.hereIs = hereIs;
		this.longDescribe = longDescribe;
	}

	
	/*
	 * Create new bound select object
	 * then reassign to attributes
	 */
	public void setBoundSelect(GameObject game){
		Message boundShort = new BoundSelect(shortDescribe,game);
		Message boundHere = new BoundSelect(hereIs,game);
		Message boundLong = new BoundSelect(longDescribe,game);
		this.shortDescribe = boundShort;
		this.hereIs = boundHere;
		this.longDescribe = boundLong;
	}

	@Override
	public void addObject(GameObject obj) {
		gameObj.add(obj);

	}

	@Override
	public void removeObject(GameObject obj) {
		if(gameObj !=null){
		gameObj.remove(obj);
		}

	}

	@Override
	public boolean contains(GameObject obj) {
		
		return gameObj.contains(obj);
	}

	@Override
	public Collection<GameObject> getContents() {
		
		return gameObj;
	}

	@Override
	public boolean match(Word w) {
		
		 return command.contains(w);
	}

	@Override
	public Message getInventoryDesc() {
		
		 return shortDescribe;
	}

	@Override
	public Message getHereIsDesc() {
		
		return hereIs;
	}

	@Override
	public Message getLongDesc() {
		
		return longDescribe;
	}

}
