/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.reader;

import java.io.*;
import java.util.*;

import cs345.game.*;
import cs345.message.Message;
import cs345.message.MessageFormatter;

/**
 * This class builds a hard coded game.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class HardCodedGame implements GameDescription {
    
    private Builder builder;
    
    private class WordData {
        MatchType m;
        Word word;
        
        WordData(MatchType m, Word word) {
            this.m = m;
            this.word = word;
        }
    }
    
    /* wordMap is a used inside the HardCodedGame to track constructed
     * words. This is used to keep build() from building the same word
     * multiple times. wordMap is private to the HardCodedGame class --
     * and should stay that way!
     */
    private Map<String, WordData> wordMap;
    
    public HardCodedGame(Builder builder) {
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
    
    @Override public Game build(InputStream in, MessageFormatter out) {
    
        /* wordMap is needed for all versions of hardcoded game. */
        wordMap = new HashMap<String, WordData>();

        /* Call the builder to start the build. */
        builder.startBuild(in, out);
        
        /* This section contains basic commands that only require words,
         * terms, and actions to work.
         */
        Term noiseWords = builder.makeTerm("noisewords");
        addWords(noiseWords, MatchType.PREFIX, "a", "an", "the", "and", "it",
                "that", "this", "to", "at", "with", "room");
        
        /* Special messages */
        final Message mDontUnderstand1 = _cm(
                _am("I don't know how to \"%1\"."),
                _am("I don't understand \"%1\"."));
        final Message mDontUnderstand2 = _cm(
                _am("I don't know how to \"%1 %2\"."),
                _am("I don't understand \"%1 %2\"."));
        final Message mDontKnowHowToMove = _am("I don't know how to %1 %2 from here.");
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
        builder.setPrompt("? ");

        /* quit, exit, kill, magic */
        final Word wQuit = makeWord("quit", MatchType.PREFIX);
        final Word wExit = makeWord("exit", MatchType.PREFIX);
        wExit.addAbbreviation("ex");
        final Word wKill = makeWord("kill", MatchType.PREFIX);
        final Word wExecute = makeWord("execute", MatchType.PREFIX);
        final Word wMagic = makeWord("xyzzy", MatchType.EXACT);
        
        /* These define some basic Terms and Actions that can be
         * used without having to define rooms and paths.
         */
        final Term vQuit = builder.makeTerm("quit");
        vQuit.addWord(wQuit);
        vQuit.addWord(wExit);
        
        final Term vKill = builder.makeTerm("kill");
        vKill.addWord(wKill);
        vKill.addWord(wExecute);
        
        final Term vMagic = builder.makeTerm("magic");
        vMagic.addWord(wMagic);
        
        final Message mFinal = builder.makeMessage(_m("Hope you enjoyed your game."), _m(" Come back and play again."));
        builder.makeAction(vQuit, null, 0, null, new ActionMethod() {
            @Override public void doAction(Game game, Word w1, Word w2) {
                mFinal.println(game.messageOut);
                game.parser.setExit(true);
            }
        });
        
        final Message mKill = _cm(
                _m(_cm(_m("What exactly is it"), _m("I don't know what")),
                        _am(" you want me to %1?")),
                _m("Why do you persist in asking that?"));
        builder.makeAction(vKill, null, 0, null, new ActionMethod() {
            @Override public void doAction(Game game, Word w1, Word w2) {
                mKill.println(game.messageOut, w1);
            }
        });
        
        final Term vKillStuff = builder.makeTerm("killstuff");
        addWords(vKillStuff, MatchType.PREFIX, "gold", "silver", "floor", "wall");

        final Message mKillStuff = _cm(
                _am("How exactly do you propose that I %1 the %2?"),
                _am("Are you so unhappy with the %2 that you want me to %1 it?"));
        builder.makeAction(vKill, vKillStuff, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                mKillStuff.println(game.messageOut, w1, w2);
            }
        });
        
        /* This section adds Rooms, Paths, and Words, Terms, and Actions
         * for moving around the game.
         */
        final Term vMove = builder.makeTerm("move");
        addWords(vMove, MatchType.PREFIX, "move", "go", "proceed", "walk");
        wordMap.get("go").word.addAbbreviation("g");

        final Term vNorth = builder.makeTerm("north");
        addWords(vNorth, MatchType.PREFIX, "north");
        wordMap.get("north").word.addAbbreviation("n");
        final Term vSouth = builder.makeTerm("south");
        addWords(vSouth, MatchType.PREFIX, "south");
        wordMap.get("south").word.addAbbreviation("s");
        final Term vEast = builder.makeTerm("east");
        addWords(vEast, MatchType.PREFIX, "east");
        wordMap.get("east").word.addAbbreviation("e");
        final Term vEastIn = builder.makeTerm("eastorin");
        addWords(vEastIn, MatchType.PREFIX, "east", "in");
        final Term vWest = builder.makeTerm("west");
        addWords(vWest, MatchType.PREFIX, "west");
        wordMap.get("west").word.addAbbreviation("w");
        final Term vWestOutExit = builder.makeTerm("westoutorexit");
        addWords(vWestOutExit, MatchType.PREFIX, "west", "out", "exit");
        final Term vDirect = builder.makeTerm("direction");
        addWords(vDirect, MatchType.PREFIX, "north", "south", "east", "west", "in", "out", "exit");
        
        final Term vLook = builder.makeTerm("look");
        addWords(vLook, MatchType.PREFIX, "look");
        
        final Term vAround = builder.makeTerm("around");
        addWords(vAround, MatchType.PREFIX, "around");

        final Room rBalcony = builder.makeRoom("balcony", _m("You are on a balcony facing west, overlooking a beautiful garden. The only exit from the balcony is behind you."),
                _m("You are on the balcony"));
        final Room rNorth = builder.makeRoom("northroom", _m("You are in the north end of the Big Room. The room extends south from here. There is an exit to the outside to the west."),
                _m("You are in the north end of the Big Room."));
        final Room rSouth = builder.makeRoom("southroom", _m("You are in the south end of the Big Room. The room extends north from here."),
                _m("You are in the south end of the Big Room."));
        final Room rMagic = builder.makeRoom("magicroom", _m("You are in the magic workshop. There are no doors in any of the walls."), _m("You are in the magic workshop."));

        final Room rBallroom = builder.makeRoom("ballroom", _m("You have entered the ballroom. The room has a high ceiling with two magnificent crystal chandeliers which illuminate the room. "
                + "The floor is a polished wooden parquet with flowers inlaid around the edge. "
                + "The north wall is lined with mirrors while the south wall has large windows which look out on a beautiful garden. "),
                _m("You are in the ballroom."));

        // Nowhere - a place to keep things that aren't anywhere else.
        final Room rNowhere = builder.makeRoom("nowhere", _m(""), _m(""));

        builder.makePath(vEastIn, rBalcony, rNorth);
        builder.makePath(vWestOutExit, rNorth, rBalcony);
        builder.makePath(vSouth, rNorth, rSouth);
        builder.makePath(vNorth, rSouth, rNorth);

        ActionMethod lookAction = new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                game.thePlayer.lookAround();
            }
        };
        builder.makeAction(vLook, null, 0, null, lookAction);
        builder.makeAction(vLook, vAround, 0, null, lookAction);
        
        builder.makeAction(vMagic, null, 0, null, new ActionMethod() {
            final Room fromRoom = rSouth;
            final Room toRoom = rMagic;

            @Override
            public void doAction(Game game, Word w1, Word w2) {
                Room loc = game.thePlayer.getLocation();
                if (loc == fromRoom) {
                    /* Can do this, we're in the right location. */
                    game.thePlayer.apportTo(toRoom);
                    game.thePlayer.lookAround();
                } else if (loc == toRoom) {
                    /* Good idea, the magic word gets you out again. */
                    game.thePlayer.apportTo(fromRoom);
                    game.thePlayer.lookAround();
                } else {
                    /* Wrong location. Act like we don't know the word. */
                    mDontUnderstand1.println(game.messageOut, w1);
                }
            }
        });
        
        /* This section adds vocabulary for GameObjects. */
        final Term vExamine = builder.makeTerm("examine");
        addWords(vExamine, MatchType.PREFIX, "examine");
        final Term vInventory = builder.makeTerm("inventory");
        addWords(vInventory, MatchType.PREFIX, "inventory");
        final Term vGet = builder.makeTerm("get");
        addWords(vGet, MatchType.PREFIX, "get");
        final Term vDrop = builder.makeTerm("drop");
        addWords(vDrop, MatchType.PREFIX, "drop");
        final Term vMessage = builder.makeTerm("message");
        addWords(vMessage, MatchType.PREFIX, "message", "paper");
        final Term vCoin = builder.makeTerm("coin");
        addWords(vCoin, MatchType.PREFIX, "coin", "goldcoin");
        final Term vWand = builder.makeTerm("magicwand");
        addWords(vWand, MatchType.PREFIX, "wand", "magicwand", "woodenwand");
        final Term vKey = builder.makeTerm("key");
        addWords(vKey, MatchType.PREFIX, "key", "doorkey");
        final Term vObjects = builder.makeTerm("objects");
        final Term vDoor = builder.makeTerm("door");
        addWords(vDoor, MatchType.PREFIX, "door");
        addWords(vObjects, MatchType.PREFIX, "wand", "magicwand", "woodenwand",
                "message", "paper", "coin", "goldcoin", "key", "doorkey");
        final Term vAllObjects = builder.makeTerm("all objects");
        addWords(vAllObjects, MatchType.PREFIX, "wand", "magicwand", "woodenwand",
                "message", "paper", "coin", "goldcoin", "key", "doorkey", "door");

        final Term vLock = builder.makeTerm("lock");
        addWords(vLock, MatchType.PREFIX, "lock");
        final Term vUnlock = builder.makeTerm("unlock");
        addWords(vUnlock, MatchType.PREFIX, "unlock");
        final Term vLockUnlock = builder.makeTerm("lockunlock");
        addWords(vLockUnlock, MatchType.PREFIX, "lock", "unlock");

        final Term vRead = builder.makeTerm("read");
        addWords(vRead, MatchType.PREFIX, "read");
        final Term vWave = builder.makeTerm("wave");
        addWords(vWave, MatchType.PREFIX, "wave");
        
        /* Create game objects. */
        final GameObject iMessage = builder.makeGameObject("paper", vMessage,
                _m("a piece of paper"),
                _m("There is a piece of paper here."),
                _m("It's a piece of paper with some writing on it."));
        
        final GameObject iCoin = builder.makeGameObject("coin", vCoin,
                _m("a gold coin"),
                _m("There is a gold coin here."),
                _m("It's a US golden double eagle."));

        final GameObject iWand = builder.makeGameObject("magicwand", vWand,
                _m("a wooden wand"),
                _m("There is a wooden wand here."),
                _m("It's a beautifully carved wooden wand make of alder."));

        final GameObject iKey = builder.makeGameObject("key", vKey,
                _m("a key"),
                _m("There is a key here."),
                _m("It's a door key. Nothing special."));

        /* Doors between south room and ball room */
        final GameObject iDoorSouth = builder.makeGameObject("south room door", vDoor,
                _m("a door"),
                _sm(_m("There is a door in the east wall. It is closed."),
                        _m("There is an open door in the east wall.")),
                _sm(_m("It is a solid oaken six panel door. It appears to be locked."),
                        _m("It is a fine oaken door that is standing open.")));
        final GameObject iDoorBallroom = builder.makeGameObject("ballroom door", vDoor,
                _m("a door"),
                _sm(_m("There is a door in the west wall. It is closed."),
                        _m("There is an open door in the west wall.")),
                _sm(_m("It is a solid oaken six panel door. It appears to be locked."),
                        _m("It is a fine oaken door that is standing open.")));

        // Put the items in the rooms.
        rSouth.addObject(iDoorSouth);
        rBallroom.addObject(iDoorBallroom);
        rNorth.addObject(iCoin);
        rBallroom.addObject(iMessage);
        rMagic.addObject(iWand);
        rNowhere.addObject(iKey);
        
        final Message mAlreadyCarryingItem = _am("You are already carrying %1.");
        final Message mNowCarryingItem = _am("You are now carrying %1.");
        final Message mCantFindItem = _am("I can't find %1 here.");
        final Message mHaveDroppedItem = _am("You have dropped %1.");
        final Message mNotCarryingItem = _am("You are not carrying %1.");
        final Message mNotCarryingAnything = _m("You are not carrying anything.");
        final Message mCarryingExact1 = _am("You are carrying %1.");
        final Message mCarryingExact2 = _am("You are carrying %1 and %2.");
        final Message mCarryingStart = _m("You are carrying ");
        final Message mCarryingSep = _m(", ");
        final Message mCarryingSepAnd = _m(", and ");
        final Message mCarryingFinal = _m(".");

        builder.makeAction(vGet, vObjects, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                GameObject obj = findObject(game, w2);
                assert (obj != null);
                if (game.thePlayer.contains(obj)) {
                    mAlreadyCarryingItem.println(game.messageOut, obj.getInventoryDesc().getString());
                } else if (game.thePlayer.getLocation().contains(obj)) {
                    game.thePlayer.getLocation().removeObject(obj);
                    game.thePlayer.addObject(obj);
                    mNowCarryingItem.println(game.messageOut, obj.getInventoryDesc().getString());
                } else {
                    mCantFindItem.println(game.messageOut, w2);
                }
            }
        });
        
        builder.makeAction(vDrop, vObjects, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                GameObject obj = findObject(game, w2);
                assert (obj != null);
                if (game.thePlayer.contains(obj)) {
                    game.thePlayer.removeObject(obj);
                    game.thePlayer.getLocation().addObject(obj);
                    mHaveDroppedItem.println(game.messageOut, obj.getInventoryDesc().getString());
                } else {
                    mNotCarryingItem.println(game.messageOut, w2);
                }
            }
        });
        
        builder.makeAction(vExamine, vAllObjects, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                GameObject obj = findObject(game, w2);
                assert (obj != null);
                if (game.thePlayer.contains(obj) || game.thePlayer.getLocation().contains(obj)) {
                    obj.getLongDesc().println(game.messageOut);
                } else {
                    mNotCarryingItem.println(game.messageOut, obj.getInventoryDesc().getString());
                }
            }
        });
        
        builder.makeAction(vInventory, null, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                Collection<GameObject> c = game.thePlayer.getContents();
                if (c.isEmpty()) {
                    mNotCarryingAnything.println(game.messageOut);
                } else if (c.size() <= 2) {
                    Iterator<GameObject> iter = c.iterator();
                    GameObject obj1 = iter.next();
                    if (c.size() == 1) {
                        mCarryingExact1.println(game.messageOut, obj1.getInventoryDesc().getString());
                    } else {
                        GameObject obj2 = iter.next();
                        mCarryingExact2.println(game.messageOut,
                                obj1.getInventoryDesc().getString(), obj2.getInventoryDesc().getString());
                    }
                } else {
                    int objNo = 1;
                    for (GameObject item : c) {
                        if (objNo == 1)
                            mCarryingStart.print(game.messageOut);
                        else if (objNo == c.size())
                            mCarryingSepAnd.print(game.messageOut);
                        else
                            mCarryingSep.print(game.messageOut);
                        item.getInventoryDesc().print(game.messageOut);
                        objNo += 1;
                    }
                    mCarryingFinal.println(game.messageOut);
                }
            }
        });

        final Message mNoWave = builder.makeMessage(
                _cm(_m(""), _m("There's a swishing sound. ")),
                _cm(_m("Nothing happens."),
                        _am("Little sparks follow the %1."),
                        _m("There's a bump, but nothing else happens.")));
        final Message mNoWand = _am("You don't have a %1.");

        final Message mKeyAppears = builder.makeMessage(
                "A key appears on the floor.");
        final Message mKeyDisappears = builder.makeMessage(
                "The key on the floor disappears.");

        builder.makeAction(vWave, vWand, 0, null, new ActionMethod() {
            @Override public void doAction(Game game, Word w1, Word w2) {
                mNoWand.println(game.messageOut, w2);
            }
        });

        builder.makeAction(vWave, vWand, 10, new ValidMethod() {
            @Override public boolean isValid(Game game, Word w1, Word w2) {
                return game.thePlayer.contains(iWand);
            }
        }, new ActionMethod() {
            @Override  public void doAction(Game game, Word w1, Word w2) {
                // If the player isn't in the south room, say nothing happens.
                mNoWave.println(game.messageOut, w2);
            }
        });

        builder.makeAction(vWave, vWand, 20, new ValidMethod() {
            @Override public boolean isValid(Game game, Word w1, Word w2) {
                return game.thePlayer.contains(iWand) && game.thePlayer.getLocation() == rSouth;
            }
        }, new ActionMethod() {
            @Override public void doAction(Game game, Word w1, Word w2) {
                /* Special case: When the player is in the south room, if the
                 * key is in the south room (not carried by the player), the
                 * key disappears, if the key is nowhere, the key appears in
                 * the south room.
                 */
                if (rSouth.contains(iKey)) {
                    rSouth.removeObject(iKey);
                    rNowhere.addObject(iKey);
                    mKeyDisappears.println(game.messageOut);
                    return;
                } else if (rNowhere.contains(iKey)) {
                    rNowhere.removeObject(iKey);
                    rSouth.addObject(iKey);
                    mKeyAppears.println(game.messageOut);
                    return;
                } else {
                    // Key is somewhere else
                    mNoWave.println(game.messageOut, w2);
                }
            }
         });

        final Message mDoorNoStateChange = _sm(
                _m("The door is already locked."),
                _m("The door is already unlocked."));
        final Message mDoorStateChange = _sm(
                _m("The door is now locked."),
                _m("The door is now unlocked."));
        final Message mNotHoldingKey = _am("You need the key to %1 the door.");
        final Message mNeedKey = _am("You need a key to %1 the door.");
        final Message mWhatToLock = _am("I don't see anything here to %1.");

        ActionMethod lockUnlockDoor = new ActionMethod() {
            private void switchDoorState(Game game) {
                int currentState = iDoorSouth.getState();
                int newState = 1 - currentState;
                iDoorBallroom.setState(newState);
                iDoorSouth.setState(newState);
                mDoorStateChange.altPrintln(newState, game.messageOut);
            }

            @Override public void doAction(Game game, Word w1, Word w2) {
                Room location = game.thePlayer.getLocation();
                if (location == rSouth || location == rBallroom) {
                    if (game.thePlayer.contains(iKey)) {
                        if ((vLock.contains(w1) && iDoorBallroom.getState() == 1) ||
                                (vUnlock.contains(w1) && iDoorBallroom.getState() == 0)) {
                            switchDoorState(game);
                        } else {
                            mDoorNoStateChange.altPrintln(
                                    iDoorBallroom.getState(), game.messageOut);
                        }
                    } else if (location.contains(iKey)) {
                        mNotHoldingKey.println(game.messageOut, w1);
                    } else {
                        // Key is not in hand or in room.
                        mNeedKey.println(game.messageOut, w1);
                    }
                } else {
                    mWhatToLock.println(game.messageOut, w1);
                }
            }
        };

        builder.makeAction(vLockUnlock, null, 0, null, lockUnlockDoor);
        builder.makeAction(vLockUnlock, vDoor, 0, null, lockUnlockDoor);

        final Message mDoorLocked = _am("You can't go %1. The door is locked.");
        builder.makeAction(vMove, vDirect, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                Boolean moved = false;
                /* Special case: If the player is in the south room and
                 * moving east and the door is unlocked, move to the ballroom.
                 */
                if (game.thePlayer.getLocation() == rSouth && vEast.contains(w2)) {
                    if (iDoorSouth.getState() == 1) {
                        game.thePlayer.apportTo(rBallroom);
                        moved = true;
                    } else {
                        mDoorLocked.println(game.messageOut, w2);
                        return;
                    }
                } else if (game.thePlayer.getLocation() == rBallroom && vWest.contains(w2)) {
                    if (iDoorBallroom.getState() == 1) {
                        game.thePlayer.apportTo(rSouth);
                        moved = true;
                    } else {
                        mDoorLocked.println(game.messageOut, w2);
                        return;
                    }
                } else if (game.thePlayer.moveOnPath(w2)) {
                    moved = true;
                }
                if (moved)
                    game.thePlayer.lookAround();
                else
                    // Player could not move
                    mDontKnowHowToMove.println(game.messageOut, w1, w2);
            }
        });

        final Message mMessageContents = _am("The %1 says, \"Enjoy your game.\"");
        final Message mNoMessage = _am("You don't have a %1 to read.");
        builder.makeAction(vRead, vMessage, 0, null, new ActionMethod() {
            @Override
            public void doAction(Game game, Word w1, Word w2) {
                if (game.thePlayer.contains(iMessage))
                    mMessageContents.println(game.messageOut, w2);
                else
                    mNoMessage.println(game.messageOut, w2);
            }
        });

        /* Welcome message */
        final Message mWelcome = _m("Welcome to your adventure. Have a good game.");
        
        /* Finally create the player and wrap up. */
        builder.makePlayer("You");
    
        /* Call the builder to finish the build. */
        Game result = builder.buildComplete();

        /* Send the player to the starting location. */
        result.thePlayer.startAt(rBalcony);
        mWelcome.println(result.messageOut);
        result.messageOut.println();
        result.thePlayer.lookAround();
        result.messageOut.startLine();
        
        return result;
    }
}
