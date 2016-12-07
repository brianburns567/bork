
package bork;

import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * A class the represents the Dungeon being played by the user. It holds a collection
 * of the rooms in the dungeon, a collection of the items in the dungeon, and has
 * methods and constructors to hydrate from a bork or sav file, as well as write
 * to a sav file.
 * @author Dr. Zeitz
 */
public class Dungeon {
    
    /**
     * A subclass of Exception that defines an exception encountered when unable
     * to correctly hydrate a dungeon.
     * @author Dr. Zeitz
     */
    public static class IllegalDungeonFormatException extends Exception {
        
        /**
         * Constructor for IllegalDungeonFormatException objects that calls the 
         * constructor of its parent class.
         * @param e String representing the exception
         */
        public IllegalDungeonFormatException(String e) {
            super(e);
        }
    }

    // Variables relating to both dungeon file and game state storage.
    public static String TOP_LEVEL_DELIM = "===";
    public static String SECOND_LEVEL_DELIM = "---";

    // Variables relating to dungeon file (.bork) storage.
    public static String ROOMS_MARKER = "Rooms:";
    public static String EXITS_MARKER = "Exits:";
    public static String ITEMS_MARKER = "Items:";
    public static String WEAPONS_MARKER = "Weapons:";
    
    // Variables relating to game state (.sav) storage.
    static String FILENAME_LEADER = "Dungeon file: ";
    static String ROOM_STATES_MARKER = "Room states:";

    private String name;
    private Room entry;
    private Hashtable<String,Room> rooms;
    private Hashtable<String,Item> items;
    private String filename;
    
    /**
     * Constructor for Dungeon objects that sets the name and entry room of the
     * Dungeon, initializes the rooms hashtable and filename variable, and calls
     * init().
     * @param name the name of the new Dungeon object
     * @param entry the entry room of the new Dungeon object
     */
    Dungeon(String name, Room entry) {
        init();
        this.filename = null;    // null indicates not hydrated from file.
        this.name = name;
        this.entry = entry;
        rooms = new Hashtable<String,Room>();
    }

    /**
     * Read from the .bork filename passed, and instantiate a Dungeon object
     * based on it.
     * @param filename the .bork file to hydrate from
     * @throws FileNotFoundException if the filename does not exist
     * @throws IllegalDungeonFormatException if the Dungeon can not be hydrated
     * due to incorrect format
     */
    public Dungeon(String filename) throws FileNotFoundException, 
        IllegalDungeonFormatException {

        this(filename, true);
    }

    /**
     * Read from the .bork filename passed, and instantiate a Dungeon object
     * based on it, including (possibly) the items in their original locations.
     * @param filename the .bork file to hydrate from
     * @param initState boolean telling whether or not to initialize the Dungeon's statae
     * @throws FileNotFoundException if the filename does not exist
     * @throws IllegalDungeonFormatException if the Dungeon can not be hydrated
     * due to incorrect format
     */
    public Dungeon(String filename, boolean initState) 
        throws FileNotFoundException, IllegalDungeonFormatException {

        init();
        this.filename = filename;

        Scanner s = new Scanner(new FileReader(filename));
        name = s.nextLine();

        s.nextLine();   // Throw away version indicator.

        // Throw away delimiter.
        if (!s.nextLine().equals(TOP_LEVEL_DELIM)) {
            throw new IllegalDungeonFormatException("No '" +
                TOP_LEVEL_DELIM + "' after version indicator.");
        }

        // Throw away Items starter.
        if (!s.nextLine().equals(ITEMS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                ITEMS_MARKER + "' line where expected.");
        }

        try {
            // Instantiate items.
            while (true) {
                add(new Item(s));
            }
        } catch (Item.NoItemException e) {  /* end of items */ }

        // Throw away Rooms starter.
        if (!s.nextLine().equals(ROOMS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                ROOMS_MARKER + "' line where expected.");
        }

        try {
            // Instantiate and add first room (the entry).
            entry = new Room(s, this, initState);
            add(entry);

            // Instantiate and add other rooms.
            while (true) {
                add(new Room(s, this, initState));
            }
        } catch (Room.NoRoomException e) {  /* end of rooms */ }

        // Throw away Exits starter.
        if (!s.nextLine().equals(EXITS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                EXITS_MARKER + "' line where expected.");
        }

        try {
            // Instantiate exits.
            while (true) {
                Exit exit = new Exit(s, this);
            }
        } catch (Exit.NoExitException e) {  /* end of exits */ }
        
        // Throw away Weapons starter.
        if (!s.nextLine().equals(WEAPONS_MARKER)) {
            throw new IllegalDungeonFormatException("No '" +
                WEAPONS_MARKER + "' line where expected.");
        }
        
        try {
            // Instantiate weapons.
            while (true) {
                add(new Weapon(s));
            }
        } catch (Item.NoItemException e) {  /* end of weapons */ }

        s.close();
    }
    
    /**
     * Common object initialization tasks, regardless of which constructor
     * is used.
     */ 
    private void init() {
        rooms = new Hashtable<String,Room>();
        items = new Hashtable<String,Item>();
    }

    /**
     * Store the current (changeable) state of this dungeon to the writer
     * passed.
     * @param w the writer used to print to the file
     * @throws IOException if unable to write to the file
     */
    void storeState(PrintWriter w) throws IOException {
        w.println(FILENAME_LEADER + getFilename());
        w.println(ROOM_STATES_MARKER);
        for (Room room : rooms.values()) {
            room.storeState(w);
        }
        w.println(TOP_LEVEL_DELIM);
    }

    /**
     * Restore the (changeable) state of this dungeon to that reflected in the
     * reader passed.
     * @param s the reader used to restore the dungeon
     * @throws GameState.IllegalSaveFormatException if unable to read from the SAV file
     */
    void restoreState(Scanner s) throws GameState.IllegalSaveFormatException {

        // Note: the filename has already been read at this point.
        
        if (!s.nextLine().equals(ROOM_STATES_MARKER)) {
            throw new GameState.IllegalSaveFormatException("No '" +
                ROOM_STATES_MARKER + "' after dungeon filename in save file.");
        }

        String roomName = s.nextLine();
        while (!roomName.equals(TOP_LEVEL_DELIM)) {
            getRoom(roomName.substring(0,roomName.length()-1)).
                restoreState(s, this);
            roomName = s.nextLine();
        }
    }

    /**
     * Getter for entry room.
     * @return the entry room of the dungeon
     */
    public Room getEntry() { return entry; }
    
    /**
     * Getter for name.
     * @return the name of the dungeon
     */
    public String getName() { return name; }
    
    /**
     * Getter for filename.
     * @return the filename to be used for hydrating the dungeon
     */
    public String getFilename() { return filename; }
    
    /**
     * Adds a room to the Dungeon's hashtable.
     * @param room the room to be added
     */
    public void add(Room room) { rooms.put(room.getTitle(),room); }
    
    /**
     * Adds an item to the Dungeon's hashtable.
     * @param item the item to be added
     */
    public void add(Item item) { items.put(item.getPrimaryName(),item); }
    
    /**
     * Finds a room in the hashtable by name.
     * @param roomTitle the name of the room to be returned
     * @return the corresponding room
     */
    public Room getRoom(String roomTitle) {
        return rooms.get(roomTitle);
    }

    /**
     * Get the Item object whose primary name is passed. This has nothing to
     * do with where the Adventurer might be, or what's in his/her inventory,
     * etc.
     * @param primaryItemName the name of the item to be returned
     * @return the corresponding item
     * @throws bork.Item.NoItemException Signals that item does not exist
     */
    public Item getItem(String primaryItemName) throws Item.NoItemException {
        
        if (items.get(primaryItemName) == null) {
            throw new Item.NoItemException();
        }
        return items.get(primaryItemName);
    }
    
    /**
     * Removes an item from the dungeon's hashtable.
     * @param itemName the name of the item to remove
     */
    void removeItem(String itemName) {
        this.items.remove(itemName);
    }
    
    Hashtable<String,Room> getRooms()
    {
        return this.rooms;
    }
    
}
