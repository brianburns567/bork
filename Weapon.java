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
     */
    public Weapon(Scanner s)
    {
        // code to come
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
