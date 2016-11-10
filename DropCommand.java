
package bork;

/**
 * A subclass of Command that executes commands from the user to drop a certain 
 * item in their inventory.
 * @author Dr. Zeitz
 */
class DropCommand extends Command {

    private String itemName;
    
    /**
     * Constructor for DropCommand objects that take in an item name as a parameter.
     * @param itemName the name of the item to be dropped from inventory
     */
    DropCommand(String itemName) {
        this.itemName = itemName;
    }
    
    /**
     * Executes a command to drop an item from the user's inventory, updates the
     * GameState, and returns a String to the user.
     * @return a String that either confirms the action or explains why it was
     * not possible
     */
    public String execute() {
        if (itemName == null || itemName.trim().length() == 0) {
            return "Drop what?\n";
        }
        try {
            Item theItem = GameState.instance().getItemFromInventoryNamed(
                itemName);
            GameState.instance().removeFromInventory(theItem);
            GameState.instance().getAdventurersCurrentRoom().add(theItem);
            return itemName + " dropped.\n";
        } catch (Item.NoItemException e) {
            return "You don't have a " + itemName + ".\n";
        }
    }
}
