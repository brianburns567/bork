package bork;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Constructs new NPC's and holds the means to change their state.
 * 
 * @author Christopher Ringham
 */
public class NPC
{
    
    /**
     * A subclass of Exception that handles exceptions that result when looking 
     * for an NPC in a bork file when there is none.
     * @author Brian Burns
     */
    static class NoNpcException extends Exception {}
    
    static String INVENTORY_STARTER = "Inventory: ";
    
    private String name;
    private String desc;
    private int health;
    private int score;
    private Room currentRoom;
    private ArrayList<Item> inventory;
    
    
    /**
     * Constructs an NPC with a name, description, health, score, and current
     * room from the file attached to the given Scanner.
     * 
     * @param s the Scanner reading the hydration file
     * @throws bork.NPC.NoNPCException
     * @throws bork.Dungeon.IllegalDungeonFormatException
     */
    public NPC(Scanner s
    ) throws NoNpcException, Dungeon.IllegalDungeonFormatException
    {
        inventory = new ArrayList<Item>();
        health = 0;
        
        name = s.nextLine();
        if (name.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoNpcException();
        }
        
        score = Integer.valueOf(s.nextLine());
        currentRoom = GameState.instance().getDungeon().getRoom(s.nextLine());
        
        String lineOfDesc = s.nextLine();
        while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
               !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {
            
            if (lineOfDesc.startsWith(INVENTORY_STARTER)) {
                String itemsList = lineOfDesc.substring(INVENTORY_STARTER.length());
                String[] itemNames = itemsList.split(",");
                for (String itemName : itemNames) {
                    try {
                        add(GameState.instance().getDungeon().getItem(itemName));
                    }
                    catch (Item.NoItemException e) {
                        throw new Dungeon.IllegalDungeonFormatException(
                            "No such item '" + itemName + "'");
                    }
                }
            }
            else {
                desc += lineOfDesc + "\n";
            }
            lineOfDesc = s.nextLine();
        }
    }
    
    /**
     * Gets the name of this NPC.
     * 
     * @return the name of this NPC
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Gets the description of this NPC.
     * 
     * @return the description of this NPC
     */
    public String describe()
    {
        return this.desc;
    }
    
    /**
     * Gets the current health of this NPC.
     * 
     * @return the current health of this NPC
     */
    public int getHealth()
    {
        return this.health;
    }
    
    /**
     * Gets the current score of this NPC.
     * 
     * @return the current score of this NPC
     */
    public int getScore()
    {
        return this.score;
    }
    
    public Weapon getWeapon() throws Weapon.NoWeaponException
    {
        for(Item item : this.inventory)
        {
            if(item instanceof Weapon)
            {
                return (Weapon)item;
            }
        }
        
        throw new Weapon.NoWeaponException();
    }
    
    /**
     * Gets the Room this NPC is currently in.
     * 
     * @return the Room this NPC is currently in
     */
    public Room getCurrentRoom()
    {
        return this.currentRoom;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }
    
    /**
     * Takes hit points away from this NPC's health.
     * 
     * @param damage the number of hit points to subtract from this NPC's health
     */
    public void wound(int damage)
    {
        this.health -= damage;
        if(this.health <= 0) {
            die();
        }
    }
    
    /**
     * Kills a NPC and removes them from the dungeon
     */
    void die(){
        for(Item item : inventory){
            remove(item);
            this.currentRoom.add(item);
        }
        this.currentRoom.removeNPC(this);
        GameState.instance().getDungeon().removeNPC(this.name);
    }
    
    /**
     * Adds the given item to this NPC's inventory.
     * 
     * @param item the item to add to this NPC's inventory
     */
    public void add(Item item)
    {
        inventory.add(item);
    }
    
    /**
     * Removes the given item from this NPC's inventory.
     * 
     * @param item the item to remove from this NPC's inventory
     */
    public void remove(Item item)
    {
        inventory.remove(item);
    }
    
}
