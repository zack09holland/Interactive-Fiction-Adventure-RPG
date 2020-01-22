/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cs345.game.Game;
import cs345.game.GameBuilder;
import cs345.message.BaseMessageFormatter;
import cs345.reader.Builder;
import cs345.reader.GameDescription;

/**
 * This class provides a main program for the interactive fiction engine. This
 * class creates a new Builder and a new parser and invokes them.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class GameMain {

    public static final String DEF_GAME_DESCRIPTION = "HardCodedGame";

    /**
     * Main program for Game.
     * 
     * Usage: This program takes zero or one command line argument. The program
     * can be invoked as follows:
     * 
     *   java -cp CLASSPATH GameMain GAMEDESCRIPTIONCLASS
     *   java -cp CLASSPATH GameMain
     *   java -cp CLASSPATH GameMain --help
     * 
     *     CLASSPATH is classpath for the game.
     *     
     *     GAMEDESCRIPTIONCLASS is the name of a class in the reader package
     *     that will be used to provide the game description. If this argument
     *     is omitted, HardCodedGame will be used as a default.
     *     
     *     --help causes a usage message to be produced.
     * 
     * @param args
     *            command line arguments
     * @throws IOException
     *             if there is an unhandled I/O error
     * 
     */
    public static void main(String[] args) throws IOException {
        boolean usage = false;
        String gamedesc = null;
        if (args.length == 0) {
            gamedesc = DEF_GAME_DESCRIPTION;
        } else if (args.length >= 2) {
            usage = true;
        } else {
            if (args[0].equals("--help")) {
                usage = true;
            } else {
                gamedesc = args[0];
            }
        }
        if (usage) {
            System.err.printf("Usage: java ... GameDescription%n"
                            + "  Run game with given description.%n%n"
                            + "  GameDescription  the name of the class describing the game%n"
                            + "  which must be in the reader subpackage of the package that"
                            + "contains this class.%n");
            return;
        }

        new GameMain().run(gamedesc);
    }

    /**
     * Construct and run a game.
     *
     * This method creates a new GameBuilder object, gets the named game
     * description object. Builds the game and runs the command parser.
     * 
     * @param gameDescName
     *            name of the gamedescription class
     */
    private void run(String gameDescName) throws IOException {
        Builder gb = new GameBuilder();
        GameDescription desc = getGameDescription(gameDescName, gb);
        if (desc != null) {
            Game game = desc.build(System.in, new BaseMessageFormatter(System.out, 72));
            game.parser.run();
        }
    }

    /**
     * Get a GameDescription object.
     * 
     * @param gameDescription
     *            The name of the game description class. The actual class
     *            should be found at thispackage.reader.gameDescription, where
     *            thispackage is the package where this class is found.
     * @param gb This is the game builder object for the constructor for the
     *           gameDescription class.
     * @return The constructed game description object. If there is a problem
     *         constructing the game description object, an error message is
     *         output and the method returns null.
     */
    private GameDescription getGameDescription(String gameDescription,
            Builder gb) {

        String thisClassName = getClass().getCanonicalName();
        String thisPackageName = thisClassName.substring(0,
                thisClassName.lastIndexOf('.'));
        String gameDescClassName = thisPackageName + ".reader."
                + gameDescription;
        try {
            Class<?> descClass = Class.forName(gameDescClassName);
            Constructor<?> con = descClass.getConstructor(Builder.class);
            Object descObj = con.newInstance(gb);
            return GameDescription.class.cast(descObj);
        } catch (ClassNotFoundException exc) {
            System.err.printf("Class %s not found %n", gameDescClassName);
        } catch (NoSuchMethodException exc) {
            System.err.printf("Can't get constructor for %s.%n",
                    gameDescClassName);
        } catch (InstantiationException exc) {
            System.err.printf("Can't instantiate %s.%n", gameDescClassName);
        } catch (ClassCastException exc) {
            System.err.printf("%s is not a GameDescription.%n",
                    gameDescClassName);
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        } catch (InvocationTargetException exc) {
            exc.printStackTrace();
        }
        return null;
    }

}
