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
    private String name;
    private String desc;
    private int health;
    private int score;
    private Room currentRoom;
    private ArrayList<Item> inventory;
    
    static class NoNpcException extends Exception {}
    
    /**
     * Constructs an NPC with a name, description, health, score, and current
     * room from the file attached to the given Scanner.
     * 
     * @param s the Scanner reading the hydration file
     */
    public NPC(Scanner s)
    {
        // code to come
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
    
    /**
     * Gets the Room this NPC is currently in.
     * 
     * @return the Room this NPC is currently in
     */
    public Room getCurrentRoom()
    {
        return this.currentRoom;
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
        GameState.instance().getDungeon().removeNPC(this);
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
