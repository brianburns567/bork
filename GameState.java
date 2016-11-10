
package bork;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Stores, recalls, and creates all the resources for the current state of the game.
 * @author Dr. Zeitz
 */
public class GameState {

    /**
     * Exception for illegal file formats that pushes the error up the stack
     */
    public static class IllegalSaveFormatException extends Exception {
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }

    static String DEFAULT_SAVE_FILE = "bork_save";
    static String SAVE_FILE_EXTENSION = ".sav";
    static String SAVE_FILE_VERSION = "Bork v3.0 save data";

    static String ADVENTURER_MARKER = "Adventurer:";
    static String CURRENT_ROOM_LEADER = "Current room: ";
    static String INVENTORY_LEADER = "Inventory: ";

    private static GameState theInstance;
    private Dungeon dungeon;
    private ArrayList<Item> inventory;
    private Room adventurersCurrentRoom;

    /**
     * Creates a new gamestate and returns it or returns the gamestate if one already exists
     * @return the GameState object
     */
    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }

    /**
     * GameState constructor that initializes a new ArrayList of type Item
     */
    private GameState() {
        inventory = new ArrayList<Item>();
    }

    /**
     * Restores the GameState conditions from file
     * @param filename the file the GameState is read in from
     * @throws FileNotFoundException Signals the the attempt to open a file has failed
     * @throws IllegalSaveFormatException Signals that the save file read from is incorrectly formated
     * @throws Dungeon.IllegalDungeonFormatException Signals that the dungeon file read from is incorrectly formated
     */
    void restore(String filename) throws FileNotFoundException,
        IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {

        Scanner s = new Scanner(new FileReader(filename));

        if (!s.nextLine().equals(SAVE_FILE_VERSION)) {
            throw new IllegalSaveFormatException("Save file not compatible.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith(Dungeon.FILENAME_LEADER)) {
            throw new IllegalSaveFormatException("No '" +
                Dungeon.FILENAME_LEADER + 
                "' after version indicator.");
        }

        dungeon = new Dungeon(dungeonFileLine.substring(
            Dungeon.FILENAME_LEADER.length()), false);
        dungeon.restoreState(s);

        s.nextLine();  // Throw away "Adventurer:".
        String currentRoomLine = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(
            currentRoomLine.substring(CURRENT_ROOM_LEADER.length()));
        if (s.hasNext()) {
            String inventoryList = s.nextLine().substring(
                INVENTORY_LEADER.length());
            String[] inventoryItems = inventoryList.split(",");
            for (String itemName : inventoryItems) {
                try {
                    addToInventory(dungeon.getItem(itemName));
                } catch (Item.NoItemException e) {
                    throw new IllegalSaveFormatException("No such item '" +
                        itemName + "'");
                }
            }
        }
    }

    /**
     * Stores the current state of the game with default save file name
     * @throws IOException Signals that an I/O exception of some sort has occured
     */
    void store() throws IOException {
        store(DEFAULT_SAVE_FILE);
    }

    /**
     * Stores the current state of the game with input parameter for the save file name
     * @param saveName save file name
     * @throws IOException Signals that an I/O exception of some sort has occured
     */
    void store(String saveName) throws IOException {
        String filename = saveName + SAVE_FILE_EXTENSION;
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println(SAVE_FILE_VERSION);
        dungeon.storeState(w);
        w.println(ADVENTURER_MARKER);
        w.println(CURRENT_ROOM_LEADER + adventurersCurrentRoom.getTitle());
        if (inventory.size() > 0) {
            w.print(INVENTORY_LEADER);
            for (int i=0; i<inventory.size()-1; i++) {
                w.print(inventory.get(i).getPrimaryName() + ",");
            }
            w.println(inventory.get(inventory.size()-1).getPrimaryName());
        }
        w.close();
    }

    /**
     * Initializes a dungeon from the input parameter with the player's current room set to the dungeon's entry
     * @param dungeon the dungeon for the game
     */
    void initialize(Dungeon dungeon) {
        this.dungeon = dungeon;
        adventurersCurrentRoom = dungeon.getEntry();
    }

    /**
     * Creates an ArrayList of type String with the names of the items in the player's inventory
     * @return ArrayList of String names for items
     */
    ArrayList<String> getInventoryNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Item item : inventory) {
            names.add(item.getPrimaryName());
        }
        return names;
    }

    /**
     * Adds and item from input into the player's inventory
     * @param item the item that will be added to inventory
     */
    void addToInventory(Item item) /* throws TooHeavyException */ {
        inventory.add(item);
    }

    /**
     * Removes an item from the player's inventory matching the one read in from input
     * @param item the item that will be removed from inventory
     */
    void removeFromInventory(Item item) {
        inventory.remove(item);
    }

    /**
     * Returns the item with the name from input if it is found in the current room or the player's inventory
     * @param name the name of the item to be returned
     * @return Item the item that is found with a name matching the searched item
     * @throws Item.NoItemException Signals that the item was not found in inventory nor the current room
     */
    Item getItemInVicinityNamed(String name) throws Item.NoItemException {

        // First, check inventory.
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        // Next, check room contents.
        for (Item item : adventurersCurrentRoom.getContents()) {
            if (item.goesBy(name)) {
                return item;
            }
        }

        throw new Item.NoItemException();
    }

    /**
     * Returns the item with the name from input if it is found in the player's inventory
     * @param name the name of the item to be returned
     * @return Item the item that is found with a name matching the searched item
     * @throws Item.NoItemException Signals that the item was not found in inventory
     */
    Item getItemFromInventoryNamed(String name) throws Item.NoItemException {

        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }

    /**
     * Returns the Room object that the player is currently in
     * @return Room the room the player is currently in
     */
    Room getAdventurersCurrentRoom() {
        return adventurersCurrentRoom;
    }

    /**
     * Sets the adventurers current room to the one read in from the input parameter
     * @param room the room being set as the current room
     */
    void setAdventurersCurrentRoom(Room room) {
        adventurersCurrentRoom = room;
    }

    /**
     * Returns the dungeon that is being used by GameState
     */
    Dungeon getDungeon() {
        return dungeon;
    }

}
