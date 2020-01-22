/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cs345.game.ActionMethod;
import cs345.game.Game;
import cs345.game.GameObject;
import cs345.game.MatchType;
import cs345.game.Player;
import cs345.game.Room;
import cs345.game.Term;
import cs345.game.ValidMethod;
import cs345.game.Word;
import cs345.interpret.*;
import cs345.message.MessageFormatter;
import cs345.message.Message;

/**
 * This class builds a game description for the game
 * "Cloak of Darkness".
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class CloakHardCoded implements GameDescription {
	
	private Builder builder;
	
	private static class WordData {
		MatchType m;
		Word word;
		
		WordData(MatchType m, Word word) {
			this.m = m;
			this.word = word;
		}
	}

	/* wordMap is a used inside the HardCodedGame to track constructed
	 * words. This is used to keep build() from building the same word
	 * multiple times. wordMap is private to the HardCodedGame class.
	 */
	private Map<String, WordData> wordMap;
	
	public CloakHardCoded(Builder builder) {
		this.builder = builder;
	}
	
	private Word makeWord(String w, MatchType m) {
		WordData wData = wordMap.get(w);
		if (wData != null) {
			assert wData.m == m : "Differing MatchTypes for " + w; 
			return wData.word;
		}
		Word newWord = builder.makeWord(w, m);
		wordMap.put(w, new WordData(m, newWord));
		return newWord;
	}
	
	private void addWords(Term vocab, MatchType match, String... words) {
		for (String w : words) {
			Word word = makeWord(w, match);
			vocab.addWord(word);
		}
	}
	
    Map<String, Message> messageMap = new HashMap<String, Message>();
	
    private Message _m(String smsg) {
        Message msg = messageMap.get(smsg);
		if (msg != null)
			return msg;
		msg = builder.makeMessage(smsg);
		messageMap.put(smsg, msg);
		return msg;
	}
	
    private Message _m(Message... msgs) {
        return builder.makeMessage(msgs);
    }
    
    private Message _sm(Message... msgs) {
        return builder.makeSelectMessage(msgs);
    }
    
    private Message _cm(Message... msgs) {
        return builder.makeCycleMessage(msgs);
    }

    private Message _am(String smsg) {
        Message msg = messageMap.get(smsg);
		if (msg != null)
			return msg;
        /* At this point scan the message looking for % markers.
		 * A %n (n in 1 .. 9) indicates that an argument is to be
		 * substituted at this point.
         * A %% substitutes a percent sign.
		 * 
         * XXX This does not allow for more than 9 arguments other niceties.
		 */
        List<Message> msgParts = new ArrayList<Message>();
		int b = 0;
		for (int i = 0; i < smsg.length(); ++i) {
			if (smsg.charAt(i) != '%')
				continue;
			/* Found a % sign. Insert substitution.
			 * XXX No check for exceeding string length, etc. is made.
			 */
			msgParts.add(_m(smsg.substring(b, i)));
			i += 1;
			b = i + 1;
            char indicator = smsg.charAt(i);
            if (indicator == '%')
                msgParts.add(_m("%"));
            else
                msgParts.add(builder.makeArgMessage(indicator - '1'));
		}
		if (b < smsg.length())
			msgParts.add(_m(smsg.substring(b, smsg.length())));
        msg = builder.makeMessage(msgParts.toArray(new Message[msgParts.size()]));
		return msg;
	}

	private List<EventHandler> eventHandlers = new ArrayList<>();
	
	/* Here begins the actual game. */
	
	static Message mDarkHere;
	static boolean messageRead;
	
	private static void describeHere(Game game, Room here) {
		if ((Boolean)here.getProperty("lit")) {
			if (!(Boolean)game.thePlayer.getProperty("verbose") &&
                    (Boolean)here.getProperty("beenhere")) {
				here.getBriefDescription().print(game.messageOut);
			} else {
				here.getDescription().print(game.messageOut);
			}
			for (GameObject item : here.getContents()) {
			    game.messageOut.print(" ");
				item.getHereIsDesc().print(game.messageOut);
			}
		} else {
			mDarkHere.print(game.messageOut);
		}
	}
	
    /**
     * This method locates an object by name in allObjects.
     * @param game the game object for the game
     * @param w a word designating the object
     * @return the found object or null if no object was found.
     */
    private static GameObject findObject(Game game, Word w) {
        for (GameObject it : game.allObjects) {
 			if (it.match(w)) {
				return it;
			}
		}
		return null;
	}

    private static void queuePlayerMovedEvent(Game game, EventType type, Room from, Room to) {
        Event evt = new Event(type);
        evt.setProperty("fromroom", from);
        evt.setProperty("toroom", to);
        game.parser.queueEvent(evt);
    }

	@Override
	public Game build(InputStream in, MessageFormatter out) {
		
		/* wordMap is needed for all versions of hardcoded game. */
		wordMap = new HashMap<String, WordData>();

		/* Call the builder to start the build. */
		builder.startBuild(in, out);
		
		Term noiseWords = builder.makeTerm("noisewords");
		addWords(noiseWords, MatchType.PREFIX, "a", "an", "the", "and", "it",
				"that", "this", "to", "at", "with", "room");
		
		/* Special Messages */
		final Message mDontUnderstand1 = _cm(
				_am("I don't know how to \"%1\"."),
				_am("I don't understand \"%1\"."));
		final Message mDontUnderstand2 = _cm(
				_am("I don't know how to \"%1 %2\"."),
				_am("I don't understand \"%1 %2\"."));
        final Message mDontKnowHowToMove = _am("I don't know how to move %1.");

        builder.setSpecialMessage("word.unknown", _am("I don't understand %1."));
		builder.setSpecialMessage("word.ambiguous",
				_am("I have more than one way to interpret %1."));
		builder.setSpecialMessage("command.allnoise", _cm(
				_m("That was noise to me."),
				_m("Did you want me to do something?")));
		builder.setSpecialMessage("command.toolong", _cm(
				_m("I only understand one and two word commands."),
				_m("You have to keep it brief.")));
		builder.setSpecialMessage("command.unknown.one", mDontUnderstand1);
		builder.setSpecialMessage("command.unknown.two",mDontUnderstand2);
		builder.setPrompt("> ");

		/* Define some vocabulary.
		 */
		final Term vVerbs = builder.makeTerm("verbs");
		final Term vObjects = builder.makeTerm("objects");
		final Term vMove = builder.makeTerm("go");
		addWords(vMove, MatchType.PREFIX, "go", "walk", "proceed");
        wordMap.get("go").word.addAbbreviation("g");
		addWords(vVerbs, MatchType.PREFIX, "go", "walk", "proceed");
		final Term vNorth = builder.makeTerm("north");
		addWords(vNorth, MatchType.PREFIX, "north");
        wordMap.get("north").word.addAbbreviation("n");
		addWords(vObjects, MatchType.PREFIX, "north");
		final Term vSouth = builder.makeTerm("south");
		addWords(vSouth, MatchType.PREFIX, "south");
        wordMap.get("south").word.addAbbreviation("s");
		addWords(vObjects, MatchType.PREFIX, "south");
		final Term vEast = builder.makeTerm("east");
		addWords(vEast, MatchType.PREFIX, "east");
        wordMap.get("east").word.addAbbreviation("e");
		addWords(vObjects, MatchType.PREFIX, "east");
		final Term vWest = builder.makeTerm("west");
		addWords(vWest, MatchType.PREFIX, "west");
        wordMap.get("west").word.addAbbreviation("w");
		addWords(vObjects, MatchType.PREFIX, "west");
		final Term vLook = builder.makeTerm("look");
		addWords(vLook, MatchType.PREFIX, "look", "examine");
		addWords(vVerbs, MatchType.PREFIX, "look", "examine");
		final Term vRead = builder.makeTerm("read");
		addWords(vRead, MatchType.PREFIX, "read");
		addWords(vVerbs, MatchType.PREFIX, "read");
		final Term vInventory = builder.makeTerm("inventory");
		addWords(vInventory, MatchType.PREFIX, "inventory");
		addWords(vVerbs, MatchType.PREFIX, "inventory");
		final Term vGet = builder.makeTerm("get");
		addWords(vGet, MatchType.PREFIX, "get");
		addWords(vVerbs, MatchType.PREFIX, "get");
		final Term vDrop = builder.makeTerm("drop");
		addWords(vDrop, MatchType.PREFIX, "drop");
		addWords(vVerbs, MatchType.PREFIX, "drop");
		final Term vHang = builder.makeTerm("hang");
		addWords(vHang, MatchType.PREFIX, "hang");
		addWords(vVerbs, MatchType.PREFIX, "hang");
		final Term vQuit = builder.makeTerm("quit");
		addWords(vQuit, MatchType.PREFIX, "quit");
		final Term vAround = builder.makeTerm("around");
		addWords(vAround, MatchType.PREFIX, "around");
		addWords(vObjects, MatchType.PREFIX, "around");
		
		final Message mIntro = _m("The Cloak of Darkness (Version 0.1)\n\n" +
								   "Hurrying through the rain swept November night, " +
								   "you're glad to see the bright lights of the Opera House. " +
								   "It's surprising that there aren't more people about; but, " +
								   "what do you expect in a CS assignment ...?\n\n");

        final EventType gameExitEventType = EventType.makeNewEventType("game.exit");
		builder.makeAction(vQuit, null, 0, null, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				game.parser.queueEvent(new Event(gameExitEventType));
			}
		});
				
		final Room rHall = builder.makeRoom("hall",
				_m("You are standing in a spacious hall, splendidly decorated in red and gold, with glittering chandeliers overhead. The entrance from the street is to the north, and there are doorways south and west."),
				_m("You are in the hall."));
		final Room rCloakRoom = builder.makeRoom("cloakroom",
				_m("The walls of this small room were clearly once lined with hooks, though now only one remains. The exit is a door to the east."),
				_m("You are in the cloak room."));
		final Room rBar = builder.makeRoom("bar",
				_m("The bar, much rougher than you'd have guessed after the opulence of the foyer to the north, is completely empty."),
				_m("You are in the bar."));

		final Term vNorthOutHall = builder.makeTerm("northouthall");
		addWords(vNorthOutHall, MatchType.PREFIX, "north", "out", "hall");
		final Term vNorthOut = builder.makeTerm("northout");
		addWords(vNorthOut, MatchType.PREFIX, "north", "out");
		builder.makePath(vNorthOutHall, rBar, rHall);
		final Term vEastOutHall = builder.makeTerm("eastouthall");
		addWords(vEastOutHall, MatchType.PREFIX, "east", "out", "exit", "hall");
		builder.makePath(vEastOutHall, rCloakRoom, rHall);
		builder.makePath(vWest, rHall, rCloakRoom);
		builder.makePath(vSouth, rHall, rBar);
		
		addWords(vObjects, MatchType.PREFIX, "out", "exit", "hall");
		
		/* Initialize properties for rooms. */
		eventHandlers.add(new AbstractEventHandler(EventType.getInstance(CommandParser.GAME_INIT_EVENT)) {
            @Override
            public void handle(Game game, Event evt) {
                rHall.setProperty("lit", true);
                rHall.setProperty("beenhere", false);
                rCloakRoom.setProperty("lit", true);
                rCloakRoom.setProperty("beenhere", false);
                rBar.setProperty("lit", false);
                rBar.setProperty("beenhere", false);

                messageRead = false;
            }
        });
		
		final Term vDirect = builder.makeTerm("direction");
		addWords(vDirect, MatchType.PREFIX, "north", "south", "east", "west", "out", "exit", "hall");

		final Term vVerbose = builder.makeTerm("verbose");
		addWords(vVerbose, MatchType.PREFIX, "verbose");
		final Word wYes = makeWord("yes", MatchType.PREFIX);
		final Word wNo = makeWord("no", MatchType.PREFIX);
		final Term vYesNo = builder.makeTerm("yesno");
		vYesNo.addWord(wYes);
		vYesNo.addWord(wNo);
		
		builder.makeAction(vVerbose, vYesNo, 0, null, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				if (w2 == wYes)
					game.thePlayer.setProperty("verbose", true);
				else
					game.thePlayer.setProperty("verbose", false);
			}		
		});

        final EventType playerMoved = EventType.makeNewEventType("player.moved");

		builder.makeAction(vMove, vDirect, 0, null, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
                Room from = game.thePlayer.getLocation();
				if (game.thePlayer.moveOnPath(w2)) {
					// The player moved.
					queuePlayerMovedEvent(game, playerMoved, from, game.thePlayer.getLocation());
				} else
					// Player could not move
					mDontKnowHowToMove.println(game.messageOut, w2);
			}
		});
		
		final Message mJustArrived =_m("You've only just arrived, and besides, the weather outside seems to be getting worse.");
		
		builder.makeAction(vMove, vNorthOut, 10, new ValidMethod() {
            @Override public boolean isValid(Game game, Word w1, Word w2) {
                return game.thePlayer.getLocation() == rHall;
            }
        }, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				mJustArrived.print(game.messageOut);
			}
		});

        final ActionMethod lookAction = new ActionMethod() {
            @Override public void doAction(Game game, Word w1, Word w2) {
                Room here = game.thePlayer.getLocation();
                Boolean beenHere = (Boolean)here.getProperty("beenhere");
                here.setProperty("beenhere", false);
                describeHere(game, here);
                here.setProperty("beenhere", beenHere);
            }
        };

		builder.makeAction(vLook, null, 0, null, lookAction);
		
		builder.makeAction(vLook, vAround, 0, null, lookAction);
		
		final Term vItems = builder.makeTerm("items");
		
		final Term vHook = builder.makeTerm("hookvocab");
		addWords(vHook, MatchType.PREFIX, "hook", "peg");
		addWords(vItems, MatchType.PREFIX, "hook", "peg");
		addWords(vObjects, MatchType.PREFIX, "hook", "peg");
		final GameObject iHook = builder.makeGameObject("hook", vHook,
				_m("a brass hook"),
				_m("There is a brass hook here."),
				_m(_m("It's just a small brass hook, screwed into the wall."),
					_sm(_m(""), _m(" Your velvet cloak is now hung upon it."))));

		final Term vCloak = builder.makeTerm("cloakvocab");
		addWords(vCloak, MatchType.PREFIX, "cloak", "velvet");
		addWords(vItems, MatchType.PREFIX, "cloak", "velvet");
		addWords(vObjects, MatchType.PREFIX, "cloak", "velvet");
		final GameObject iCloak = builder.makeGameObject("cloak", vCloak,
				_m(_m("a velvet cloak"), _sm(_m(" (worn)"), _m(""))),
				_m("A black velvet cloak hangs on the hook."),
				_m("It is a handsome cloak, of velvet trimmed with satin, and slightly spattered with raindrops. Its blackness is so deep that it almost seems to suck light from the room."));
		
		final Term vMessage = builder.makeTerm("messagevocab");
		addWords(vMessage, MatchType.PREFIX, "message", "sawdust", "floor");
		addWords(vItems, MatchType.PREFIX, "message", "sawdust", "floor");
		addWords(vObjects, MatchType.PREFIX, "message", "sawdust", "floor");
		final GameObject iMessage = builder.makeGameObject("message", vMessage,
				_m(""),
				_m("There seems to be some sort of message scrawled in the sawdust on the floor."),
				_m(_m("The message "),
				   _sm(_m("has been carelessly trampled, making it difficult to read. You can just distinguish the words"),
					   _m("neatly marked in the sawdust reads")),
				   _m("...\n\n"),
				   _m("YOU HAVE "), _sm(_m("LOST"), _m("WON")), _m(" !!!\n")));
		
		/* Properties and locations for game items. */
		eventHandlers.add(new AbstractEventHandler(EventType.getInstance(CommandParser.GAME_INIT_EVENT)) {
            @Override public void handle(Game game, Event evt) {
                rCloakRoom.addObject(iHook);
                game.thePlayer.addObject(iCloak);
                rBar.addObject(iMessage);

                iHook.setState(0);
                iCloak.setState(0);
                iMessage.setState(4);
            }
        });
		
		final Message mPartingShot = _sm(
				_m("I can see why you want to quit.\n"),
				_m("You figured it out too late I see!\n"),
				_m("As you wish! ...\n"),
				_m("Giving up so easily ... you've almost won.\n")
				);
		
		/* Exit handler, print an appropriate message. */
        eventHandlers.add(new AbstractEventHandler(gameExitEventType) {
            @Override public void handle(Game game, Event evt) {
                game.parser.setExit(true);
                if (messageRead) {
                    iMessage.getLongDesc().print(game.messageOut);
                } else {
                    int option = 0;
                    if (iCloak.getState() > 0) {
                        // Cloak is on hook
                        option += 1;
                    }
                    if (iMessage.getState() > 1) {
                        // Message can still be read
                        option += 2;
                    }
                    mPartingShot.altPrintln(option, game.messageOut);
                }
            }
        });

		final Message mAlreadyHung = _m("The cloak is already hung up!");
		final Message mAlreadyWearing = _m("You're already wearing the cloak!");
		final Message mHangingCloak = _m("You hang the cloak on the hook.");
		final Message mWearingCloak = _m("You are wearing the cloak.");
		
		final Message mBetterIdea = _m("I have a better idea: let's get out of here before you disturb anything.");
		mDarkHere = _m("It is dark in here!");
		final Message mNothingToRead = _m("There's nothing here one could read!");
		
		final Message mNotCarryingItem = _am("I see no %1 here!.");
		final Message mNotCarryingAnything = _m("You are empty-handed.");
		final Message mCarryingStart = _m("You are carrying ");
		final Message mCarryingAnd = _m(" and ");
		final Message mCarryingSep = _m(", ");
		final Message mCarryingSepAnd = _m(", and ");
		final Message mCarryingFinal = _m(".");
		
		builder.makeAction(vHang, vCloak, 0, new ValidMethod () {
            @Override public boolean isValid(Game game, Word w1, Word w2) {
                return game.thePlayer.getLocation() == rCloakRoom;
            }
        }, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				if (iCloak.getState() == 1) {
					mAlreadyHung.println(game.messageOut);
					return;
				}
				iCloak.setState(1);
				iHook.setState(1);
				game.thePlayer.removeObject(iCloak);
				rCloakRoom.addObject(iCloak);
				rBar.setProperty("lit", true);
				mHangingCloak.print(game.messageOut);
			}
		});
		
		builder.makeAction(vGet, vCloak, 0, new ValidMethod () {
            @Override public boolean isValid(Game game, Word w1, Word w2) {
                return game.thePlayer.getLocation() == rCloakRoom;
            }
        }, new ActionMethod() {
			@Override
			public void doAction(Game game, Word w1, Word w2) {
				if (iCloak.getState() == 0) {
					mAlreadyWearing.print(game.messageOut);
					return;
				}
				iCloak.setState(0);
				iHook.setState(0);
				rCloakRoom.removeObject(iCloak);
				game.thePlayer.addObject(iCloak);
				rBar.setProperty("lit", false);
				mWearingCloak.print(game.messageOut);
			}
		});
		
		builder.makeAction(vLook, vItems, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                GameObject item = findObject(game, w2);
                assert (item != null);
                Room here = game.thePlayer.getLocation();
                if (game.thePlayer.contains(item) || here.contains(item)) {
                    if ((Boolean) here.getProperty("lit")) {
                        item.getLongDesc().println(game.messageOut);
                    } else {
                        mDarkHere.println(game.messageOut);
                    }
                } else {
                    mNotCarryingItem.println(game.messageOut, w2);
                }
            }
        });
		
		builder.makeAction(vInventory, null, 0, null, new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				Collection<GameObject> c = game.thePlayer.getContents();
				if (c.isEmpty()) {
					mNotCarryingAnything.print(game.messageOut);
				} else if (c.size() <= 2) {
					mCarryingStart.print(game.messageOut);
					Iterator<GameObject> iter = c.iterator();
					GameObject item1 = iter.next();
					item1.getInventoryDesc().print(game.messageOut);
					if (c.size() == 2) {
						mCarryingAnd.print(game.messageOut);
						GameObject item2 = iter.next();
						item2.getInventoryDesc().print(game.messageOut);
					}
					mCarryingFinal.print(game.messageOut);
				} else {
					int itemNo = 1;
					for (GameObject item : c) {
						if (itemNo == 1)
							mCarryingStart.print(game.messageOut);
						else if (itemNo == c.size())
							mCarryingSepAnd.print(game.messageOut);
						else
							mCarryingSep.print(game.messageOut);
						item.getInventoryDesc().print(game.messageOut);
						itemNo += 1;
					}
					mCarryingFinal.print(game.messageOut);
				}
			}
		});
		
		final ValidMethod inBar = new ValidMethod() {
			@Override public boolean isValid(Game game, Word w1, Word w2) {
				return game.thePlayer.getLocation() == rBar;
			}
		};
		
		final ActionMethod readMessage = new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				/* The player is in the Bar. */
				if (!(Boolean)rBar.getProperty("lit")) {
					mDarkHere.print(game.messageOut);
					game.messageOut.print(" ");
					mBetterIdea.print(game.messageOut);
				} else {
					/* In the bar without the cloak. */
					messageRead = true;
					game.parser.queueEvent(new Event(gameExitEventType));
				}
			}			
		};
		
		final ActionMethod nothingToRead = new ActionMethod() {
			@Override public void doAction(Game game, Word w1, Word w2) {
				mNothingToRead.print(game.messageOut);
			}
		};
		
		builder.makeAction(vLook, vMessage, 10, inBar, readMessage);
		builder.makeAction(vRead, vMessage, 10, inBar, readMessage);
		builder.makeAction(vRead, null, 10, inBar, readMessage);
		builder.makeAction(vRead, vMessage, 0, null, nothingToRead);
		builder.makeAction(vRead, null, 0, null, nothingToRead);
	
		final ValidMethod inDarkBar = new ValidMethod() {
			@Override
			public boolean isValid(Game game, Word w1, Word w2) {
				return game.thePlayer.getLocation() == rBar && !(Boolean)rBar.getProperty("lit");
			}
		};
		final ActionMethod betterIdea = new ActionMethod() {
			@Override
			public void doAction(Game game, Word w1, Word w2) {
				mBetterIdea.print(game.messageOut);
			}
		};

		builder.makeAction(vVerbs, null, -10, inDarkBar, betterIdea);
		builder.makeAction(vVerbs, vObjects, -10, inDarkBar, betterIdea);
	
		/* Create and position player and initialize player properties. */
		builder.makePlayer("You");

        eventHandlers.add(new AbstractEventHandler(EventType.getInstance(CommandParser.GAME_INIT_EVENT)) {
            @Override public void handle(Game game, Event evt) {
                game.thePlayer.apportTo(rHall);
                game.thePlayer.setProperty("verbose", false);
            }
        });
		
		/* Final initialization and start. */

        eventHandlers.add(new AbstractEventHandler(EventType.getInstance(CommandParser.GAME_INIT_EVENT)) {
            @Override public void handle(Game game, Event evt) {
				/* Introductory message and start game. */
                mIntro.print(game.messageOut);
                queuePlayerMovedEvent(game, playerMoved, null, game.thePlayer.getLocation());
            }
        });
		
        /* Disturb the message when we are in the Bar and it's not lit. */
        final EventHandler disturbMessage = new AbstractEventHandler(EventType.getInstance(CommandParser.COMMAND_EXEC_EVENT)) {
            @Override public void handle(Game game, Event evt) {
                // We don't count a verbose command as disturbing the message
                if (!vVerbose.contains((Word)evt.getProperty("word1"))) {
                    int state = iMessage.getState();
                    if (state > 0) {
                        iMessage.setState(state - 1);
                        // System.out.println("Decremented message count");
                    }
                }
            }
        };

        /* Respond to player moved event. */
        eventHandlers.add(new AbstractEventHandler(playerMoved) {
            @Override public void handle(Game game, Event evt) {
                Room fromRoom = (Room)evt.getProperty("fromroom");
                Room toRoom = (Room)evt.getProperty("toroom");
                // Describe the location and adjust beenhere
                describeHere(game, toRoom);
                if ((Boolean)toRoom.getProperty("lit"))
                    toRoom.setProperty("beenhere", true);

                // If we've arrived in the Bar, queue the message countdown
                if (toRoom == rBar && !(Boolean)rBar.getProperty("lit"))
                    game.parser.addHandler(disturbMessage);
                //If we've left a dark Bar, remove the message countdown
                if (fromRoom == rBar && !(Boolean)rBar.getProperty("lit"))
                    game.parser.removeHandler(disturbMessage);
            }
        });

		/* Call the builder to finish the build. */
		Game game = builder.buildComplete();

        for (EventHandler eh : eventHandlers) {
            game.parser.addHandler(eh);
        }

        return game;
	}
}