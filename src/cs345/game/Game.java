/* This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 */

package cs345.game;

import java.util.*;
import java.io.*;

import cs345.interpret.CommandParser;
import cs345.message.Message;
import cs345.message.MessageFormatter;

/**
 * This class contains global objects for a game.
 *
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class Game extends myAbstractProperties{
	
	
	/**
	 * A set containing all the Word objects.
	 */
	public Set<Word> allWords = new HashSet<Word>();
	
	public List<Term> noise = new ArrayList<Term>();
	
	
	/**
	 * A map of error string commands associated with values
	 */
	public Map<String, Message> special = new HashMap<String, Message>();
				
    /**
     * A set containing all the game objects.
     */
    public Set<GameObject> allObjects = new HashSet<GameObject>();
    
    /**
	 * A set containing all the Action objects.
	 */
    public Set<Action> allActions = new HashSet<Action>();

    /**
     * The player.
     */
	public Player thePlayer;
	
    /**
     * This boolean controls the execution of the command parser.
     * Setting this boolean to true, using parser.setExit(...),
     * will cause the command parser to exit after completing the
     * current command.
     */
	public boolean exit = false;
    
    /**
     * The command parser.
     */
	public CommandParser parser;
	
    /**
     * An input stream that is used to get commands from the user.
     */
	public InputStream commandIn;
	

    /**
     * A MessageFormatter where output is sent to the user.
     */
    public MessageFormatter messageOut;
    

	/**
	 *  String message prompt
	 */
	public String prompt;

    public Game() {
        // Nothing to do so far.
    }
}
