/**
 * This work is licensed under the Creative Commons Attribution 3.0
 * Unported License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/3.0/ or send a letter to
 * Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA. 
 */

package cs345.message;

import java.util.Arrays;
import java.io.*;

/**
 * This class is a MessageFormatter.
 * 
 * @author Chris Reedy (Chris.Reedy@wwu.edu)
 */
public class BaseMessageFormatter implements MessageFormatter {
    
    public final int MAX_LINE;
    public final String LINE_SEP = "\n";
    
    private PrintStream out;
    private StringBuilder buf = new StringBuilder();
    
    public BaseMessageFormatter(PrintStream out, int maxLine) {
        this.out = out;
        this.MAX_LINE = maxLine;
    }

    @Override
    public void print(String s) {
        buf.append(s);
        doOutput(false);
    }
    
    @Override
    public void println(String s) {
        buf.append(s);
        println();
    }
    
    @Override
    public void println() {
        buf.append(LINE_SEP);
        doOutput(false);
    }

    @Override
    public void startLine() {
        if (buf.length() > 0) {
            println();
        }
    }

    @Override
    public void prompt(String s) {
        buf.append(s);
        doOutput(true);
    }

    /**
     * Stub for doOutput. Does nothing.
     */
//    private void doOutput(boolean flush) {
//        out.print(buf.toString().replace("\n", System.lineSeparator()));
//        buf.setLength(0);
//    }
    
    /**
     * Process output. The contents of the buffer is output to the
     * PrintStream out. Some subset of the buffer may be output. Any
     * residual part of the buffer that is not output remains in the
     * buffer for subsequent calls.
     *
     * All complete lines (lines ended by LINE_SEP) are output. Any
     * partial final line is retained unless flush is true, in which
     * case the partial line is output.
     * 
     * The following rules are observed when producing output:
     *   - The buffer consists of lines which are separated by the string
     *     given by LINE_SEP.
     *   - A "full line" is a line that is ended by a LINE_SEP. A "partial
     *     line" is a line that is not a full line.
     *   - Each line consists of words separated by white space characters.
     *   - A line is output by outputting the words in the line separated
     *     by spaces. Any white space at the beginning of the line is 
     *     eliminated. Multiple white space characters are compressed to a
     *     single space.
     *   - At any point where the words in a line would exceed MAX_LINE,
     *     a call to out.println() is substituted for the space that
     *     would appear between words.
     *   - A full line is ended by a call to out.println(). No white space
     *     appears at the end of the output of a full line.
     *   - If a partial line ends in a white space character, a single white
     *     space character ends the output
     */
    private void doOutput(boolean flush) {
        // Eliminate empty buffer case.
        if (buf.length() == 0)
            return;

        // Split string into lines. If there is a new line at the end
        // of the line, there will be an empty string for the last line.
        String current = buf.toString();
        buf.setLength(0);
        String[] lines = current.split(LINE_SEP, -1);
        for (String line : Arrays.copyOf(lines, lines.length - 1)) {
            outputLine(line, true);
        }
        
        // Handle last line.
        String line = lines[lines.length - 1];
        if (flush)
            outputLine(line, false);
        else
            buf.append(line);

        // Clean up
        out.flush();
    }
    
    private void outputLine(String line, boolean endLine) {
        int curLineLoc = 0;
        boolean space = false;
        if (!line.trim().isEmpty()) {
            // The line is non-empty. Split the line into words. Leave
            // extra "" at beginning and end if blanks at start and end.
            String[] words = line.split("\\s+", -1);
            // If there is white space at the beginning of the line, that
            // will appear as an empty string in words[0].
            int start = (words[0].isEmpty() ? 1 : 0);
            for (String w : Arrays.copyOfRange(words, start, words.length - 1)) {
                curLineLoc = outputWord(w, curLineLoc, space);
                space = true;
            }
            // Handle the last word.
            String w = words[words.length - 1];
            if (!w.isEmpty() || !endLine) {
                curLineLoc = outputWord(w, curLineLoc, space);
            }
            if (endLine) {
                out.println();
            }
        } else {
            // Line is all whitespace
            if (endLine) {
                out.println();
            } else if (!line.isEmpty()) {
                out.print(' ');
            }
        }
    }
    
    private int outputWord(String w, int oldLineLoc, boolean space) {
        int curLineLoc = oldLineLoc;
        if (curLineLoc + w.length() + (space ? 1 : 0) > MAX_LINE) {
            out.println();
            curLineLoc = 0;
        } else if (space) {
            out.print(' ');
            curLineLoc += 1;
        }
        out.print(w);
        return curLineLoc + w.length();
    }
}
