/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.game;

import java.io.*;

import cs345.interpret.*;
import cs345.message.*;
import cs345.reader.Builder;

/**
 * This class is an implementation of a Builder for a game.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class GameBuilder implements Builder {

    /* The game this GameBuilder is building. */
    Game game;

    /**
     * Do any setup that is required before calling any of the makeXXX 
     * methods. This method should create the game object that's
     * returned by buildComplete and capture the values of in and out
     * for use later.
     */
    @Override public void startBuild(InputStream in, MessageFormatter out) {
        // Start a new game.
        game = new Game();
        game.commandIn = in;
        game.messageOut = out;
        game.parser = new BaseCommandParser(game);
    }

    /**
     * Do any final work that is required after all the makeXXX
     * methods have been called.
     *
     * This implementation just returns the game object.
     */
    @Override public Game buildComplete() {
        return game;
    }
    
    /*
	 * Make a Word and add it to the game.
	 */
	 
	@Override public Word makeWord(String word, MatchType match) {
      
		
		if (match == MatchType.PREFIX){
			Word pword = new WordPrefix();
			pword.setWord(word.toLowerCase());
			pword.setMatch(match);
			game.allWords.add(pword);
			return pword;
		}
		
		
			Word eword = new WordExact();
			eword.setWord(word.toLowerCase());
			eword.setMatch(match);
			game.allWords.add(eword);
			return eword;
		
    
   }
	
	/**
	 * Make an empty Term and add it to the game.
	 *
	 * This method checks for creation of the "noisewords" term and does
	 * the right thing in that case.
	 */
	@Override public Term makeTerm(String name) {
	    
			Term term = new Terms();			
	        if (name == "noisewords"){		        
	        	game.noise.add(term);	        
	        	return term;	        	
	        }
	        
	        return term;
	    }
	
	/**
	 * Make a Room and add it to the game.
	 */
	@Override public Room makeRoom(String name, Message desc, Message brief) {
		Room newRoom = new Rooms();
		newRoom.setName(name);
		newRoom.setBriefDescription(brief);
		newRoom.setBoundSelect(desc,newRoom);
	
		return newRoom;
	}
	
	/**
	 *Make a Path between rooms.
	 */
	@Override public void makePath(Term vocab, Room from, Room to) {		
		from.addpath(vocab, to);
	    
	}
	
	/**
	 * Make an Action and add it to the set of all Actions.
	 */
	@Override public void makeAction(Term term1, Term term2, int priority, ValidMethod valid, ActionMethod action) {
		Action act = new Action();
		act.setTerm1(term1);
		act.setTerm2(term2);
		act.setAction(action);
		act.setPriority(priority);
		act.setValid(valid);
		game.allActions.add(act);

	}
	
	/**
	 * Make a Player for the game.
	 */
	@Override public Player makePlayer(String name){
		Player player =  new Players(game,name);
		game.thePlayer = player;
		
		
		return player;
	}
	
	/**
	 * Make a GameObject and add it to the game.
	 */
	@Override public GameObject makeGameObject(String name, Term vocab,
		Message inventoryDesc, Message hereIsDesc, Message longDesc) {
		
		GameObject gameObj = new GameObjects(name, vocab,inventoryDesc,hereIsDesc,longDesc);
		game.allObjects.add(gameObj);
		gameObj.setBoundSelect(gameObj);
		
		return gameObj;
	}

    /**
     * Make a message containing the given string.
     * @param msg  the String with the message
     * @return  the created message object
     */
    @Override
    public Message makeMessage(String msg) {
        return new StringMsg(msg);
    }

    /**
     * Make a message consisting of the concatenation of msgs.
     * @param msgs  the Messages that make up the message
     * @return  the created message object
     */
    @Override
    public Message makeMessage(Message... msgs) {
        return new MultiMsg(msgs);
    }

    /**
     * Make a message that produces the indexth argument.
     * @param index   the index of the argument
     * @return  the created message object
     */
    @Override
    public Message makeArgMessage(int index) {
        return new ArgMsg(index);
    }

    /**
     * Make a message that selects one of a number of alternative messages.
     * @param msgs  the messages making up the message.
     * @return the created message object
     */
    @Override public Message makeSelectMessage(Message... msgs) {
        return new SelectMsg(msgs);
    }

    /**
     * Make a message that cycles through a collection of messages. The first
     * time the message is printed, the first of the messages will be used.
     * The second time, the second, and so on. After the last is printed,
     * the messages will cycle back to the first message.
     * 
     * @param msgs  the Messages that make up the cycle
     * @return  the created message object
     */
    @Override
    public Message makeCycleMessage(Message... msgs) {
        return new CycleMsg(msgs);
    }

    /**
     * Set a message as a special message.
     *
     * @param name the name of the message
     * @param msg the message to be used
     */
    @Override public void setSpecialMessage(String name, Message msg) {
    	game.special.put(name, msg);

    }

    /**
     * Set game.prompt to prompt.
     *
     * @param prompt - the new prompt string
     */
    @Override public void setPrompt(String prompt) {
    	game.prompt = prompt;

    }
}
