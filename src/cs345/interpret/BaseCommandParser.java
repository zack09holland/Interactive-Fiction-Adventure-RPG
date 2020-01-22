/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.interpret;

import cs345.game.Action;

import cs345.game.Game;
import cs345.game.MatchType;
import cs345.game.Word;
import cs345.game.Words;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * This class is a skeleton for the command parser for a game.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class BaseCommandParser implements CommandParser{
	 List<EventHandler> eventHandlers = new ArrayList<EventHandler>();
	 List<Event> eventQueue = new ArrayList<Event>();
	
	
// add protected special message attribute?
	private Game game;
		

	/**
     * Construct a new command parser.
     * 
     * @param game the Game object for this parser.
     */
	public BaseCommandParser(Game game) {
	    this.game = game;
		game.parser = this;
	}
	
	
	public void handleEvents(){
		//List<Event> copyQueue = new ArrayList<Event>(eventQueue);
		List<EventHandler> copy = new ArrayList<EventHandler>(eventHandlers);
		while (!eventQueue.isEmpty()){
						 
			Event e = eventQueue.remove(0);
			for (EventHandler h : copy){
				if (h.getEventType() == e.getType()){
					h.handle(game, e);
					
				}
				
			}
		}
	}

    /**
     * Run the command interpreter.
     *
     * @throws IOException
     */
	public void run() throws IOException {
		Scanner input = new Scanner(game.commandIn);
		//game.messageOut.prompt( "? ");
		queueEvent(new Event ( EventType.makeNewEventType("game.init")));
		handleEvents();				
	    while (!game.exit) {	
	    	game.messageOut.startLine();
	    	game.messageOut.prompt("? ");	    	
			 if (!ProcessLine(input.nextLine()))
	                break;
	            	            
			handleEvents();
		}
		game.messageOut.println();
	}
	
    /**
     * Set the exit flag to the specified value.
     *
     * @param exit the new value for the exit flag
     * @return the old value of the exit flag
     */
	@Override
	public boolean setExit(boolean exit) {
		boolean old = game.exit;
		game.exit = exit;
		return old;
	}
	
    /**
     * Run one command
     * @throws IOException if there is an IOException when reading or
     *         writing the input or output streams.
     */

	@Override
	//Process input into user tokens
	public boolean ProcessLine(String s) {
		  s = s.trim();
	        if (s.length() == 0) {
	            // Empty line, do nothing
	            return true;
	        }
	        String[] tokens = s.trim().split("\\s+");
	        
	        // Convert the tokens to words. Build words. Exit if there
	        // was an error
	       
	        List<Word> words = new ArrayList<Word>();
	        	        	
	            for (String t : tokens) {
	            	Word aWord = findWord(t);
	            	//System.out.println(aWord);
	            	   	    
	            	if(!game.noise.get(0).getWords().contains(aWord)){	            
	            		words.add(aWord);	
	            	}
  	
	            	else{
	            		game.special.get("command.allnoise").println(game.messageOut);
	            		return true;
	            	}
	            }
	            
	            	        
	        // Check for the correct number of words.
	        if (words.size() > 2) {
	        	game.special.get("command.toolong").println(game.messageOut);
	        	//game.messageOut.println("I only understand one or two word commands.");
	            return true;
	        }
	        
	        
	        
	        // Find an action
	        Word word1 = words.get(0);
	        Word word2 = (words.size() > 1) ? words.get(1) : null;
	        Action act = findAction(word1, word2);
             
	        if (act == null) {
	        	if(word1 !=null && word2 == null){
	        		game.special.get("command.unknown.one").println(game.messageOut, word1.getWord());
	        	
	        	}
	        	 if(word1 !=null && word2 != null){
	        			game.special.get("command.unknown.two").println(game.messageOut,word1.getWord(),word2.getWord());
	        			
	        		}
	        	
	            return true;
	        }
	        return !doAction(act,game, word1, word2);
	    }
		
	@Override
	//Takes parameter user input string 
	// returns correct word
	public Word findWord(String s) {
    	int count = 0;
    	Word newword = new Words();
    	
    	for (Word w : game.allWords){ 
    		
    		    		
    		if (w.match(s) == (MatchType.PREFIX) && !(w.getwordAbbs().contains(s)) ){

    			if(count == 0){
    				newword = w;
    			}
    			
    			count++;
    		}

    		if (s.equalsIgnoreCase(w.getWord()) || w.getwordAbbs().contains(s)){
    			return w;			
    		}
    		
    	}

    	if(count == 1){
    		return newword;

    	}else if(count > 1){

    		game.special.get("word.ambiguous").println(game.messageOut, s);

    	}else if(count == 0){
    		game.special.get("word.unknown").println(game.messageOut, s);
    		
    	}

    	return null; 		
 }
	
	
   //Takes two word objects form user and 
	// 
   public Action findAction(Word word1, Word word2) {  
	Action chosenAction = null;
   	for(Action act : game.allActions){    
   		
   		if ((act.getTerm1().getWords().contains(word1)) &&
                               ((word2 == null && act.getTerm2() == null) || 
                                ((word2 != null && act.getTerm2()!= null &&
                                        act.getTerm2().getWords().contains(word2)))) && 
                                        	act.checkValid(game,word1,word2)){
   			
   			if (chosenAction == null || 
   					act.getPriority() > chosenAction.getPriority()){
   				
   				chosenAction = act;
   			}
   			
   			return chosenAction;
   		}    				
   	}
   	 	
       return null;
  }	
	
			
   public boolean doAction(Action act, Game game, Word w1, Word w2) {
   act.doAction(game, w1, w2);
   Event execute = new Event ( EventType.makeNewEventType("command.exec"));
   execute.setProperty("word1", w1);
   execute.setProperty("word2", w2);
   eventQueue.add(execute);
   
   	
       return (game.exit);
   }

@Override
public void addHandler(EventHandler handler) {
	
	eventHandlers.add(handler);
	
}

@Override
public void removeHandler(EventHandler handler) {		
	if(eventHandlers != null){
		eventHandlers.remove(handler);
	}
	
}

@Override
public void queueEvent(Event event) {
	eventQueue.add(event);
	}
	
}
	
	
	
	
	
	
