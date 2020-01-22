package cs345.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cs345.message.BoundSelect;
import cs345.message.Message;
import cs345.message.MessageFormatter;

public class Rooms extends myContainer implements Room {
	private String name;
	private Collection<GameObject> gameObj =  new ArrayList<GameObject>();
	//private Message description;
	private Message boundSelect;
	private Message brief;
	Map<Term, Room> paths = new HashMap<Term, Room>();
	


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
	public Message getDescription() {				
		return boundSelect;
	}
	
	/* 
	 * add path to Map
	 */
	public void addpath(Term vocab, Room to){
		paths.put(vocab, to);
	}
	
	/* 
	 * Searches through map for Term word that matches 
	 * parameter, returns room
	 */
	public Room getRoom(Word w){
		for(Term key : paths.keySet()){
			if (key.getWords().contains(w)){
				return paths.get(key);
			}
		}
		return null;
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

/*	@Override
	public void setDescription(Message desc) {
		this.description = desc;
		
	}*/


	/**
	 * @param boundSelect the boundSelect to set
	 */
	public void setBoundSelect(Message messageObject, Room r) {
		Message bound = new BoundSelect(messageObject,r);		
		this.boundSelect = bound;
	}


	/**
	 * @return the brief
	 */
	public Message getBriefDescription() {
		return brief;
	}

	/**
	 * @param brief the brief to set
	 */
	public void setBriefDescription(Message brief) {
		this.brief = brief;
	}

	

}
