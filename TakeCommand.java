package bork;

/**
 * Handles the take command.
 * 
 * @author Dr. Zeitz
 */
class TakeCommand extends Command {

    private String itemName;

    /**
     * Constructs a new TakeComand, storing the item to be taken.
     * 
     * @param itemName the item to be taken
     */
    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Checks the surrounding area and inventory for the item, places it in the
     * player's inventory if it is there, and returns an appropriate message.
     * 
     * @return a message based on where the item is
     */
    public String execute() {
        if (itemName == null || itemName.trim().length() == 0) {
            return "Take what?\n";
        }
        try {
            Room currentRoom = 
                GameState.instance().getAdventurersCurrentRoom();
            Item theItem = currentRoom.getItemNamed(itemName);
            GameState.instance().addToInventory(theItem);
            currentRoom.remove(theItem);
            return itemName + " taken.\n";
        } catch (Item.NoItemException e) {
            // Check and see if we have this already. If no exception is
            // thrown from the line below, then we do.
            try {
                GameState.instance().getItemFromInventoryNamed(itemName);
                return "You already have the " + itemName + ".\n";
            } catch (Item.NoItemException e2) {
                return "There's no " + itemName + " here.\n";
            }
        }
    }
}
