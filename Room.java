package bork;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Constructs new Rooms and holds the methods needed to change or access the
 * properties of any dungeon Room.
 * 
 * @author Dr. Zeitz
 */
public class Room {

    class NoRoomException extends Exception {}

    static String CONTENTS_STARTER = "Contents: ";
    static String NPC_STARTER = "NPCs: ";

    private String title;
    private String desc;
    private boolean beenHere;
    private boolean isLit;
    private ArrayList<NPC> npcList;
    private ArrayList<Item> contents;
    private ArrayList<Exit> exits;

    /**
     * A Room constructor that initializes with the Room title.
     * 
     * @param title the title of the Room
     */
    Room(String title) {
        init();
        this.title = title;
    }

    /**
     * Constructs a room based off of data in the file attached to the given
     * Scanner.
     * 
     * @param s passed by the Dungeon constructor, this is the fileScanner
     * reading the '.bork' file
     * @param d the Dungeon this Room belongs to
     * @throws bork.Room.NoRoomException The reader object is not positioned at
     * the start of a room entry. A side effect of this is the reader's cursor
     * is now positioned one line past where it was.
     * @throws bork.Dungeon.IllegalDungeonFormatException A structural problem
     * with the dungeon file itself, detected when trying to read this room.
     */
    Room(Scanner s, Dungeon d) throws NoRoomException,
        Dungeon.IllegalDungeonFormatException {

        this(s, d, true);
    }
    
    /**
     * Given a Scanner object positioned at the beginning of a "room" file 
     * entry, read and return a Room object representing it. 
     * 
     * @param s passed by the Dungeon constructor, this is the fileScanner
     * reading the '.bork' file
     * @param d the Dungeon this Room belongs to, necessary to hydrate dungeon
     * items
     * @param initState should items listed for this room be added to it?
     * @throws bork.Room.NoRoomException The reader object is not positioned at
     * the start of a room entry. A side effect of this is the reader's cursor
     * is now positioned one line past where it was.
     * @throws bork.Dungeon.IllegalDungeonFormatException A structural problem
     * with the dungeon file itself, detected when trying to read this room.
     */
    Room(Scanner s, Dungeon d, boolean initState) throws NoRoomException,
        Dungeon.IllegalDungeonFormatException {

        init();
        title = s.nextLine();
        desc = "";
        if (title.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoRoomException();
        }
        
        String lineOfDesc = s.nextLine();
        while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
               !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {

            if (lineOfDesc.startsWith(CONTENTS_STARTER)) {
                String itemsList = lineOfDesc.substring(CONTENTS_STARTER.length());
                String[] itemNames = itemsList.split(",");
                for (String itemName : itemNames) {
                    try {
                        if (initState) {
                            add(d.getItem(itemName));
                        }
                    } catch (Item.NoItemException e) {
                        throw new Dungeon.IllegalDungeonFormatException(
                            "No such item '" + itemName + "'");
                    }
                }
            } else {
                desc += lineOfDesc + "\n";
            }
            lineOfDesc = s.nextLine();
        }

        // throw away delimiter
        if (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after room.");
        }
    }

    /**
     * Performs common object initialization including the Room contents, exits,
     * and whether the player has visited the Room.
     */
    private void init() {
        contents = new ArrayList<Item>();
        exits = new ArrayList<Exit>();
        beenHere = false;
    }
    
    /**
     * Gets the title of this Room.
     * 
     * @return The Room title.
     */
    String getTitle() { return title; }
    
    /**
     * Sets a new description for this Room.
     * 
     * @param desc the new description of the Room
     */
    void setDesc(String desc) { this.desc = desc; }
    
    /**
     * Store the current (changeable) state of this room to the writer passed.
     * 
     * @param w received from the Dungeon storeState() method, this is the
     * writer accessing '.sav' file being written to
     * @throws IOException If something goes wrong with reading or writing the
     * file.
     */
    void storeState(PrintWriter w) throws IOException {
        w.println(title + ":");
        w.println("beenHere=" + beenHere);
        if (contents.size() > 0) {
            w.print(CONTENTS_STARTER);
            for (int i=0; i<contents.size()-1; i++) {
                w.print(contents.get(i).getPrimaryName() + ",");
            }
            w.println(contents.get(contents.size()-1).getPrimaryName());
        }
        if (npcList.size() > 0) {
            w.print(NPC_STARTER);
            for (int i = 0; i < npcList.size() - 1; i++) {
                w.print(npcList.get(i).getName() + " " + npcList.get(i).getHealth() + ",");
            }
            w.print(npcList.get(npcList.size() - 1).getName() + " " + npcList.get(npcList.size() - 1).getHealth());
        }
        w.println(Dungeon.SECOND_LEVEL_DELIM);
    }

    /**
     * Restores the save conditions of each room, including their contents and
     * whether the player has visited the Room.
     * 
     * @param s received from the Dungeon restoreState() method, this is the
     * scanner accessing the '.sav' file being read from.
     * @param d the dungeon associated with the '.sav' file
     * @throws GameState.IllegalSaveFormatException Signals an illegal format for the save file
     */
    void restoreState(Scanner s, Dungeon d) throws 
        GameState.IllegalSaveFormatException {

        String line = s.nextLine();
        if (!line.startsWith("beenHere")) {
            throw new GameState.IllegalSaveFormatException("No beenHere.");
        }
        beenHere = Boolean.valueOf(line.substring(line.indexOf("=")+1));

        line = s.nextLine();
        if (line.startsWith(CONTENTS_STARTER)) {
            String itemsList = line.substring(CONTENTS_STARTER.length());
            String[] itemNames = itemsList.split(",");
            for (String itemName : itemNames) {
                try {
                    add(d.getItem(itemName));
                } catch (Item.NoItemException e) {
                    throw new GameState.IllegalSaveFormatException(
                        "No such item '" + itemName + "'");
                }
            }
        }
        line = s.nextLine();
        if (line.startsWith(NPC_STARTER)) {
            String npcLine = line.substring(NPC_STARTER.length());
            String[] npcHealths = npcLine.split(",");
            for (String npcHealth : npcHealths) {
                String[] nH = npcHealth.split(" ");
                try {
                    for (NPC npc : npcList) {
                        if (npc.getName().equals(nH[0])) {
                            npc.setHealth(Integer.parseInt(nH[1]));
                        }
                    }
                } catch (NPC.NoNPCException e) {
                    throw new GameState.IllegalSaveFormatException("No such NPC '" + ng[0] + "'");
                }
            }
        }   
        s.nextLine();  // Consume "---".
    }

    /**
     * Gets the description of this Room if the player has not visited it yet as
     * well as the contents and exits of this Room.
     * 
     * @return an appropriate description of the Room and its contents
     */
    public String describe() {
        String description;
        if (beenHere) {
            description = title;
        } else {
            description = title + "\n" + desc;
        }
        for (Item item : contents) {
            description += "\nThere is a " + item.getPrimaryName() + " here.";
        }
        if (contents.size() > 0) { description += "\n"; }
        if (!beenHere) {
            for (Exit exit : exits) {
                description += "\n" + exit.describe();
            }
        }
        beenHere = true;
        return description;
    }
    
    /**
     * Gets the Room that the Player reaches when they travel through the Exit
     * in the given direction, if there is an exit there.
     * 
     * @param dir the direction in which the player is trying to exit
     * @return the Room that the Player reaches when they travel through the
     * Exit in the given direction
     */
    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }

    /**
     * Adds a new Exit to this Room.
     * 
     * @param exit an Exit to be added to the Room
     */
    void addExit(Exit exit) {
        exits.add(exit);
    }

    /**
     * Adds an item to this room.
     * 
     * @param item the item to add
     */
    void add(Item item) {
        contents.add(item);
    }
    
    /**
     * Removes an item from this room.
     * 
     * @param item the item to remove
     */
    void remove(Item item) {
        contents.remove(item);
    }
    
    /**
     * Adds a new NPC to this Room
     * @param npc an NPC to be added to the Room
     */
    void addNPC(NPC npc) {
        npcList.add(npc);
    }
    
    /**
     * Removes an NPC from this room.
     * @param npc an NPC to be removed from the Room
     */
    void removeNPC(NPC npc) {
        npcList.remove(npc);
    }

    /**
     * Gets an item that goes by the given name from the contents of this Room.
     * 
     * @param name the name of the item to retrieve
     * @return an item that goes by the given name from the contents of room
     * @throws bork.Item.NoItemException If there are no more items in this Room.
     */
    Item getItemNamed(String name) throws Item.NoItemException {
        for (Item item : contents) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new Item.NoItemException();
    }

    /**
     * Gets a list of all contents in this Room.
     * 
     * @return a list of all contents in this Room
     */
    ArrayList<Item> getContents() {
        return contents;
    }
    
    /**
     * Getter method for isLit.
     * @return true or false if the room is lit or not
     */
    boolean isLit() {
        return false;
    }
    
    /**
     * Getter method for npcList.
     * @return the list of NPCs in the Room
     */
    ArrayList<NPC> getNPCs() {
        return this.npcList;
    }
}
