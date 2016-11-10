package bork;

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
     * @param damage the inherent damage the weapon deals
     */
    public Weapon(int damage)
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
