package bork;

import java.util.Scanner;

/**
 *  A Weapon is a type of Item that deals damage.
 * 
 * @author Christopher Ringham
 */
public class Weapon extends Item
{
    private int damage;
    
    /**
     * Constructs a new Weapon Item, storing the inherent damage it deals.
     * 
     * @param s the Scanner that is reading the hydration file
     * @throws bork.Item.NoItemException Signals that there is no Item left to create
     * @throws bork.Dungeon.IllegalDungeonFormatException Signals that the format of the input file is incorrect
     */
    public Weapon(Scanner s) throws NoItemException, Dungeon.IllegalDungeonFormatException
    {
        super(s);
        damage = Integer.valueOf(s.nextLine());
        
        if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) 
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                    Dungeon.SECOND_LEVEL_DELIM + "' after weapon.");
    }
    
    /**
     * Gets the inherent damage this weapon deals.
     * 
     * @return the inherent damage this weapon deals
     */
    public int getDamage()
    {
        return this.damage;
    }
    
    
}
