
package bork;

import java.util.Scanner;

/**
 * A class that represents the means by which an adventurer leaves or enters a room. 
 * It includes getters for the rooms that it connects, as well as the direction
 * it represents, and can describe itself.
 * @author Dr. Zeitz
 */
public class Exit {

    /**
     * A subclass of Exception that handles exceptions that result from the user
     * trying to exit a room when there is no exit.
     * @author Dr. Zeitz
     */
    class NoExitException extends Exception {}

    private String dir;
    private Room src, dest;

    /**
     * Constructor of exit objects that sets the direction, source room, and 
     * destination room of the exit, as well as adding the exit to the source 
     * room's hashtable.
     * @param dir the direction by which the exit can be accessed
     * @param src the room to be left
     * @param dest the room to be entered
     */
    Exit(String dir, Room src, Room dest) {
        init();
        this.dir = dir;
        this.src = src;
        this.dest = dest;
        src.addExit(this);
    }

    /** Given a Scanner object positioned at the beginning of an "exit" file
        entry, read and return an Exit object representing it.
        @param s the Scanner object
        @param d The dungeon that contains this exit (so that Room objects 
        may be obtained.)
        @throws NoExitException The reader object is not positioned at the
        start of an exit entry. A side effect of this is the reader's cursor
        is now positioned one line past where it was.
        @throws IllegalDungeonFormatException A structural problem with the
        dungeon file itself, detected when trying to read this room.
     */
    Exit(Scanner s, Dungeon d) throws NoExitException,
        Dungeon.IllegalDungeonFormatException {

        init();
        String srcTitle = s.nextLine();
        if (srcTitle.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoExitException();
        }
        src = d.getRoom(srcTitle);
        dir = s.nextLine();
        dest = d.getRoom(s.nextLine());
        
        // I'm an Exit object. Great. Add me as an exit to my source Room too,
        // though.
        src.addExit(this);

        // throw away delimiter
        if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after exit.");
        }
    }

    /** 
     * Common object initialization tasks.
     */
    private void init() {
    }

    /**
     * Describes the exit for the user.
     * @return the String description
     */
    String describe() {
        return "You can go " + dir + " to " + dest.getTitle() + ".";
    }

    /**
     * Getter for the direction of the exit.
     * @return the direction
     */
    String getDir() { return dir; }
    
    /**
     * Getter for the source Room of the exit.
     * @return the source room
     */
    Room getSrc() { return src; }
    
    /**
     * Getter for the destination Room of the exit.
     * @return the destination room
     */
    Room getDest() { return dest; }
}
